import { useEffect, useState } from "react";

export default function TableHeader(props) {
  const [sortDir, setSortDir] = useState(props.defaultSort || "");

  useEffect(() => {
    if (props.sortBy !== props.keyName) {
      setSortDir("");
    }
    // eslint-disable-next-line
  }, [props.sortBy]);

  function handleClick() {
    if (sortDir === "asc") {
      setSortDir("desc");
      props.sortByProperty(props.keyName);
      props.reverse();
    } else {
      setSortDir("asc");
      props.sortByProperty(props.keyName);
    }
    props.getSortBy(props.keyName);
  }

  return (
    <th onClick={handleClick} className={sortDir}>
      {props.title}
    </th>
  );
}
