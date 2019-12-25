import React from "react";
import * as tools from "../functions";
import { map } from "../fakeData";

class ConnectionPipe extends React.Component {
  constructor(props) {
    super(props);
    this.state = {};
  }

  componentDidUpdate() {}

  render() {
    // render city position

    var city1 = {
      x: 0.1,
      y: 0.9
    };

    var city2 = {
      x: 0.3,
      y: 0.8
    };

    var length =
      tools.pyth(Math.abs(city2.x - city1.x), Math.abs(city2.y - city1.y)) +
      "%";

    console.log(length);

    var positionStyle = {
      left: "10%",
      bottom: "90%",
      width: length,
      transform: "rotate(20deg)"
    };

    return <div className="connectionPipe" style={positionStyle}></div>;
  }
}

export default ConnectionPipe;
