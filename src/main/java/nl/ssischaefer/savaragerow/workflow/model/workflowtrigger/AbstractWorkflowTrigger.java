package nl.ssischaefer.savaragerow.workflow.model.workflowtrigger;


import nl.ssischaefer.savaragerow.workflow.model.task.AbstractWorkflowTask;

import java.util.Map;

public abstract class AbstractWorkflowTrigger {
    private String workflowId;
    private AbstractWorkflowTask initialTask;

    public String getWorkflowId() {
        return workflowId;
    }

    public void setWorkflowId(String workflowId) {
        this.workflowId = workflowId;
    }

    public AbstractWorkflowTask getInitialTask() {
        return initialTask;
    }

    public void setInitialTask(AbstractWorkflowTask initialTask) {
        this.initialTask = initialTask;
    }

    public void trigger(Map<String, String> input) {
        initialTask.execute(input);
    }
}
