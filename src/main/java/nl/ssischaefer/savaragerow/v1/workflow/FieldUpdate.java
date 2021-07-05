package nl.ssischaefer.savaragerow.v1.workflow;

import nl.ssischaefer.savaragerow.v1.util.sql.SQLSetAction;

public class FieldUpdate {
    private String column;
    private SQLSetAction action;
    private String value;

    public String getColumn() {
        return column;
    }

    public FieldUpdate setColumn(String column) {
        this.column = column;
        return this;
    }

    public SQLSetAction getAction() {
        return action;
    }

    public FieldUpdate setAction(SQLSetAction action) {
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
