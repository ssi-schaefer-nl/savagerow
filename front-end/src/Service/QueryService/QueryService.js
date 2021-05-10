import HttpHelper from "../HttpHelper";

class QueryService {
    constructor(table) {
        this.prefix = `/api/query/${localStorage.getItem('database')}/${table}`
        this.httpHelper = new HttpHelper();
    }

    getRowSet(onSuccess, onFailure) {
        this.httpHelper.get(`${this.prefix}/rows/all`)
            .then(res => { onSuccess(res) })
            .catch(res => { onFailure(res) });
    }

    getSchema(onSuccess, onFailure) {
        this.httpHelper.get(`${this.prefix}/schema`)
            .then(res => { onSuccess(res) })
            .catch(res => { onFailure(res) })
    }

    getTables(onSuccess, onFailure) {
        this.httpHelper.get(`/api/query/${localStorage.getItem('database')}/tables`)
        .then(res => { onSuccess(res) })
        .catch(res => { onFailure(res) });
    }
}

export default QueryService;