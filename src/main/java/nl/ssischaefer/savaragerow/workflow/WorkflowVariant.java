package nl.ssischaefer.savaragerow.workflow;

import nl.ssischaefer.savaragerow.workflow.scheduledworkflow.ScheduledWorkflow;
import nl.ssischaefer.savaragerow.workflow.triggeredworkflow.TriggeredWorkflow;

public enum WorkflowVariant {
    TRIGGERED("triggered", TriggeredWorkflow.class),
    SCHEDULED("scheduled", ScheduledWorkflow.class);

    private String name;
    private Class<? extends AbstractWorkflow> type;

    WorkflowVariant(String variant, Class<? extends AbstractWorkflow> type) {
        this.type = type;
        this.name = variant;
    }

    public static WorkflowVariant fromString(String type) {
        for (WorkflowVariant t : WorkflowVariant.values()) {
            if (t.name.equalsIgnoreCase(type)) {
                return t;
            }
        }
        return null;
    }


    public Class<? extends AbstractWorkflow> getType() {
        return type;
    }

    public String getName() {
        return this.name;
    }
}
