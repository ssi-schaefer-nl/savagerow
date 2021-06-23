import React, { useEffect, useState } from "react";
import AddIcon from '@material-ui/icons/Add';
import { ContextMenu, ContextMenuTrigger, MenuItem } from "react-contextmenu"
import RemoveIcon from '@material-ui/icons/Remove';

import { Menu, Paper, Table, TableBody, TableCell, TableContainer, TableHead, TableRow } from "@material-ui/core";
import { Checkbox, Divider, FormControlLabel } from "@material-ui/core"

import { Grid, TextField, Typography } from '@material-ui/core';
import { InputLabel, Select } from '@material-ui/core';

import Button from '@material-ui/core/Button';
import RowCriterion from "../AddSimpleWorkflow/AddAction/RowCriterion";
import PopupForm from "../../../../../../Components/PopupForm/PopupForm";
import QueryService from "../../../../../../Service/QueryService/QueryService";



const WorkflowConditions = props => {
    // const [conditions, setConditions] = useState([])
    const { onChange, conditions, table } = props

    const [newCondition, setNewCondition] = useState(false)
    const [initial, setInitial] = React.useState(null)
    const [actionSubmit, setActionSubmit] = React.useState(null);

    const handleAdd = () => {
        setActionSubmit(() => (x) => addCondition(x))
        setNewCondition(true)
    }

    const addCondition = (condition) => {
        const newConditions = [...conditions, condition]
        onChange(newConditions)
        setActionSubmit(null)
        setNewCondition(false)

    }

    const removeCondition = (i) => {
        const copyOfConditions = [...conditions];
        copyOfConditions.splice(i, 1)
        onChange(copyOfConditions);
    }

    const onClose = () => setNewCondition(false)
    return (
        <div>
            <Typography variant="h6">Define the conditions for the workflow execution</Typography>
            <Condition placeholders={[]} conditions={conditions} onAdd={handleAdd} onDelete={removeCondition} />
            <NewWorkflowCondition open={newCondition} onClose={onClose} initial={initial} workflowTable={table} onSubmit={actionSubmit} />
        </div >
    )

}

const Condition = props => {
    const { conditions, onDelete, onAdd } = props
    return (
        <TableContainer component={Paper} style={{ maxHeight: "50vh", maxWidth: "40%", margin: "4em 0" }}>
            <Table stickyHeader >
                <TableHead >
                    <TableRow>
                        <TableCell>Table</TableCell>
                        <TableCell align="right">Must Match</TableCell>
                        <TableCell align="right">Criteria</TableCell>
                        <TableCell align="right">
                            <Button
                                aria-controls="add-action"
                                aria-haspopup="true"
                                color="primary"
                                onClick={onAdd}
                            >
                                <AddIcon />
                            </Button>
                        </TableCell>
                    </TableRow>
                </TableHead>
                <TableBody>
                    {conditions.length > 0
                        ?
                        conditions.map((data, i) => (
                            <TableRow key={data.table}>
                                <TableCell component="th" scope="row">{data.table}</TableCell>
                                <TableCell align="right">{data.match ? "Yes" : "No"}</TableCell>
                                <TableCell align="right">{data.rowCriteria.length}</TableCell>
                                <TableCell align="right">
                                    <Button
                                        aria-controls="add-action"
                                        aria-haspopup="true"
                                        color="secondary"
                                        onClick={(e) => onDelete(i)}
                                    >
                                        <RemoveIcon />
                                    </Button>
                                </TableCell>
                            </TableRow>
                        ))
                        :
                        <TableRow>
                            <TableCell component="th" scope="row" />
                            <TableCell align="right" />
                            <TableCell align="right" />
                        </TableRow>
                    }
                </TableBody>

            </Table>
        </TableContainer >
    )
}

const NewWorkflowCondition = props => {
    const { onSubmit, workflowTable, open, onClose } = props

    const [tables, setTables] = useState([])
    const [tableColumns, setTableColumns] = React.useState(null);

    const [table, setTable] = useState("")
    const [thisRow, setThisRow] = useState(true)
    const [match, setMatch] = useState(true)
    const [rowCriteria, setRowCriteria] = useState([])

    const handleSubmit = e => {
        e.preventDefault()
        if (thisRow) {
            const crit = [...rowCriteria, ...tableColumns.filter(c => c.pk).map(c => ({ column: c.name, operator: "==", required: `{${c.name}}` }))]
            onSubmit({ rowCriteria: crit, table: workflowTable, match: match })
            console.log({ rowCriteria: crit, table: workflowTable, match: match })
        }
        else {
            onSubmit({ rowCriteria: rowCriteria, table: table, match: match })
        }
        setThisRow(true)
        setTable("")
        setMatch(true)
        setRowCriteria([])
    }

    useEffect(() => {
        const queryService = new QueryService(workflowTable)
        queryService.getTables(data => setTables(data.data), () => setTables([]))
        queryService.getSchema(data => setTableColumns(data.data.columns), () => setTableColumns([]))

    }, [])
    console.log(table)
    return (
        <PopupForm open={open} onSubmit={handleSubmit} onClose={onClose} title="Create a workflow condition">
            <FormControlLabel
                control={
                    <Checkbox
                        checked={thisRow}
                        onChange={(e) => setThisRow(e.target.checked)}
                        name="trigger"
                        color="primary"
                    />
                }
                label="Condition is based on the row that triggered the workflow"
            />
            {!thisRow ?
                <>
                    <Grid container direction="row" alignItems="center" spacing={2}>
                        <Grid item>
                            <Typography>A row in table </Typography>
                        </Grid>
                        <Grid item>
                            <Select
                                InputLabelProps={{ shrink: true }}
                                style={{ minWidth: "20%" }}
                                onChange={(e) => setTable(e.target.value)}
                                value={table}
                                required
                            >
                                {tables.map(item => (<MenuItem key={item} value={item}>{item}</MenuItem>))}
                            </Select>
                        </Grid>
                        <Grid item>
                            <Select
                                InputLabelProps={{ shrink: true }}
                                style={{ minWidth: "20%" }}
                                onChange={(e) => setMatch(e.target.value)}
                                value={match}
                                required
                            >
                                {[{ label: "should", value: true }, { label: "should not", value: false }].map(item => (<MenuItem key={item.value} value={item.value}>{item.label}</MenuItem>))}
                            </Select>
                        </Grid>
                        <Grid item>
                            <Typography> exist</Typography>
                        </Grid>

                    </Grid>
                    <Typography style={{ marginTop: "1em" }}>That matches the following criteria</Typography>
                    <RowCriterion
                        requireValues={false}
                        onChange={setRowCriteria}
                        value={rowCriteria}
                        placeholders={{ table: workflowTable, values: tableColumns != null ? tableColumns.map(c => c.name) : [] }}
                        table={table}
                    />
                </>
                :
                <>
                    <Typography>The row should match the following criteria</Typography>
                    < RowCriterion
                        requireValues={false}
                        onChange={setRowCriteria}
                        value={rowCriteria}
                        placeholders={{ table: workflowTable, values: tableColumns != null ? tableColumns.map(c => c.name) : [] }}
                        table={workflowTable}
                    />
                </>
            }


        </PopupForm >
    )
}


export default WorkflowConditions