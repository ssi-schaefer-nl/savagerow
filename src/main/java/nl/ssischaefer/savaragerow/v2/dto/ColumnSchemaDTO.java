package nl.ssischaefer.savaragerow.v2.dto;

import nl.ssischaefer.savaragerow.v2.util.SQLiteDatatype;

public class ColumnSchemaDTO {
    private String name;
    private boolean nullable;
    private String fk;
    private boolean pk;
    private SQLiteDatatype datatype;
    private String defaultValue;

    public String getName() {
        return name;
    }

    public ColumnSchemaDTO setName(String name) {
        this.name = name;
        return this;
    }

    public boolean isNullable() {
        return nullable;
    }

    public ColumnSchemaDTO setNullable(boolean nullable) {
        this.nullable = nullable;
        return this;
    }

    public String getFk() {
        return fk;
    }

    public ColumnSchemaDTO setFk(String fk) {
        this.fk = fk;
        return this;
    }

    public ColumnSchemaDTO setPk(boolean pk) {
        this.pk = pk;
        return this;
    }

    public boolean isPk() {
        return pk;
    }


    public String getDefaultValue() {
        return defaultValue;
    }

    public SQLiteDatatype getDatatype() {
        return datatype;
    }

    public ColumnSchemaDTO setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
        return this;
    }

    public ColumnSchemaDTO setDatatype(SQLiteDatatype datatype) {
        this.datatype = datatype;
        return this;
    }
}
