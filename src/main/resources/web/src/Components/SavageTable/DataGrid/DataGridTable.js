import DataGrid, { TextEditor, Row as GridRow } from "react-data-grid";
import React, { useEffect, useState } from 'react';
import { withStyles, } from '@material-ui/core/styles';
import { ContextMenu, MenuItem, ContextMenuTrigger } from 'react-contextmenu';
import Tooltip from '@material-ui/core/Tooltip';
import './react-contextmenu.css'
import './highlight.css'
import AddIcon from '@material-ui/icons/Add';
import TextField from '@material-ui/core/TextField';
import InputBase from '@material-ui/core/InputBase';
import Autocomplete, { createFilterOptions } from '@material-ui/lab/Autocomplete';

import Button from '@material-ui/core/Button';
import Dialog from '@material-ui/core/Dialog';
import DialogActions from '@material-ui/core/DialogActions';
import DialogContent from '@material-ui/core/DialogContent';
import DialogTitle from '@material-ui/core/DialogTitle';
import Checkbox from '@material-ui/core/Checkbox';
import { CircularProgress, Grid, Select, Typography } from '@material-ui/core';
import FormControlLabel from '@material-ui/core/FormControlLabel';
import FilterListIcon from '@material-ui/icons/FilterList';
import DeleteIcon from '@material-ui/icons/Delete';
import RefreshIcon from '@material-ui/icons/Refresh';
import PublishIcon from '@material-ui/icons/Publish';
import { Delete, Filter } from "@material-ui/icons";
import { grey } from "@material-ui/core/colors";
import QueryService from "../../../Service/QueryService/QueryService";


const LightTooltip = withStyles((theme) => ({
    tooltip: {
        backgroundColor: theme.palette.common.white,
        color: 'rgba(0, 0, 0, 0.87)',
        boxShadow: theme.shadows[1],
        fontSize: 12,
    },
}))(Tooltip);

