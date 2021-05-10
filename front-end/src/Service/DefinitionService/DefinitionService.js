import HttpHelper from "../HttpHelper";

class DefinitionService {
    constructor(table) {
        this.table = table
        this.prefix = `/api/definition/${localStorage.getItem('database')}/${table}`
        this.httpHelper = new HttpHelper();
    }

    addColumn(data, onSuccess, onFailure) {
        this.httpHelper.post(`${this.prefix}/column/add`, data)
            .then(res => { onSuccess(res) })
            .catch(res => { onFailure(res) });

    }

    dropColumn(column, onSuccess, onFailure) {
        this.httpHelper.post(`${this.prefix}/${column}/drop`)
            .then(res => { onSuccess(res) })
            .catch(res => { onFailure(res) });

    }

    renameColumn(column, newName, onSuccess, onFailure) {
        this.httpHelper.post(`${this.prefix}/${column}/rename/${newName}`)
            .then(res => { onSuccess(res) })
            .catch(res => { onFailure(res) });

    }

}

export default DefinitionService;