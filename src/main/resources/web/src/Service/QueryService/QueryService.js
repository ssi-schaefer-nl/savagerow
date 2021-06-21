import HttpHelper from "../HttpHelper";

class QueryService {
    constructor(table) {
        this.table = table
        let database = localStorage.getItem('database');
        if(database == null) database = "unknown"
        this.prefix = `/api/v1/${database.toLowerCase()}/database`
        this.httpHelper = new HttpHelper();
    }

    getRowSet(onSuccess, onFailure) {
        this.httpHelper.get(`${this.prefix}/${this.table}`)
            .then(res => { onSuccess(res) })
            .catch(res => { onFailure(res) });
    }

    getSchema(onSuccess, onFailure) {
        const url = `${this.prefix}/${this.table}/schema`
        this.httpHelper.get(url)
            .then(res => { onSuccess(res) })
            .catch(res => { onFailure(res) })
    }

    getTables(onSuccess, onFailure) {
        this.httpHelper.get(this.prefix)
        .then(res => { onSuccess(res) })
        .catch(res => { onFailure(res) });
    }
}

export default QueryService;