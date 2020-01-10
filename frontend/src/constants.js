const serverURL = "http://localhost:8080";

const emptyGameStateSchema = {
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

const emptyClientGameStateSchema = {
  userProfile: {
    name: "",
    color: "",
    uuid: ""
  },
  gameState: emptyGameStateSchema,
  showGame: false,
  showPowerPlantMarket: false,
  showResourcesMarket: false,
  highestBidder: "",
  selectedCities: [],
  costToPurchaseCities: 0
};

const translationsNeeded = [
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
    serverKeys: [
      "id",
      "minimumAcceptableBid",
      "resourceType",
      "resourcesRequired",
      "homesPowered"
    ],
    clientKeys: ["id", "cost", "type", "resourcesNeeded", "citiesPowered"]
  },
  {
    serverName: "futureMarket",
    clientName: "futureMarket",
    arrayToMapTrue: false,
    serverKeys: [
      "id",
      "minimumAcceptableBid",
      "resourceType",
      "resourcesRequired",
      "homesPowered"
    ],
    clientKeys: ["id", "cost", "type", "resourcesNeeded", "citiesPowered"]
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

const map = {
  id: "US",
  name: "United States",
  width: 3023,
  height: 1956,
  cities: {
    seattle: {
      name: "Seattle",
      id: "seattle",
      top: "0.15",
      left: "0.1",
      section: "purple",
      connections: {
        billings: {
          name: "Billings",
          id: "billings",
          cost: 9
        },
        boise: {
          name: "Boise",
          id: "boise",
          cost: 12
        },
        portland: {
          name: "Portland",
          id: "portland",
          cost: 3
        }
      }
    },
    portland: {
      name: "Portland",
      id: "portland",
      top: "0.28",
      left: "0.08",
      section: "purple",
      connections: {
        seattle: {
          name: "Seattle",
          id: "seattle",
          cost: 3
        },
        boise: {
          name: "Boise",
          id: "boise",
          cost: 13
        }
      }
    },
    boise: {
      name: "Boise",
      id: "boise",
      top: "0.3",
      left: "0.18",
      section: "purple",
      connections: {
        seattle: {
          name: "Seattle",
          id: "seattle",
          cost: 12
        },
        portland: {
          name: "Portland",
          id: "portland",
          cost: 13
        },
        billings: {
          name: "Billings",
          id: "billings",
          cost: 12
        }
      }
    },
    billings: {
      name: "Billings",
      id: "billings",
      top: "0.22",
      left: "0.32",
      section: "purple",
      connections: {
        seattle: {
          name: "Seattle",
          id: "seattle",
          cost: 9
        },
        boise: {
          name: "Boise",
          id: "boise",
          cost: 12
        }
      }
    }
  }
};

export {
  serverURL,
  emptyGameStateSchema,
  emptyClientGameStateSchema,
  translationsNeeded,
  map
};
