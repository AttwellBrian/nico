import React from "react";

class City extends React.Component {
  constructor(props) {
    super(props);
    this.state = {};
  }

  componentDidUpdate() {}

  render() {
    // render city position
    var cssTop = this.props.top * 100 + "%";
    var cssLeft = this.props.left * 100 + "%";
    var positionStyle = {
      top: cssTop,
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
