package nl.ssischaefer.savaragerow.v1.dto;

public class ColumnSchemaDTO {
    private String column;
    private boolean editable;
    private boolean nullable;

    public String getColumn() {
        return column;
    }

    public ColumnSchemaDTO setColumn(String column) {
        this.column = column;
        return this;
    }

    public boolean isEditable() {
        return editable;
    }

    public ColumnSchemaDTO setEditable(boolean editable) {
        this.editable = editable;
        return this;
    }

    public boolean isNullable() {
        return nullable;
    }

    public ColumnSchemaDTO setNullable(boolean nullable) {
        this.nullable = nullable;
        return this;
    }
}
