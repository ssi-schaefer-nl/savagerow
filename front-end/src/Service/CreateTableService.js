import HttpHelper from './HttpHelper';

class DatabaseHelper {
    constructor() {
        this.httpHelper = new HttpHelper()
    }

    listAll(onSuccess, onFailure) {
        this.httpHelper.post('/api/database/all', onSuccess, onFailure)
    }    
    

}

export default CreateTableService;