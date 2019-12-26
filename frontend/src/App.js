import React, { useReducer } from "react";
import "./App.css";
import mainMapImage from "./assets/images/usmap-background.jpg";
import { map, userProfile, gameState, powerPlants } from "./fakeData";
import City from "./components/City";
import ConnectionPipe from "./components/ConnectionPipe";
import * as tools from "./functions";

class App extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      userProfile: {
        name: "",
        color: "",
        uuid: "" // uuidABC
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
        ]
      },
      powerPlants: {}
    };
  }

  componentDidUpdate() {}

  componentDidMount() {
    this.setState({
      userProfile: userProfile,
      gameState: gameState,
      powerPlants: powerPlants
    });
  }

  render() {
    // Prep map data
    var citiesArray = [];
    var connectionsArray = [];

    for (var key in map.cities) {
      // add all cities to an array
      citiesArray.push(map.cities[key]);

      // add all connections to an array
      for (var connection in map.cities[key].connections) {
        connectionsArray.push({
          startCity: key,
          endCity: map.cities[key].connections[connection].id,
          cost: map.cities[key].connections[connection].cost
        });
      }
    }

    var cities = citiesArray.map((city, index) => {
      return (
        <City
          key={index}
          name={city.name}
          id={city.id}
          top={city.top}
          left={city.left}
        />
      );
    });

    // Pipe Connections
    // remove repeats
    for (var i in connectionsArray) {
      for (var t in connectionsArray) {
        if (
          connectionsArray[i].startCity === connectionsArray[t].endCity &&
          connectionsArray[i].endCity === connectionsArray[t].startCity
        ) {
          connectionsArray.splice(t, 1);
        }
      }
    }
    // render connections
    var allConnections = connectionsArray.map((connection, index) => {
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
        <div className="topBar"></div>
        <div className="resourcesContainer">
          <div className="containerTitle">Resources Market</div>
        </div>
        <div className="userContainer">
          <div className="containerTitle">{userProfile.name}</div>
        </div>

        <div className="marketContainer">
          <div className="containerTitle">Powerplant Market</div>
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
    );
  }
}

export default App;
