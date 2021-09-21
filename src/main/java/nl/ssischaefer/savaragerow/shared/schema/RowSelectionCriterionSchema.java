package nl.ssischaefer.savaragerow.shared.schema;

public class RowSelectionCriterionSchema {
    private String column;
    private String comparator;
    private String value;

    public String getColumn() {
        return column;
    }

    public void setColumn(String column) {
        this.column = column;
    }

    public String getComparator() {
        return comparator;
    }

    public void setComparator(String comparator) {
        this.comparator = comparator;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}