import HttpHelper from "../HttpHelper";

class ManipulationService {
    constructor(table) {
        this.prefix = `/api/manipulate/${localStorage.getItem('database')}/${table}`
        this.httpHelper = new HttpHelper();
    }

    addRow(rows, index) {
        var copyOfRows = [...rows]
        var newRow = {}
        Object.keys(rows[0]).forEach(col => { newRow[col] = "" })
        copyOfRows.splice(index, 0, newRow)
        return copyOfRows
    }


    update(rows, newRow, index, onSuccess, onFailure) {
        this.httpHelper.post(`${this.prefix}/update`, { oldRow: rows[index], newRow: newRow })
            .then(() => onSuccess(this.updateLocal(rows, newRow, index)))
            .catch((data) => onFailure(data.response.data));
    }

    updateLocal(rows, newRow, index) {
        var copyOfRows = [...rows]
        copyOfRows[index] = newRow
        return copyOfRows
    }

    delete(rows, index, onSuccess, onFailure) {
        this.httpHelper.post(`${this.prefix}/delete`, { row: rows[index] })
            .then(() => {
                var copyOfRows = [...rows]
                copyOfRows.splice(index, 1)
                onSuccess(copyOfRows)
            })
            .catch(res => onFailure(res.response.data));
    }

    save(rows, index, onSuccess, onFailure) {
        this.httpHelper.post(`${this.prefix}/add`, { row: rows[index] })
            .then(res => {
                var copyOfRows = [...rows]
                copyOfRows[index] = res.data.row
                onSuccess(copyOfRows)
            })
            .catch(res => onFailure(res.response.data));
    }
}
export default ManipulationService