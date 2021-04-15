import React, { Component } from "react";
import {
  HashRouter
} from "react-router-dom";
import DefaultLayout from "./Layout/MiniDrawer";



class Main extends Component {
  render() {
    return (
      <HashRouter>
        <DefaultLayout/>
      </HashRouter>
    );
  }
}

export default Main;
