package nl.ssischaefer.savaragerow.common.schema;

import java.util.List;

public class WorkflowSchema {
    private String id;
    private String name;
    private Boolean enabled;
    private WorkflowTriggerSchema trigger;
    private List<AbstractWorkflowTaskSchema> tasks;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public WorkflowTriggerSchema getTrigger() {
        return trigger;
    }

    public void setTrigger(WorkflowTriggerSchema trigger) {
        this.trigger = trigger;
    }

    public List<AbstractWorkflowTaskSchema> getTasks() {
        return tasks;
    }

    public void setTasks(List<AbstractWorkflowTaskSchema> tasks) {
        this.tasks = tasks;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }
}
