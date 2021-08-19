import React, { useEffect, useState } from 'react';
import PropTypes from 'prop-types';

import DialogTitle from '@material-ui/core/DialogTitle';
import Dialog from '@material-ui/core/Dialog';
import { DialogContent, Grid, Toolbar, IconButton } from '@material-ui/core';
import CloseIcon from '@material-ui/icons/Close';



function PopupWindow(props) {
    const { onClose, open, children, title, wide } = props;

    return (
        <Dialog aria-labelledby="new-action-dialog" open={open} maxWidth={false}>
            <Toolbar>
                <IconButton edge="start" onClick={onClose} aria-label="close">
                    <CloseIcon />
                </IconButton>
                <DialogTitle id="simple-dialog-title">{title}</DialogTitle>

            </Toolbar>

            <DialogContent>
                <Grid container direction="column" >
                    {React.Children.toArray(children).map(child =>
                        <Grid item style={{ margin: "1em 0", width: "100%" }}>
                            {child}
                        </Grid>
                    )}
                </Grid>
            </DialogContent>
        </Dialog >
    );
}

PopupWindow.propTypes = {
    open: PropTypes.bool.isRequired,
};

export default PopupWindow
