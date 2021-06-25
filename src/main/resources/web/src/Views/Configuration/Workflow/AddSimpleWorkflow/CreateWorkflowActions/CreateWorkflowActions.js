import React, { useEffect, useState } from "react";

import Button from '@material-ui/core/Button';
import {  Divider, Menu, Paper, Table, TableBody, TableCell, TableContainer, TableHead, TableRow, Typography } from "@material-ui/core";
import AddIcon from '@material-ui/icons/Add';
import EditIcon from '@material-ui/icons/MoreVert';
import { MenuItem } from "react-contextmenu";
import QueryService from "../../../../../Service/QueryService/QueryService";
import InsertAction from "../AddAction/CRUDActions/InsertAction";
import UpdateAction from "../AddAction/CRUDActions/UpdateAction";
import DeleteAction from "../AddAction/CRUDActions/DeleteAction";

import APICallAction from "../AddAction/APICallAction";


const CreateWorkflowActions = props => {
    const [editAction, setEditAction] = React.useState(null)
    const [actionType, setActionType] = React.useState(null);
    const [actionSubmit, setActionSubmit] = React.useState(null);
    const [tableColumns, setTableColumns] = React.useState(null);

    const { onChange, actions, table } = props
    const queryService = new QueryService(table)
    useEffect(() => {
        queryService.getSchema(data => setTableColumns(data.data.columns), () => setTableColumns([]))
    }, [])

    const handleCloseDialog = () => {
        setActionType(null)
        setEditAction(null)
    }

    const addActionToList = (action) => {
        const step = actions.length + 1
        const newActions = [...actions, { step: step, ...action}]
        onChange(newActions)
        setActionSubmit(null)
    }

    const replaceAction = (step, action) => {
        const pos = actions.findIndex(a => a.step == step);
        const copyOfActions = [...actions]
        copyOfActions[pos] = action
        onChange(copyOfActions)
        setActionSubmit(null)
    }

    const deleteStep = (step) => {
        const newActions = actions.filter(a => a.step != step).map(a => ({ ...a, step: a.step > step ? a.step - 1 : a.step }))
        onChange(newActions)
    }

    const editStep = (step) => {
        let a = actions.find(a => a.step == step)
        console.log(a)
        if (a != undefined) {
            setActionSubmit(() => (x) => replaceAction(step, x))
            setEditAction(a)
            setActionType(a.type)
        }
    }

    const handleEdit = (type, step) => {
        if (type == "delete") deleteStep(step)
        else if (type === "edit") editStep(step)
    }

    const handleAdd = (action) => {
        setActionType(action)
        setActionSubmit(() => (x) => addActionToList(x))
    }

    return (
        <div>
            <Typography variant="h6">Create actions for the workflow</Typography>
            {actions != undefined && <ActionList onAdd={handleAdd} onEdit={handleEdit} actions={actions}/>}

            <ActionDialogSwitch
                type={actionType}
                placeholders={{table: table, values: tableColumns != null ? tableColumns.map(c => c.name) : []}}
                onSubmit={actionSubmit}
                onClose={handleCloseDialog}
                initial={editAction}
                table={table}
                columns={tableColumns}
            />
        </div >
    )

}


const ActionDialogSwitch = props => {
    const { type, placeholders, onSubmit, initial, onClose, table, columns } = props
    switch (type) {
        // case "email": return <EmailAction open={Boolean(type) && Boolean(onSubmit)} onClose={onClose} initial={initial} placeholders={placeholders} onSubmit={onSubmit} />
        case "insert": return <InsertAction open={Boolean(type) && Boolean(onSubmit)} onClose={onClose} initial={initial} placeholders={placeholders} onSubmit={onSubmit} />
        case "update": return <UpdateAction open={Boolean(type) && Boolean(onSubmit)} onClose={onClose} initial={initial} workflowTable={table} onSubmit={onSubmit} />
        case "delete": return <DeleteAction open={Boolean(type) && Boolean(onSubmit)} workflowTable={table} onClose={onClose} initial={initial} onSubmit={onSubmit} />
        case "api call": return <APICallAction open={Boolean(type) && Boolean(onSubmit)} onClose={onClose} initial={initial} placeholders={placeholders} onSubmit={onSubmit} />


        default: return null
    }
}



const ActionList = props => {
    const [addActionAnchor, setAddActionAnchor] = React.useState(null);
    const [editActionMenu, setEditActionMenu] = React.useState(null);

    const { onAdd, onEdit, actions } = props
    const [editStep, setEditStep] = useState(0)

    const handleSelectEdit = (type) => {
        onEdit(type, editStep)
        setEditStep(0)
    }

    return (
        <>
            <TableContainer component={Paper} style={{ maxHeight: "50vh", margin: "2em 0" }}>
                <Table stickyHeader >
                    <TableHead >
                        <TableRow>
                            <TableCell>Step</TableCell>
                            <TableCell align="right">Name</TableCell>
                            <TableCell align="right">Type</TableCell>
                            <TableCell align="right">
                                <Button
                                    aria-controls="add-action"
                                    aria-haspopup="true"
                                    color="primary"
                                    onClick={(e) => setAddActionAnchor(e.currentTarget)}
                                >
                                    <AddIcon />
                                </Button>
                            </TableCell>
                        </TableRow>
                    </TableHead>
                    <TableBody>
                        {actions.length > 0
                            ?
                            actions.map((data) => (
                                <TableRow key={data.step}>
                                    <TableCell component="th" scope="row">{data.step}</TableCell>
                                    <TableCell align="right">{data.name}</TableCell>
                                    <TableCell align="right">{data.type}</TableCell>
                                    <TableCell align="right">
                                        <Button
                                            aria-controls="edit-action"
                                            aria-haspopup="true"
                                            color="primary"
                                            onClick={(e) => {
                                                setEditActionMenu(e.currentTarget)
                                                setEditStep(data.step)
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
                                <TableCell align="right" />
                            </TableRow>
                        }
                    </TableBody>

                </Table>
            </TableContainer>
            <SelectActionTypeMenu onSelect={onAdd} anchorMenu={addActionAnchor} onClose={() => setAddActionAnchor(null)} />
            <EditActionMenu onSelect={handleSelectEdit} anchorMenu={editActionMenu} onClose={() => setEditActionMenu(null)} />

        </>
    )
}


const SelectActionTypeMenu = props => {
    const { onSelect, onClose, anchorMenu } = props

    const handleClick = (type) => {
        onSelect(type)
        onClose()
    }
    return (
        <Menu
            id="add-action"
            anchorEl={anchorMenu}
            keepMounted
            open={Boolean(anchorMenu)}
            onClose={onClose}
            transformOrigin={{ vertical: 'top', horizontal: 'center', }}
        >
            <MenuItem disabled>Add Action</MenuItem>
            <MenuItem onClick={() => handleClick('insert')}>Insert</MenuItem>
            <MenuItem onClick={() => handleClick('update')}>Update</MenuItem>
            <MenuItem onClick={() => handleClick('delete')}>Delete</MenuItem>
            <Divider/>
            <MenuItem onClick={() => handleClick('api call')}>API Call</MenuItem>

        </Menu>
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




export default CreateWorkflowActions