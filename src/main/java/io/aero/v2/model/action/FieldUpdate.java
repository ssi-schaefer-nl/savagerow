package io.aero.v2.model.action;

public class FieldUpdate {
    private String column;
    private String action;
    private String value;

    public String getColumn() {
        return column;
    }

    public FieldUpdate setColumn(String column) {
        this.column = column;
        return this;
    }

    public String getAction() {
        return action;
    }

    public FieldUpdate setAction(String action) {
        this.action = action;
        return this;
    }

    public String getValue() {
        return value;
    }

    public FieldUpdate setValue(String value) {
        this.value = value;
        return this;
    }
}
