import moment from "moment";
export default function TableRow(props) {
  function handleClick() {
    if (props.vesselData.longitude && props.vesselData.latitude) {
      props.getMarkerPosition(props.vesselData.latitude, props.vesselData.longitude, props.vesselData);
      window.scrollTo({ top: 0, left: 0, behavior: "smooth" });
    }
    console.log(props.vesselData);
  }

  return (
    <tr
      className={`tableRow ${
        props.vesselData.failureString || props.vesselData.resultSummary === "VERY_LIKELY_SPOOFING"
          ? "redBG"
          : props.vesselData.resultSummary === "POTENTIAL_SPOOFING"
          ? "yellowBG"
          : ""
      }`}
      onClick={handleClick}
    >
      <td className="bold">{props.vesselData.name}</td>
      <td className="bold">{props.vesselData.mmsi}</td>
      <td>{props.vesselData.imo}</td>
      <td>{props.vesselData.callSign}</td>
      <td>
        {typeof props.vesselData.predictedValue === "number"
          ? props.vesselData.predictedValue.toFixed(2)
          : "Position message not received"}
      </td>
      <td>
        {typeof props.vesselData.actualValue === "number"
          ? props.vesselData.actualValue.toFixed(2)
          : "Position message not received"}
      </td>
      <td>{typeof props.vesselData.error === "number" ? props.vesselData.error.toFixed(2) : "Position message not received"}</td>
      <td>{props.vesselData.resultSummary}</td>
      <td>{props.vesselData.failureString}</td>
      <td>{moment(props.vesselData.lastUpdated, "YYYY-MM-DD HH:mm:ss").fromNow()}</td>
    </tr>
  );
}
