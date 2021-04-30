import TableServiceHelper from '../../Service/TableServiceHelper';


class TableService {
    constructor(table) {
        this.table = table
        this.tableServiceHelper = new TableServiceHelper()
    }

    getRowSet(onSuccess, onFailure) {
        this.tableServiceHelper.getRowsOfTable(this.table, onSuccess, onFailure)
    }

    getSchema(onSuccess, onFailure) {
        this.tableServiceHelper.getSchemaOfTable(this.table, onSuccess, onFailure)
    }

    addRow(rows, index) {
        var copyOfRows = [...rows]
        var newRow = {}
        Object.keys(rows[0]).forEach(col => { newRow[col] = "" })
        copyOfRows.splice(index, 0, newRow)
        return copyOfRows
    }


    update(rows, newRow, index, onSuccess, onFailure) {
        this.tableServiceHelper.updateRowOfTable(
            this.table,
            { oldRow: rows[index], newRow: newRow },
            () => onSuccess(this.updateLocal(rows, newRow, index)),
            (data) => onFailure(data.response.data)
        );
    }

    updateLocal(rows, newRow, index) {
        var copyOfRows = [...rows]
        copyOfRows[index] = newRow
        return copyOfRows
    }

    delete(rows, index, onSuccess, onFailure) {
        this.tableServiceHelper.deleteRowOfTable(
            this.table,
            { row: rows[index] },
            () => {
                var copyOfRows = [...rows]
                copyOfRows.splice(index, 1)
                onSuccess(copyOfRows)
            },
            (data) => onFailure(data.response.data)
        )
    }

    save(rows, index, onSuccess, onFailure) {
        this.tableServiceHelper.addRowToTable(
            this.table,
            { row: rows[index] },
            (data) => {
                var copyOfRows = [...rows]
                copyOfRows[index] = data.data.row
                onSuccess(copyOfRows)
            },
            (data) => onFailure(data.response.data)
        )
    }

}
export default TableService