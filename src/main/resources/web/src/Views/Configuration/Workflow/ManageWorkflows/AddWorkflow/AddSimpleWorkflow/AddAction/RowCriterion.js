import { Button, Grid, Paper } from "@material-ui/core"
import { Divider } from "@material-ui/core"
import { useState, useEffect } from "react"
import { ContextMenu, ContextMenuTrigger, MenuItem } from "react-contextmenu"
import AddIcon from '@material-ui/icons/Add';
import RemoveIcon from '@material-ui/icons/Remove';
import { TextField, Typography } from '@material-ui/core';
import { InputLabel, Select } from '@material-ui/core';

import QueryService from '../../../../../../../Service/QueryService/QueryService';

const RowCriterion = props => {
    const { onChange, placeholders, table, value } = props
    const [initialTable, setInitialTable] = useState(table)
    const [columns, setColumns] = useState([])
    const [criteria, setCriteria] = useState((value == undefined ? [] : value))

    console.log(value)
    useEffect(() => {
        if (table != null && table.length > 0) {
            new QueryService(table).getSchema(data => {
                const tempColumns = data.data.columns
                const newRow = {}
                tempColumns.map(c => newRow[c.name] = '')
                if (value.length == 0 || table != initialTable) onChange(newRow)
                setColumns(tempColumns)
            }, () => setColumns([]))
        }

    }, [table])

    const addNewCriterion = () => setCriteria([...criteria, { column: "", operator: "", required: "" }])
    const removeCriterion = (i) => {
        const copyOfCriteria = [...criteria];
        copyOfCriteria.splice(i, 1)
        onChange(copyOfCriteria);
        setCriteria(copyOfCriteria)
    }

    if (criteria.length == 0) addNewCriterion()


    if (columns.length > 0) {
        return (
            <div style={{ maxHeight: "20vh", overflow: "auto"}}>
                
                {criteria.map((criterion, i) =>
                    <Criterion
                        placeholders={placeholders}
                        columns={columns.map(c => c.name)}
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
    "==": "Equals",
    "!=": "Not equals",
    "~=": "Contains",
    "<": "Smaller than",
    ">": "Greater than"
}

const Criterion = props => {
    const { onChange, placeholders, criterion, onDelete, columns } = props

    const [appender, setAppender] = useState(() => () => undefined)
    const [contextMenuId, setContextMenuId] = useState(Math.floor(Math.random() * 100))
    // const [criterion, setCriterion] = useState({ column: "", operator: "", required: "" })

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
                    onChange={(e) => handleChange('operator', e.target.value)}
                    value={criterion["operator"]}
                    required
                >
                    {Object.keys(operators).map(key => (<MenuItem key={key} value={key}>{operators[key]}</MenuItem>))}
                </Select>
            </Grid>
            <Grid item xs={4}>
                <ContextMenuTrigger id={`contextmenu-${contextMenuId}`} collect={() => setAppender(() => (x) => handleChange('required', (criterion["required"] == undefined ? x : criterion["required"] + x)))}>
                    <TextField
                        id="required"
                        value={criterion["required"]}
                        required={true}
                        InputLabelProps={{ shrink: true }}
                        label="Required value"
                        InputProps={{
                            autoComplete: 'new-password', form: {
                                autocomplete: 'off',
                            },
                        }}
                        autoComplete='off'
                        onChange={(e) => handleChange('required', e.target.value)}
                    />
                </ContextMenuTrigger>
                <ContextMenu id={`contextmenu-${contextMenuId}`}>
                    <MenuItem disabled><b>Insert field placeholder</b></MenuItem>
                    <MenuItem divider />
                    {placeholders.map(f => (<MenuItem onClick={(e) => { appender(`{${f}}`); }}>{f}</MenuItem>))}
                </ContextMenu>
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