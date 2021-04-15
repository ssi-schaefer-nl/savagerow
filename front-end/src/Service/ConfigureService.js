import HttpHelper from './HttpHelper';

class ConfigureService {
    constructor() {
        this.httpHelper = new HttpHelper()
    }

    changeDatabases(db, onSuccess, onFailure) {
        this.httpHelper.post('/api/database/set/'+db, {}, onSuccess, onFailure)
    }    

    listAllDatabases(onSuccess, onFailure) {
        this.httpHelper.get('/api/database/all', onSuccess, onFailure)
    }    
    
    getCurrentDatabase(onSuccess, onFailure) {
        this.httpHelper.get('/api/database/current', onSuccess, onFailure)
    }

    
    getTables(onSuccess, onFailure) {
        this.httpHelper.get('/api/database/tables/all', onSuccess, onFailure)
    }

}

export default ConfigureService;