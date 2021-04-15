import HttpHelper from './HttpHelper';

class TableDataService {
    constructor() {
        this.httpHelper = new HttpHelper();
    }


    getTableData(table, onSuccess, onFailure) {
        this.httpHelper.get('/api/table/'+table+'/all', onSuccess, onFailure)
    }

    addTableData(table, data, onSuccess, onFailure) {
        this.httpHelper.post('/api/table/'+table+'/add', data, onSuccess, onFailure)
    }

    updateTableData(table, data, onSuccess, onFailure) {
        this.httpHelper.post('/api/table/'+table+'/update', data, onSuccess, onFailure)
    }

    deleteTableData(table, data, onSuccess, onFailure) {
        this.httpHelper.post('/api/table/'+table+'/delete', data, onSuccess, onFailure)
    }

}

export default TableDataService;