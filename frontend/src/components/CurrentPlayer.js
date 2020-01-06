import React from "react";

class CurrentPlayer extends React.Component {
  constructor(props) {
    super(props);
    this.state = {};
  }

  componentDidUpdate() {}

  render() {
    let currentPlayerText = "";
    let currentPlayerLight = "";
    if (this.props.currentPlayer) {
      if (this.props.currentPlayer === this.props.userProfile.uuid) {
        currentPlayerText = <strong>It's your turn.</strong>;
        currentPlayerLight = "green";
      } else {
        currentPlayerText =
          "It's " +
          this.props.players[this.props.currentPlayer].name +
          " turn to go.";
      }
    }

    return (
      <div className="currentPlayer">
        <div className={currentPlayerLight + "Light"}></div>
        <div>{currentPlayerText}</div>
      </div>
    );
  }
}

export default CurrentPlayer;
