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

const powerPlants = {
  1: {
    type: "oil",
    cost: 3,
    resourcesNeeded: 2,
    citiesPowered: 1
  },
  2: {
    type: "coal",
    cost: 4,
    resourcesNeeded: 2,
    citiesPowered: 1
  },
  3: {
    type: "hybrid",
    cost: 5,
    resourcesNeeded: 2,
    citiesPowered: 1
  },
  4: {
    type: "garbage",
    cost: 6,
    resourcesNeeded: 1,
    citiesPowered: 1
  },
  5: {
    type: "oil",
    cost: 7,
    resourcesNeeded: 3,
    citiesPowered: 2
  },
  6: {
    type: "coal",
    cost: 8,
    resourcesNeeded: 3,
    citiesPowered: 2
  },
  7: {
    type: "oil",
    cost: 9,
    resourcesNeeded: 1,
    citiesPowered: 1
  },
  8: {
    type: "coal",
    cost: 10,
    resourcesNeeded: 2,
    citiesPowered: 2
  },
  9: {
    type: "uranium",
    cost: 11,
    resourcesNeeded: 1,
    citiesPowered: 2
  },
  10: {
    type: "hybrid",
    cost: 12,
    resourcesNeeded: 2,
    citiesPowered: 2
  },
  11: {
    type: "ecological",
    cost: 13,
    resourcesNeeded: 0,
    citiesPowered: 1
  },
  12: {
    type: "garbage",
    cost: 14,
    resourcesNeeded: 2,
    citiesPowered: 2
  },
  13: {
    type: "coal",
    cost: 15,
    resourcesNeeded: 2,
    citiesPowered: 3
  },
  14: {
    type: "oil",
    cost: 16,
    resourcesNeeded: 2,
    citiesPowered: 3
  },
  15: {
    type: "uranium",
    cost: 17,
    resourcesNeeded: 1,
    citiesPowered: 2
  },
  16: {
    type: "ecological",
    cost: 18,
    resourcesNeeded: 0,
    citiesPowered: 2
  },
  17: {
    type: "garbage",
    cost: 19,
    resourcesNeeded: 2,
    citiesPowered: 3
  },
  18: {
    type: "coal",
    cost: 20,
    resourcesNeeded: 3,
    citiesPowered: 5
  },
  19: {
    type: "hybrid",
    cost: 21,
    resourcesNeeded: 2,
    citiesPowered: 4
  },
  20: {
    type: "ecological",
    cost: 22,
    resourcesNeeded: 0,
    citiesPowered: 2
  },
  21: {
    type: "uranium",
    cost: 23,
    resourcesNeeded: 1,
    citiesPowered: 3
  },
  22: {
    type: "garbage",
    cost: 24,
    resourcesNeeded: 2,
    citiesPowered: 4
  },
  23: {
    type: "coal",
    cost: 25,
    resourcesNeeded: 2,
    citiesPowered: 5
  },
  24: {
    type: "oil",
    cost: 26,
    resourcesNeeded: 2,
    citiesPowered: 5
  },
  25: {
    type: "ecological",
    cost: 27,
    resourcesNeeded: 3,
    citiesPowered: 1
  },
  26: {
    type: "uranium",
    cost: 28,
    resourcesNeeded: 1,
    citiesPowered: 4
  },
  27: {
    type: "hybrid",
    cost: 29,
    resourcesNeeded: 1,
    citiesPowered: 4
  },
  28: {
    type: "garbage",
    cost: 30,
    resourcesNeeded: 3,
    citiesPowered: 6
  },
  29: {
    type: "coal",
    cost: 31,
    resourcesNeeded: 3,
    citiesPowered: 6
  },
  30: {
    type: "oil",
    cost: 32,
    resourcesNeeded: 3,
    citiesPowered: 6
  },
  31: {
    type: "ecological",
    cost: 33,
    resourcesNeeded: 0,
    citiesPowered: 4
  },
  32: {
    type: "uranium",
    cost: 34,
    resourcesNeeded: 1,
    citiesPowered: 5
  },
  33: {
    type: "oil",
    cost: 35,
    resourcesNeeded: 1,
    citiesPowered: 5
  },
  34: {
    type: "coal",
    cost: 36,
    resourcesNeeded: 3,
    citiesPowered: 7
  },
  35: {
    type: "ecological",
    cost: 37,
    resourcesNeeded: 0,
    citiesPowered: 4
  },
  36: {
    type: "garbage",
    cost: 38,
    resourcesNeeded: 3,
    citiesPowered: 7
  },
  37: {
    type: "uranium",
    cost: 39,
    resourcesNeeded: 1,
    citiesPowered: 6
  },
  38: {
    type: "oil",
    cost: 40,
    resourcesNeeded: 2,
    citiesPowered: 6
  },
  39: {
    type: "coal",
    cost: 42,
    resourcesNeeded: 2,
    citiesPowered: 6
  },
  40: {
    type: "ecological",
    cost: 44,
    resourcesNeeded: 0,
    citiesPowered: 5
  },
  41: {
    type: "hybrid",
    cost: 46,
    resourcesNeeded: 3,
    citiesPowered: 7
  },
  42: {
    type: "ecological",
    cost: 50,
    resourcesNeeded: 0,
    citiesPowered: 6
  }
};

const userProfile = {
  name: "Lightning Bug, LLC.",
  color: "blue",
  uuid: "PLYR1"
};

const gameState = {
  players: {
    PLYR1: {
      name: "Lightning Bug, LLC.",
      color: "blue"
    },
    PLYR2: {
      name: "Oil Barron, Inc.",
      color: "green"
    },
    PLYR3: {
      name: "Armco, Co.",
      color: "red"
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
    PLYR1: ["seattle"],
    PLYR2: ["boise"],
    PLYR3: ["billings"]
  },
  actualMarket: ["10", "11", "12", "13"],
  futureMarket: ["14", "15", "16", "17"]
};

export { map, userProfile, gameState, powerPlants };
