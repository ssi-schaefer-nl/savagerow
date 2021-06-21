import HttpHelper from "../HttpHelper";

class DefinitionService {
    constructor(table) {
        this.table = table
        let database = localStorage.getItem('database');
        if(database == null) database = "unknown"
        this.prefix = `/api/v1/${database.toLowerCase()}/database/${table}/column`
        this.httpHelper = new HttpHelper();
    }

    addColumn(data, onSuccess, onFailure) {
        this.httpHelper.post(`${this.prefix}`, data)
            .then(res => { onSuccess(res) })
            .catch(res => { onFailure(res) });

    }

    dropColumn(column, onSuccess, onFailure) {
        this.httpHelper.delete(`${this.prefix}/${column}`)
            .then(res => { onSuccess(res) })
            .catch(res => { onFailure(res) });

    }

    renameColumn(column, newName, onSuccess, onFailure) {
        this.httpHelper.put(`${this.prefix}/${column}`, {name: newName})
            .then(res => { onSuccess(res) })
            .catch(res => { onFailure(res) });

    }

}

export default DefinitionService;