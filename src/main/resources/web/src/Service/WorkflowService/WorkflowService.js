import HttpHelper from "../HttpHelper";

class WorkflowService {
    constructor() {
        this.database = localStorage.getItem('database');
        if(this.database != null && !this.database.length > 0) this.database = null
        this.prefix = `/api/v1/${this.database}/workflow`
        this.httpHelper = new HttpHelper();
    }

    getAllWorkflows(onSuccess, onFailure) {
        console.log(this.database)
        if (this.database != null) {

            this.httpHelper.get(`${this.prefix}/all`)
                .then(res => { onSuccess(res.data) })
                .catch(res => { onFailure(res) });
        }
    }

    getDbSummary(onSuccess, onFailure) {
        if (this.database != null) {

            this.httpHelper.get(this.prefix)
                .then(res => { onSuccess(res.data) })
                .catch(res => { onFailure(res) });
        }
    }

    getTableWorkflows(table, type, onSuccess, onFailure) {
        if (this.database != null) {

            this.httpHelper.get(`${this.prefix}/${table}/${type}`)
                .then(res => { onSuccess(res.data) })
                .catch(res => { onFailure(res) });
        }
    }

    saveWorkflow(type, data, onSuccess, onFailure) {
        if (this.database != null) {

            this.httpHelper.post(`${this.prefix}/${type}`, data)
                .then(res => onSuccess(res))
                .catch(res => onFailure(res.response.data));
        }
    }


    changeActive(table, type, name, active, onSuccess, onFailure) {
        if (this.database != null) {

            this.httpHelper.post(`${this.prefix}/${table}/${type}/${name}/active/${active}`)
                .then(res => onSuccess(res))
                .catch(res => onFailure(res.
                    response.data));
        }
    }

    deleteWorkflow(table, type, name, onSuccess, onFailure) {
        if (this.database != null) {

            this.httpHelper.delete(`${this.prefix}/${table}/${type}/${name}`)
                .then(res => onSuccess(res))
                .catch(res => onFailure(res.response.data));
        }
    }
}

export default WorkflowService