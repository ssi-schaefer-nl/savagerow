import React from 'react';
import PropTypes from 'prop-types';
import { makeStyles } from '@material-ui/core/styles';

import DialogTitle from '@material-ui/core/DialogTitle';
import Dialog from '@material-ui/core/Dialog';

import { blue } from '@material-ui/core/colors';
import { DialogActions, DialogContent, IconButton, Toolbar } from '@material-ui/core';
import CloseIcon from '@material-ui/icons/Close';

const useStyles = makeStyles({
    avatar: {
        backgroundColor: blue[100],
        color: blue[600],
    },
});

function SimpleFormDialog(props) {
    const { onClose, open, title, children, outsideClickClose } = props;

    return (
        <Dialog onClose={outsideClickClose ? onClose : () => undefined} aria-labelledby="simple-dialog-title" open={open} fullWidth={true}>
            <Toolbar>
                <IconButton edge="start" color="inherit" onClick={onClose} aria-label="close">
                    <CloseIcon />
                </IconButton>
                <DialogTitle id="simple-dialog-title">{title}</DialogTitle>

            </Toolbar>
            {children}
        </Dialog>
    );
}

SimpleFormDialog.propTypes = {
    onClose: PropTypes.func.isRequired,
    open: PropTypes.bool.isRequired,
};

export default SimpleFormDialog
