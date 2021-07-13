package nl.ssischaefer.savaragerow.v3.workflow.model;

public class RowCriteria {
    private String column;
    private String comparator;
    private String required;

    public String getColumn() {
        return column;
    }

    public RowCriteria setColumn(String column) {
        this.column = column;
        return this;
    }

    public String getComparator() {
        return comparator;
    }

    public RowCriteria setComparator(String comparator) {
        this.comparator = comparator;
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