const DataGridTable = (props) => {
    const [selectedColumn, setSelectedColumn] = useState(null)
    const [selectedRow, setSelectedRow] = useState(null)
    const [openColumnFilter, setOpenColumnFilter] = useState(false)
    const [columnFilters, setColumnFilters] = useState([])

    var rows = [...props.rows]
    const onRowChange = props.onRowChange
    const highlightedRows = props.highlightRows ? props.highlightRows : []
    var changingColumnName = null

    const HeaderRenderer = (item) => {
        return (
            <ContextMenuTrigger
                id="header-context-menu"
                collect={() => {
                    setSelectedColumn(item.column.name)
                    return ({ column: item.column.key })
                }}
                holdToDisplay="-1"
            >
                <InputBase
                    inputProps={{ 'aria-label': 'naked' }}
                    autoComplete='off'
                    id="name"
                    onChange={(e) => { changingColumnName = e.target.value }}
                    defaultValue={item.column.name}
                    value={changingColumnName}
                    onBlur={() => {
                        if (changingColumnName != null) onColumnRename(item.column.name, changingColumnName)
                    }}
                    required
                    style={{ fontSize: "1em", fontWeight: "bold" }}
                />
            </ContextMenuTrigger>
        )

    }

    const RowRenderer = (props) => {
        var classname = "row"
        var rowTooltip = ""

        var highlightedRow = highlightedRows.find(hr => hr.id == props.rowIdx)
        if (highlightedRow != undefined) {
            rowTooltip = highlightedRow.message
            classname = highlightedRow.type === "warning" ? "row-warning" : "row-error"
        }

        return (
            <ContextMenuTrigger id="grid-context-menu" collect={() => {
                setSelectedRow(props.rowIdx)
                return ({ rowIdx: props.rowIdx })
            }}
                holdToDisplay="-1"
            >
                {highlightedRow == undefined ?
                    <GridRow {...props} className={classname} />
                    :
                    <LightTooltip
                        title={rowTooltip}
                        enterDelay={1000}
                        enterNextDelay={1000}
                        interactive
                        placement="bottom-start"
                        arrow
                    >
                        <GridRow {...props} className={classname} />
                    </LightTooltip>
                }
            </ContextMenuTrigger>
        );
    }



    const columns = props.columns.map(col => ({
        key: col.name,
        name: col.name,
        headerRenderer: HeaderRenderer,
        resizable: true,
        nullable: col.nullable,
        fk: col.fk,
        editorOptions: {
            editOnClick: col.fk == null
        },
        editor: col.pk
            ?
            undefined
            :
            col.fk == null ? col.datatype == "Text" ? TextEditor : NumberEditor : DropDownEditor
    }));


    const onRowDelete = (e, { rowIdx }) => props.onDelete(rowIdx)
    const onRowInsert = (e, { rowIdx }) => props.onInsert(rowIdx)
    const onRowSave = (e, { rowIdx }) => props.onSave(rowIdx)
    const onRefresh = (e, { rowIdx }) => props.onRefresh(rowIdx)
    const onColumnDelete = (e, { column }) => props.onColumnDelete(column)
    const onColumnRename = (oldName, newName) => props.onColumnRename(oldName, newName)
    const onColumnInsert = (e, { column }) => props.onColumnInsert(column)
    const onColumnFilter = (e) => setOpenColumnFilter(true)



    const rowContextMenu = [
        { text: "Save Row", icon: PublishIcon, onClick: onRowSave },
        { text: "Delete Row", icon: DeleteIcon, onClick: onRowDelete },
        { text: "Insert Row", icon: AddIcon, onClick: onRowInsert, dividerAfter: true },
        { text: "Refresh Table", icon: RefreshIcon, onClick: onRefresh },
    ]

    const columnContextMenu = [
        { text: "Delete Column", icon: Delete, onClick: onColumnDelete, dividerAfter: true },
        { text: "Add Column", icon: AddIcon, onClick: onColumnInsert },
        { text: "Show/Hide Columns", icon: FilterListIcon, onClick: onColumnFilter },
    ]

    const defaultRowHeight = 35
    const height = ((rows.length + 1) * defaultRowHeight) + 2;

    return (
        <>
            <DataGrid
                columns={columnFilters.length > 0 ? columns.filter(f => columnFilters.includes(f.name)) : columns}
                rows={rows}
                onRowsChange={(updatedRows, index) => {
                    var idx = index.indexes[0]
                    rows[idx] = updatedRows[idx]
                    onRowChange(updatedRows[idx], idx)
                }}
                rowGetter={i => rows[i]}
                enableCellSelect={true}
                rowRenderer={RowRenderer}
                headerRenderer={HeaderRenderer}
                style={{ height: height, maxHeight: "100%", overflowX: 'hidden' }}
                rowHeight={defaultRowHeight}
            />
            <Button
                aria-controls="add-row"
                aria-haspopup="true"
                onClick={() => props.onInsert(-1)}
                variant="contained"
                style={{ marginTop: "0em", backgroundColor: grey[100], width: "100%" }}
            >
                <AddIcon />
            </Button>
            <TableContextMenu
                id="grid-context-menu"
                title={"Row " + selectedRow}
                items={rowContextMenu}
            />

            <TableContextMenu
                id="header-context-menu"
                title={selectedColumn}
                items={columnContextMenu}
            />

            <HideColumnsPopup
                open={openColumnFilter}
                values={columns.map(col => col.name)}
                selection={columnFilters}
                onApply={(i) => { setColumnFilters(i); setOpenColumnFilter(false) }}
                onCancel={() => setOpenColumnFilter(false)}
            />

        </>
    )
}

function TableContextMenu(props) {
    const { title, id, items } = props

    return (
        <ContextMenu
            id={id}
            style={{ background: "#fafafa", borderRadius: "0px 30px 0px 0px", boxShadow: "2px 2px  5px grey" }}
        >
            <Typography style={{ margin: "0.5em 1em" }}><strong>{title}</strong></Typography>
            <MenuItem divider />
            {items.map(item => (<ContextMenuItem onClick={item.onClick} text={item.text} icon={item.icon} dividerAfter={item.dividerAfter} />))}
        </ContextMenu>
    )
}

function ContextMenuItem(props) {
    const { onClick, text, dividerAfter } = props
    const MenuIcon = props.icon

    return (
        <>
            <MenuItem onClick={onClick} >
                <Grid container direction="row" alignItems="center" spacing={2}>
                    <Grid item>
                        <MenuIcon fontSize="small" style={{ position: 'relative', top: '2px' }} />
                    </Grid>
                    <Grid item>{text}</Grid>
                </Grid>
            </MenuItem>
            {dividerAfter && <MenuItem divider />}
        </>
    )
}


