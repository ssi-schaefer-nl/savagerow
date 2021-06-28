import { Button, Grid } from "@material-ui/core"
import { useState, useEffect } from "react"
import { ContextMenu, ContextMenuTrigger, MenuItem } from "react-contextmenu"
import AddIcon from '@material-ui/icons/Add';
import RemoveIcon from '@material-ui/icons/Remove';
import { TextField } from '@material-ui/core';
import { InputLabel, Select } from '@material-ui/core';

import QueryService from '../../Service/QueryService/QueryService';
import TableColumnContextMenu from "../TableColumnContextMenu/TableColumnContextMenu";

const RowCriterion = props => {
    const { onChange, placeholders, table, value } = props
    const [initialTable, setInitialTable] = useState(table)
    const [columns, setColumns] = useState([])
    const [criteria, setCriteria] = useState((value == undefined || value.length == 0 ? [{ column: "", comparator: "", required: "" }] : value))

    useEffect(() => {
        if (table != null && table.length > 0) {
            new QueryService(table).getSchema(data => {
                setColumns(data.data.columns.map(c => c.name))

            }, () => setColumns([]))
        }

    }, [table])

    const addNewCriterion = () => setCriteria(c => [...c, { column: "", comparator: "", required: "" }])
    const removeCriterion = (i) => {
        const copyOfCriteria = [...criteria];
        copyOfCriteria.splice(i, 1)
        onChange(copyOfCriteria);
        setCriteria(copyOfCriteria)
    }

    console.log(table)
    if (criteria != null && !Array.isArray(criteria)) setCriteria([criteria])
    if (columns.length > 0) {
        return (
            <div style={{ maxHeight: "30vh", overflow: "auto" }}>

                {criteria != null && criteria.map((criterion, i) =>
                    <Criterion
                        placeholders={placeholders}
                        columns={columns}
                        criterion={criteria[i]}
                        onDelete={() => removeCriterion(i)}
                        onChange={(cr) => {
                            const copyOfCriteria = [...criteria];
                            copyOfCriteria[i] = cr;
                            onChange(copyOfCriteria);
                            setCriteria(copyOfCriteria)
                        }}
                    />
                )}
                <Button
                    aria-controls="add-action"
                    aria-haspopup="true"
                    color="primary"
                    style={{ justifyContent: "flex-start" }}
                    onClick={(e) => addNewCriterion()}
                >
                    <AddIcon />
                </Button>
            </div>
        )
    }

    else return null;
}

const operators = {
    "equals": "Equals",
    "not": "Not equals",
    "smaller": "Smaller than",
    "greater": "Greater than"
}

const Criterion = props => {
    const { onChange, placeholders, criterion, onDelete, columns } = props

    const [appender, setAppender] = useState(() => () => undefined)
    const [contextMenuId, setContextMenuId] = useState(Math.floor(Math.random() * 100))

    const handleChange = (field, value) => {
        const newCriterion = { ...criterion, [field]: value }
        onChange(newCriterion)
    }


    return (
        <Grid container direction="row" spacing={2} justify="space-around" style={{ width: "100%", margin: "0.5em 0", border: "1px solid grey", borderRadius: "5px" }}>
            <Grid item xs={3}>
                <InputLabel shrink required id="table">Column</InputLabel>
                <Select
                    InputLabelProps={{ shrink: true }}
                    style={{ width: "100%" }}
                    onChange={(e) => handleChange('column', e.target.value)}
                    value={criterion["column"]}
                    required
                >
                    {Object.keys(columns).map(key => (<MenuItem key={key} value={columns[key]}>{columns[key]}</MenuItem>))}
                </Select>
            </Grid>
            <Grid item xs={3}>
                <InputLabel shrink required id="table">Operator</InputLabel>
                <Select
                    InputLabelProps={{ shrink: true }}
                    style={{ width: "100%" }}
                    onChange={(e) => handleChange('comparator', e.target.value)}
                    value={criterion["comparator"]}
                    required
                >
                    {Object.keys(operators).map(key => (<MenuItem key={key} value={key}>{operators[key]}</MenuItem>))}
                </Select>
            </Grid>
            <Grid item xs={4}>
                <ContextMenuTrigger id={placeholders.length > 0 ? `contextmenu-${contextMenuId}` : "none"} collect={() => setAppender(() => (x) => handleChange('required', (criterion["required"] == undefined ? x : criterion["required"] + x)))}>
                    <TextField
                        id="required"
                        value={criterion["required"]}
                        required={true}
                        InputLabelProps={{ shrink: true }}
                        label="Required value"

                        autoComplete='off'
                        onChange={(e) => handleChange('required', e.target.value)}
                    />
                </ContextMenuTrigger>
                <TableColumnContextMenu id={`contextmenu-${contextMenuId}`} onClick={(f) => appender(`{${f}}`)} placeholders={placeholders} />

            </Grid>
            <Grid item >
                <Button
                    aria-controls="add-action"
                    aria-haspopup="true"
                    color="secondary"
                    onClick={(e) => onDelete()}
                >
                    <RemoveIcon />
                </Button>
            </Grid>
        </Grid>
    )
}

export default RowCriterion