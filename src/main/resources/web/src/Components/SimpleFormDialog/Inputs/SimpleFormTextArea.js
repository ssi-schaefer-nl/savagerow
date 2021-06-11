import React from "react";

import { Grid, InputLabel, TextField } from "@material-ui/core";


const DefaultTextArea = props => {
    return (
        <Grid item style={{margin: "1em 0" }}>
            <TextField
                InputLabelProps={{ shrink: true }}
                style={{ width: "70%"}}
                autoFocus
                autoComplete='off'
                maxWidth={50}
                multiline
                rows={5}
                {...props}
            />
        </Grid>
    )
}
export default DefaultTextArea;