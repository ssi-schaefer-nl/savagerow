import React, { Component } from "react";
import {
    Route,
} from "react-router-dom";
import Home from "../Views/Home";
import Contact from "../Views/Contact";
import Configure from "../Views/Configuration/Configure";
import Workflow from "../Views/Configuration/Workflow/Workflow";
import Tables from "../Views/DataTables/Tables";

class RoutedComponent extends Component {
    render() {
        return (
            <>
                <Route exact path="/" component={Home} />
                <Route path="/tables" component={Tables} />
                <Route path="/contact" component={Contact} />
                <Route exact path="/configuration" component={Configure} />
                <Route exact path="/configuration/workflow" component={Workflow}/>
            </>
        );
    }
}

export default RoutedComponent;
