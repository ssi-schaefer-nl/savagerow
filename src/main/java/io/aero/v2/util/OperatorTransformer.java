package io.aero.v2.util;

public class OperatorTransformer {
    public static String convertToSql(String operator) {
        switch(operator) {
            case "==": return "=";
            case "<": return "<";
            case "!=": return "!=";
            case ">": return ">";
            default: return "=";
        }
    }

    public static boolean compare(String operator, String source, String destination) {
        switch(operator) {
            case "==": return source.equals(destination);
            case "!=": return !source.equals(destination);
            case "~=": return source.contains(destination);
            default: return false;
        }
    }
}
