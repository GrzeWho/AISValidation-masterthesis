import TableHeader from "./TableHeader";
import TableRow from "./TableRow";
export default function Table(props) {
  return (
    <table>
      <thead>
        <tr>
          <TableHeader
            keyName={"name"}
            title="Vessel name"
            sortBy={props.sortBy}
            getSortBy={props.getSortBy}
            reverse={props.reverse}
            sortByProperty={props.sortByProperty}
          />
          <TableHeader
            keyName={"mmsi"}
            title="MMSI"
            sortBy={props.sortBy}
            getSortBy={props.getSortBy}
            reverse={props.reverse}
            sortByProperty={props.sortByProperty}
          />
          <TableHeader
            keyName={"imo"}
            title="IMO"
            sortBy={props.sortBy}
            getSortBy={props.getSortBy}
            reverse={props.reverse}
            sortByProperty={props.sortByProperty}
          />
          <TableHeader
            keyName={"callSign"}
            title="Callsign"
            sortBy={props.sortBy}
            getSortBy={props.getSortBy}
            reverse={props.reverse}
            sortByProperty={props.sortByProperty}
          />
          <TableHeader
            keyName={"predictedValue"}
            title="Predicted RSSI (dB)"
            sortBy={props.sortBy}
            getSortBy={props.getSortBy}
            reverse={props.reverse}
            sortByProperty={props.sortByProperty}
          />
          <TableHeader
            keyName={"actualValue"}
            title="Actual RSSI (dB)"
            sortBy={props.sortBy}
            getSortBy={props.getSortBy}
            reverse={props.reverse}
            sortByProperty={props.sortByProperty}
          />
          <TableHeader
            keyName={"error"}
            title="Discrepancy (dB)"
            sortBy={props.sortBy}
            getSortBy={props.getSortBy}
            reverse={props.reverse}
            sortByProperty={props.sortByProperty}
          />
          <TableHeader
            keyName={"resultSummary"}
            title="Current position validation state"
            sortBy={props.sortBy}
            getSortBy={props.getSortBy}
            defaultSort={"asc"}
            reverse={props.reverse}
            sortByProperty={props.sortByProperty}
          />
          <TableHeader
            keyName={"failureString"}
            title="Reason for external check failure"
            sortBy={props.sortBy}
            getSortBy={props.getSortBy}
            reverse={props.reverse}
            sortByProperty={props.sortByProperty}
          />
          <TableHeader
            keyName={"lastUpdated"}
            title="Last updated"
            sortBy={props.sortBy}
            getSortBy={props.getSortBy}
            reverse={props.reverse}
            sortByProperty={props.sortByProperty}
          />
        </tr>
      </thead>
      <tbody>
        {props.vesselList.map((data) => (
          <TableRow vesselData={data} getMarkerPosition={props.getMarkerPosition} key={data.mmsi} />
        ))}
      </tbody>
    </table>
  );
}
