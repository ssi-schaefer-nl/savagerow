package nl.ssischaefer.savaragerow.workflow.task;

import java.util.Map;

public class WorkflowTaskInput {
    private Map<String, String> data;

    public WorkflowTaskInput(Map<String, String> data) {
        this.data = data;
    }

    public Map<String, String> getData() {
        return data;
    }

    public void setData(Map<String, String> data) {
        this.data = data;
    }
}
