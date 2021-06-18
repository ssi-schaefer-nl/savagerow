package nl.ssischaefer.savaragerow.v2.util;

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
        return null;
    }

    private SQLiteDatatype(String datatype) {
        this.datatype = datatype;
    }
}
