import React from "react";

import { Grid, InputLabel, TextField } from "@material-ui/core";


const DefaultTextField = props => {
    return (
        <Grid item style={{margin: "1em 0" }}>
            <TextField
                InputLabelProps={{ shrink: true }}
                style={{ width: "70%", marginLeft: "0"}}
                autoFocus
                autoComplete='off'
                {...props}
            />
        </Grid>

    )
}

export default DefaultTextField;