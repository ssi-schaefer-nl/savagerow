package io.aero.dto;

public class AddColumnDTO {
    private String column;
    private String datatype;
    private boolean nullable;
    private String defaultValue;

    public String getDefaultValue() {
        return defaultValue;
    }

    public AddColumnDTO setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
        return this;
    }

    public String getColumn() {
        return column;
    }

    public AddColumnDTO setColumn(String column) {
        this.column = column;
        return this;
    }

    public String getDatatype() {
        return datatype;
    }

    public AddColumnDTO setDatatype(String datatype) {
        this.datatype = datatype;
        return this;
    }

    public boolean isNullable() {
        return nullable;
    }

    public AddColumnDTO setNullable(boolean nullable) {
        this.nullable = nullable;
        return this;
    }

}
