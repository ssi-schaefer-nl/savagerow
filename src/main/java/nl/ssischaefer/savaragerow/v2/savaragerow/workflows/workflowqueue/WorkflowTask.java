package nl.ssischaefer.savaragerow.v2.savaragerow.workflows.workflowqueue;

import nl.ssischaefer.savaragerow.v2.savaragerow.workflows.AbstractWorkflow;

import java.util.Map;

public class WorkflowTask {
    private AbstractWorkflow workflow;
    private Map<String, String> data;

    public AbstractWorkflow getWorkflow() {
        return workflow;
    }

    public WorkflowTask setWorkflow(AbstractWorkflow workflow) {
        this.workflow = workflow;
        return this;
    }

    public Map<String, String> getData() {
        return data;
    }

    public WorkflowTask setData(Map<String, String> data) {
        this.data = data;
        return this;
    }

    public void execute() {
        workflow.execute(data);
    }
}
