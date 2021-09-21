package nl.ssischaefer.savaragerow.workflow.trigger;

import java.util.Map;

public abstract class AbstractTrigger {
    private String workflow;
    private Long task;

    public String getWorkflow() {
        return workflow;
    }

    public void setWorkflow(String workflow) {
        this.workflow = workflow;
    }

    public Long getTask() {
        return task;
    }

    public void setTask(Long task) {
        this.task = task;
    }

    public abstract Map<String, String> getOutput();
}
