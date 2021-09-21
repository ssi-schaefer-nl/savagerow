import { Button, CircularProgress, Grid, MenuItem, Select, TextField, Typography } from "@material-ui/core";
import Table from '@material-ui/core/Table';
import TableBody from '@material-ui/core/TableBody';
import TableCell from '@material-ui/core/TableCell';
import TableContainer from '@material-ui/core/TableContainer';
import TableHead from '@material-ui/core/TableHead';
import TableRow from '@material-ui/core/TableRow';
import { useEffect, useState } from "react";
import TableService from "../table/TableService";

const CrudTaskForm = ({ task, onChange }) => {
    const [tables, setTables] = useState([])
    const [loading, setLoading] = useState(true)
    const [columns, setColumns] = useState([])
    const [initial, setInitial] = useState(true)

    const updateTable = (table) => {
        onChange({ ...task, table: table })
    }
    const updateRowTemplate = (rt) => onChange({ ...task, rowTemplate: rt })
    const updateRowSelections = (rs) => onChange({ ...task, rowSelectionCriteria: rs })
    const updateRowUpdates = (ru) => onChange({ ...task, updateInstructions: ru })

    useEffect(() => {
        new TableService().getTables(setTables, console.log)
    }, [])

    useEffect(() => {
        if (!initial) {
            onChange({ ...task, rowTemplate: null, rowSelectionCriteria: null, updateInstructions: null })
        }

        if (task.table != undefined) {
            setLoading(true)
            new TableService().getSchema(task.table, (r) => {
                setColumns(r.columns)
                setLoading(false)
                setInitial(false)
            }, console.log)

        }
    }, [task.table])

    console.log(task)
    return (
        <Grid container direction='column' style={{ padding: "2em" }} spacing={5}>
            <Grid item>
                <SelectTableForm tables={tables} table={task.table == undefined ? '' : task.table} onChange={updateTable} />
            </Grid>
            {loading
                ?
                task.table == undefined ? null
                    : <Grid item><CircularProgress /></Grid>
                :
                <>
                    {["delete", "update", "select"].includes(task.subtype) &&
                        <Grid item>
                            <RowSelectionForm columns={columns.filter(c => c.pk).map(c => c.name)} rowSelections={task.rowSelectionCriteria} onChange={updateRowSelections} />
                        </Grid>
                    }
                    {task.subtype == "update" &&
                        <Grid item>
                            <RowUpdateForm columns={columns.map(c => c.name)} rowUpdates={task.updateInstructions} onChange={updateRowUpdates} />
                        </Grid>
                    }
                    {task.subtype == "insert" &&
                        <Grid item>
                            <RowDefinitionForm columns={columns.map(c => c.name)} rowTemplate={task.rowTemplate} onChange={updateRowTemplate} />
                        </Grid>
                    }
                </>
            }
        </Grid>

    )

}

const SelectTableForm = ({ tables, table, onChange }) => {

    return (
        <>
            <Typography variant="h6">Table</Typography>
            <Select
                id="select-table"
                InputLabelProps={{ shrink: true }}
                style={{ minWidth: "10em" }}
                onChange={(e) => onChange(e.target.value)}
                value={table}
                placeholder="Select the table"
                required
            >
                {tables.map(item => (<MenuItem key={item} value={item}>{item}</MenuItem>))}
            </Select>
        </>
    )
}

const RowSelectionForm = ({ rowSelections, onChange, columns }) => {
    const setValue = (index, value) => {
        let selections = [...rowSelections]
        let newSelection = { ...selections[index], "value": value }
        selections[index] = newSelection
        onChange(selections)
    }
    if (rowSelections == null) {
        console.log(columns)
        onChange(columns.map(c => ({ "column": c, "comparator": "equals", "value": "" })))
        return null
    }
    return (
        <TableContainer style={{ width: "30em" }}>
            <Table>
                <TableHead>
                    <Typography variant="h6">Row Selection</Typography>
                    <TableRow>
                        <TableCell>Column Name</TableCell>
                        <TableCell >Value</TableCell>
                    </TableRow>
                </TableHead>
                <TableBody>
                    {rowSelections.map((s, i) => (
                        <TableRow key={i}>
                            <TableCell component="th" scope="row">
                                {s.column}
                            </TableCell>
                            <TableCell >
                                <TextField value={s.value} onChange={(e) => setValue(i, e.target.value)} />
                            </TableCell>
                        </TableRow>
                    ))}
                </TableBody>
            </Table>
        </TableContainer>
    )
}

