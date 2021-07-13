package nl.ssischaefer.savaragerow.util;

public class Configuration {
    public static Integer parseOrDefaultInteger(String variable, Integer defaultValue) {

        String getenv = System.getenv(variable);
        if (getenv == null) {
            return defaultValue;
        }
        if (getenv.trim().isEmpty()) {
            return defaultValue;
        }

        try {
            return Integer.valueOf(getenv);
        } catch (Exception e) {
            return defaultValue;
        }
    }

    public static String parseOrDefault(String variable, String defaultValue) {

        String getenv = System.getenv(variable);
        if (getenv == null) {
            return defaultValue;
        }
        if (getenv.trim().isEmpty()) {
            return defaultValue;
        }

        return getenv;
    }
}
