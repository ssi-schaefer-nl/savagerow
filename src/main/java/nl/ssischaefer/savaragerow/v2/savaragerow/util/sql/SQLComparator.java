package nl.ssischaefer.savaragerow.v2.savaragerow.util.sql;

public enum SQLComparator {
    EQUALS("=="),
    SMALLER("<"),
    GREATER(">"),
    NOT("!=");

    private String comparator;

    SQLComparator(String comparator) {
        this.comparator = comparator;
    }

    public String getComparator() {
        return this.comparator;
    }

    public static SQLComparator fromString(String type) {
        for (SQLComparator t : SQLComparator.values()) {
            if (t.comparator.equalsIgnoreCase(type)) {
                return t;
            }
        }
        return EQUALS;
    }


}
