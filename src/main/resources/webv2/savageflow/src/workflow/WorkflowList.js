import { Button, Divider } from '@material-ui/core';
import IconButton from '@material-ui/core/IconButton';
import List from '@material-ui/core/List';
import ListItem from '@material-ui/core/ListItem';
import ListItemSecondaryAction from '@material-ui/core/ListItemSecondaryAction';
import ListItemText from '@material-ui/core/ListItemText';
import CheckBoxIcon from '@material-ui/icons/CheckBox';
import CheckBoxOutlineBlankIcon from '@material-ui/icons/CheckBoxOutlineBlank';
import DeleteIcon from '@material-ui/icons/Delete';
import EditIcon from '@material-ui/icons/Edit';
import React from 'react';

const WorkflowList = ({ workflows, onDelete, onEdit, onToggle }) => {
    return (
        <List
            style={{
                maxHeight: "70vh",
                width: "40em",
                overflow: 'auto',
                marginTop: '2em'
            }}
        >
            {workflows.map(w =>
                <WorkflowListItem
                    onDelete={onDelete}
                    onToggle={onToggle}
                    onEdit={onEdit}
                    workflow={w} />
            )}
        </List>
    );
}

const WorkflowListItem = ({ workflow, onToggle, onDelete, onEdit }) => {
    const id = workflow.id
    const name = workflow.name
    const enabled = workflow.enabled

    return (
        <>
            <ListItem style={{backgroundColor: "white", margin: '0.5em 0'}} >
                <ListItemText
                    style={{overflow: 'auto', maxWidth: "70%"}}
                    primary={name}
                />
                <ListItemSecondaryAction>
                    <Button variant="outlined" onClick={() => onToggle(id)}>
                        {enabled ? "Disable" : "Enable"}
                    </Button>
                    <IconButton edge="end" aria-label="edit" onClick={() => onEdit(id)}>
                        <EditIcon />
                    </IconButton>
                    <IconButton edge="end" aria-label="delete" onClick={() => onDelete(id)}>
                        <DeleteIcon />
                    </IconButton>
                </ListItemSecondaryAction>

            </ListItem>
            <Divider />
        </>
    );
}


export default WorkflowList