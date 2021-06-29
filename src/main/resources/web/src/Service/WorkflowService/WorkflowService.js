import HttpHelper from "../HttpHelper";

class WorkflowService {
    constructor() {
        this.database = localStorage.getItem('database');
        if(this.database != null && !this.database.length > 0) this.database = null
        this.prefix = `/api/v1/${this.database}/workflow`
        this.httpHelper = new HttpHelper();
    }

    getAllWorkflows(onSuccess, onFailure) {
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

    getTriggeredWorkflows(onSuccess, onFailure) {
        if (this.database != null) {

            this.httpHelper.get(`${this.prefix}/triggeredworkflow/all`)
                .then(res => { onSuccess(res.data) })
                .catch(res => { onFailure(res) });
        }
    }

    getScheduledWorkflows(onSuccess, onFailure) {
        if (this.database != null) {

            this.httpHelper.get(`${this.prefix}/scheduledworkflow/all`)
                .then(res => { onSuccess(res.data) })
                .catch(res => { onFailure(res) });
        }
    }


    saveScheduledWorkflow(data, onSuccess, onFailure) {
        if (this.database != null) {

            this.httpHelper.post(`${this.prefix}/scheduled`, data)
                .then(res => onSuccess(res))
                .catch(res => onFailure(res.response.data));
        }
    }

    saveTriggeredWorkflow(type, data, onSuccess, onFailure) {
        if (this.database != null) {

            this.httpHelper.post(`${this.prefix}/triggered/${type}`, data)
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

    deleteWorkflow(variant, workflow, onSuccess, onFailure) {
        if (this.database != null) {

            this.httpHelper.deleteWithData(`${this.prefix}/${variant}`, workflow)
                .then(res => onSuccess(res))
                .catch(res => onFailure(res));
        }
    }

    deleteTriggeredWorkflow(workflow, onSuccess, onFailure) {
        this.deleteWorkflow("triggeredworkflow", workflow, onSuccess, onFailure)
    }

    deleteScheduledWorkflow(workflow, onSuccess, onFailure) {
        this.deleteWorkflow("scheduledworkflow", workflow, onSuccess, onFailure)
    }
}

export default WorkflowService