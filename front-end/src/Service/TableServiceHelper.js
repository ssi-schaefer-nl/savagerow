import HttpHelper from './HttpHelper';

class TableService {
    constructor() {
        this.httpHelper = new HttpHelper();
    }

    getRowsOfTable(table, onSuccess, onFailure) {
        var db = localStorage.getItem('database')
        this.httpHelper.get('/api/'+db+'/table/' + table + '/all')
            .then(res => { onSuccess(res) })
            .catch(res => { onFailure(res) });
    }


    getSchemaOfTable(table, onSuccess, onFailure) {
        var db = localStorage.getItem('database')
        this.httpHelper.get('/api/'+db+'/table/' + table + '/schema')
            .then(res => { onSuccess(res) })
            .catch(res => { onFailure(res) });
    }


    addRowToTable(table, data, onSuccess, onFailure) {
        var db = localStorage.getItem('database')
        this.httpHelper.post('/api/'+db+'/table/' + table + '/add', data)
            .then(res => { onSuccess(res) })
            .catch(res => { onFailure(res) });

    }

    updateRowOfTable(table, data, onSuccess, onFailure) {
        var db = localStorage.getItem('database')
        this.httpHelper.post('/api/'+db+'/table/' + table + '/update', data)
            .then(res => { onSuccess(res) })
            .catch(res => { onFailure(res) });

    }

    deleteRowOfTable(table, data, onSuccess, onFailure) {
        var db = localStorage.getItem('database')
        this.httpHelper.post('/api/'+db+'/table/' + table + '/delete', data)
            .then(res => { onSuccess(res) })
            .catch(res => { onFailure(res) });

    }

    addColumnToTable(table, data, onSuccess, onFailure) {
        var db = localStorage.getItem('database')
        this.httpHelper.post('/api/'+db+'/'+table+'/column/add', data)
            .then(res => { onSuccess(res) })
            .catch(res => { onFailure(res) });

    }

    dropColumnFromTable(table, column, onSuccess, onFailure) {
        var db = localStorage.getItem('database')
        this.httpHelper.post('/api/'+db+'/'+table+'/'+column+'/drop')
            .then(res => { onSuccess(res) })
            .catch(res => { onFailure(res) });

    }
}

export default TableService;