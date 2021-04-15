import React from "react";
import Typography from '@material-ui/core/Typography';

import { CircularProgress, Divider } from "@material-ui/core";
import DatabaseSelect from "../Components/DatabaseSelect/DatabaseSelect";
import ConfigureService from "../Service/ConfigureService";



class Configure extends React.Component {
    constructor(props) {
        super(props);
    }

    state = {
        configureService: new ConfigureService(),
    }


    componentDidMount() {
        this.state.configureService.getCurrentDatabase(
            function (data) {
                this.setState({ database: data.data })
            }.bind(this),
            function (data) {
                console.log(data)
            }.bind(this));
    }

    render() {
        return (
            <div>
                <Typography variant="h6" noWrap>Configure Database</Typography>
                <Divider style={{ marginBottom: "2em" }} />
                <DatabaseSelect initialValue={this.state.database}/>

            </div>
        );
    }
}


export default Configure;
