import axios from "axios";

function pyth(x, y) {
  if (typeof x !== "number" || typeof y !== "number") return false;
  return Math.sqrt(x * x + y * y);
}

function radians_to_degrees(radians) {
  var pi = Math.PI;
  return radians * (180 / pi);
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
    // just return a specific value
    if (apiState[serverName]) {
      // check to see if value also has a translation map
      if (serverKeys.includes(apiState[serverName])) {
        let translationKeyIndex = 0;
        translationKeyIndex = serverKeys.indexOf(apiState[serverName]);
        return clientKeys[translationKeyIndex];
      } else {
        // if not, return the value from the server
        return apiState[serverName];
      }
    }
  }
}

function translateState(apiState) {
  let outputState = {
    players: {},
    playerOrder: [],
    currentPlayer: "",
    ownedPowerPlants: {},
    ownedResources: {},
    ownedCities: {},
    actualMarket: [],
    futureMarket: [],
    gamePhase: "",
    currentBid: 0,
    highestBidder: ""
  };

  let replaceEditMatchArray = [
    {
      serverName: "users",
      clientName: "players",
      arrayToMapTrue: true,
      serverKeys: ["name", "color"],
      clientKeys: ["name", "color"]
    },
    {
      serverName: "currentPlayerOrder",
      clientName: "playerOrder",
      arrayToMapTrue: false,
      serverKeys: [],
      clientKeys: []
    },
    {
      serverName: "currentUser",
      clientName: "currentPlayer",
      arrayToMapTrue: false,
      serverKeys: [],
      clientKeys: []
    },
    {
      serverName: "userPowerPlants",
      clientName: "ownedPowerPlants",
      arrayToMapTrue: false,
      serverKeys: [],
      clientKeys: []
    },
    {
      serverName: "userWithHighestPowerplantBid",
      clientName: "highestBidder",
      arrayToMapTrue: false,
      serverKeys: [],
      clientKeys: []
    },
    {
      serverName: "actualMarket",
      clientName: "actualMarket",
      arrayToMapTrue: false,
      serverKeys: [],
      clientKeys: []
    },
    {
      serverName: "futureMarket",
      clientName: "futureMarket",
      arrayToMapTrue: false,
      serverKeys: [],
      clientKeys: []
    },
    {
      serverName: "userMoney",
      clientName: "ownedMoney",
      arrayToMapTrue: false,
      serverKeys: [],
      clientKeys: []
    },
    {
      serverName: "userResources",
      clientName: "ownedResources",
      arrayToMapTrue: false,
      serverKeys: [],
      clientKeys: []
    },
    {
      serverName: "currentPowerPlantBid",
      clientName: "currentBid",
      arrayToMapTrue: false,
      serverKeys: [],
      clientKeys: []
    },
    {
      serverName: "gamePhase",
      clientName: "gamePhase",
      arrayToMapTrue: false,
      serverKeys: ["LOBBY", "AUCTION_PICK_PLANT", "AUCTION_BIDDING"],
      clientKeys: ["lobby", "auctionPickPlant", "auctionBidding"]
    }
  ];

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

  console.log(outputState);

  /*
  return {
    players: {
      PLYR1: {
        name: "Lightning Bug, LLC.",
        color: "Red"
      },
      PLYR2: {
        name: "Oil Barron, Inc.",
        color: "Black"
      },
      PLYR3: {
        name: "Armco, Co.",
        color: "Blue"
      }
    },
    playerOrder: ["PLYR2", "PLYR3", "PLYR1"],
    currentPlayer: "PLYR1",
    ownedPowerPlants: {
      PLYR1: ["1", "2", "3"],
      PLYR2: ["4", "5", "6"],
      PLYR3: ["7", "8", "9"]
    },
    ownedResources: {
      PLYR1: {
        money: 40,
        coal: 5,
        garbage: 2,
        uranium: 1,
        oil: 3
      },
      PLYR2: {
        money: 20,
        coal: 1,
        garbage: 0,
        uranium: 3,
        oil: 2
      },
      PLYR3: {
        money: 95,
        coal: 5,
        garbage: 1,
        uranium: 5,
        oil: 10
      }
    },
    ownedCities: {
      PLYR1: ["portland"],
      PLYR2: ["boise"],
      PLYR3: ["billings"]
    },
    actualMarket: ["18", "19", "20", "21"],
    futureMarket: ["30", "31", "28", "25"],
    gamePhase: "auctionBidding", // "auctionPickPlant", "auctionBidding",
    currentBid: 0,
    highestBidder: "PLYR3"
  };
  */
}

function apiUpdateState() {
  axios.get(`http://localhost:8080/state`).then(data => {
    translateState(data.data);
    // create players object
  });
}

function apiCreateUser() {}

function apiStartGame() {}

function apiAuctionAction() {
  // {"userId":"d108e378-ee12-441a-bc3a-524db44a4529","actionType":"CHOOSE_PLANT", "choosePlantId":"b182b03f-b23c-463e-8f6c-0abe063c1aa2", "bid": 1}
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

export { pyth };
export { radians_to_degrees };
export { apiUpdateState };
export { bidOnPowerPlant };
export { resetPlayers };
export { newPlayer };
export { apiCreateUser };
export { apiStartGame };
export { apiAuctionAction };
export { translateState };
export { replaceEditMatch };
