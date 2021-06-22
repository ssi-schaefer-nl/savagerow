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
    "integer",
    "real"
]

export default function DefineColumnDialog(props) {
    const { open, onSubmit, handleClose, errorMessage } = props
    const [name, setName] = useState("")
    const [dataType, setDataType] = useState(datatypes[0])
    const [nullable, setNullable] = useState(true)
    const [pk, setPk] = useState(false)
    const [fk, setFk] = useState(false)
    const [fkTable, setFkTable] = useState("")
    const [fkColumn, setFkColumn] = useState("")
    const [defaultValue, setDefaultValue] = useState("")

    const onEnter = (e) => {
        e.preventDefault()
        const data = {
            name: name,
            datatype: dataType,
            nullable: nullable,
            pk: pk,
            fk: fk ? `${fkTable}.${fkColumn}` : null,
            defaultValue: defaultValue
        }

        onSubmit(data)
    }

    const onClose = () => {
        setName("")
        setDataType(datatypes[0])
        setNullable(true)
        setDefaultValue("")
        setPk(false)
        setFk(false)
        setFkTable("")
        setFkColumn("")
        handleClose()
    }

    return (
        <div>
            <Dialog
                fullWidth
                open={open}
                onClose={onClose}
                aria-labelledby="form-dialog-title"
            >
                <DialogTitle id="form-dialog-title">Define Column</DialogTitle>
                <form onSubmit={onEnter}>

                    <DialogContent dividers style={{ margin: "1em" }}>
                        <DialogContentText>Define the column by entering the fields below.</DialogContentText>
                        {errorMessage && <Typography variant="body1" color="error">{errorMessage}</Typography>}
                        <Grid container direction="row" justify="space-between" style={{ padding: "1em" }}>
                            <Grid container direction="column" spacing={2} xs={4}>
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
                                        fullWidth
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
                                    <TextField
                                        id="default"
                                        label="Default value"
                                        type={dataType === "text" ? "text" : "number"}
                                        value={defaultValue}
                                        required={!nullable}
                                        onChange={(e) => setDefaultValue(e.target.value)}
                                    />
                                </Grid>
                            </Grid>
                            <Grid container direction="column" spacing={1} xs={6} alignItems="flex-end">
                                <Grid item>
                                    <FormControlLabel
                                        label="Nullable"
                                        labelPlacement="start"
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
                                <Grid item>
                                    <FormControlLabel
                                        label="Primary Key"
                                        labelPlacement="start"
                                        control={
                                            <Checkbox
                                                checked={pk}
                                                onChange={(e) => setPk(e.target.checked)}
                                                name="primary key"
                                                color="primary"
                                            />
                                        }

                                    />
                                </Grid>
                                <Grid item>
                                    <FormControlLabel
                                        label="Add Reference"
                                        labelPlacement="start"
                                        control={
                                            <Checkbox
                                                checked={fk}
                                                onChange={(e) => setFk(e.target.checked)}
                                                name="foreign key"
                                                color="primary"

                                            />
                                        }

                                    />
                                </Grid>
                                {fk &&
                                    <Grid container item direction="row" spacing={2} justify="flex-end">
                                        <Grid item xs={6}>
                                            <TextField
                                                id="fkTable"
                                                label="Table"
                                                value={fkTable}
                                                required
                                                onChange={(e) => setFkTable(e.target.value)}
                                            />
                                        </Grid>
                                        <Grid item xs={6}>
                                            <TextField
                                                required
                                                id="fkColumn"
                                                label="Column"
                                                value={fkColumn}
                                                onChange={(e) => setFkColumn(e.target.value)}
                                            />
                                        </Grid>
                                    </Grid>
                                }
                            </Grid>
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