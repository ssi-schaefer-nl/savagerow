import { makeStyles, Typography } from '@material-ui/core';
import React from 'react';
import { Handle } from 'react-flow-renderer';
import { nodeHeight, nodeWidth } from './FlowDiagramElementUtils';


const useStyles = makeStyles({
    root: {
        padding: '0.2em',
        width: nodeWidth,
        display: "flex",
        flexDirection: "column",
        justifyContent: "center",
        height: nodeHeight,
        border: "2px solid black",
        backgroundColor: "white"
    },
});

const ReactFlowTaskNode = ({ data, isConnectable }) => {
    const classes = useStyles();

    return (
        <div className={classes.root}>
            <Handle
                type="target"
                position="left"
                id="targetA"
                isConnectable={isConnectable}
            />
            <Handle
                type="source"
                id="sourceA"
                position="right"
                isConnectable={isConnectable}
            />
            <Typography align="center" style={{ wordWrap: "break-word" }}>{data.label}</Typography>
        </div>
    )
}


const ReactFlowTriggerNode = ({ data, isConnectable }) => {
    const classes = useStyles();

    return (

        <div className={classes.root} style={{ borderRadius: '100px' }}>
            <Handle
                type="source"
                position="right"
                id="sourceA"
                isConnectable={isConnectable}
            />
            <Typography align="center" style={{ wordWrap: "break-word" }}>{data.label}</Typography>
        </div>
    )
}

const ReactFlowDecisionNode = ({ data, isConnectable }) => {
    const classes = useStyles();

    return (

        <div className={classes.root} style={{ borderRadius: '100px' }}>
            <Handle
                type="target"
                position="left"
                id="targetA"
                isConnectable={isConnectable}
            />
            <Handle
                type="source"
                id="sourceA"
                position="right"
                style={{ top: 10, background: '#555' }}
                isConnectable={isConnectable}
            />
            <Handle
                type="source"
                id="sourceB"
                position="right"
                style={{ bottom: 10, top: 'auto', background: '#555' }}
                isConnectable={isConnectable}
            />
            <Typography align="center" style={{ wordWrap: "break-word" }}>{data.label}</Typography>
        </div>
    )
}


export { ReactFlowTaskNode, ReactFlowTriggerNode };
