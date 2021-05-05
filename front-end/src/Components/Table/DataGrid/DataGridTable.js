import DataGrid, { TextEditor, Row as GridRow } from "react-data-grid";
import React, { useState } from 'react';
import { withStyles, makeStyles } from '@material-ui/core/styles';
import { ContextMenu, MenuItem, SubMenu, ContextMenuTrigger } from 'react-contextmenu';
import Tooltip from '@material-ui/core/Tooltip';
import './react-contextmenu.css'
import './highlight.css'
import { TextField } from "@material-ui/core";


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
    if (props.columns.length == 0) {
        return null
    }

    var rows = [...props.rows]
    const onRowChange = props.onRowChange
    const highlightedRows = props.highlightRows ? props.highlightRows : []


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
                {item.column.name}
            </ContextMenuTrigger>
        )

    }


    const columns = props.columns.map(col => ({
        key: col.column,
        name: col.column,
        headerRenderer: HeaderRenderer,
        resizable: true,
        nullable: col.nullable,
        editor: col.editable ? TextEditor : undefined
    }));


    const onRowDelete = (e, { rowIdx }) => props.onDelete(rowIdx)
    const onRowInsert = (e, { rowIdx }) => props.onInsert(rowIdx)
    const onRowSave = (e, { rowIdx }) => props.onSave(rowIdx)
    const onRefresh = (e, { rowIdx }) => props.onRefresh(rowIdx)
    const onColumnDelete = (e, { column }) => props.onColumnDelete(column)
    const onColumnRename = (e, { column }) => { }
    const onColumnInsert = (e, { column }) => props.onColumnInsert(column)


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


    return (
        <>
            <DataGrid
                columns={columns}
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
                style={{ marginTop: '1em', 'height': "65vh", overflowX: 'hidden' }}
                className="fill-grid"
                minHeight="1000"
            />

            <ContextMenu
                id="grid-context-menu"
                style={{ background: "#fafafa", borderRadius: "0px 30px 0px 0px", boxShadow: "2px 2px  5px grey" }}
            >
                <MenuItem disabled>{"Row " + selectedRow}</MenuItem>
                <MenuItem divider />
                <MenuItem onClick={onRowSave}>Save Row</MenuItem>
                <MenuItem onClick={onRowDelete}>Delete Row</MenuItem>
                <MenuItem onClick={onRowInsert}>Insert Row</MenuItem>
                <MenuItem divider />
                <MenuItem onClick={onRefresh}>Refresh table</MenuItem>
                <MenuItem divider />
                <MenuItem ><b>Cancel</b></MenuItem>
            </ContextMenu>

            <ContextMenu
                id="header-context-menu"
                style={{ background: "#fafafa", borderRadius: "0px 30px 0px 0px", boxShadow: "2px 2px  5px grey" }}
            >
                <MenuItem disabled>{selectedColumn}</MenuItem>
                <MenuItem divider />
                <MenuItem onClick={onColumnDelete}>Delete Column</MenuItem>
                <MenuItem onClick={onColumnRename}>Rename Column</MenuItem>
                <MenuItem divider />
                <MenuItem onClick={onColumnInsert}>Add Column</MenuItem>

            </ContextMenu>
        </>
    )
}

export default DataGridTable