import DataGrid, { TextEditor } from "react-data-grid";


const DataGridTable = (props) => {
    if(props.columns.length == 0) {
        return null
    }
    const columns = props.columns.map(col => ({
        key: col.column,
        name: col.column,
        resizable: true,
        nullable: col.nullable,
        editor: col.editable ? TextEditor : undefined
    }));

    const rows = props.rows
    const onRowChange = props.onRowChange
    const onRowSelect = props.onRowSelect

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
                style={{ 'height': "65vh", overflowX: 'hidden' }}
                className="fill-grid"
                minHeight="1000"
                onSelectedCellChange={(o) => onRowSelect(o.rowIdx)}
            />
        </>
    )
}

export default DataGridTable