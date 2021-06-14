package io.aero.v2.util;

public class OperatorToSQLComparator {
    public static String convert(String operator) {
        switch(operator) {
            case "==": return "=";
            default: return "=";
        }
    }
}
