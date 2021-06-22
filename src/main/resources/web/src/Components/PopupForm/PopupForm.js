import React, { useEffect, useState } from 'react';
import PropTypes from 'prop-types';
import { makeStyles } from '@material-ui/core/styles';

import DialogTitle from '@material-ui/core/DialogTitle';
import Dialog from '@material-ui/core/Dialog';
import { Button, DialogActions, DialogContent, Divider, Grid, TextField, Toolbar, IconButton } from '@material-ui/core';
import { InputLabel, Select } from '@material-ui/core';

import { blue } from '@material-ui/core/colors';
import CloseIcon from '@material-ui/icons/Close';


const useStyles = makeStyles({
    avatar: {
        backgroundColor: blue[100],
        color: blue[600],
    },
});




function PopupForm(props) {
    const { onClose, open, onSubmit, children, title, wide, hide } = props;
    const classes = useStyles();


    return (
        <Dialog aria-labelledby="new-action-dialog" open={open} fullWidth={true} maxWidth={wide ? "lg" : "sm"}>
            <Toolbar>
                <IconButton edge="start" onClick={onClose} aria-label="close">
                    <CloseIcon />
                </IconButton>
                <DialogTitle id="simple-dialog-title">{title}</DialogTitle>

            </Toolbar>

            <form onSubmit={onSubmit} className={classes.root} autoComplete="off">
                <DialogContent >
                    <Grid container direction="column" >
                        {React.Children.toArray(children).map(child =>
                            <Grid item style={{ margin: "1em 0", width: "100%" }}>
                                {child}
                            </Grid>
                        )}
                    </Grid>
                </DialogContent>
                <DialogActions>
                    {!hide &&
                        <>
                            <Button color="primary" type="submit">Apply</Button>
                            <Button color="primary" onClick={onClose}>Cancel</Button>
                        </>
                    }
                </DialogActions>
            </form>
        </Dialog>
    );
}





PopupForm.propTypes = {
    open: PropTypes.bool.isRequired,
};

export default PopupForm
