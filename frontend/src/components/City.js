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
    this.props.addRemoveSelectedCity(this.props.id);
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

    let selected = "";
    if (this.props.selected === true) {
      selected = "citySelected";
    }

    return (
      <div>
        <div
          className={"cityContainer " + selected + " " + this.props.disabled}
          style={positionStyle}
          id={"ID" + this.props.id}
          onClick={this.props.disabled === "" ? this.toggleClick : undefined}
        >
          <div className="cityName">{this.props.name}</div>
        </div>
      </div>
    );
  }
}

export default City;
