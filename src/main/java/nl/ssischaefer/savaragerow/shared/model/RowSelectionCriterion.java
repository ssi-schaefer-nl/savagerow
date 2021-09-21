package nl.ssischaefer.savaragerow.shared.model;

public class RowSelectionCriterion {
    private String column;
    private String comparator;
    private String value;

    public static RowSelectionCriterion getRowIdCriterion(Long rowid) {
        return new RowSelectionCriterion("rowid", "==", rowid.toString());
    }

    public RowSelectionCriterion(String column, String comparator, String value) {
        this.column = column;
        this.comparator = comparator;
        this.value = value;
    }

    public RowSelectionCriterion() {
    }

    public String getColumn() {
        return column;
    }

    public RowSelectionCriterion setColumn(String column) {
        this.column = column;
        return this;
    }

    public String getComparator() {
        return comparator;
    }

    public RowSelectionCriterion setComparator(String comparator) {
        this.comparator = comparator;
        return this;
    }

    public String getValue() {
        return value;
    }

    public RowSelectionCriterion setValue(String value) {
        this.value = value;
        return this;
    }
}
