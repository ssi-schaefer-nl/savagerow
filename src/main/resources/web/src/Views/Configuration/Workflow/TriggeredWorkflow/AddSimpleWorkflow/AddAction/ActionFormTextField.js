import React, { useState } from 'react';

import { TextField } from '@material-ui/core';



const ActionFormTextField = props => {

    return (
        <TextField
            InputLabelProps={{ shrink: true }}
            style={{ width: "20em", marginLeft: "0" }}
            autoComplete='off'
            {...props}
            onChange={(e) => props.onChange(e.target.value)}
        />
    )
}

export default ActionFormTextField