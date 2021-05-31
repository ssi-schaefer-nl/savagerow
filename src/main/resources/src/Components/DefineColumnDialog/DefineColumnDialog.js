import React, { useState } from 'react';
import Button from '@material-ui/core/Button';
import TextField from '@material-ui/core/TextField';
import Dialog from '@material-ui/core/Dialog';
import DialogActions from '@material-ui/core/DialogActions';
import DialogContent from '@material-ui/core/DialogContent';
import DialogContentText from '@material-ui/core/DialogContentText';
import DialogTitle from '@material-ui/core/DialogTitle';
import MenuItem from '@material-ui/core/MenuItem';
import Checkbox from '@material-ui/core/Checkbox';
import { Grid, Typography } from '@material-ui/core';
import FormControlLabel from '@material-ui/core/FormControlLabel';

const datatypes = [
    "text",
    "number",
]

export default function DefineColumnDialog(props) {
    const { open, onSubmit, handleClose, errorMessage } = props
    const [name, setName] = useState("")
    const [dataType, setDataType] = useState(datatypes[0])
    const [nullable, setNullable] = useState(true)
    const [defaultValue, setDefaultValue] = useState("")

    const onEnter = (e) => {
        e.preventDefault()
        const data = {
            column: name,
            datatype: dataType,
            nullable: nullable,
            defaultValue: defaultValue
        }

        onSubmit(data)
    }

    const onClose = () => {
        setName("")
        setDataType(datatypes[0])
        setNullable(true)
        setDefaultValue("")
        handleClose()
    }

    return (
        <div>
            <Dialog
                open={open}
                onClose={onClose}
                aria-labelledby="form-dialog-title"
            >
                <DialogTitle id="form-dialog-title">Define Column</DialogTitle>
                <form onSubmit={onEnter}>

                    <DialogContent>

                        <Grid container direction="column" spacing={2}>
                            <DialogContentText>Define the column by entering the fields below.</DialogContentText>
                            {errorMessage && <Typography variant="body1" color="error">{errorMessage}</Typography>}
                            <Grid item>
                                <TextField
                                    autoFocus
                                    autoComplete='off'
                                    margin="dense"
                                    id="name"
                                    onChange={(e) => setName(e.target.value)}
                                    required
                                    label="Column name"
                                    fullWidth
                                />
                            </Grid>
                            <Grid item>
                                <TextField
                                    id="datatype"
                                    select
                                    required
                                    label="Datatype"
                                    value={dataType}
                                    onChange={(e) => setDataType(e.target.value)}
                                >
                                    {datatypes.map((option) => (
                                        <MenuItem key={option} value={option}>
                                            {option}
                                        </MenuItem>
                                    ))}
                                </TextField>
                            </Grid>
                            <Grid item>
                                <FormControlLabel
                                    label="Nullable"
                                    control={
                                        <Checkbox
                                            checked={nullable}
                                            onChange={(e) => setNullable(e.target.checked)}
                                            name="nullable"
                                            color="primary"
                                        />
                                    }

                                />
                            </Grid>
                            {!nullable &&
                                <Grid item>
                                    <TextField
                                        id="default"
                                        required
                                        label="Default"
                                        type={dataType}
                                        value={defaultValue}
                                        onChange={(e) => setDefaultValue(e.target.value)}
                                    />
                                </Grid>
                            }

                        </Grid>

                    </DialogContent>


                    <DialogActions>
                        <Button onClick={onClose} color="primary">Cancel</Button>
                        <Button color="primary" type="submit">Submit</Button>
                    </DialogActions>
                </form>

            </Dialog>
        </div>
    );
}