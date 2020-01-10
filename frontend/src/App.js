import React from "react";
import "./App.css";
import mainMapImage from "./assets/images/usmap-background.jpg";
import City from "./components/City";
import PowerPlantMarket from "./components/PowerPlantMarket";
import ResourcesMarket from "./components/ResourcesMarket";
import ConnectionPipe from "./components/ConnectionPipe";
import CurrentPlayer from "./components/CurrentPlayer";
import PlayersTray from "./components/PlayersTray";

import Login from "./components/Login";
import * as tools from "./functions";
import * as constants from "./constants";

class App extends React.Component {
  constructor(props) {
    super(props);
    this.state = constants.emptyClientGameStateSchema;
    this.toggleShowPowerPlantModal = this.toggleShowPowerPlantModal.bind(this);
    this.toggleShowResourcesModal = this.toggleShowResourcesModal.bind(this);
    this.addRemoveSelectedCity = this.addRemoveSelectedCity.bind(this);
    this.handleReset = this.handleReset.bind(this);
  }

  toggleShowPowerPlantModal() {
    this.setState(prevState => ({
      showPowerPlantMarket: !prevState.showPowerPlantMarket
    }));
  }

  toggleShowResourcesModal() {
    this.setState(prevState => ({
      showResourcesMarket: !prevState.showResourcesMarket
    }));
  }

  addRemoveSelectedCity = city => {
    // make a new array of temporarily selected power plants
    let selectedCities = this.state.selectedCities;
    if (!selectedCities.includes(city)) {
      selectedCities.push(city);
    } else {
      let targetIndex = selectedCities.indexOf(city);
      selectedCities.splice(targetIndex, 1);
    }
  };

  handleReset() {
    tools.resetPlayers(this);
  }

  componentDidUpdate() {}

  componentDidMount() {
    // create the 100ms interval for state ping from server
    tools.apiUpdateStateInterval(this);
  }

  componentWillUnmount() {
    clearInterval(this.interval);
  }

  render() {
    const generatedMap = tools.generateMap(this);

    const cities = generatedMap.citiesArray.map((city, index) => {
      // disable city is owned
      let disabled = "";
      if (
        Object.keys(this.state.gameState.ownedCities).length > 0 &&
        this.state.userProfile.uuid
      ) {
        if (generatedMap.ownedCities.includes(city.id)) {
          disabled = "disabled";
        }
      }

      // disable if city is not in connected cities
      if (!generatedMap.connectedCities.includes(city.id)) {
        disabled = "disabled";
      }

      let selected = false;
      if (generatedMap.selectedCities.includes(city.id)) {
        selected = true;
      }

      return (
        <City
          key={index}
          name={city.name}
          id={city.id}
          top={city.top}
          left={city.left}
          addRemoveSelectedCity={this.addRemoveSelectedCity}
          disabled={disabled}
          ownedCities={
            this.state.gameState.ownedCities[this.state.userProfile.uuid]
          }
          selectedCities={this.state.selectedCities}
          selected={selected}
        />
      );
    });

    // Pipe Connections
    const allConnections = generatedMap.uniqueConnectionsArray.map(
      (connection, index) => {
        return (
          <ConnectionPipe
            key={index}
            startCity={connection.startCity}
            endCity={connection.endCity}
            cost={connection.cost}
          />
        );
      }
    );

    return (
      <div className="App">
        <Login parent={this} gameState={this.state.gameState} />
        {this.state.showGame === true && (
          <div>
            <div className="topBar">
              <div className="endGameButton" onClick={this.handleReset}>
                End Game
              </div>
              <CurrentPlayer
                currentPlayer={this.state.gameState.currentPlayer}
                userProfile={this.state.userProfile}
                players={this.state.gameState.players}
                ownedPlants={
                  this.state.gameState.ownedPowerPlants[
                    this.state.userProfile.uuid
                  ]
                }
              />
            </div>
            <div className="resourcesContainer">
              <div className="containerTitle">Resources Market</div>
            </div>
            <PlayersTray state={this.state} />

            <div className="marketContainer">
              <PowerPlantMarket
                state={this.state}
                parentToggle={this.toggleShowPowerPlantModal}
              />
              <ResourcesMarket
                state={this.state}
                parentToggle={this.toggleShowResourcesModal}
              />
            </div>
            {/* <div className="playersContainer">
              <div className="containerTitle">The Competition</div>
            </div> */}

            <div className="connections">
              <div className="connectionPipesContainer">{allConnections} </div>
            </div>
            <div className="mainMap">
              {cities}
              <img
                className="mainMapImage"
                alt="Map of US Background"
                src={mainMapImage}
              />
            </div>
          </div>
        )}
      </div>
    );
  }
}

export default App;
