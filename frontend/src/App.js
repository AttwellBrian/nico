import React from "react";
import "./App.css";
import mainMapImage from "./assets/images/usmap-background.jpg";
import { map } from "./fakeData";
import City from "./components/City";
import ConnectionPipe from "./components/ConnectionPipe";
import * as tools from "./functions";

function App() {
  // prep map
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

  // Render Connections
  // list all connections
  // Remove Repeats
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
  console.log(connectionsArray);

  return (
    <div className="App">
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

export default App;
