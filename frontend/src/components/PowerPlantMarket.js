import React from "react";
import { Row, Col, Button, Modal, ModalBody } from "reactstrap";
import PowerPlantCard from "./PowerPlantCard";
import BidInput from "./BidInput";
import PassButton from "./PassButton";

import * as tools from "../functions";

class PowerPlantMarket extends React.Component {
  constructor(props) {
    super(props);
    this.state = {};
    this.toggleModal = this.toggleModal.bind(this);
    this.handleSubmitBid = this.handleSubmitBid.bind(this);
  }

  toggleModal() {
    this.props.parentToggle();
  }

  handleSubmitBid(bid) {
    tools.apiAuctionAction("BID", this.props.state.userProfile.uuid, null, bid);
  }

  componentDidUpdate() {}

  render() {
    // render city position
    const userProfile = this.props.state.userProfile;
    const gameState = this.props.state.gameState;
    const showModal = this.props.state.showPowerPlantMarket;

    let biddingStatus = "Not in bidding phase";
    if (gameState.gamePhase === "auctionPickPlant") {
      biddingStatus =
        "Bidding Active: " +
        gameState.players[gameState.currentPlayer].name +
        " is picking a plant...";
    } else if (gameState.gamePhase === "auctionBidding") {
      biddingStatus =
        "Bidding Active: " +
        gameState.players[gameState.currentPlayer].name +
        " is bidding...";
    }

    const marketPlants = gameState.actualMarket.map((plant, index) => {
      // if it's user's turn to pick a power plant
      let clickable = false;
      if (
        gameState.gamePhase === "auctionPickPlant" &&
        gameState.currentPlayer === userProfile.uuid
      ) {
        clickable = true;
      }
      return (
        <Col key={index}>
          <PowerPlantCard
            plantId={plant.id}
            userId={userProfile.uuid}
            cost={plant.cost}
            type={plant.type}
            input={plant.resourcesNeeded}
            output={plant.citiesPowered}
            clickable={clickable}
          />
        </Col>
      );
    });

    const futurePlants = gameState.futureMarket.map((plant, index) => {
      return (
        <Col key={index}>
          <PowerPlantCard
            cost={plant.cost}
            type={plant.type}
            input={plant.resourcesNeeded}
            output={plant.citiesPowered}
            onClick=""
          />
        </Col>
      );
    });

    return (
      <div>
        <Button color="primary" onClick={this.toggleModal}>
          Power Plant Market
        </Button>
        <Modal
          isOpen={showModal}
          toggle={this.toggleModal}
          size="xl"
          className="powerPlantModal"
        >
          <ModalBody>
            <Row>
              <Col xl={9}>
                <Row>
                  <Col>
                    <h1>The Power Plant Market</h1>
                  </Col>
                </Row>
                <Row className="mt-2">
                  <Col>
                    <h4>Available for purchase</h4>
                  </Col>
                </Row>
                <Row>{marketPlants}</Row>
                <Row className="mt-4">
                  <Col>
                    <h4>Future market</h4>
                  </Col>
                </Row>
                <Row>{futurePlants}</Row>
              </Col>
              <Col xl={3}>
                <Row>
                  <Col>
                    <h4>{biddingStatus}</h4>
                  </Col>
                </Row>
                {gameState.gamePhase === "auctionPickPlant" && (
                  <div>
                    {gameState.currentPlayer === userProfile.uuid && (
                      <div>
                        <div>
                          Pick a plant on the left to place the minimum bid
                        </div>
                        <div>
                          {gameState.ownedPowerPlants[userProfile.uuid].length >
                            0 && <PassButton uuid={userProfile.uuid} />}
                        </div>
                      </div>
                    )}
                  </div>
                )}
                {gameState.gamePhase === "auctionBidding" && (
                  <div>
                    <div>
                      Highest bidder: {gameState.currentBid} (
                      {gameState.players[gameState.highestBidder].name})
                    </div>
                    {gameState.currentPlayer === userProfile.uuid && (
                      <div>
                        <div>
                          <BidInput
                            minValue={gameState.currentBid + 1}
                            onClick={this.handleSubmitBid}
                          />
                        </div>
                        <div>
                          <PassButton uuid={userProfile.uuid} />
                        </div>
                      </div>
                    )}
                  </div>
                )}
                <Row>
                  <Col></Col>
                </Row>
              </Col>
            </Row>
          </ModalBody>
        </Modal>
      </div>
    );
  }
}

export default PowerPlantMarket;
