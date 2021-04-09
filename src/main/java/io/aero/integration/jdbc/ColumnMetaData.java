package io.aero.integration.jdbc;

public class ColumnMetaData {
    private String name;
    private int datatype;
    private String autoIncrement;


    public String getName() {
        return name;
    }

    public ColumnMetaData setName(String name) {
        this.name = name;
        return this;
    }

    public int getDatatype() {
        return datatype;
    }

    public ColumnMetaData setDatatype(int datatype) {
        this.datatype = datatype;
        return this;
    }

    public String getAutoIncrement() {
        return autoIncrement;
    }

    public ColumnMetaData setAutoIncrement(String autoIncrement) {
        this.autoIncrement = autoIncrement;
        return this;
    }
}
