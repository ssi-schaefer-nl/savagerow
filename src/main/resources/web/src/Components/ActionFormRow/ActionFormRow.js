import { Divider } from "@material-ui/core"
import { useState, useEffect } from "react"
import { ContextMenu, ContextMenuTrigger, MenuItem } from "react-contextmenu"

import { TextField, Typography } from '@material-ui/core';
import { Table, TableBody, TableCell, TableContainer, TableHead, TableRow, Paper } from '@material-ui/core';

import { grey } from '@material-ui/core/colors';
import QueryService from '../../Service/QueryService/QueryService';
import TableColumnContextMenu from "../TableColumnContextMenu/TableColumnContextMenu";

const ActionFormRow = props => {
    const { onChange, placeholders, table, value } = props
    const [appender, setAppender] = useState(() => () => undefined)
    const [initialTable, setInitialTable] = useState(table)
    const [columns, setColumns] = useState([])
    const [contextMenuId, setContextMenuId] = useState(Math.floor(Math.random() * 100))


    useEffect(() => {
        if (table != null && table.length > 0) {
            new QueryService(table).getSchema(data => {
                const tempColumns = data.data.columns
                const newRow = {}
                tempColumns.map(c => newRow[c.name] = '')
                setColumns(tempColumns)
                if (value.length == 0 || table != initialTable) onChange(newRow)
            }, () => setColumns([]))
        }

    }, [table])


    if (columns.length > 0) {
        return (
            <>
                <TableContainer style={{ padding: "1em 0", width: "90%" }}>

                    <Table  >
                        <TableHead >
                            <TableRow>
                                {columns.map((col) =>
                                    <TableCell size="small" style={{ border: "1px solid", borderColor: grey[200] }}>{col.name + (props.requireValues && (!col.pk && !col.nullable) ? "*" : "")}</TableCell>
                                )}
                            </TableRow>
                        </TableHead>
                        <TableBody>
                            <TableRow key={1}>
                                {value != null && columns.map(c => (
                                    <TableCell size="small" style={{ padding: "0em", margin: "0em", border: "1px solid", borderColor: grey[200] }}>
                                        <ContextMenuTrigger id={`contextmenu-${contextMenuId}`} collect={() => setAppender(() => (x) => onChange({ ...value, [c.name]: value != undefined ? value[c.name] + x : x }))}>
                                            <TextField
                                                id={c.name}
                                                value={value[c.name]}
                                                required={props.requireValues && (!c.pk && !c.nullable)}
                                                InputLabelProps={{ shrink: true }}
                                                InputProps={{ disableUnderline: true }}
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
                <TableColumnContextMenu id={`contextmenu-${contextMenuId}`} onClick={(f) => appender(`{${f}}`) } placeholders={placeholders} />




            </>
        )
    }
    else return null;
}



export default ActionFormRow;