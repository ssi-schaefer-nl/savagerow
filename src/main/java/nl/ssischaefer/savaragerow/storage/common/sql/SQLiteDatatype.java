package nl.ssischaefer.savaragerow.storage.common.sql;

public enum SQLiteDatatype {
    Integer("INTEGER"),
    Text("TEXT"),
    Real("REAL"),
    Blob("BLOB"),
    Date("DATE"),
    Datetime("DATETIME"),
    Boolean("BOOLEAN");


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
