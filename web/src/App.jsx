import "./App.css";
import Map from "./Map";
import Table from "./Table";
import { useEffect, useState } from "react";
import { Client } from "@stomp/stompjs";
import moment from "moment";

function App() {
  const [vesselList, setVesselList] = useState([]);
  const [markerPosition, setMarkerPosition] = useState(null);
  const [newMessage, setNewMessage] = useState(null);
  const [markerInfo, setMarkerInfo] = useState(null);
  const [sortBy, setSortBy] = useState("resultSummary");

  const queueurl = "ws://localhost:15674/ws";
  const stompConfig = {
    connectHeaders: { ack: "client", login: "web", passcode: "123456" },
    brokerURL: queueurl,
    reconnectDelay: 2000,
    onConnect: function (frame) {
      let connectHeaders = { ack: "client" };
      stompClient.subscribe(
        "/exchange/ais-validator/MessagePublishedEvent",
        function (message) {
          setNewMessage(JSON.parse(message.body));
          message.ack();
        },
        connectHeaders
      );
    },
    onStompError: (frame) => {
      console.log("Error from RabbitMQ: " + frame.body);
    },
  };

  const stompClient = new Client(stompConfig);

  const url = "http://localhost:8082/validated";

  useEffect(() => {
    removeOldRecords();
    if (newMessage !== null) {
      if (vesselList.some((vessel) => vessel.mmsi === newMessage.mmsi)) {
        const newState = vesselList.map((vessel) => {
          if (vessel.mmsi === newMessage.mmsi) {
            if (newMessage.validationResult !== null) {
              return {
                ...vessel,
                mmsi: newMessage.mmsi,
                longitude: newMessage.validationResult.longitude,
                latitude: newMessage.validationResult.latitude,
                predictedValue: newMessage.validationResult.predictedValue,
                actualValue: newMessage.validationResult.actualValue,
                error: newMessage.validationResult.error,
                resultSummary: newMessage.validationResult.resultSummary,
                usedModelId: newMessage.validationResult.usedModelId,
                lastUpdated: newMessage.validationResult.lastUpdated,
              };
            } else if (newMessage.externalVerificationResult !== null) {
              return {
                ...vessel,
                name: newMessage.externalVerificationResult.name,
                imo: newMessage.externalVerificationResult.imo,
                callSign: newMessage.externalVerificationResult.callSign,
                failureString: newMessage.externalVerificationResult.failureString,
                lastUpdated: newMessage.externalVerificationResult.lastUpdated,
              };
            }
          }
          return vessel;
        });
        setVesselList(newState);
      } else {
        let newVessel = {};
        if (newMessage.validationResult !== null) {
          let validation = newMessage.validationResult;
          newVessel = {
            mmsi: newMessage.mmsi,
            longitude: validation.longitude,
            latitude: validation.latitude,
            predictedValue: validation.predictedValue,
            actualValue: validation.actualValue,
            error: validation.error,
            resultSummary: validation.resultSummary,
            usedModelId: validation.usedModelId,
            lastUpdated: validation.lastUpdated,
          };
        } else if (newMessage.externalVerificationResult !== null) {
          let verification = newMessage.externalVerificationResult;
          newVessel = {
            mmsi: verification.mmsi,
            name: verification.name,
            imo: verification.imo,
            callSign: verification.callSign,
            failureString: verification.failureString,
            lastUpdated: verification.lastUpdated,
          };
        }
        setVesselList((current) => [...current, newVessel]);
      }
    }
    // eslint-disable-next-line
  }, [newMessage]);

  useEffect(() => {
    fetch(url)
      .then((response) => response.json())
      .then((data) => {
        stompClient.activate();
        const sortedByValidation = [...data].sort((a, b) => {
          if (a.resultSummary === "VERY_LIKELY_SPOOFING" && b.resultSummary !== "VERY_LIKELY_SPOOFING") {
            return -1;
          } else if (a.resultSummary !== "VERY_LIKELY_SPOOFING" && b.resultSummary === "VERY_LIKELY_SPOOFING") {
            return 1;
          } else if (a.resultSummary === "POTENTIAL_SPOOFING" && b.resultSummary === "VALIDATED") {
            return -1;
          } else if (a.resultSummary === "VALIDATED" && b.resultSummary === "POTENTIAL_SPOOFING") {
            return 1;
          } else {
            return 0;
          }
        });
        setVesselList(sortedByValidation);
      });
    // eslint-disable-next-line
  }, []);

  function getMarkerPosition(latitude, longitude, vessel) {
    setMarkerPosition([latitude, longitude]);
    setMarkerInfo(vessel);
  }

  function removeOldRecords() {
    const filtered = vesselList.filter((item) => moment.duration(moment().diff(item.lastUpdated)).asHours() < 1);
    setVesselList(filtered);
  }

  function getSortBy(sortBy) {
    setSortBy(sortBy);
  }

  function sortByProperty(property) {
    if (property === "resultSummary") {
      setVesselList(sortByValidation());
    } else if (property === "failureString") {
      setVesselList(sortByFailure());
    } else if (property === "lastUpdated") {
      setVesselList(sortByTime());
    } else {
      const strAscending = [...vesselList].sort((a, b) => (a[property] > b[property] ? 1 : -1));
      setVesselList(strAscending);
    }
  }

  function reverse() {
    const reversed = [...vesselList].reverse();
    setVesselList(reversed);
  }

  function sortByValidation() {
    const sortedByValidation = [...vesselList].sort((a, b) => {
      if (a.resultSummary === b.resultSummary) {
        return 0;
      } else if (a.resultSummary === "VERY_LIKELY_SPOOFING") {
        return -1;
      } else if (b.resultSummary === "VERY_LIKELY_SPOOFING") {
        return 1;
      } else if (a.resultSummary === "POTENTIAL_SPOOFING" && b.resultSummary !== "VERY_LIKELY_SPOOFING") {
        return -1;
      } else if (a.resultSummary !== "VERY_LIKELY_SPOOFING" && b.resultSummary === "POTENTIAL_SPOOFING") {
        return 1;
      } else if (!a.resultSummary && b.resultSummary === "VALIDATED") {
        return 1;
      } else if (!b.resultSummary && a.resultSummary === "VALIDATED") {
        return -1;
      } else {
        return 0;
      }
    });
    return sortedByValidation;
  }

  function sortByFailure() {
    const sortedByFailure = [...vesselList].sort((a, b) => {
      if (a.failureString !== undefined && b.failureString !== undefined) {
        return 0;
      } else if (a.failureString !== undefined) {
        return -1;
      } else if (b.failureString !== undefined) {
        return 1;
      } else {
        return 0;
      }
    });
    return sortedByFailure;
  }

  function sortByTime() {
    const sortedByTime = [...vesselList].sort((a, b) =>
      moment(b.lastUpdated, "YYYY-MM-DD HH:mm:ss").diff(moment(a.lastUpdated, "YYYY-MM-DD HH:mm:ss"))
    );
    return sortedByTime;
  }

  return (
    <div className="App">
      <h1>AIS Validator</h1>
      <h2>Tool for validating whether the received AIS data can be trusted</h2>
      <Map vesselList={vesselList} markerPosition={markerPosition} markerInfo={markerInfo} />
      <Table
        vesselList={vesselList}
        getMarkerPosition={getMarkerPosition}
        sortByProperty={sortByProperty}
        getSortBy={getSortBy}
        sortBy={sortBy}
        reverse={reverse}
      />
    </div>
  );
}

export default App;