function HideColumnsPopup(props) {
    const { open, values, onApply, onCancel } = props
    const initialSelection = props.selection
    const [selection, setSelection] = useState(props.selection)

    const onEnter = (e) => {
        e.preventDefault()
        onApply(selection)
    }

    const cancel = (e) => {
        setSelection(initialSelection)
        onCancel()
    }



    return (
        <div>
            <Dialog open={open} aria-labelledby="form-dialog-title" onClose={cancel}>
                <form onSubmit={onEnter}>

                    <DialogTitle id="form-dialog-title">Show/Hide Columns</DialogTitle>
                    <DialogContent>
                        <Grid container direction="column">
                            {values.map(v => (
                                <Grid item>
                                    <FormControlLabel
                                        label={v}
                                        control={
                                            <Checkbox
                                                checked={selection.includes(v)}
                                                onChange={(e) => {
                                                    if (e.target.checked && !selection.includes(v)) {
                                                        setSelection(s => [...s, v])
                                                    } else if (!e.target.checked && selection.includes(v)) {
                                                        const i = selection.indexOf(v)
                                                        setSelection(s => [...s.slice(0, i), ...s.slice(i + 1)])
                                                    }
                                                }}
                                                name={v}
                                                color="primary"
                                            />
                                        }

                                    />
                                </Grid>
                            ))}
                        </Grid>
                    </DialogContent>
                    <DialogActions>
                        <Button color="primary" type="submit">Apply</Button>
                        <Button onClick={() => setSelection([])} color="primary">Clear</Button>
                    </DialogActions>
                </form>

            </Dialog>
        </div>
    );
}

const NumberEditor = ({ row, onRowChange, column }) => {
    return (
        <TextField
            value={row[column.key]}
            type="number"
            autoFocus
            style={{
                appearance: 'none',
                boxSizing: 'border-box',
                width: '100%',
                height: '100%',
                padding: '1px 6px 0 6px',
                border: '2px solid #ccc',
                verticalAlign: 'top',
            }}
            onChange={event => {
                let val = event.target.value
                if (val === "") val = 0
                if (!isNaN(val)) onRowChange({ ...row, [column.key]: val }, false)
                console.log(val)
            }}
            InputProps={{
                disableUnderline: true,
                style: {
                    fontSize: 14,
                },
            }}

        />
    )
}

const DropDownEditor = ({ row, onRowChange, column }) => {
    const fkTable = column.fk.split(".")[0]
    const fkColumn = column.fk.split(".")[1]
    const [rows, setRows] = useState([])
    const currentRow = rows.find(r => r[fkColumn] = row[fkColumn]);

    console.log(currentRow)
    useEffect(() => {
        const queryService = new QueryService(fkTable);
        queryService.getRowSet(data => setRows(data.data), () => undefined)

    }, [])

    const filterOptions = createFilterOptions({
        matchFrom: 'any',
        limit: 500,
    });
    return (
        <LightTooltip  title={currentRow != null ? Object.values(currentRow).join(', ') : ""} placement="right-start" >

            <Autocomplete
                options={rows}
                getOptionLabel={(r) => Object.values(r).join(", ")}
                style={{
                    appearance: 'none',
                    boxSizing: 'border-box',
                    width: '100%',
                    height: '100%',
                    padding: '1px 6px 0 6px',
                    border: '2px solid #ccc',
                    verticalAlign: 'top',
                }}
                filterOptions={filterOptions}
                disableListWrap
                groupBy={(option) => option[0]}
                onChange={(event, value) => {
                    if (value != null)
                        onRowChange({ ...row, [column.key]: value[fkColumn] }, false)
                }}
                renderOption={(r) => (
                    <React.Fragment >
                        <Typography style={{ width: "100%" }}>
                            <span><strong>{r[fkColumn]} - </strong></span>
                            {Object.keys(r).filter(k => k != fkColumn && k != "rowid").map(k => r[k]).join(", ")}
                        </Typography>
                    </React.Fragment>
                )}
                renderInput={(params) => <TextField {...params} />}
            />
        </LightTooltip>
    );
}
export default DataGridTable