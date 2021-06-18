package io.aero.v2.model;

public enum WorkflowType {
    UPDATE("update"),
    DELETE("delete"),
    INSERT("insert"),
    UNKNOWN("unknown");

    private String type;

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
        return UNKNOWN;
    }


}
