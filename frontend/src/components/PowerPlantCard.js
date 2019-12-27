import React from "react";
import { Card, CardBody } from "reactstrap";

class PowerPlantCard extends React.Component {
  constructor(props) {
    super(props);
    this.state = {};
  }

  componentDidUpdate() {}

  render() {
    return (
      <div>
        <Card>
          <CardBody>
            <div>Cost: {this.props.cost}</div>
            <div>Type: {this.props.type}</div>
            <div>Resources Needed: {this.props.input}</div>
            <div>Cities Powered: {this.props.output}</div>
          </CardBody>
        </Card>
      </div>
    );
  }
}

export default PowerPlantCard;
