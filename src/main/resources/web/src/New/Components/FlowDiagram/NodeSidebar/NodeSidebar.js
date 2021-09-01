import Collapse from '@material-ui/core/Collapse';
import List from '@material-ui/core/List';
import ListItem from '@material-ui/core/ListItem';
import ListItemIcon from '@material-ui/core/ListItemIcon';
import ListItemText from '@material-ui/core/ListItemText';
import ListSubheader from '@material-ui/core/ListSubheader';
import { makeStyles } from '@material-ui/core/styles';
import AssignmentIcon from '@material-ui/icons/Assignment';
import CompareArrowsIcon from '@material-ui/icons/CompareArrows';
import ExpandLess from '@material-ui/icons/ExpandLess';
import ExpandMore from '@material-ui/icons/ExpandMore';
import FlashOnIcon from '@material-ui/icons/FlashOn';
import React, { useState } from "react";

const useStyles = makeStyles((theme) => ({
    root: {
        width: '100%',
        maxWidth: 360,
        backgroundColor: theme.palette.background.paper,
    },
    nested: {
        paddingLeft: theme.spacing(4),
    },
}));

const NodeSidebar = (props) => {
    const classes = useStyles();
    const [openTriggers, setOpenTriggers] = useState(false)
    const [openEvaluations, setOpenEvaluations] = useState(false)
    const [openTasks, setOpenTasks] = useState(false)


    return (
        <List
            component="nav"
            aria-labelledby="nested-list-subheader"
            subheader={
                <ListSubheader component="div" id="nested-list-subheader">
                    Workflow Elements
                </ListSubheader>
            }
            className={classes.root}
        >
            <ElementSublist text="Triggers" onExpand={() => setOpenTriggers(v => !v)} expand={openTriggers} Icon={FlashOnIcon} />
            <ElementSublist text="Tasks" onExpand={() => setOpenTasks(v => !v)} expand={openTasks} Icon={AssignmentIcon} />
            <ElementSublist text="Evaluations" onExpand={() => setOpenEvaluations(v => !v)} expand={openEvaluations} Icon={CompareArrowsIcon} />
        </List>

    );
}


const ElementSublist = ({ onExpand, expand, Icon, elements, text }) => {
    const classes = useStyles();

    const onDragStart = (event, nodeType) => {
        event.dataTransfer.setData('application/reactflow', nodeType);
        event.dataTransfer.effectAllowed = 'move';
    }

    return (
        <>
            <ListItem button onClick={onExpand}>
                <ListItemIcon>
                    <Icon />
                </ListItemIcon>
                <ListItemText primary={text} />
                {expand ? <ExpandLess /> : <ExpandMore />}
            </ListItem>

            <Collapse in={expand} timeout="auto" unmountOnExit>
                <List component="div" disablePadding>
                    {elements && elements.map(e => (
                        <div draggable onDragStart={(event) => onDragStart(event, e.id)}>
                            <ListItem button className={classes.nested}>
                                {e.name}
                            </ListItem>
                        </div>
                    ))}
                </List>
            </Collapse>
        </>
    )
}

export default NodeSidebar;
