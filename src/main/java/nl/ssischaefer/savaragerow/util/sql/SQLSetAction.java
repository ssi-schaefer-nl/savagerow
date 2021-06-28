package nl.ssischaefer.savaragerow.util.sql;

public enum SQLSetAction {
    ADD("+"),
    SUBTRACT("-"),
    MULTIPLY("*"),
    SET("=");

    private String action;

    SQLSetAction(String action) {
        this.action = action;
    }

    public String setAction() {
        return this.action;
    }

    public static SQLSetAction fromString(String type) {
        for (SQLSetAction t : SQLSetAction.values()) {
            if (t.action.equalsIgnoreCase(type)) {
                return t;
            }
        }
        return SET;
    }


}
