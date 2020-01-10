import React from "react";
import * as tools from "../functions";

class PassButton extends React.Component {
  constructor(props) {
    super(props);
    this.state = {};

    this.handlePass = this.handlePass.bind(this);
  }

  handlePass() {
    tools.apiAuctionAction("PASS", this.props.uuid);
  }

  componentDidUpdate() {}

  render() {
    let bidButtonClass = "bidButton";
    if (this.state.value === this.props.minValue) {
      bidButtonClass = "bidDisabled";
    }

    return <button onClick={this.handlePass}>Pass</button>;
  }
}

export default PassButton;
