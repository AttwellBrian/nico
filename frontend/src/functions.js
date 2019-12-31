import axios from "axios";

function pyth(x, y) {
  if (typeof x !== "number" || typeof y !== "number") return false;
  return Math.sqrt(x * x + y * y);
}

function radians_to_degrees(radians) {
  var pi = Math.PI;
  return radians * (180 / pi);
}

function apiUpdateState() {
  axios.get(`http://localhost:8080/state`).then(data => {
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
  });
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
