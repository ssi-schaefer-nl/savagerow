import HttpHelper from "../HttpHelper";

class ManipulationService {
    constructor(table) {
        this.prefix = `/api/v1/${localStorage.getItem('database').toLowerCase()}/database/${table}`
        this.httpHelper = new HttpHelper();
    }

    addRow(rows, index, columns) {
        var copyOfRows = [...rows]
        var newRow = {}
        columns.map(c => newRow[c.name] = "" )
        copyOfRows.splice(index, 0, newRow)
        return copyOfRows
    }


    update(rows, newRow, index, onSuccess, onFailure) {
        this.httpHelper.put(`${this.prefix}/${newRow.rowid}`, { row: newRow })
            .then(() => onSuccess(this.updateLocal(rows, newRow, index)))
            .catch((data) => onFailure(data.response.data));
    }

    updateLocal(rows, newRow, index) {
        var copyOfRows = [...rows]
        copyOfRows[index] = newRow
        return copyOfRows
    }

    delete(rows, index, onSuccess, onFailure) {
        this.httpHelper.delete(`${this.prefix}/${rows[index].rowid}`, { row: rows[index] })
            .then(() => onSuccess(this.deleteLocal(rows, index)))
            .catch(res => onFailure(res.response.data));
    }

    deleteLocal(rows, index) {
        var copyOfRows = [...rows]
        copyOfRows.splice(index, 1)
        return copyOfRows
    }

    save(rows, index, onSuccess, onFailure) {
        this.httpHelper.post(`${this.prefix}`, { row: rows[index] })
            .then(res => {
                var copyOfRows = [...rows]
                copyOfRows[index] = res.data
                onSuccess(copyOfRows)
            })
            .catch(res => onFailure(res.response.data));
    }
}
export default ManipulationService