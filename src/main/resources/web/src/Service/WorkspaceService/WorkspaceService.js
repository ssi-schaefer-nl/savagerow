import HttpHelper from "../HttpHelper";

class WorkspaceService {
    constructor() {
        this.prefix = "/api/v1/workspace"
        this.httpHelper = new HttpHelper();
    }

    export(database, onSuccess, onFailure) {
        const method = 'GET'
        const url = `${this.prefix}/${database}`
        this.httpHelper.request({
            url,
            method,
            reponseType: 'blob'
        }).then(res => { onSuccess(res) })
            .catch(res => { onFailure(res) });
    }

    import(payload, onSuccess, onFailure) {
    
        this.httpHelper.post(this.prefix, { file: payload})
            .then(res => { onSuccess(res) })
            .catch(res => { onFailure(res.response.data) });
    }

}

export default WorkspaceService;