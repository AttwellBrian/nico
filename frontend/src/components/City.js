import React from "react";

class City extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      clicked: false
    };
    this.toggleClick = this.toggleClick.bind(this);
  }

  toggleClick() {
    this.setState(prevState => ({
      clicked: !prevState.clicked
    }));
  }

  componentDidUpdate() {}

  render() {
    // render city position
    const cssTop = this.props.top * 100 + "%";
    const cssLeft = this.props.left * 100 + "%";
    const positionStyle = {
      top: cssTop,
      left: cssLeft
    };

    let owned = false;
    let selected = "";
    if (this.state.clicked === true) {
      selected = "citySelected";
    }

    return (
      <div>
        <div
          className={"cityContainer " + selected}
          style={positionStyle}
          id={"ID" + this.props.id}
          disabled={owned}
          onClick={this.toggleClick}
        >
          <div className="cityName">{this.props.name}</div>
        </div>
      </div>
    );
  }
}

export default City;
