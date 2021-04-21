import TableService from '../../Service/TableService';


class TableManager {
    constructor(table) {
        this.table = table
        this.tableService = new TableService()
    }

    getRowSet(onSuccess, onFailure) {
        this.tableService.getRowsOfTable(this.table, onSuccess, onFailure)
    }

    getSchema(onSuccess, onFailure) {
        this.tableService.getSchemaOfTable(this.table, onSuccess, onFailure)
    }

    addRow(rows, index) {
        var copyOfRows = [...rows]
        var newRow = {}
        Object.keys(rows[0]).forEach(col => { newRow[col] = "" })
        copyOfRows.splice(index, 0, newRow)
        return copyOfRows
    }


    update(rows, newRow, index, onSuccess, onFailure) {
        this.tableService.updateRowOfTable(
            this.table,
            { oldRow: rows[index], newRow: newRow },
            () => {
                var copyOfRows = [...rows]
                copyOfRows[index] = newRow
                onSuccess(copyOfRows)
            },
            (data) => onFailure(data.response.data)
        );
    }

    delete(rows, index, onSuccess, onFailure) {
        this.tableService.deleteRowOfTable(
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
        this.tableService.addRowToTable(
            this.table,
            { row: rows[index] },
            (data) => {
                var copyOfRows = Object.assign([], rows, { index: data.data.row })
                onSuccess(copyOfRows)
            },
            (data) => onFailure(data.response.data)
        )
    }

}
export default TableManager