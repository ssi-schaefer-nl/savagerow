package io.aero.v2.workflowqueue;

import io.aero.v2.model.Workflow;
import io.aero.v2.model.WorkflowType;

import java.util.Map;

public class WorkflowTask {
    private Map<String, String> data;
    private String table;
    private WorkflowType type;

//    private Workflow workflow;

    public Map<String, String> getData() {
        return data;
    }

    public WorkflowTask setData(Map<String, String> data) {
        this.data = data;
        return this;
    }

    public String getTable() {
        return table;
    }

    public WorkflowTask setTable(String table) {
        this.table = table;
        return this;
    }

    public WorkflowType getType() {
        return type;
    }

    public WorkflowTask setType(WorkflowType type) {
        this.type = type;
        return this;
    }

    //
//    public Workflow getWorkflow() {
//        return workflow;
//    }
//
//    public WorkflowTask setWorkflow(Workflow workflow) {
//        this.workflow = workflow;
//        return this;
//    }
}
