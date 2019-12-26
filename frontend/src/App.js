import React from "react";
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
