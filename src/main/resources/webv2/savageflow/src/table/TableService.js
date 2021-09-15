import HttpHelper from "../common/HttpHelper";

class TableService {

    getSchema(table, onSuccess, onFailure) {
        new HttpHelper().get(`/database/${table}/schema`)
            .then(res => { onSuccess(res.data) })
            .catch(res => onFailure(res))
    }

    getTables(onSuccess, onFailure) {
        new HttpHelper().get("/database/tables")
            .then(res => { onSuccess(res.data) })
            .catch(res => { onFailure(res) })
    }

    getRows(table, onSuccess, onFailure) {
        new HttpHelper().get(`/database/${table}/rows`)
            .then(res => { onSuccess(res.data) })
            .catch(res => { onFailure(res) });
    }

    update(table, row, onSuccess, onFailure) {

        new HttpHelper().put(`/database/${table}/rows/${row.rowid}`, { row: row })
            .then((res) => onSuccess(res.data))
            .catch(res => onFailure(res));
    }

    delete(table, ids, onSuccess, onFailure) {

        new HttpHelper().delete(`/database/${table}/rows`, { ids: ids })
            .then((res) => onSuccess(res.data))
            .catch(res => onFailure(res));
    }

    add(table, row, onSuccess, onFailure) {
        new HttpHelper().post(`/database/${table}/rows`, { row: row })
            .then((res) => onSuccess(res.data))
            .catch(res => onFailure(res));
    }

}

export default TableService;