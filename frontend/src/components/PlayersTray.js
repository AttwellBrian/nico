import React from "react";
import { Row, Col } from "reactstrap";
import PowerPlantCard from "./PowerPlantCard";

class PlayersTray extends React.Component {
  constructor(props) {
    super(props);
    this.state = {};
  }

  componentDidUpdate() {}

  render() {
    let ownedPlants = "";
    if (
      this.props.state.userProfile.uuid &&
      this.props.state.gameState.ownedPowerPlants[
        this.props.state.userProfile.uuid
      ]
    ) {
      ownedPlants = this.props.state.gameState.ownedPowerPlants[
        this.props.state.userProfile.uuid
      ].map((plant, index) => {
        return (
          <Col key={index}>
            <PowerPlantCard
              cost={plant.minimumAcceptableBid}
              type={plant.resourceType}
              input={plant.resourcesRequired}
              output={plant.homesPowered}
              onClick=""
            />
          </Col>
        );
      });
    } else {
      ownedPlants = "";
    }

    return (
      <div className="userContainer">
        <div className="containerTitle">
          {this.props.state.userProfile.name}
        </div>
        <div>
          <Row className="ownedPlantsRow">{ownedPlants}</Row>
        </div>
      </div>
    );
  }
}

export default PlayersTray;
