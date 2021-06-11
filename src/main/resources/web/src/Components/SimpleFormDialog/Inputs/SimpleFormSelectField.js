import React from "react";

import { Grid, InputLabel, Select } from "@material-ui/core";
import { MenuItem } from "react-contextmenu";



const DefaultSelectField = props => {
    return (
        <Grid item style={{margin: "1em 0" }}>
            <InputLabel shrink required={props.required} id={props.id}>{props.label}</InputLabel>
            <Select
                InputLabelProps={{ shrink: true }}
                style={{ width: "70%"}}
                {...props}
            >
                {props.items.map(item => (<MenuItem key={item} value={item}>{item}</MenuItem>))}
            </Select>
        </Grid>

    )
}

export default DefaultSelectField;