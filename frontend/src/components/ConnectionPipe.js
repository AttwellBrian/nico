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
    // calculate city position
    const city1 = map.cities[this.props.startCity];
    const city2 = map.cities[this.props.endCity];

    let city1XY = {
      x: city1.left * map.width,
      y: (1 - city1.top) * map.height
    };

    let city2XY = {
      x: city2.left * map.width,
      y: (1 - city2.top) * map.height
    };

    // calculate connection length and angle
    let x = city2XY.x - city1XY.x;
    let y = city2XY.y - city1XY.y;
    let hypo = tools.pyth(Math.abs(x), Math.abs(y));
    let angle = -1 * tools.radians_to_degrees(Math.asin(Math.abs(y) / hypo));

    // make adjustments to angles in case it is in different quadrant
    if (x < 0 && y > 0) {
      angle = -180 - angle;
    } else if (y < 0 && x > 0) {
      angle = -1 * angle;
    } else if (x < 0 && y < 0) {
      angle = -180 + angle;
    }

    // output CSS strings
    let cssLength = (100 * hypo) / map.width + "%"; // width
    let cssAngle = "translate(0px,0px) rotate(" + angle + "deg)";

    let positionStyle = {
      top: city1.top * 100 + "%",
      left: city1.left * 100 + "%",
      width: cssLength,
      transform: cssAngle
    };

    let costStyle = {
      top: ((parseFloat(city1.top) + parseFloat(city2.top)) * 100) / 2 + "%",
      left: ((parseFloat(city1.left) + parseFloat(city2.left)) * 100) / 2 + "%"
    };

    return (
      <div>
        <div className="connectionCost" style={costStyle}>
          {this.props.cost}
        </div>
        <div className="connectionPipe" style={positionStyle}></div>
      </div>
    );
  }
}

export default ConnectionPipe;
