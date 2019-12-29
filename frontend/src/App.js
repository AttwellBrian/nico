import React from "react";
import "./App.css";
import mainMapImage from "./assets/images/usmap-background.jpg";
import {
  map,
  userProfile,
  gameState,
  gameStatePre,
  powerPlants
} from "./fakeData";
import City from "./components/City";
import PowerPlantMarket from "./components/PowerPlantMarket";
import ConnectionPipe from "./components/ConnectionPipe";
import Login from "./components/Login";

import { Button } from "reactstrap";
import * as tools from "./functions";

class App extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      userProfile: {
        name: "",
        color: "",
        uuid: "PLYR1" // uuidABC
      },
      gameState: {
        players: {
          uuidABC: {
            name: "",
            color: ""
          }
        },
        playerOrder: [
          /*
          "uuidABC",
          "uuidABC2"
          */
        ],
        currentPlayer: "", // uuid
        ownedPowerPlants: {
          /* 
          uuidABC: [
            "plantUUID",
            "plantUUID",
            "plantUUID",
            "plantUUID"
          ]
          */
        },
        ownedResources: {
          /*
          uuidABC: {
            money: 95,
            coal: 25,
            garbage: 15,
            uranium: 12,
            oil: 22
          }
          */
        },
        ownedCities: {
          /*
          uuidABC: [
            "cityID",
            "cityID",
            "cityID",
            "cityID"
          ]
          */
        },
        actualMarket: [
          /*
          "plantUUID",
          "plantUUID",
          "plantUUID",
          "plantUUID",
          */
        ],
        futureMarket: [
          /*
          "plantUUID",
          "plantUUID",
          "plantUUID",
          "plantUUID",
          */
        ],
        gamePhase: "",
        currentBid: 0
      },
      powerPlants: {},
      showGame: false,
      showPowerPlantMarket: false,
      highestBidder: "",
      selectedCities: [],
      costToPurchaseCities: 0
    };
    this.toggleShowPowerPlantModal = this.toggleShowPowerPlantModal.bind(this);
    this.addRemoveSelectedCity = this.addRemoveSelectedCity.bind(this);
  }

  toggleShowPowerPlantModal() {
    this.setState(prevState => ({
      showPowerPlantMarket: !prevState.showPowerPlantMarket
    }));
  }

  addRemoveSelectedCity = (city, addTrue) => {
    // make a new array of temporarily selected power plants
    let selectedCities = this.state.selectedCities;
    if (addTrue === true) {
      selectedCities.push(city);
    }
    if (addTrue === false) {
      let targetIndex = selectedCities.indexOf(city);
      selectedCities.splice(targetIndex, 1);
    }

    console.log(selectedCities);
  };

  componentDidUpdate() {}

  componentDidMount() {
    // first ping the API for the first initial state
    // FAKE API RETURN HERE (actually, we imported it from fakeData.js)
    // then set initial state so there's a state
    let self = this;
    this.setState(
      {
        userProfile: userProfile,
        gameState: gameStatePre,
        powerPlants: powerPlants
      },
      () => {
        // after first state is set, setInterval to ping gameState API every 100ms
        this.interval = setInterval(function() {
          // ping API every 100ms
          // FAKE API RETURN HERE (actually we imported it from fakeData.js)

          // Now run all the callback checks
          // Check to see if it is the first time we are entering auctionPickPlant so we can pop the modal up
          if (
            self.state.gameState.gamePhase !== "auctionPickPlant" &&
            gameState.gamePhase === "auctionPickPlant" &&
            self.state.showPowerPlantMarket === false
          ) {
            self.setState({
              showPowerPlantMarket: true
            });
          }

          // Finally, update the gameState
          self.setState({
            gameState: gameState
          });
        }, 100);
      }
    );
  }

  componentWillUnmount() {
    clearInterval(this.interval);
  }

  render() {
    // login prep

    // Prep map data
    let citiesArray = [];
    let connectionsArray = [];

    for (let key in map.cities) {
      // add all cities to an array
      citiesArray.push(map.cities[key]);

      // add all connections to an array
      for (let connection in map.cities[key].connections) {
        connectionsArray.push({
          startCity: key,
          endCity: map.cities[key].connections[connection].id,
          cost: map.cities[key].connections[connection].cost
        });
      }
    }

    const cities = citiesArray.map((city, index) => {
      return (
        <City
          key={index}
          name={city.name}
          id={city.id}
          top={city.top}
          left={city.left}
          addRemoveSelectedCity={this.addRemoveSelectedCity}
        />
      );
    });

    // Pipe Connections
    // remove repeats
    let uniqueConnectionsArray = [];

    for (let i in connectionsArray) {
      // create an array of duplicate connections
      let duplicatesArray = uniqueConnectionsArray.filter(
        // eslint-disable-next-line no-loop-func
        connection =>
          (connection.startCity === connectionsArray[i].startCity &&
            connection.endCity === connectionsArray[i].endCity) ||
          (connection.startCity === connectionsArray[i].endCity &&
            connection.endCity === connectionsArray[i].startCity)
      );
      // if no duplicates, then add new unique connection
      if (duplicatesArray.length === 0) {
        uniqueConnectionsArray.push(connectionsArray[i]);
      }
    }

    // render connections
    const allConnections = uniqueConnectionsArray.map((connection, index) => {
      return (
        <ConnectionPipe
          key={index}
          startCity={connection.startCity}
          endCity={connection.endCity}
          cost={connection.cost}
        />
      );
    });

    return (
      <div className="App">
        <Login parent={this} gameState={this.state.gameState} />
        {this.state.showGame === true && (
          <div>
            <div className="topBar"></div>
            <div className="resourcesContainer">
              <div className="containerTitle">Resources Market</div>
            </div>
            <div className="userContainer">
              <div className="containerTitle">{userProfile.name}</div>
            </div>

            <div className="marketContainer">
              <PowerPlantMarket
                state={this.state}
                parentToggle={this.toggleShowPowerPlantModal}
              />
            </div>
            <div className="playersContainer">
              <div className="containerTitle">The Competition</div>
            </div>
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
