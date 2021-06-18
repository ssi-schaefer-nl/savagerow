package nl.ssischaefer.savaragerow.v2.workflowqueue;

import nl.ssischaefer.savaragerow.v2.model.WorkflowType;

import java.util.Map;

public class WorkflowTask {
    private Map<String, String> data;
    private String table;
    private WorkflowType type;


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

}
