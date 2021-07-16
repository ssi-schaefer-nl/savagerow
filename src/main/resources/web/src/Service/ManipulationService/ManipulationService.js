import HttpHelper from "../HttpHelper";

class ManipulationService {
    constructor(table) {
        this.database = localStorage.getItem('database');
        if(this.database != null && !this.database.length > 0) this.database = null
        this.prefix = `/api/v1/${this.database}/database/${table}`
        this.httpHelper = new HttpHelper();
    }

    addRow(rows, index, columns) {
        var copyOfRows = [...rows]
        var newRow = {}
        columns.map(c => newRow[c.name] = "")
        copyOfRows.splice(index, 0, newRow)
        return copyOfRows
    }


    update(rows, newRow, index, onSuccess, onFailure) {
        if (this.database != null) {

            this.httpHelper.put(`${this.prefix}/rows/${newRow.rowid}`, { row: newRow })
                .then(() => onSuccess(this.updateLocal(rows, newRow, index)))
                .catch((data) => onFailure(data.response.data));
        }
    }

    updateLocal(rows, newRow, index) {
        var copyOfRows = [...rows]
        copyOfRows[index] = newRow
        return copyOfRows
    }

    delete(rows, index, onSuccess, onFailure) {
        if (this.database != null) {

            this.httpHelper.delete(`${this.prefix}/rows/${rows[index].rowid}`, { row: rows[index] })
                .then(() => onSuccess(this.deleteLocal(rows, index)))
                .catch(res => onFailure(res.response));
        }
    }

    deleteLocal(rows, index) {
        var copyOfRows = [...rows]
        copyOfRows.splice(index, 1)
        return copyOfRows
    }

    save(rows, index, onSuccess, onFailure) {
        if (this.database != null) {

            this.httpHelper.post(`${this.prefix}/rows`, { row: rows[index] })
                .then(res => {
                    var copyOfRows = [...rows]
                    copyOfRows[index] = res.data
                    onSuccess(copyOfRows)
                })
                .catch(res => onFailure(res.response.data));
            if (this.database != null) {

            }
        }
    }
}

export default ManipulationService