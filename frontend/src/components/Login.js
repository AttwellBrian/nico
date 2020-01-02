import React from "react";
import * as tools from "../functions";

class Login extends React.Component {
  constructor(props) {
    super(props);
    this.state = { name: "", color: "", selectedOption: "", show: true };

    this.handleChange = this.handleChange.bind(this);
    this.handleColor = this.handleColor.bind(this);
    this.handleExistingPlayer = this.handleExistingPlayer.bind(this);
    this.handleSubmit = this.handleSubmit.bind(this);
    this.handleReset = this.handleReset.bind(this);
  }

  handleChange(event) {
    this.setState({ name: event.target.value });
  }

  handleColor(event) {
    this.setState({ color: event.target.value });
  }

  handleExistingPlayer(player) {
    this.setState({ show: false });
    this.props.parent.setState({
      userProfile: {
        name: player.name,
        uuid: player.uuid,
        color: player.color
      },
      showGame: true
    });
  }

  handleReset() {
    tools.resetPlayers();
  }

  handleSubmit(event) {
    if (this.state.name === "") {
      var playerName = "Poopypants";
    } else {
      playerName = this.state.name;
    }
    // send to new player
    tools.apiCreateUser(
      { name: playerName, color: this.state.color },
      this,
      this.props.parent
    );
    event.preventDefault();
  }

  render() {
    var showLoginClassName = "loginPage";

    if (this.state.show === false) {
      showLoginClassName = "loginPage hide";
    }

    let playersArray = [];

    let colorsPossible = ["Red", "White", "Blue", "Green", "Yellow"];

    let colorsUsed = [];
    let colorsAvailable = [];

    for (let key in this.props.gameState.players) {
      colorsUsed.push(this.props.gameState.players[key].color);
      playersArray.push({
        name: this.props.gameState.players[key].name,
        uuid: key,
        color: this.props.gameState.players[key].color
      });
    }

    for (var i = 0; i < colorsPossible.length; i++) {
      if (!colorsUsed.includes(colorsPossible[i])) {
        colorsAvailable.push(colorsPossible[i]);
      }
    }

    return (
      <div className={showLoginClassName}>
        <div className="loginBox">
          <div className="logo">Powergrid</div>
          <form onSubmit={this.handleSubmit}>
            <label>
              <div className="labelName">Your Company Name:</div>
              <input
                className="inputName"
                type="text"
                value={this.state.name}
                onChange={this.handleChange}
              />
            </label>
            <br />
            <div className="rocketSelection">
              {colorsAvailable.map(color => (
                <label key={color}>
                  <div key={color} className={color}>
                    <input
                      type="radio"
                      name={color}
                      value={color}
                      onChange={this.handleColor}
                      checked={this.state.color === color}
                    />
                  </div>
                </label>
              ))}
            </div>
            <div className="existingPlayers">
              {playersArray.map((player, index) => (
                <div
                  key={index}
                  className="existingPlayer"
                  onClick={() => {
                    this.handleExistingPlayer(player);
                  }}
                >
                  {player.name}
                </div>
              ))}
              <div
                className="existingPlayerReset"
                onClick={() => {
                  this.handleReset();
                }}
              >
                Reset
              </div>
            </div>
            <div className="submitButton">
              <input
                className="submitButtonButton"
                type="submit"
                value="Join Game"
              />
            </div>
          </form>
        </div>
      </div>
    );
  }
}

export default Login;
