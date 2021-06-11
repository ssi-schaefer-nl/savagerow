import HttpHelper from "../HttpHelper";

class WorkflowService {
    constructor() {
        this.prefix = `/api/v1/${localStorage.getItem('database')}/workflow`
        this.httpHelper = new HttpHelper();
    }

    getDbSummary(onSuccess, onFailure) {
        this.httpHelper.get(this.prefix)
            .then(res => { onSuccess(res.data) })
            .catch(res => { onFailure(res) });
    }

    getTableWorkflows(table, type, onSuccess, onFailure) {
        this.httpHelper.get(`${this.prefix}/${table}/${type}`)
            .then(res => { onSuccess(res.data) })
            .catch(res => { onFailure(res) });
    }

    saveWorkflow(type, data, onSuccess, onFailure) {
        this.httpHelper.post(`${this.prefix}/${type}`, data)
            .then(res => onSuccess(res))
            .catch(res => onFailure(res.response.data));
    }


    changeActive(table, type, name, active, onSuccess, onFailure) {
        this.httpHelper.post(`${this.prefix}/${table}/${type}/${name}/active/${active}`)
            .then(res => onSuccess(res))
            .catch(res => onFailure(res.response.data));
    }

    deleteWorkflow(table, type, name, onSuccess, onFailure) {
        this.httpHelper.delete(`${this.prefix}/${table}/${type}/${name}`)
            .then(res => onSuccess(res))
            .catch(res => onFailure(res.response.data));
    }
}

export default WorkflowService