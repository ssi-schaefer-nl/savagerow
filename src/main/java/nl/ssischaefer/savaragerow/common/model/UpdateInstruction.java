package nl.ssischaefer.savaragerow.common.model;

public class UpdateInstruction {
    private String field;
    private String operation;
    private String value;

    public UpdateInstruction(String field, String operation, String value) {
        this.field = field;
        this.operation = operation;
        this.value = value;
    }

    public UpdateInstruction() {
    }

    public String getField() {
        return field;
    }

    public UpdateInstruction setField(String field) {
        this.field = field;
        return this;
    }

    public String getOperation() {
        return operation;
    }

    public UpdateInstruction setOperation(String operation) {
        this.operation = operation;
        return this;
    }

    public String getValue() {
        return value;
    }

    public UpdateInstruction setValue(String value) {
        this.value = value;
        return this;
    }
}
