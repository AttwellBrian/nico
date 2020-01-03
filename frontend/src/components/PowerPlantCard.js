import React from "react";
import { Card, CardBody, Button, Row, Col } from "reactstrap";
import * as tools from "../functions";

class PowerPlantCard extends React.Component {
  constructor(props) {
    super(props);
    this.state = {};
    this.handleStartBid = this.handleStartBid.bind(this);
  }

  componentDidUpdate() {}

  handleStartBid() {
    tools.bidOnPowerPlant(this.props.userId, this.props.plantId);
  }

  render() {
    // style card based on props
    // defineCardStyle
    let mainCardStyle =
      "ppType" +
      this.props.type.toLowerCase().replace(/^\w/, c => c.toUpperCase());

    // render input icons
    let inputIcons = "";
    let inputIconsArray = [];
    for (var i = 0; i < this.props.input; i++) {
      inputIconsArray.push("x");
    }
    inputIcons = inputIconsArray.map((input, index) => {
      return <div className={mainCardStyle + "Input input"}></div>;
    });

    return (
      <div className={mainCardStyle + " ppContainer"}>
        <Card className={"ppCard " + mainCardStyle + "Card"}>
          <CardBody>
            {this.props.clickable === true && (
              <div>
                <Button onClick={this.handleStartBid}>
                  Bid: ${this.props.cost}
                </Button>
              </div>
            )}
            <div className="cost">{this.props.cost}</div>
            <div className={"bottomBar " + mainCardStyle}>
              <Row className="text-center">
                <Col xs="6">
                  <div className="inputContainer">{inputIcons}</div>
                </Col>
                <Col xs="3">
                  <div className="inputContainer ppPowers"></div>
                </Col>
                <Col xs="3">
                  <div className="output">{this.props.output}</div>
                </Col>
              </Row>
            </div>
          </CardBody>
        </Card>
      </div>
    );
  }
}

export default PowerPlantCard;
