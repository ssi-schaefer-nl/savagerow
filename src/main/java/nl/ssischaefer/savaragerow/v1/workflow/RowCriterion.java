package nl.ssischaefer.savaragerow.v1.workflow;

import nl.ssischaefer.savaragerow.v1.util.sql.SQLComparator;

public class RowCriterion {
    private String column;
    private SQLComparator comparator;
    private String required;

    public String getColumn() {
        return column;
    }

    public RowCriterion setColumn(String column) {
        this.column = column;
        return this;
    }

    public String getRequired() {
        return required;
    }

    public RowCriterion setRequired(String required) {
        this.required = required;
        return this;
    }

    public SQLComparator getComparator() {
        return comparator;
    }

    public RowCriterion setComparator(SQLComparator comparator) {
        this.comparator = comparator;
        return this;
    }
}
