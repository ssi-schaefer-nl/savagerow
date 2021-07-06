import React, { useState } from 'react';

import { TextField } from '@material-ui/core';



const ActionFormTextArea = props => {
    
    return (
        <TextField
            InputLabelProps={{ shrink: true }}
            style={{ width: "70%" }}
            autoComplete='off'
            maxWidth={50}
            multiline
            rows={5}
            {...props}
            onChange={(e) => props.onChange(e.target.value)}
        />
    )
}

export default ActionFormTextArea