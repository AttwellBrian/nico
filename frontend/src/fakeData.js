var map = {
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

export { map };
