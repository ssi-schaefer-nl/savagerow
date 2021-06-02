import React, { Component } from "react";
import {
  BrowserRouter,
  HashRouter
} from "react-router-dom";
import AppLayout from "./Layout/AppLayout";

class Main extends Component {
  render() {
    return (
      <HashRouter>
        <AppLayout/>
      </HashRouter>
    );
  }
}

export default Main;
