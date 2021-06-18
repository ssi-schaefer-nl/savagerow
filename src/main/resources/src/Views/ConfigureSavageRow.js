import React from "react";
import Typography from '@material-ui/core/Typography';

import { CircularProgress, Divider } from "@material-ui/core";
import DatabaseSelect from "../Components/DatabaseSelect/DatabaseSelect";
import ConfigureService from "../Service/ConfigureService";



export default function Configure(props) {

    return (
        <div style={{margin: '1em'}}>
            <Typography variant="h6" noWrap>Configure Database</Typography>
            <Divider style={{ marginBottom: "2em" }} />
            <DatabaseSelect initialValue={localStorage.getItem("database")} />

        </div>
    );

}

