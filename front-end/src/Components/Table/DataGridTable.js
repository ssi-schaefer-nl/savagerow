import DataGrid, { TextEditor, Row as GridRow } from "react-data-grid";
import { ContextMenu, MenuItem, SubMenu, ContextMenuTrigger } from 'react-contextmenu';
import Tooltip from '@material-ui/core/Tooltip';
import './react-contextmenu.css'

import './highlight.css'


const DataGridTable = (props) => {
    if (props.columns.length == 0) {
        return null
    }

    const columns = props.columns.map(col => ({
        key: col.column,
        name: col.column,
        resizable: true,
        nullable: col.nullable,
        editor: col.editable ? TextEditor : undefined
    }));

    var rows = [...props.rows]
    const onRowChange = props.onRowChange
    const highlightedRows = props.highlightRows ? props.highlightRows : []

    const onRowDelete = (e, { rowIdx }) => {
        props.onDelete(rowIdx)
    }

    const onRowInsertAbove = (e, { rowIdx }) => {
        props.onInsertAbove(rowIdx)
    }

    const onRowInsertBelow = (e, { rowIdx }) => {
        props.onInsertBelow(rowIdx)
    }

    const onRowSave = (e, { rowIdx }) => {
        props.onSave(rowIdx)
    }


    const onRefresh = (e, { rowIdx }) => {
        props.onRefresh(rowIdx)
    }

    const RowRenderer = (props) => {
        var classname = "row"
        var rowTooltip = "Row " + props.rowIdx
        
        var highlightedRow = highlightedRows.find(hr => hr.id == props.rowIdx)
        if(highlightedRow != undefined) {
            rowTooltip = rowTooltip.concat(" - " + highlightedRow.message)
            classname = highlightedRow.type === "warning" ? "row-warning" : "row-error"
        }

        return (
            <ContextMenuTrigger id="grid-context-menu" collect={() => ({ rowIdx: props.rowIdx })}>
                <Tooltip
                    title={rowTooltip}
                    enterDelay={1000}
                    enterNextDelay={1000}
                    interactive
                    placement="bottom-start"
                    arrow
                >
                    <GridRow {...props} className={classname} />
                </Tooltip>
            </ContextMenuTrigger>
        );
    }

    console.log(highlightedRows)
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
                style={{ 'height': "65vh", overflowX: 'hidden' }}
                className="fill-grid"
                // rowClass={row => highlightedRows.includes(rows.indexOf(row)) ? "row-highlighted" : "row"}
                minHeight="1000"
            />

            <ContextMenu id="grid-context-menu" style={{ background: "#fafafa", borderRadius: "0px 30px 0px 0px" }}>
                <MenuItem onClick={onRowSave}>Save Row</MenuItem>
                <MenuItem onClick={onRowDelete}>Delete Row</MenuItem>
                <SubMenu title="Insert Row" onClick={onRowInsertAbove}>
                    <MenuItem onClick={onRowInsertAbove}>Above</MenuItem>
                    <MenuItem onClick={onRowInsertBelow}>Below</MenuItem>
                </SubMenu>
                <MenuItem divider />
                <MenuItem onClick={onRefresh}>Refresh table</MenuItem>
                <MenuItem divider />
                <MenuItem ><b>Cancel</b></MenuItem>
            </ContextMenu>
        </>
    )
}

export default DataGridTable