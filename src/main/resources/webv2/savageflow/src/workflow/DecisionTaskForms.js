import { Button, Divider, Grid, MenuItem, Select, TextField, Typography } from "@material-ui/core";
import Table from '@material-ui/core/Table';
import TableBody from '@material-ui/core/TableBody';
import TableCell from '@material-ui/core/TableCell';
import TableContainer from '@material-ui/core/TableContainer';
import TableHead from '@material-ui/core/TableHead';
import TableRow from '@material-ui/core/TableRow';
import { useState } from "react";

const DecisionTaskForms = ({ workflow, task, onChange }) => {
    const updateCriteria = (c) => onChange({ ...task, criteria: c })
    const updateFalseRoute = (id) => {
        onChange({ ...task, falseroute: id })
    }
    const updateTrueRoute = (id) => {
        onChange({ ...task, trueroute: id })
    }

    const [trueroute, setTrueroute] = useState(task.trueroute ? task.trueroute : '')
    const [falseroute, setFalseroute] = useState(task.falseroute ? task.falseroute : '')


    return (
        <Grid container direction='column' style={{ padding: "2em" }} spacing={5}>
            <DecisionForm decisionCriteria={task.criteria} onChange={updateCriteria} />
            <Grid item style={{ margin: '0.5em 0' }} />
            <Typography variant="h6">Decision Routing</Typography>
            <Grid item>
                <strong>If true, go to</strong>
                <Select
                    InputLabelProps={{ shrink: true }}
                    style={{ marginLeft: "1em", minWidth: "10em" }}
                    onChange={(e) => updateTrueRoute(e.target.value)}
                    value={task.trueroute ? task.trueroute : ''}
                    required
                >
                    <MenuItem key={''} value={''}>None</MenuItem>
                    {task.neighbors.map(item => (<MenuItem key={item} value={item}>{workflow.tasks.find(t => t.id == item).name}</MenuItem>))}
                </Select>
            </Grid>
            <Grid item>
                <strong>If false, go to</strong>
                <Select
                    InputLabelProps={{ shrink: true }}
                    style={{ marginLeft: "1em", minWidth: "10em" }}
                    onChange={(e) => updateFalseRoute(e.target.value)}
                    value={task.falseroute ? task.falseroute : ''}
                    required
                >
                    <MenuItem key={''} value={''}>None</MenuItem>
                    {task.neighbors.map(item => (<MenuItem key={item} value={item}>{workflow.tasks.find(t => t.id == item).name}</MenuItem>))}
                </Select>
            </Grid>
        </Grid>

    )
}


const DecisionForm = ({ decisionCriteria, onChange }) => {
    const setValue = (index, field, value) => {
        let d = [...decisionCriteria]
        let newDecision = { ...d[index], [field]: value }
        d[index] = newDecision
        onChange(d)
    }

    const newCriterion = () => onChange(decisionCriteria.concat({ value1: "", comparison: "", value2: "" }))
    const deleteCriterion = (index) => onChange(decisionCriteria.filter((u, i) => i != index))


    if (decisionCriteria == null) {
        onChange([])
        return null
    }

    return (
        <TableContainer style={{ width: "50em" }}>
            <Table>
                <TableHead>
                    <Typography variant="h6">Decision Criteria</Typography>
                    <TableRow>
                        <TableCell>Value 1</TableCell>
                        <TableCell >Comparison</TableCell>
                        <TableCell>Value 2</TableCell>
                        <TableCell align="right"><Button variant='outlined' onClick={newCriterion}>Add</Button></TableCell>
                    </TableRow>
                </TableHead>
                <TableBody>
                    {decisionCriteria.map((d, i) => (
                        <TableRow key={i}>
                            <TableCell component="th" scope="row">
                                <TextField value={d.value1} onChange={(e) => setValue(i, 'value1', e.target.value)} />
                            </TableCell>
                            <TableCell component="th" scope="row">
                                <Select
                                    InputLabelProps={{ shrink: true }}
                                    style={{ minWidth: "10em" }}
                                    onChange={(e) => setValue(i, 'comparison', e.target.value)}
                                    value={d.comparison}
                                    required
                                >
                                    {["equals", "greater than", "smaller than", "not equals"].map(item => (<MenuItem key={item} value={item}>{item}</MenuItem>))}
                                </Select>
                            </TableCell>
                            <TableCell >
                                <TextField value={d.value2} onChange={(e) => setValue(i, 'value2', e.target.value)} />
                            </TableCell>
                            <TableCell align="right"><Button variant='outlined' onClick={() => deleteCriterion(i)}>Delete</Button></TableCell>
                        </TableRow>
                    ))}
                </TableBody>
            </Table>
        </TableContainer>
    )
}

export default DecisionTaskForms;