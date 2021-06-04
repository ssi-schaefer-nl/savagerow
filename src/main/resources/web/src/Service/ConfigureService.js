import HttpHelper from './HttpHelper';

class DatabaseService {
    constructor() {
        this.httpHelper = new HttpHelper()
    }
    
    listAllDatabases(onSuccess, onFailure) {
        this.httpHelper.get('/api/v1/database')
            .then(res => { onSuccess(res) })
            .catch(res => { onFailure(res) });

    }
}

export default DatabaseService;