package nl.ssischaefer.savaragerow.v2.workflow;

public class RowCriteria {
    private String column;
    private String operator;
    private String required;

    public String getColumn() {
        return column;
    }

    public RowCriteria setColumn(String column) {
        this.column = column;
        return this;
    }

    public String getOperator() {
        return operator;
    }

    public RowCriteria setOperator(String operator) {
        this.operator = operator;
        return this;
    }

    public String getRequired() {
        return required;
    }

    public RowCriteria setRequired(String required) {
        this.required = required;
        return this;
    }
}
