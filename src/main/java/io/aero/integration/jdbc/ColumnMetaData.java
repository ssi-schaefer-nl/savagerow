package io.aero.integration.jdbc;

public class ColumnMetaData {
    private String name;
    private String datatype;


    public String getName() {
        return name;
    }

    public ColumnMetaData setName(String name) {
        this.name = name;
        return this;
    }

    public String getDatatype() {
        return datatype;
    }

    public ColumnMetaData setDatatype(String datatype) {
        this.datatype = datatype;
        return this;
    }
}
