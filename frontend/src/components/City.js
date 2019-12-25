import React from "react";

class City extends React.Component {
  constructor(props) {
    super(props);
    this.state = {};
  }

  componentDidUpdate() {}

  render() {
    // render city position
    var cssBottom = this.props.y * 100 + "%";
    var cssLeft = this.props.x * 100 + "%";
    var positionStyle = {
      bottom: cssBottom,
      left: cssLeft
    };

    return (
      <div>
        <div
          className="cityContainer"
          style={positionStyle}
          id={"ID" + this.props.id}
        >
          <div className="cityName">{this.props.name}</div>
        </div>
      </div>
    );
  }
}

export default City;
