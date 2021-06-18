import React, { Component } from 'react';
import Alert from '@material-ui/lab/Alert';
import IconButton from '@material-ui/core/IconButton';
import Collapse from '@material-ui/core/Collapse';
import CloseIcon from '@material-ui/icons/Close';

export default function CollapsableAlert(props) {
    return <Collapse in={props.shouldShow ?  props.shouldShow : true} style={{ marginBottom: "0.5em" }}>
    <Alert
        severity={ props.severity}
        action={ props.onClose ?
            <IconButton
                aria-label="close"
                color="inherit"
                size="small"
                onClick={ props.onClose}
            >
                <CloseIcon fontSize="inherit" />
            </IconButton>
            : undefined}
    >
        { props.message}
    </Alert>
</Collapse>;
}
