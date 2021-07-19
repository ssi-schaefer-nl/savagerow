package nl.ssischaefer.savaragerow.data.common.sql;

public enum SQLiteDatatype {
    Integer("INTEGER"),
    Text("TEXT"),
    Real("REAL"),
    Blob("BLOB");


    public final String datatype;

    public static SQLiteDatatype fromString(String s) {
        for(SQLiteDatatype d: SQLiteDatatype.values()) {
            if(d.datatype.equalsIgnoreCase(s)) return d;
        }
        return Text;
    }

    private SQLiteDatatype(String datatype) {
        this.datatype = datatype;
    }
}
