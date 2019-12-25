var map = {
  id: "US",
  name: "United States",
  cities: {
    seattle: {
      name: "Seattle",
      id: "seattle",
      x: "0.1",
      y: "0.9",
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
      x: "0.05",
      y: "0.8",
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
      x: "0.15",
      y: "0.75",
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
      x: "0.30",
      y: "0.8",
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
