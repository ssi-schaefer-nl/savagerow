package nl.ssischaefer.savaragerow.v2.api.dto;

import nl.ssischaefer.savaragerow.v2.savaragerow.util.sql.SQLiteDatatype;

public class SQLColumn {
    private String name;
    private boolean nullable;
    private String fk;
    private boolean pk;
    private SQLiteDatatype datatype;
    private String defaultValue;

    public String getName() {
        return name;
    }

    public SQLColumn setName(String name) {
        this.name = name;
        return this;
    }

    public boolean isNullable() {
        return nullable;
    }

    public SQLColumn setNullable(boolean nullable) {
        this.nullable = nullable;
        return this;
    }

    public String getFk() {
        return fk;
    }

    public SQLColumn setFk(String fk) {
        this.fk = fk;
        return this;
    }

    public SQLColumn setPk(boolean pk) {
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

    public SQLColumn setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
        return this;
    }

    public SQLColumn setDatatype(SQLiteDatatype datatype) {
        this.datatype = datatype;
        return this;
    }

    @Override
    public String toString() {
        return "ColumnSchemaDTO{" +
                "name='" + name + '\'' +
                ", nullable=" + nullable +
                ", fk='" + fk + '\'' +
                ", pk=" + pk +
                ", datatype=" + datatype +
                ", defaultValue='" + defaultValue + '\'' +
                '}';
    }
}
