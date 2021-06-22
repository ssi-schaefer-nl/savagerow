import HttpHelper from "../HttpHelper";

class DefinitionService {
    constructor(table) {
        this.table = table
        this.database = localStorage.getItem('database');
        if (this.database != null && !this.database.length > 0) this.database = null

        this.prefix = `/api/v1/${this.database}/database/${table}`
        this.httpHelper = new HttpHelper();
    }

    addColumn(data, onSuccess, onFailure) {
        if (this.database != null) {
            this.httpHelper.post(`${this.prefix}/column`, data)
                .then(res => { onSuccess(res) })
                .catch(res => { onFailure(res) });
        }

    }

    createTable(onSuccess, onFailure) {
        if (this.database != null) {
            this.httpHelper.post(`${this.prefix}`)
                .then(res => { onSuccess(res) })
                .catch(res => { onFailure(res) });
        }
    }

    deleteTable(onSuccess, onFailure) {
        if (this.database != null) {
            this.httpHelper.delete(`${this.prefix}`)
                .then(res => { onSuccess(res) })
                .catch(res => { onFailure(res) });
        }
    }

    dropColumn(column, onSuccess, onFailure) {
        if (this.database != null) {

            this.httpHelper.delete(`${this.prefix}/column/${column}`)
                .then(res => { onSuccess(res) })
                .catch(res => { onFailure(res) });
        }
    }

    renameColumn(column, newName, onSuccess, onFailure) {
        if (this.database != null) {
            this.httpHelper.put(`${this.prefix}/${column}/column`, { name: newName })
                .then(res => { onSuccess(res) })
                .catch(res => { onFailure(res) });
        }

    }

}

export default DefinitionService;