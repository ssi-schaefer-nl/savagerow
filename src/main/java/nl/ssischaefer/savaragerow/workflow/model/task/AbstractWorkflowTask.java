package nl.ssischaefer.savaragerow.workflow.model.task;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

public abstract class AbstractWorkflowTask {
    private static final Logger logger = LoggerFactory.getLogger("WorkflowTask");

    private Long id;
    private String workflowId;
    private AbstractWorkflowTask next;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public AbstractWorkflowTask getNext() {
        return next;
    }

    public void setNext(AbstractWorkflowTask next) {
        this.next = next;
    }


    public void execute(Map<String, String> input) {
        logger.info(String.format("Running task %d for workflow %s", id, workflowId));
        Map<String, String> output = this.performTask(input);
        if(next != null)
            next.execute(output);
    }

    protected abstract Map<String, String> performTask(Map<String, String> input);

    public abstract Map<String, String> getOutput();

    public String getWorkflowId() {
        return workflowId;
    }

    public void setWorkflowId(String workflowId) {
        this.workflowId = workflowId;
    }
}
