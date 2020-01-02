import axios from "axios";
import * as constants from "./constants";

function pyth(x, y) {
  if (typeof x !== "number" || typeof y !== "number") return false;
  return Math.sqrt(x * x + y * y);
}

function radians_to_degrees(radians) {
  var pi = Math.PI;
  return radians * (180 / pi);
}

function generateMap(that) {
  // login prep
  // Prep map data
  let map = constants.map;
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

  // Disable logic for cities that can't be bought (cities that aren't connected to existing cities)

  var ownedCities = {};
  let connectedCities = [];
  let selectedCities = that.state.selectedCities;

  // if user owns cities, add those as connections
  if (
    Object.keys(that.state.gameState.ownedCities).length > 0 &&
    that.state.userProfile.uuid
  ) {
    ownedCities = that.state.gameState.ownedCities[that.state.userProfile.uuid];
    for (let i = 0; i < ownedCities.length; i++) {
      for (let t in map.cities[ownedCities[i]].connections) {
        connectedCities.push(map.cities[ownedCities[i]].connections[t].id);
      }
    }
  }

  // if user has selected cities, add those as connections
  if (selectedCities.length > 0) {
    for (let i = 0; i < selectedCities.length; i++) {
      for (let t in map.cities[selectedCities[i]].connections) {
        connectedCities.push(map.cities[selectedCities[i]].connections[t].id);
      }
    }
  }

  // remove duplicates from connections
  connectedCities = [...new Set(connectedCities)];
  // remove owned cities from connections
  for (let i = 0; i < ownedCities.length; i++) {
    if (connectedCities.includes(ownedCities[i])) {
      let targetIndex = connectedCities.indexOf(ownedCities[i]);
      connectedCities.splice(targetIndex, 1);
    }
  }

  // if existing selected city is no longer connected, remove it
  if (selectedCities.length > 0) {
    for (let i = 0; i < selectedCities.length; i++) {
      if (!connectedCities.includes(selectedCities[i])) {
        let targetIndex = selectedCities.indexOf(selectedCities[i]);
        selectedCities.splice(targetIndex, 1);
        if (that.state.selectedCities !== selectedCities) {
          that.setState({
            selectedCities: selectedCities
          });
        }
      }
    }
  }

  let uniqueConnectionsArray = [];

  // remove repeated connections
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

  return {
    citiesArray: citiesArray,
    ownedCities: ownedCities,
    connectedCities: connectedCities,
    selectedCities: selectedCities,
    connectionsArray: connectionsArray,
    uniqueConnectionsArray: uniqueConnectionsArray
  };
}

function replaceEditMatch(
  apiState,
  outputState,
  serverName,
  clientName,
  arrayToMapTrue,
  serverKeys,
  clientKeys
) {
  // if we need to covert an array to an object
  if (arrayToMapTrue) {
    // create object from array
    for (let i = 0; i < apiState[serverName].length; i++) {
      for (let t = 0; t < serverKeys.length; t++) {
        if (!outputState[clientName]) {
          outputState[clientName] = {};
        }
        if (!outputState[clientName][apiState[serverName][i].id]) {
          outputState[clientName][apiState[serverName][i].id] = {};
        }
        outputState[clientName][apiState[serverName][i].id][clientKeys[t]] =
          apiState[serverName][i][serverKeys[t]];
      }
    }
    return outputState[clientName];
  } else {
    // otherwise, just translate the words
    if (apiState[serverName]) {
      // special condition for powerplant markets
      if (serverName === "actualMarket" || serverName === "futureMarket") {
        let translatedArray = [];
        for (let i in apiState[serverName]) {
          let tempObject = {};
          for (let t in serverKeys) {
            tempObject[clientKeys[t]] = apiState[serverName][i][serverKeys[t]];
          }
          translatedArray.push(tempObject);
        }
        return translatedArray;
      } else {
        // for all other fields with translations
        if (serverKeys.includes(apiState[serverName])) {
          let translationKeyIndex = 0;
          translationKeyIndex = serverKeys.indexOf(apiState[serverName]);
          return clientKeys[translationKeyIndex];
        } else {
          // if no translation needed, return the value from the server
          return apiState[serverName];
        }
      }
    }
  }
}

function translateState(apiState) {
  console.log(constants.emptyGameStateSchema);
  let outputState = constants.emptyGameStateSchema; // define an empty state schema to start

  let replaceEditMatchArray = constants.translationsNeeded;

  for (let i = 0; i < replaceEditMatchArray.length; i++) {
    outputState[replaceEditMatchArray[i].clientName] = replaceEditMatch(
      apiState,
      outputState,
      replaceEditMatchArray[i].serverName,
      replaceEditMatchArray[i].clientName,
      replaceEditMatchArray[i].arrayToMapTrue,
      replaceEditMatchArray[i].serverKeys,
      replaceEditMatchArray[i].clientKeys
    );
  }

  return outputState;
}

function apiUpdateState(that) {
  that.setState(
    {
      userProfile: constants.emptyClientGameStateSchema.userProfile,
      gameState: constants.emptyGameStateSchema
    },
    () => {
      // after first state is set, setInterval to ping gameState API every 100ms
      that.interval = setInterval(function() {
        axios.get(constants.serverURL + `/state`).then(data => {
          // take the returned data and translate it
          const translatedState = translateState(data.data);
          // CALLBACK CHECKS

          // 1. Check to see if we are entering auctionPickPlant so we can pop the modal up
          if (
            translatedState.gamePhase !== "auctionPickPlant" &&
            translatedState.gamePhase === "auctionPickPlant" &&
            that.state.showPowerPlantMarket === false
          ) {
            that.setState({
              showPowerPlantMarket: true
            });
          }

          // CALLBACK CHECKS COMPLETE

          // Update the gameState
          that.setState({
            gameState: translatedState
          });
        });
        // do every 100ms
      }, 100);
    }
  );
}

function apiCreateUser(player, loginScreen, parent) {
  axios
    // get data from server
    .post(
      constants.serverURL +
        "/createuser?name=" +
        player.name +
        "&color=" +
        player.color
    )
    .then(data => {
      console.log(data);
      // hide the login screen
      loginScreen.setState({ show: false });
      parent.setState({
        // set the client-side user profile
        userProfile: {
          name: player.name,
          uuid: data.user.id,
          color: player.color
        },
        // show game screen
        showGame: true
      });
    });
}

function apiStartGame() {}

function apiAuctionAction() {
  // note for format later: {"userId":"d108e378-ee12-441a-bc3a-524db44a4529","actionType":"CHOOSE_PLANT", "choosePlantId":"b182b03f-b23c-463e-8f6c-0abe063c1aa2", "bid": 1}
}

// API CALLS
function bidOnPowerPlant(userId, plantId) {
  alert(userId + " bid on power plant " + plantId);
}

function resetPlayers() {
  console.log(resetPlayers);
}

function newPlayer(player) {
  console.log(player.name + " (" + player.color + ") has joined");
}

export {
  pyth,
  radians_to_degrees,
  generateMap,
  apiUpdateState,
  bidOnPowerPlant,
  resetPlayers,
  newPlayer,
  apiCreateUser,
  apiStartGame,
  apiAuctionAction,
  translateState,
  replaceEditMatch
};
