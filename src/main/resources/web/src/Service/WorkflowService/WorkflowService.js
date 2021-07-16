import HttpHelper from "../HttpHelper";

class WorkflowService {
    constructor() {
        this.database = localStorage.getItem('database');
        if(this.database != null && !this.database.length > 0) this.database = null
        this.prefix = `/api/v1/${this.database}/workflow`
        this.httpHelper = new HttpHelper();
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

            this.httpHelper.post(`${this.prefix}/scheduledworkflow`, data)
                .then(res => onSuccess(res))
                .catch(res => onFailure(res.response.data));
        }
    }

        
    updateScheduledWorkflow(data, onSuccess, onFailure) {
        if (this.database != null) {

            this.httpHelper.put(`${this.prefix}/scheduledworkflow`, data)
                .then(res => onSuccess(res))
                .catch(res => onFailure(res.response.data));
        }
    }


    getTriggeredWorkflows(onSuccess, onFailure) {
        if (this.database != null) {

            this.httpHelper.get(`${this.prefix}`)
                .then(res => { onSuccess(res.data) })
                .catch(res => { onFailure(res) });
        }
    }

    saveTriggeredWorkflow(data, onSuccess, onFailure) {
        if (this.database != null) {

            this.httpHelper.post(`${this.prefix}`, data)
                .then(res => onSuccess(res))
                .catch(res => onFailure(res.response.data));
        }
    }

    updateTriggeredWorkflow(data, onSuccess, onFailure) {
        if (this.database != null) {

            this.httpHelper.put(`${this.prefix}`, data)
                .then(res => onSuccess(res))
                .catch(res => onFailure(res.response.data));
        }
    }

    deleteTriggeredWorkflow(workflow, onSuccess, onFailure) {
        if (this.database != null) {
            this.httpHelper.deleteWithData(`${this.prefix}`, workflow)
                .then(res => onSuccess(res))
                .catch(res => onFailure(res));
        }
    }
    

    deleteScheduledWorkflow(workflow, onSuccess, onFailure) {
        this.deleteWorkflow("scheduledworkflow", workflow, onSuccess, onFailure)
    }

    deleteWorkflow(variant, workflow, onSuccess, onFailure) {
        if (this.database != null) {

            this.httpHelper.deleteWithData(`${this.prefix}/${variant}`, workflow)
                .then(res => onSuccess(res))
                .catch(res => onFailure(res));
        }
    }
}

export default WorkflowService