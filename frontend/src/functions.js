function pyth(x, y) {
  if (typeof x !== "number" || typeof y !== "number") return false;
  return Math.sqrt(x * x + y * y);
}

export { pyth };
