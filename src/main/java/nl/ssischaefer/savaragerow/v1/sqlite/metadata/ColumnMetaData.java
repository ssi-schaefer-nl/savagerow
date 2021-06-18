package nl.ssischaefer.savaragerow.v1.sqlite.metadata;

public class ColumnMetaData {
    private String name;
    private int datatype;
    private String autoIncrement;
    private String nullable;


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

    public String getNullable() {
        return nullable;
    }

    public ColumnMetaData setNullable(String nullable) {
        this.nullable = nullable;
        return this;
    }
}
