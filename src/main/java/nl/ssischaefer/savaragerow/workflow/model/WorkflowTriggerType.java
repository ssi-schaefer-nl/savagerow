package nl.ssischaefer.savaragerow.workflow.model;

public enum WorkflowTriggerType {
    UPDATE("update"),
    DELETE("delete"),
    INSERT("insert"),
    UNKNOWN("unknown");

    private String type;

    WorkflowTriggerType(String type) {
        this.type = type;
    }

    public String getType() {
        return this.type;
    }

    public static WorkflowTriggerType fromString(String type) {
        for (WorkflowTriggerType t : WorkflowTriggerType.values()) {
            if (t.type.equalsIgnoreCase(type)) {
                return t;
            }
        }
        return UNKNOWN;
    }


}
