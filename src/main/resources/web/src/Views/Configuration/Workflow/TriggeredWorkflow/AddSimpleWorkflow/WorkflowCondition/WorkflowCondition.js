import { Checkbox, FormControlLabel, Grid, Menu, Paper, InputLabel, Select, Table, TableBody, TableCell, TableContainer, TableHead, TableRow, Typography } from "@material-ui/core";
import Button from '@material-ui/core/Button';
import AddIcon from '@material-ui/icons/Add';
import RemoveIcon from '@material-ui/icons/Remove';
import React, { useEffect, useState } from "react";
import { MenuItem } from "react-contextmenu";
import QueryService from "../../../../../../Service/QueryService/QueryService";
import RowCriterion from "../../../../../../Components/RowCriterion/RowCriterion";
import VerticalLinearStepper from "../../../../../../Components/VerticalLinearStepper/VerticalLinearStepper";
import PopupWindow from "../../../../../../Components/PopupWindow/PopupWindow";
import EditIcon from '@material-ui/icons/MoreVert';

const WorkflowConditions = props => {
    // const [conditions, setConditions] = useState([])
    const { onChange, conditions, table } = props

    const [newCondition, setNewCondition] = useState(false)
    const [actionSubmit, setActionSubmit] = React.useState(null);
    const [editCondition, setEditCondition] = React.useState(null)

    const handleAdd = () => {
        setActionSubmit(() => (x) => addCondition(x))
        setNewCondition(true)
    }

    const handleEdit = (i) => {
        setActionSubmit(() => (x) => replaceAction(i, x))
        setEditCondition(conditions[i])
        setNewCondition(true)
    }

    const replaceAction = (i, c) => {
        const newConditions = [...conditions]
        newConditions[i] = c
        onChange(newConditions)
        setActionSubmit(null)
        setNewCondition(false)
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

    const onClose = () => {
        setEditCondition(null)
        setNewCondition(false)
    }

    return (
        <div >
            <Typography variant="h6">Define the conditions for the workflow execution</Typography>
            <Typography >
                You can optionally define conditions that must be met in order to execute this workflow.
            </Typography>
            {table != undefined &&
                <Typography>
                    The most basic condition is defining criteria that must be satisfied by the row that triggered this workflow.
                    This is useful, for example, when you only want to execute this workflow if a threshold is exceeded or a certain status is set.
                </Typography>
            }
            <Typography style={{ marginTop: "1em" }}>
                You can {table != undefined && "also"} create conditions by specifying criteria for rows in any table that you either want to exist, or don't want to exist.
                An example of when this is useful is when you want to insert a new row into a table with this workflow, but only if that row does not already exist.
            </Typography>

            <Condition placeholders={[]} conditions={conditions} onAdd={handleAdd} onEdit={handleEdit} onDelete={removeCondition} />
            {newCondition &&
                <NewWorkflowCondition open={newCondition} onClose={onClose} initial={editCondition} workflowTable={table} onSubmit={actionSubmit} />
            }
        </div >
    )

}

const Condition = props => {
    const [addActionAnchor, setAddActionAnchor] = React.useState(null);
    const [editActionMenu, setEditActionMenu] = React.useState(null);
    const [editStep, setEditStep] = useState(0)
    const { conditions, onDelete, onAdd, onEdit } = props

    const handleEditAction = (action) => {
        switch (action) {
            case "delete":
                onDelete(editStep)
                break
            case "edit":
                onEdit(editStep)
                break
        }
        setEditStep(0)
    }


    return (
        <>
            <TableContainer component={Paper} style={{ maxHeight: "35vh", overflow: "auto", maxWidth: "40%", margin: "4em 0" }}>
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
                                <TableRow key={i}>
                                    <TableCell component="th" scope="row">{data.table}</TableCell>
                                    <TableCell align="right">{data.match ? "Yes" : "No"}</TableCell>
                                    <TableCell align="right">{data.rowCriteria.length}</TableCell>
                                    <TableCell align="right">
                                        <Button
                                            aria-controls="edit-action"
                                            aria-haspopup="true"
                                            color="primary"
                                            onClick={(e) => {
                                                setEditActionMenu(e.currentTarget)
                                                setEditStep(i)
                                            }}
                                        >
                                            <EditIcon />
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
            <EditActionMenu onSelect={handleEditAction} anchorMenu={editActionMenu} onClose={() => setEditActionMenu(null)} />

        </>
    )
}

const EditActionMenu = props => {
    const { onSelect, onClose, anchorMenu } = props

    const handleClick = (type) => {
        onSelect(type)
        onClose()
    }
    return (
        <Menu
            id="edit-action"
            anchorEl={anchorMenu}
            keepMounted
            open={Boolean(anchorMenu)}
            onClose={onClose}
            transformOrigin={{ vertical: 'top', horizontal: 'center', }}
        >
            <MenuItem onClick={() => handleClick('delete')}>Delete</MenuItem>
            <MenuItem onClick={() => handleClick('edit')}>Edit</MenuItem>
        </Menu>
    )
}

const NewWorkflowCondition = props => {
    const { onSubmit, workflowTable: workflowTriggerTable, open, onClose, initial } = props

    const [tables, setTables] = useState([])
    const [tableColumns, setTableColumns] = React.useState(null);

    const [table, setTable] = useState(initial == null ? workflowTriggerTable != undefined ? workflowTriggerTable : "" : initial.table)
    const [thisRow, setThisRow] = useState(false)
    const [match, setMatch] = useState(initial == null ? true : initial.match)
    const [rowCriteria, setRowCriteria] = useState(initial == null ? [] : initial.rowCriteria)

    const handleSubmit = () => {
        if (thisRow) {
            const crit = [...rowCriteria, ...tableColumns.filter(c => c.pk).map(c => ({ column: c.name, comparator: "equals", required: `{${c.name}}` }))]
            onSubmit({ rowCriteria: crit, table: workflowTriggerTable, match: match })
            console.log({ rowCriteria: crit, table: workflowTriggerTable, match: match })
        }
        else {
            onSubmit({ rowCriteria: rowCriteria, table: table, match: match })
        }
        setThisRow(true)
        setTable("")
        setMatch(true)
        setRowCriteria([])
    }

    const steps = [
        {
            "name": "Optional: Base workflow condition query on triggering row",
            "Component":
                <>
                    <Typography style={{ marginBottom: "1em", width: "70%" }}>
                        If the workflow condition query should query the row that triggered this workflow, check this option.
                        Certain fields will be automatically set for you.
                    </Typography>
                    <FormControlLabel
                        control={
                            <Checkbox
                                checked={thisRow}
                                onChange={(e) => {
                                    setThisRow(e.target.checked)
                                    setTable(workflowTriggerTable)
                                    setRowCriteria([])
                                }}

                                name="trigger"
                                color="primary"
                            />
                        }
                        label="Condition is based on the row that triggered the workflow"
                    />
                </>,
            "disabled": workflowTriggerTable == undefined
        },
        {
            "name": "Specify the table",
            "Component":
                <>
                    <InputLabel id="select-table">Table</InputLabel>
                    <Select
                        id="select-table"
                        InputLabelProps={{ shrink: true }}
                        style={{ minWidth: "30%" }}
                        onChange={(e) => {
                            setTable(e.target.value)
                            setRowCriteria([])
                        }}
                        value={table}
                        required
                    >
                        {tables.map(item => (<MenuItem key={item} value={item}>{item}</MenuItem>))}
                    </Select>
                </>,
            "nextAllowed": table.length > 0,
            "disabled": thisRow
        },
        {
            "name": "Define whether the condition query should return results",
            "Component":
                <>
                    <Typography style={{ marginBottom: "1em", width: "70%" }}>
                        In the next step you will define criteria for your workflow condition query.
                        With this option you define whether that query should return results or not.
                    </Typography>
                    <InputLabel id="select-true">Return results</InputLabel>
                    <Select
                        id="select-true"
                        InputLabelProps={{ shrink: true }}
                        style={{ minWidth: "20%" }}
                        onChange={(e) => setMatch(e.target.value)}
                        value={match}
                        required
                    >
                        {[{ label: "Yes", value: true }, { label: "No", value: false }].map(item => (<MenuItem key={item.value} value={item.value}>{item.label}</MenuItem>))}
                    </Select>
                </>,
        },
        {
            "name": "Specify the row criteria",
            "Component":
                <>
                    <Typography style={{ marginBottom: "1em", width: "70%" }}>
                        According to your current configuration, there {match ? "should" : "should not"} be rows in the table that match the following criteria.

                    </Typography>
                    {match &&
                        <Typography style={{ marginBottom: "1em", width: "70%" }}>
                            <b>Note</b>: because your workflow condition query is based on the triggering row, the criteria to identify this row have already been set for you.
                        </Typography>
                    }
                    <RowCriterion
                        requireValues={false}
                        onChange={setRowCriteria}
                        value={rowCriteria}
                        placeholders={{ table: workflowTriggerTable, values: tableColumns != null ? tableColumns.map(c => c.name) : [] }}
                        table={table}
                    />
                </>,
            "nextButton": "Save",
            "nextAllowed": rowCriteria.length > 0 && rowCriteria.filter(r => Object.values(r).filter(v => v.length == 0).length > 0).length == 0 ,
            "onNext": handleSubmit,
        },
    ]


    useEffect(() => {
        const queryService = new QueryService(workflowTriggerTable)
        queryService.getTables(data => setTables(data.data), () => setTables([]))
        queryService.getSchema(data => setTableColumns(data.data.columns), () => setTableColumns([]))

    }, [])

    return (
        <PopupWindow open={open} onClose={onClose} title="Create a workflow condition query" wide>
            <Typography style={{ width: "40vw" }}>
                The workflow condition uses queries that either should or should not return a result. The workflow will be executed only if all condition queries are satisfied.
            </Typography>
            <div style={{ width: "50vw", height: "60vh" }}>

                <VerticalLinearStepper steps={steps} />

            </div>

        </PopupWindow >
    )
}


export default WorkflowConditions