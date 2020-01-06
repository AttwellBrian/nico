import React from "react";
import * as tools from "../functions";
import { read_cookie } from "sfcookies";

class Login extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      name: "",
      color: "",
      selectedOption: "",
      show: true,
      submitted: false
    };

    this.handleChange = this.handleChange.bind(this);
    this.handleColor = this.handleColor.bind(this);
    this.handleExistingPlayer = this.handleExistingPlayer.bind(this);
    this.handleSubmit = this.handleSubmit.bind(this);
    this.handleReset = this.handleReset.bind(this);
    this.handleStartGame = this.handleStartGame.bind(this);
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
    tools.resetPlayers(this.props.parent);
    this.setState({
      submitted: false
    });
  }

  handleStartGame() {
    tools.apiStartGame(this, this.props.parent);
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
    this.setState({
      submitted: true
    });
    event.preventDefault();
  }

  componentDidUpdate() {
    // manage cookies
    let userCookie = read_cookie("user");

    // set the userProfile from cookie and then bring user to either lobby pt.2 or to the game
    if (typeof userCookie.uuid !== "undefined") {
      if (this.props.parent.state.userProfile.name === "") {
        this.props.parent.setState({
          userProfile: userCookie
        });
      }
      if (this.props.parent.state.gameState.gamePhase === "lobby") {
        if (!this.state.submitted) {
          this.setState({
            submitted: true
          });
        }
      } else if (this.props.parent.state.gameState.gamePhase !== "") {
        if (this.state.show) {
          this.setState({ show: false });
        }
        if (!this.props.parent.state.showGame) {
          this.props.parent.setState({
            showGame: true
          });
        }
      }
    } else {
      // if no cookie exists (or if cookie recently got removed) go back to lobby pt.1
      if (this.state.submitted) {
        this.setState({
          submitted: false
        });
      }
      if (!this.state.show) {
        this.setState({ show: true });
      }
      if (this.props.parent.state.showGame) {
        this.props.parent.setState({
          showGame: false
        });
      }
    }
  }

  componentDidMount() {}

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

    // if color selected is no longer available, reset the color state
    if (
      !colorsAvailable.includes(this.state.color) &&
      this.state.color !== ""
    ) {
      this.setState({ color: "" });
    }

    return (
      <div className={showLoginClassName}>
        <div className="loginBox">
          <div className="logo">Powergrid</div>
          {!this.state.submitted && (
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

              <div className="submitButton">
                <input
                  className="submitButtonButton"
                  type="submit"
                  value="Join Game"
                  disabled={this.state.color === ""}
                />
              </div>
            </form>
          )}

          {this.state.submitted && (
            <div>
              <div className="existingPlayers">
                <h4>Players in Lobby</h4>
                {playersArray.map((player, index) => (
                  <div key={index} className="existingPlayer">
                    {player.name}
                  </div>
                ))}
              </div>
              <div className="startGameButton" onClick={this.handleStartGame}>
                Start Game
              </div>
            </div>
          )}

          <div
            className="existingPlayerReset"
            onClick={() => {
              this.handleReset();
            }}
          >
            Reset
          </div>
        </div>
      </div>
    );
  }
}

export default Login;
