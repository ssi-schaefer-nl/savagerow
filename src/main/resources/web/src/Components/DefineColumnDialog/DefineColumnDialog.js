import React, { useEffect, useState } from 'react';
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
import QueryService from '../../Service/QueryService/QueryService';
import DefinitionService from '../../Service/DefinitionService/DefinitionService';

const datatypes = [
    "Text",
    "Integer",
    "Real",
    "Boolean",
    "Date",
    "Datetime"
]

export default function DefineColumnDialog(props) {
    const { open, onSuccess, table, handleClose } = props
    const [name, setName] = useState("")
    const [dataType, setDataType] = useState(datatypes[0])
    const [nullable, setNullable] = useState(true)
    const [pk, setPk] = useState(false)
    const [fk, setFk] = useState(false)
    const [fkTable, setFkTable] = useState("")
    const [fkColumn, setFkColumn] = useState("")
    const [defaultValue, setDefaultValue] = useState("")
    const [fkDataType, setFkDataType] = useState("")
    const [availableFkColumns, setAvailableFkColumns] = useState([])
    const [availableFkTables, setAvailableFkTables] = useState([])
    const [errorMsg, setErrorMsg] = useState("")
    const [empty, setEmpty] = useState(false)


    useEffect(() => {
        if (open) {
            const q = new QueryService(table)
            q.getRowSet(data => setEmpty(data.data.length == 0), () => setEmpty(false))
            q.getTables(
                (data) => setAvailableFkTables(data.data),
                () => setErrorMsg("Error loading tables for column reference")
            )
        }
    }, [open])


    const onEnter = (e) => {
        e.preventDefault()
        const data = {
            name: name,
            datatype: dataType,
            nullable: pk ? false : nullable, // if column is PK it shouldnt be nullable
            pk: pk,
            fk: fk ? `${fkTable}.${fkColumn}` : null,
            defaultValue: defaultValue
        }

        new DefinitionService(table).addColumn(data,
            () => {
                onSuccess()
                onClose()
            },
            (e) => { setErrorMsg(e.response.data) })
    }

    const onClose = () => {
        handleClose()
    }

    const handleSetFkTable = (table) => {
        setFkTable(table)
        const queryService = new QueryService(table)
        queryService.getSchema(data => {
            console.log(data)
            setAvailableFkColumns(data.data.columns)
        },
            () => setErrorMsg("Error loading columns for column reference")
        )
    }

    const handleSetFKColumn = (col) => {
        setFkColumn(col)
        const datatype = availableFkColumns.find(c => c.name == col).datatype.toLowerCase()
        setFkDataType(datatype)
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
                        {errorMsg.length > 0 && <Typography variant="body1" color="error">{errorMsg}</Typography>}
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
                                        disabled={fk}
                                        label="Datatype"
                                        value={fk && fkDataType.length > 0 ? fkDataType : dataType}
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
                                        required={!nullable && !empty}
                                        onChange={(e) => setDefaultValue(e.target.value)}
                                    />
                                </Grid>

                            </Grid>
                            <Grid container direction="column" spacing={1} xs={6} alignItems="flex-end">
                                <Grid item>
                                    <FormControlLabel
                                        label="Nullable"
                                        labelPlacement="start"
                                        disabled={pk}
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
                                                fullWidth
                                                select
                                                onChange={(e) => handleSetFkTable(e.target.value)}
                                            >
                                                {availableFkTables.map((t) => (
                                                    <MenuItem key={t} value={t}>
                                                        {t}
                                                    </MenuItem>
                                                ))}
                                            </TextField>
                                        </Grid>
                                        <Grid item xs={6}>
                                            <TextField
                                                required
                                                id="fkColumn"
                                                label="Column"
                                                fullWidth
                                                value={fkColumn}
                                                onChange={(e) => handleSetFKColumn(e.target.value)}
                                                select
                                            >
                                                {availableFkColumns.map((c) => (
                                                    <MenuItem key={c.name} value={c.name}>
                                                        {c.name}
                                                    </MenuItem>
                                                ))}
                                            </TextField>
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