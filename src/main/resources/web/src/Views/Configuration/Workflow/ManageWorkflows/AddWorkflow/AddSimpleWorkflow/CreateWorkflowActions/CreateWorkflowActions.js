import React, { useEffect, useState } from "react";

import Button from '@material-ui/core/Button';
import { Grid, makeStyles, Menu, Paper, Table, TableBody, TableCell, TableContainer, TableHead, TableRow, Typography } from "@material-ui/core";
import AddIcon from '@material-ui/icons/Add';
import EditIcon from '@material-ui/icons/MoreVert';
import SimpleDialog from "../../../../../../../Components/SimpleDialog/SimpleDialog";
import AddAction from "../AddAction/AddEmailAction";
import { MenuItem } from "react-contextmenu";
import QueryService from "../../../../../../../Service/QueryService/QueryService";


const CreateWorkflowActions = props => {

    const [anchorMenu, setAnchorMenu] = React.useState(null);
    const [actionType, setActionType] = React.useState(null);
    const [tableColumns, setTableColumns] = React.useState(null);
    const { onChange, actions, table } = props
    const queryService = new QueryService(table)

    useEffect(() => {
        queryService.getSchema(data => setTableColumns(data.data.columns), () => setTableColumns([]))
    }, [])


    const addActionToList = (action) => {
        const step = actions.length + 1
        const newActions = [...actions, { step: step, ...action }]
        onChange(newActions)
    }

    return (
        <div>
            <Typography variant="h6">Create actions for the workflow</Typography>

            <TableContainer component={Paper} style={{ maxHeight: "50vh", margin: "2em 0" }}>
                <Table stickyHeader >
                    <TableHead >
                        <TableRow>
                            <TableCell>Step</TableCell>
                            <TableCell align="right">Name</TableCell>
                            <TableCell align="right">Type</TableCell>
                            <TableCell align="right">
                                <Button
                                    aria-controls="add-workflow"
                                    aria-haspopup="true"
                                    color="primary"
                                    onClick={(e) => setAnchorMenu(e.currentTarget)}
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
                                        <Button>
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

            <SelectActionTypeMenu onSelect={setActionType} anchorMenu={anchorMenu} onClose={() => setAnchorMenu(null)} />

            <AddActionDialogSwitch columns={tableColumns} type={actionType} onClose={() => setActionType(null)} onAdd={(action) => addActionToList(action)} />



        </div>
    )

}


const AddActionDialogSwitch = props => {
    const { type, onClose, onAdd, columns } = props

    const getDialog = () => {
        switch (type) {
            case "email": return <AddAction columns={columns} onApply={(email) => {
                const action = {
                    ...email,
                    "type": "email"
                }
                onAdd(action)
                onClose()
            }} />
        }
    }
    return (
        <SimpleDialog title="Create new action" open={Boolean(type)} onClose={onClose} >
            {getDialog()}
        </SimpleDialog>
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
            anchorOrigin={{ vertical: "bottom", horizontal: "center" }}
            transformOrigin={{ vertical: 'top', horizontal: 'center', }}
        >
            <MenuItem disabled>Add Action</MenuItem>
            <MenuItem onClick={() => handleClick('email')} >E-mail</MenuItem>
            <MenuItem onClick={() => handleClick('crud')}>CRUD</MenuItem>
            <MenuItem onClick={() => handleClick('alert')}>Alert</MenuItem>
        </Menu>
    )
}

export default CreateWorkflowActions