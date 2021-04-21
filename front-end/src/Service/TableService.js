import HttpHelper from './HttpHelper';

class TableService {
    constructor() {
        this.httpHelper = new HttpHelper();
    }

    getRowsOfTable(table, onSuccess, onFailure) {
        this.httpHelper.get('/api/table/' + table + '/all')
            .then(res => { onSuccess(res) })
            .catch(res => { onFailure(res) });
    }


    getSchemaOfTable(table, onSuccess, onFailure) {
        this.httpHelper.get('/api/table/' + table + '/schema')
            .then(res => { onSuccess(res) })
            .catch(res => { onFailure(res) });
    }


    addRowToTable(table, data, onSuccess, onFailure) {
        this.httpHelper.post('/api/table/' + table + '/add', data)
            .then(res => { onSuccess(res) })
            .catch(res => { onFailure(res) });

    }

    updateRowOfTable(table, data, onSuccess, onFailure) {
        this.httpHelper.post('/api/table/' + table + '/update', data)
            .then(res => { onSuccess(res) })
            .catch(res => { onFailure(res) });

    }

    deleteRowOfTable(table, data, onSuccess, onFailure) {
        this.httpHelper.post('/api/table/' + table + '/delete', data)
            .then(res => { onSuccess(res) })
            .catch(res => { onFailure(res) });

    }

}

export default TableService;