import HttpHelper from './HttpHelper';

class DatabaseService {
    constructor() {
        this.httpHelper = new HttpHelper()
    }

    listAll(onSuccess, onFailure) {
        this.httpHelper.post('/api/database/all')
            .then(res => { onSuccess(res) })
            .catch(res => { onFailure(res) });
    }
}    
    

}

export default CreateTableService;