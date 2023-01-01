import pika, sys, os
import uuid
import json
import numpy as np
import pandas as pd
import argparse
from pykrige.ok import OrdinaryKriging
from pymongo import MongoClient
from datetime import datetime, timedelta


def connectToDatabase(host, port, username, password, db):
    if username and password:
        mongo_uri = 'mongodb://%s:%s@%s:%s/%s' % (username, password, host, port, db)
        conn = MongoClient(mongo_uri)
    else:
        print(host, port, username, password, db)
        conn = MongoClient(host, port)
    return conn[db]
def collectData(dbConnection, latBoundMin, latBoundMax, lonBoundMin, lonBoundMax, modelLengthDays):
    def readDatabase(db, collection, query={}):
        cursor = db[collection].find(query)
        df =  pd.DataFrame(list(cursor))
        return df
    end = datetime.now()
    print(modelLengthDays)
    timeDelta = timedelta(days=modelLengthDays)
    start = end - timeDelta
    print(start)
    fullData = readDatabase(dbConnection, 'positionmessage', {'timestamp': {'$lt': end, '$gte': start}, "messageType" : { "$in" : [ "PositionReportClassAScheduled", "PositionReportClassAAssignedSchedule", "PositionReportClassAResponseToInterrogation"] }})
    latitudeBounded = fullData[(fullData.latitude > latBoundMin) & (fullData.latitude < latBoundMax)]
    bounded = latitudeBounded[(latitudeBounded.longitude > lonBoundMin) & (latitudeBounded.longitude < lonBoundMax)]
    return bounded
def prepareData(finalTimeBounded):
    return pd.DataFrame({"lon": finalTimeBounded["longitude"], "lat": finalTimeBounded["latitude"], "z": finalTimeBounded["signalpower"]})
def generateModel(data):
    binNumber = 100
    d1 = data.assign(
        Lon_cut=pd.cut(data.lon, binNumber),
        Lat_cut=pd.cut(data.lat, binNumber)
    )
    d2 = d1.assign(cartesian=pd.Categorical(d1.filter(regex='_cut').apply(tuple, 1)))
    df2 = d2.groupby(by='cartesian').agg('mean')
    lon = df2["lon"].to_numpy()
    lat = df2["lat"].to_numpy()
    z = df2["z"].to_numpy()
    print("Size of lon", lon.size , lon)
    print("Size of lat", lat.size, lat)
    print("Size of z", z.size, z)
    grid_lon = np.linspace(lon.min(), lon.max(), 100)
    grid_lat = np.linspace(lat.min(), lat.max(), 100)
    # Create ordinary kriging object:
    OK = OrdinaryKriging(
        lon,
        lat,
        z,
        nlags = 16,
        variogram_model="power",
        verbose=True
    )
    ordinaryKrigingResult, ss1 = OK.execute("grid", grid_lon, grid_lat)
    return ordinaryKrigingResult, ss1
def main(args):
    print('Model generation service starting.')
    def callback(ch, method, properties, body):
        eventObject = json.loads(body)
        print('Model generation event received.')
        modelLengthDays = eventObject["lengthInDays"]
        dbConnection = connectToDatabase(args.dbhost, args.dbport, args.dbusername, args.dbpassword, args.dbname)
        collectedData = collectData(dbConnection, eventObject["latitudeMinBound"], eventObject["latitudeMaxBound"], eventObject["longitudeMinBound"], eventObject["longitudeMaxBound"], modelLengthDays )
        preparedData = prepareData(collectedData)
        ordinaryKrigingResult, ss1 = generateModel(preparedData)
        modelsCollection = dbConnection["models"]
        resultdict = { "type": "OrdinaryKriging", "generatedAt": datetime.now(), "minlon": preparedData["lon"].min(), "maxlon": preparedData["lon"].max(), "minlat": preparedData["lat"].min(), "maxlat": preparedData["lat"].max(), "predictionGrid": ordinaryKrigingResult.data.tolist(), "minVariance": np.sqrt(ss1.data.min()), "maxVariance": np.sqrt(ss1.data.max()), "varianceGrid": ss1.data.tolist()}
        modelsCollection.insert_one(resultdict)
        print('Model uploaded.')
    queueName = 'ais-validator-model-queue-' + str(uuid.uuid4())
    connection = pika.BlockingConnection(pika.ConnectionParameters(host=args.rabbitmqhost, heartbeat=3600))
    channel = connection.channel()
    channel.queue_declare(queue=queueName)
    channel.queue_bind(exchange='ais-validator', queue=queueName, routing_key="SignalModelUpdateEvent")
    channel.basic_consume(queue=queueName, on_message_callback=callback, auto_ack=True)
    channel.start_consuming()


if __name__ == '__main__':
    try:
        argParser = argparse.ArgumentParser()
        argParser.add_argument("-dbhost", required=True ,help="database host")
        argParser.add_argument("-dbport", type=int, required=True, help="database port")
        argParser.add_argument("-dbusername", help="database username")
        argParser.add_argument("-dbpassword", help="database password")
        argParser.add_argument("-dbname", required=True, help="database name")
        argParser.add_argument("-rabbitmqhost", required=True, help="rabbitmq host")
        argParser.add_argument("-rabbitmqusername", help="rabbitmq username")
        argParser.add_argument("-rabbitmqpassword", help="rabbitmq password")
        args, unknown = argParser.parse_known_args()
        main(args)
    except KeyboardInterrupt:
        print('Interrupted')
        try:
            sys.exit(0)
        except SystemExit:
            os._exit(0)