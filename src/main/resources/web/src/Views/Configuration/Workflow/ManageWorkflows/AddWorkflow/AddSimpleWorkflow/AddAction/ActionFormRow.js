import { Divider } from "@material-ui/core"
import { useState, useEffect } from "react"
import { ContextMenu, ContextMenuTrigger, MenuItem } from "react-contextmenu"

import { TextField, Typography } from '@material-ui/core';
import { Table, TableBody, TableCell, TableContainer, TableHead, TableRow, Paper } from '@material-ui/core';

import { grey } from '@material-ui/core/colors';
import QueryService from '../../../../../../../Service/QueryService/QueryService';

const ActionFormRow = props => {
    const { onChange, placeholders, table, value } = props
    const [appender, setAppender] = useState(() => () => undefined)
    const [initialTable, setInitialTable] = useState(table)
    const [columns, setColumns] = useState(null)


    useEffect(() => {
        if (table != null && table.length > 0) {
            new QueryService(table).getSchema(data => {
                const tempColumns = data.data.columns
                const newRow = {}
                tempColumns.map(c => newRow[c.name] = '')
                if(value.length == 0 || table != initialTable) onChange(newRow)
                setColumns(tempColumns)
            }, () => setColumns([]))
        }

    }, [table])


    if (columns != null && columns.length > 0) {
        console.log(value)
        return (
            <>
                <Typography style={{ margin: "1em 0em" }}>Define the fields for the row</Typography>
                <TableContainer component={Paper} style={{ padding: "1em", width: "90%" }}>

                    <Table  >
                        <TableHead >
                            <TableRow>
                                {columns.map((col) =>
                                    <TableCell size="small" style={{ border: "1px solid", borderColor: grey[200] }}>{col.name + (!col.pk && !col.nullable ? "*" : "")}</TableCell>
                                )}
                            </TableRow>
                        </TableHead>
                        <TableBody>
                            <TableRow key={1}>
                                {value != null && columns.map(c => (
                                    <TableCell size="small" style={{ padding: "0em", margin: "0em", border: "1px solid", borderColor: grey[200] }}>
                                        <ContextMenuTrigger id="x-menu" collect={() => setAppender(() => (x) => onChange({...value, [c.name]: value != undefined ? value[c.name] + x : value[c.name]}))}>
                                            <TextField
                                                id={c.name}
                                                value={value[c.name]}
                                                required={!c.pk && !c.nullable}
                                                InputLabelProps={{ shrink: true }}
                                                InputProps={{ disableUnderline: true, autoComplete: 'new-password' }}
                                                autoComplete='off'
                                                onChange={(e) => onChange({ ...value, [c.name]: e.target.value })}
                                            />
                                        </ContextMenuTrigger>
                                    </TableCell>
                                ))}
                            </TableRow>

                        </TableBody>
                    </Table>
                </TableContainer>


                <ContextMenu id="x-menu">
                    <MenuItem disabled><b>Insert field placeholder</b></MenuItem>
                    <MenuItem divider />
                    {placeholders.map(f => (<MenuItem onClick={(e) => appender(`{${f}} `)}>{f}</MenuItem>))}
                </ContextMenu>
            </>
        )
    }
    else return null;
}

    export default ActionFormRow;