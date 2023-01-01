import "./leaflet.css";
import { MapContainer, TileLayer, CircleMarker, Popup, Marker, Tooltip } from "react-leaflet";
import Rainbow from "rainbowvis.js";
import { useEffect, useRef } from "react";
export default function Map(props) {
  const mapRef = useRef(null);

  useEffect(() => {
    if (props.vesselList.length && mapRef && mapRef.current._renderer._center.lat === 0) {
      mapRef.current.flyTo([props.vesselList[0].latitude, props.vesselList[0].longitude], 10);
    }
  });

  function getColorBasedOnError(actualError) {
    let colorSelector = new Rainbow();
    colorSelector.setSpectrum("green", "yellow", "red");
    colorSelector.setNumberRange(
      0,
      Math.max.apply(
        Math,
        props.vesselList.filter((vessel) => vessel.actualValue).map((vessel) => Math.abs(vessel.actualValue))
      )
    );
    return colorSelector.colorAt(actualError);
  }

  function setColor(vessel) {
    if (vessel.resultSummary === "POTENTIAL_SPOOFING") return "yellow";
    else if (vessel.resultSummary === "VERY_LIKELY_SPOOFING" || vessel.failureString) return "red";
    else return "#" + getColorBasedOnError(vessel.error);
  }

  return (
    <MapContainer ref={mapRef} center={[0, 0]} zoom={2} scrollWheelZoom={true} style={{ height: "600px", margin: "1rem" }}>
      <TileLayer
        attribution='&copy; <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a> contributors'
        url="https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png"
      />
      {props.vesselList.map((vessel) =>
        Object.hasOwn(vessel, "longitude") ? (
          <CircleMarker
            key={vessel.mmsi}
            center={[vessel.latitude, vessel.longitude]}
            radius={6}
            pathOptions={{ color: setColor(vessel) }}
          >
            <Popup>
              {vessel.name && (
                <div>
                  Name: {vessel.name}
                  <br />
                </div>
              )}
              MMSI: {vessel.mmsi}
              <br />
              Lat: {vessel.latitude}
              <br />
              Lon: {vessel.longitude}
              <br />
              Error: {vessel.error}
            </Popup>
          </CircleMarker>
        ) : (
          <></>
        )
      )}
      {props.markerPosition !== null && (
        <Marker position={props.markerPosition}>
          <Tooltip>
            <span>
              {props.markerInfo.name && (
                <div>
                  Name: {props.markerInfo.name}
                  <br />
                </div>
              )}
              MMSI: {props.markerInfo.mmsi}
              <br />
              Lat: {props.markerInfo.latitude}
              <br />
              Lon: {props.markerInfo.longitude}
              <br />
              Error: {props.markerInfo.error}
            </span>
          </Tooltip>
        </Marker>
      )}
    </MapContainer>
  );
}
