import React from "react";
import * as tools from "../functions";
import { Row, Col } from "reactstrap";

class BidInput extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      value: this.props.minValue
    };
    this.toggleClick = this.toggleClick.bind(this);
    this.handleIncreaseBid = this.handleIncreaseBid.bind(this);
    this.handleDecreaseBid = this.handleDecreaseBid.bind(this);
    this.handleSubmitBid = this.handleSubmitBid.bind(this);
  }

  toggleClick() {
    this.props.addRemoveSelectedCity(this.props.id);
  }

  handleSubmitBid() {
    this.props.onClick(this.state.value);
  }

  handleIncreaseBid() {
    this.setState({
      value: this.state.value + 1
    });
  }

  handleDecreaseBid() {
    if (this.state.value > this.props.minValue) {
      this.setState({
        value: this.state.value - 1
      });
    }
  }

  componentDidUpdate() {}

  render() {
    let bidButtonClass = "bidButton";
    if (this.state.value === this.props.minValue) {
      bidButtonClass = "bidDisabled";
    }

    return (
      <Row className="bidComponent">
        <Col>
          <div className="bidInput text-center" onClick={this.handleSubmitBid}>
            {tools.formatMoney(this.state.value)}
          </div>
        </Col>
        <Col>
          <Row>
            <div
              className="bidButton text-center"
              onClick={this.handleIncreaseBid}
            >
              +
            </div>
          </Row>
          <Row>
            <div
              className={bidButtonClass + " text-center"}
              onClick={this.handleDecreaseBid}
            >
              -
            </div>
          </Row>
        </Col>
      </Row>
    );
  }
}

export default BidInput;
