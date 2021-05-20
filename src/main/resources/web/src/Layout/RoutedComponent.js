import React, { Component } from "react";
import {
    Route,
} from "react-router-dom";
import Home from "../Views/Home";
import Tables from "../Views/Tables";
import Contact from "../Views/Contact";
import Configure from "../Views/ConfigureSavageRow";

class RoutedComponent extends Component {
    render() {
        return (
            <>
                <Route exact path="/" component={Home} />
                <Route path="/tables" component={Tables} />
                <Route path="/contact" component={Contact} />
                <Route path="/configuration" component={Configure} />
            </>
        );
    }
}

export default RoutedComponent;

