import HttpHelper from './HttpHelper';

class DatabaseService {
    constructor() {
        this.httpHelper = new HttpHelper()
    }

    changeDatabases(db, onSuccess, onFailure) {
        this.httpHelper.post('/api/database/set/' + db, {})
            .then(res => { onSuccess(res) })
            .catch(res => { onFailure(res) });

    }

    listAllDatabases(onSuccess, onFailure) {
        this.httpHelper.get('/api/database/all')
            .then(res => { onSuccess(res) })
            .catch(res => { onFailure(res) });

    }

    getCurrentDatabase(onSuccess, onFailure) {
        this.httpHelper.get('/api/database/current')
            .then(res => { onSuccess(res) })
            .catch(res => { onFailure(res) });

    }
}

export default DatabaseService;