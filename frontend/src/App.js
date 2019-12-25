import React from "react";
import "./App.css";
import mainMapImage from "./assets/images/usmap-background.jpg";
import { map } from "./fakeData";
import City from "./components/City";
import ConnectionPipe from "./components/ConnectionPipe";
import * as tools from "./functions";

function App() {
  var citiesArray = [];

  // Render Cities

  for (var key in map.cities) {
    citiesArray.push(map.cities[key]);
  }

  var cities = citiesArray.map((city, index) => {
    return (
      <City key={index} name={city.name} id={city.id} x={city.x} y={city.y} />
    );
  });

  // Render Connections

  return (
    <div className="App">
      <div className="connections">
        <div className="connectionPipesContainer">
          <ConnectionPipe startCity="" endCity="" cost={10} />
        </div>
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
