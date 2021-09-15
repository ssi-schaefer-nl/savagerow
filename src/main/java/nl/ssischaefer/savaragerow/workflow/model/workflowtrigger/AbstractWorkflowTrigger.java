package nl.ssischaefer.savaragerow.workflow.model.workflowtrigger;


import nl.ssischaefer.savaragerow.workflow.model.task.AbstractWorkflowTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

public abstract class AbstractWorkflowTrigger {
    private final Logger logger = LoggerFactory.getLogger("WorkflowTrigger");

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
        logger.info(String.format("Workflow %s has been triggered by: %s", workflowId, getTriggerCause()));
        initialTask.execute(input);
    }

    public abstract Map<String, String> getOutput();
    protected abstract String getTriggerCause();

}
