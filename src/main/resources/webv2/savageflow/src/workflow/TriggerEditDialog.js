import { Button, Grid, MenuItem, Select, TextField, Typography } from "@material-ui/core";
import Dialog from '@material-ui/core/Dialog';
import DialogActions from '@material-ui/core/DialogActions';
import DialogContent from '@material-ui/core/DialogContent';
import DialogTitle from '@material-ui/core/DialogTitle';
import { useEffect, useState } from "react";
import TableService from "../table/TableService";


const TriggerEditDialog = ({ open, handleClose, trigger, onChange, onSave }) => {

    console.log(trigger)
    return (
        <Dialog
            open={open}
            onClose={handleClose}
            fullWidth
            maxWidth="lg"
        >
            <DialogTitle>
                <TextField
                    InputProps={{ style: { fontSize: "1.5em" } }}
                    value={trigger.name}
                    fullWidth
                    onChange={(e) => onChange(({ ...trigger, name: e.target.value }))}
                />
            </DialogTitle>
            <DialogContent style={{ height: '60vh' }}>
                <Grid container direction='row' style={{ width: '100%', height: '90%' }}>
                    <Grid item xs={true}>
                        <WorkflowTriggerEditor trigger={trigger} onChange={onChange} />
                    </Grid>
                </Grid>
            </DialogContent>
            <DialogActions>
                <Button onClick={handleClose} color="primary">Cancel</Button>
                <Button onClick={onSave} color="primary" autoFocus>Save</Button>
            </DialogActions>
        </Dialog>
    )
}

const WorkflowTriggerEditor = ({ trigger, onChange }) => {
    switch (trigger.triggerType) {
        case "table": return <UpdateTriggerForm trigger={trigger} onChange={onChange} />
        default: return null;
    }
}



const UpdateTriggerForm = ({ trigger, onChange }) => {
    const [tables, setTables] = useState([])

    const updateTable = (table) =>
        onChange({ ...trigger, table: table })


    const updateTableEvent = (e) =>
        onChange({ ...trigger, tableEvent: e })


    useEffect(() => {
        new TableService().getTables(setTables, console.log)
    }, [])

    return (
        <Grid container direction='column' style={{ padding: "2em" }} spacing={5}>
            <Grid item>
                <Typography variant="h6">Table</Typography>
                <Select
                    id="select-table"
                    InputLabelProps={{ shrink: true }}
                    style={{ minWidth: "10em" }}
                    onChange={(e) => updateTable(e.target.value)}
                    value={trigger.table}
                    placeholder="Select the table"
                    required
                >
                    {tables.map(item => (<MenuItem key={item} value={item}>{item}</MenuItem>))}
                </Select>
            </Grid>
            <Grid item>
                <Typography variant="h6">Table Event</Typography>
                <Select
                    id="select-table"
                    InputLabelProps={{ shrink: true }}
                    style={{ minWidth: "10em" }}
                    onChange={(e) => updateTableEvent(e.target.value)}
                    value={trigger.tableEvent}
                    placeholder="Select the table event"
                    required
                >
                    {["update", "insert", "delete"].map(item => (<MenuItem key={item} value={item}>{item}</MenuItem>))}
                </Select>
            </Grid>

        </Grid >

    )
}


export default TriggerEditDialog;
