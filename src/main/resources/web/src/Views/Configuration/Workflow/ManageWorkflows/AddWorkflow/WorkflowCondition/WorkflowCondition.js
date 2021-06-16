import React, { useEffect, useState } from "react";
import AddIcon from '@material-ui/icons/Add';
import { ContextMenu, ContextMenuTrigger, MenuItem } from "react-contextmenu"
import RemoveIcon from '@material-ui/icons/Remove';

import { Menu, Paper, Table, TableBody, TableCell, TableContainer, TableHead, TableRow } from "@material-ui/core";

import { Grid, TextField, Typography } from '@material-ui/core';
import { InputLabel, Select } from '@material-ui/core';

import Button from '@material-ui/core/Button';
import RowCriterion from "../AddSimpleWorkflow/AddAction/RowCriterion";
import PopupForm from "../AddSimpleWorkflow/AddAction/PopupForm";
import QueryService from "../../../../../../Service/QueryService/QueryService";



const WorkflowConditions = props => {
    // const [conditions, setConditions] = useState([])
    const { onChange, conditions, table } = props

    const [newCondition, setNewCondition] = useState(false)
    const [initial, setInitial] = React.useState(null)
    const [actionSubmit, setActionSubmit] = React.useState(null);
    const [placeholders, setPlaceholders] = React.useState([]);

    useEffect(() => {
        const queryService = new QueryService(table)
        queryService.getSchema(data => setPlaceholders(data.data.columns.map(c => c.name)), () => setPlaceholders([]))
    }, [])

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
            <NewWorkflowCondition open={newCondition} onClose={onClose} initial={initial} placeholders={placeholders} onSubmit={actionSubmit} />
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
    const { onSubmit, placeholders, open, onClose, initial } = props
    const [tables, setTables] = useState([])

    const [table, setTable] = useState(initial == null ? "" : initial.table)
    const [rowCriteria, setRowCriteria] = useState(initial == null ? [] : initial.fieldUpdates)

    const handleSubmit = e => {
        e.preventDefault()
        onSubmit({ rowCriteria: rowCriteria, table: table })
    }

    useEffect(() => {
        const queryService = new QueryService("")
        queryService.getTables(data => setTables(data.data), () => setTables([]))
    }, [])

    return (
        <PopupForm open={open} onSubmit={handleSubmit} onClose={onClose} title="Create a workflow condition">
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
                    <Typography> Has to exist</Typography>
                </Grid>
            </Grid>
            {table.length > 0 && <>
                <Typography >Of which its rows satisfy the following criteria</Typography>
                <RowCriterion requireValues={false} onChange={setRowCriteria} value={rowCriteria} placeholders={placeholders} table={table}/>
            </>}


        </PopupForm>
    )
}


export default WorkflowConditions