package nl.ssischaefer.savaragerow.v1.workflow;

import nl.ssischaefer.savaragerow.v1.workflow.scheduledworkflow.ScheduledWorkflow;
import nl.ssischaefer.savaragerow.v1.workflow.triggeredworkflow.TriggeredWorkflow;

public enum WorkflowVariant {
    TRIGGERED(TriggeredWorkflow.class),
    SCHEDULED(ScheduledWorkflow.class);

    private Class<? extends AbstractWorkflow> type;

    WorkflowVariant(Class<? extends AbstractWorkflow> type) {
        this.type = type;
    }

    public static WorkflowVariant fromString(String type) {
        for (WorkflowVariant t : WorkflowVariant.values()) {
            if (t.type.getSimpleName().equalsIgnoreCase(type)) {
                return t;
            }
        }
        return null;
    }


    public Class<? extends AbstractWorkflow> getType() {
        return type;
    }
}
