import React, { Component } from 'react';
import Alert from '@material-ui/lab/Alert';
import IconButton from '@material-ui/core/IconButton';
import Collapse from '@material-ui/core/Collapse';
import Button from '@material-ui/core/Button';
import CloseIcon from '@material-ui/icons/Close';

class CollapsableAlert extends Component {

    render() {
        return <Collapse in={this.props.shouldShow ? this.props.shouldShow : true} style={{ marginBottom: "2em" }}>
            <Alert
                severity={this.props.severity}
                action={this.props.onClose ?
                    <IconButton
                        aria-label="close"
                        color="inherit"
                        size="small"
                        onClick={this.props.onClose}
                    >
                        <CloseIcon fontSize="inherit" />
                    </IconButton>
                    : undefined}
            >
                {this.props.message}
            </Alert>
        </Collapse>;
    }
}

export default CollapsableAlert