import HttpHelper from "../HttpHelper";

class QueryService {
    constructor(table) {
        this.table = table
        this.database = localStorage.getItem('database');
        if(this.database != null && !this.database.length > 0) this.database = null
        this.prefix = `/api/v1/${this.database}/database`
        this.httpHelper = new HttpHelper();
    }

    getRowSet(onSuccess, onFailure) {
        if (this.database != null) {
            this.httpHelper.get(`${this.prefix}/${this.table}/rows`)
                .then(res => { onSuccess(res) })
                .catch(res => { onFailure(res) });
        }
    }

    getSchema(onSuccess, onFailure) {
        const url = `${this.prefix}/${this.table}/schema`
        if (this.database != null) {

            this.httpHelper.get(url)
                .then(res => { onSuccess(res) })
                .catch(res => { onFailure(res) })
        }
    }

    getTables(onSuccess, onFailure) {
        if (this.database != null) {

            this.httpHelper.get(`${this.prefix}/tables`)
                .then(res => { onSuccess(res) })
                .catch(res => { onFailure(res) });
        }
    }
}

export default QueryService;