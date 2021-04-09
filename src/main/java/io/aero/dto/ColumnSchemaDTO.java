package io.aero.dto;

public class ColumnSchemaDTO {
    private String column;
    private String datatype;
    private boolean editable;

    public String getColumn() {
        return column;
    }

    public ColumnSchemaDTO setColumn(String column) {
        this.column = column;
        return this;
    }

    public String getDatatype() {
        return datatype;
    }

    public ColumnSchemaDTO setDatatype(String datatype) {
        this.datatype = datatype;
        return this;
    }

    public boolean isEditable() {
        return editable;
    }

    public ColumnSchemaDTO setEditable(boolean editable) {
        this.editable = editable;
        return this;
    }
}
