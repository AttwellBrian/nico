function pyth(x, y) {
  if (typeof x !== "number" || typeof y !== "number") return false;
  return Math.sqrt(x * x + y * y);
}

function radians_to_degrees(radians) {
  var pi = Math.PI;
  return radians * (180 / pi);
}

// API CALLS
function bidOnPowerPlant(userId, plantId) {
  alert(userId + " bid on power plant " + plantId);
}

function resetPlayers() {
  alert(resetPlayers);
}

function newPlayer(player) {
  alert(player.name + " (" + player.color + ") has joined");
}

export { pyth };
export { radians_to_degrees };
export { bidOnPowerPlant };
export { resetPlayers };
export { newPlayer };
