import { Button, Grid } from "@material-ui/core"
import { useState, useEffect } from "react"
import { ContextMenu, ContextMenuTrigger, MenuItem } from "react-contextmenu"
import AddIcon from '@material-ui/icons/Add';
import RemoveIcon from '@material-ui/icons/Remove';
import { TextField } from '@material-ui/core';
import { InputLabel, Select } from '@material-ui/core';

import QueryService from '../../Service/QueryService/QueryService';
import TableColumnContextMenu from "../TableColumnContextMenu/TableColumnContextMenu";

const ActionFormRow = props => {
    const { onChange, placeholders, table, value } = props
    const [columns, setColumns] = useState([])
    const [fields, setFields] = useState((value == undefined ? [] : value))

    useEffect(() => {
        if (table != null && table.length > 0) {
            new QueryService(table).getSchema(data => {
                setColumns(data.data.columns.map(c => c.name))
            }, () => setColumns([]))
        }

    }, [table])

    const addNewField = () => setFields([...fields, { column: "", action: "", value: "" }])
    const removeField = (i) => {
        const copyOfFields = [...fields];
        copyOfFields.splice(i, 1)
        onChange(copyOfFields);
        setFields(copyOfFields)
    }

    if (fields.length == 0) addNewField()

    if (columns.length > 0) {
        return (
            <div style={{ maxHeight: "20vh", overflow: "auto" }}>

                {Array.isArray(fields) && fields.map((f, i) =>
                    <Columns
                        placeholders={placeholders}
                        columns={columns}
                        fields={fields[i]}
                        onDelete={() => removeField(i)}
                        onChange={(cr) => {
                            const copyOfFields = [...fields];
                            copyOfFields[i] = cr;
                            onChange(copyOfFields);
                            setFields(copyOfFields)
                        }}
                    />
                )}
                <Button
                    aria-controls="add-action"
                    aria-haspopup="true"
                    color="primary"
                    style={{ justifyContent: "flex-start" }}
                    onClick={(e) => addNewField()}
                >
                    <AddIcon />
                </Button>
            </div>
        )
    }

    else return null;
}

const actions = ["set", "subtract", "add"]


const Columns = props => {
    const { onChange, placeholders, fields, onDelete, columns } = props

    const [appender, setAppender] = useState(() => () => undefined)
    const [contextMenuId, setContextMenuId] = useState(Math.floor(Math.random() * 100))

    const handleChange = (field, value) => {
        const newField = { ...fields, [field]: value }
        onChange(newField)
    }


    return (
        <Grid container direction="row" spacing={2} justify="space-around" style={{ width: "100%", margin: "0.5em 0", border: "1px solid grey", borderRadius: "5px" }}>
            <Grid item xs={3}>
                <InputLabel shrink required id="table">Column</InputLabel>
                <Select
                    InputLabelProps={{ shrink: true }}
                    style={{ width: "100%" }}
                    onChange={(e) => handleChange('column', e.target.value)}
                    value={fields["column"]}
                    required
                >
                    {Object.keys(columns).map(key => (<MenuItem key={key} value={columns[key]}>{columns[key]}</MenuItem>))}
                </Select>
            </Grid>
            <Grid item xs={3}>
                <InputLabel shrink required id="table">Action</InputLabel>
                <Select
                    InputLabelProps={{ shrink: true }}
                    style={{ width: "100%" }}
                    onChange={(e) => handleChange('action', e.target.value)}
                    value={fields["action"]}
                    required
                >
                    {actions.map(key => (<MenuItem key={key} value={key}>{key}</MenuItem>))}
                </Select>
            </Grid>
            <Grid item xs={4}>
                <ContextMenuTrigger id={placeholders.length > 0 ? `contextmenu-${contextMenuId}` : "none"} collect={() => setAppender(() => (x) => handleChange('value', (fields["value"] == undefined ? x : fields["value"] + x)))}>
                    <TextField
                        id="value"
                        value={fields["value"]}
                        required={true}
                        InputLabelProps={{ shrink: true }}
                        label="Value"
                        autoComplete='off'
                        onChange={(e) => handleChange('value', e.target.value)}
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

export default ActionFormRow