import HttpHelper from "../HttpHelper";

class WorkflowService {
    constructor() {
        // this.prefix = `/api/query/${localStorage.getItem('database')}/${table}`
        this.httpHelper = new HttpHelper();
    }

    getDbSummary(onSuccess, onFailure) {
        const data = [
            { "table": "Customer", "update": { "active": 2, "total": 4 }, "delete": { "active": 1, "total": 5 }, "insert": { "active": 2, "total": 3 } },
            { "table": "Employee", "update": { "active": 1, "total": 4 }, "delete": { "active": 4, "total": 5 }, "insert": { "active": 3, "total": 3 } },
            { "table": "Customer", "update": { "active": 2, "total": 4 }, "delete": { "active": 1, "total": 5 }, "insert": { "active": 2, "total": 3 } },

            { "table": "Customer", "update": { "active": 2, "total": 4 }, "delete": { "active": 1, "total": 5 }, "insert": { "active": 2, "total": 3 } },

            { "table": "Customer", "update": { "active": 2, "total": 4 }, "delete": { "active": 1, "total": 5 }, "insert": { "active": 2, "total": 3 } },

            { "table": "Customer", "update": { "active": 2, "total": 4 }, "delete": { "active": 1, "total": 5 }, "insert": { "active": 2, "total": 3 } },

            { "table": "Order", "update": { "active": 3, "total": 4 }, "delete": { "active": 5, "total": 5 }, "insert": { "active": 1, "total": 3 } },
            { "table": "Invoice", "update": { "active": 2, "total": 4 }, "delete": { "active": 0, "total": 5 }, "insert": { "active": 3, "total": 3 } },
            { "table": "Article", "update": { "active": 4, "total": 4 }, "delete": { "active": 1, "total": 5 }, "insert": { "active": 1, "total": 3 } },
        ]

        onSuccess(data)
    }

    getTableSummary(table, type, onSuccess, onFailure) {
        const data = [
            { "table": table, "workflows": [
                {"name": "Workflow A", "active": true},
                {"name": "Workflow B", "active": true},
                {"name": "Workflow C", "active": true},
                {"name": "Workflow D", "active": true},
                {"name": "Workflow E", "active": true},
            ]},
            
        ]

        onSuccess(data)
    }
}

export default WorkflowService