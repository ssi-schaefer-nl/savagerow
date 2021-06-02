import React from "react";

import { Button, Divider, Grid, Menu, MenuItem, Typography } from "@material-ui/core";
import { useHistory, useParams } from "react-router"
import ManageWorkflows from "../ManageWorkflows/ManageWorkflows";

export default function DeleteWorkflows(props) {
    const { table } = useParams();
    const history = useHistory()
    return (
        <div>
            <Grid container direction="row" justify="space-between" style={{ padding: "1em" }}>
                <Grid item>
                    <Typography variant="h6">Delete Workflows ({table})</Typography>
                </Grid>
                <Grid item>
                    <Button variant="contained" onClick={() => history.goBack()}>Back</Button>
                </Grid>
            </Grid>
            <ManageWorkflows />

        </div>
    )
}