const RowDefinitionForm = ({ rowTemplate, onChange, columns }) => {
    const setValue = (column, value) => {
        const rt = { ...rowTemplate }
        rt[column] = value
        onChange(rt)
    }

    if (rowTemplate == null) {
        onChange(Object.fromEntries(columns.map(c => [c, ""])))
        return null
    }

    return (
        <TableContainer style={{ width: "30em" }}>
            <Table>
                <TableHead>
                    <Typography variant="h6">Row Definition</Typography>
                    <TableRow>
                        <TableCell>Column</TableCell>
                        <TableCell >Value</TableCell>
                    </TableRow>
                </TableHead>
                <TableBody>
                    {Object.keys(rowTemplate).map(c => (
                        <TableRow key={c}>
                            <TableCell component="th" scope="row">
                                {c}
                            </TableCell>
                            <TableCell >
                                <TextField value={rowTemplate[c]} onChange={(e) => setValue(c, e.target.value)} />
                            </TableCell>
                        </TableRow>
                    ))}
                </TableBody>
            </Table>
        </TableContainer>
    )
}

const RowUpdateForm = ({ columns, rowUpdates, onChange }) => {

    const newRowUpdate = () => onChange(rowUpdates.concat({ field: "", operation: "", value: "" }))
    const deleteRowUpdate = (index) => onChange(rowUpdates.filter((u, i) => i != index))

    const operations = ["subtract", "add", "multiply", "set"]

    const setField = (index, key, value) => {
        let updates = [...rowUpdates]
        let newUpdate = { ...updates[index], [key]: value }
        updates[index] = newUpdate
        onChange(updates)
    }

    if (rowUpdates == null) {
        onChange([])
        return null
    }

    return (
        <TableContainer style={{ width: "50em" }}>
            <Table>
                <TableHead>
                    <Typography variant="h6">Row Updates</Typography>
                    <TableRow>
                        <TableCell>Column</TableCell>
                        <TableCell>Operator</TableCell>
                        <TableCell>Value</TableCell>
                        <TableCell align="right"><Button variant='outlined' onClick={newRowUpdate}>Add</Button></TableCell>
                    </TableRow>
                </TableHead>
                <TableBody>
                    {rowUpdates.map((r, i) =>
                        <TableRow key={i}>
                            <TableCell component="th" scope="row">
                                <Select
                                    InputLabelProps={{ shrink: true }}
                                    style={{ minWidth: "10em" }}
                                    onChange={(e) => setField(i, 'field', e.target.value)}
                                    value={r.field}
                                    required
                                >
                                    {columns.map(item => (<MenuItem key={item} value={item}>{item}</MenuItem>))}
                                </Select>
                            </TableCell>
                            <TableCell>
                                <Select
                                    InputLabelProps={{ shrink: true }}
                                    style={{ minWidth: "10em" }}
                                    onChange={(e) => setField(i, 'operation', e.target.value)}
                                    value={r.operation}
                                    required
                                >
                                    {operations.map(item => (<MenuItem key={item} value={item}>{item}</MenuItem>))}
                                </Select>
                            </TableCell>
                            <TableCell><TextField value={r.value} onChange={(e) => setField(i, "value", e.target.value)} /></TableCell>
                            <TableCell align="right"><Button variant='outlined' onClick={() => deleteRowUpdate(i)}>Delete</Button></TableCell>
                        </TableRow>
                    )}
                </TableBody>
            </Table>
        </TableContainer>
    )
}
export default CrudTaskForm;