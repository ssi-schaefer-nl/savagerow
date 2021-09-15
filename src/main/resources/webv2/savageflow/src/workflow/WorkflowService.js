import HttpHelper from "../common/HttpHelper";

class WorkflowService {

    getAll(onSuccess, onFailure) {
        let httpHelper = new HttpHelper()
        httpHelper.get("/workflow")
            .then(res => onSuccess(res.data))
            .catch(res => onFailure(res.response))
    }

    delete(id, onSuccess, onFailure) {
        let httpHelper = new HttpHelper()
        if (id.length > 0)
            httpHelper.delete(`/workflow/${id}`)
                .then(res => onSuccess(res.data))
                .catch(res => onFailure(res.response))
    }

    update(workflow, onSuccess, onFailure) {
        let httpHelper = new HttpHelper()

        httpHelper.put("/workflow", workflow)
            .then(res => onSuccess(res))
            .catch(res => onFailure(res.response));
    }

    get(id, onSuccess, onFailure) {
        let httpHelper = new HttpHelper()
        httpHelper.get(`/workflow/${id}`)
            .then(res => onSuccess(res.data))
            .catch(res => onFailure(res.response))
    }

    save(workflow, onSuccess, onFailure) {
        new HttpHelper().put("/workflow", workflow)
            .then(res => onSuccess(res.data))
            .catch(res => onFailure(res))
    }

    createNew(name, onSuccess, onFailure) {
        let httpHelper = new HttpHelper()

        httpHelper.get("/workflow/generate/id")
            .then(idResponse => {
                httpHelper.post("/workflow", { name: name, id: idResponse.data.id, enabled: false })
                    .then(createResponse => onSuccess(createResponse.data))
                    .catch(createFailure => onFailure(createFailure))
            })
            .catch(idFailure => onFailure(idFailure))
    }

    getTaskInput(workflow, taskId, onSuccess, onFailure) {
        let httpHelper = new HttpHelper()

        httpHelper.get(`/workflow/${workflow}/${taskId}/input`)
            .then(res => onSuccess(res.data))
            .catch(res => onFailure(res.response))
    }
}

export default WorkflowService