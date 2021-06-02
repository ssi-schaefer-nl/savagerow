import React, { useState } from "react";

import Button from '@material-ui/core/Button';
import { Grid, makeStyles, Menu, Paper, Table, TableBody, TableCell, TableContainer, TableHead, TableRow } from "@material-ui/core";
import AddIcon from '@material-ui/icons/Add';
import EditIcon from '@material-ui/icons/MoreVert';
import SimpleDialog from "../../../../../../../Components/SimpleDialog/SimpleDialog";
import AddAction from "../AddAction/AddEmailAction";
import { MenuItem } from "react-contextmenu";


const CreateWorkflowActions = props => {
    const [anchorMenu, setAnchorMenu] = React.useState(null);
    const [actionType, setActionType] = React.useState(null);
    const [actions, setActions] = useState([]);
    const [addAction, setAddAction] = useState(null)

    const addActionToList = (action) => {
        const step = actions.length + 1
        const newAction = { step: step, ...action }
        setActions(prevActions => [...prevActions, newAction]);

    }

    return (
        <div>

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

            <AddActionDialogSwitch type={actionType} onClose={() => setActionType(null)} onAdd={(action) => addActionToList(action)}/>



        </div>
    )

}


const AddActionDialogSwitch = props => {
    const { type, onClose, onAdd } = props

    const getDialog = () => {
        switch (type) {
            case "email": return <AddAction onApply={(email) => {
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