package nl.ssischaefer.savaragerow.v2.workflow;

public enum WorkflowType {
    SCHEDULED("scheduled"),
    TRIGGERED("triggered");

    private final String type;

    WorkflowType(String type) {
        this.type = type;
    }

    public String getType() {
        return this.type;
    }

    public static WorkflowType fromString(String type) {
        for (WorkflowType t : WorkflowType.values()) {
            if (t.type.equalsIgnoreCase(type)) {
                return t;
            }
        }
        return null;
    }


}
