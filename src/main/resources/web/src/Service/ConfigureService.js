import HttpHelper from './HttpHelper';

class DatabaseService {
    constructor() {
        this.httpHelper = new HttpHelper()
    }
    
    listAllDatabases(onSuccess, onFailure) {
        
        this.httpHelper.get('/api/v1/workspace')
            .then(res => { onSuccess(res) })
            .catch(res => { onFailure(res) });

    }
    

    removeDatabase(database, onSuccess, onFailure) {
        this.httpHelper.delete(`/api/v1/workspace/${database}`)
            .then(res => { onSuccess(res) })
            .catch(res => { onFailure(res) });

    }

    
    addDatabase(database, onSuccess, onFailure) {
        this.httpHelper.post(`/api/v1/workspace/${database}`)
            .then(res => { onSuccess(res) })
            .catch(res => { onFailure(res) });

    }
}

export default DatabaseService;