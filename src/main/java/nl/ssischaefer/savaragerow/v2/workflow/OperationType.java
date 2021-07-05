package nl.ssischaefer.savaragerow.v2.workflow;

public enum OperationType {
    UPDATE("update"),
    DELETE("delete"),
    INSERT("insert");

    private final String type;

    OperationType(String type) {
        this.type = type;
    }

    public String getType() {
        return this.type;
    }

    public static OperationType fromString(String type) {
        for (OperationType t : OperationType.values()) {
            if (t.type.equalsIgnoreCase(type)) {
                return t;
            }
        }
        return null;
    }


}
