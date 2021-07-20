package nl.ssischaefer.savaragerow.workflow.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringPlaceholderTransformer {

    public static final String TEMPLATE_PATTERN = "\\{(.*?)\\}";
    public static final String TEMPLATE_PATTERN_JSON = "\\$(.*?)\\$";
    public static final String DATE_FORMAT = "yyyy-MM-dd HH:mm";

    public static String transformPlaceholders(String source, Map<String, String> lookup) {
        Map<String, String> replacementMap = generateReplacementMap(source, lookup);
        var transformedString = source;
        for (Map.Entry<String, String> placeholders : replacementMap.entrySet()) {
            transformedString = transformedString.replace(placeholders.getKey(), placeholders.getValue());
        }
        return transformedString;
    }

    public static String transformPlaceholdersJson(String source, Map<String, String> lookup) {
        Map<String, String> replacementMap = generateReplacementMapForJson(source, lookup);
        var transformedString = source;
        for (Map.Entry<String, String> placeholders : replacementMap.entrySet()) {
            transformedString = transformedString.replace(placeholders.getKey(), placeholders.getValue());
        }
        return transformedString;
    }

    private static Map<String, String> generateReplacementMap(String source, Map<String, String> lookup) {
        Map<String, String> toReplace = new HashMap<>();
        var dtf = DateTimeFormatter.ofPattern(DATE_FORMAT);
        var now = LocalDateTime.now();
        var m = Pattern.compile(TEMPLATE_PATTERN).matcher(source);
        while (m.find()) {
            String placeholder = m.group(1);
            if (lookup.get(placeholder) != null)
                toReplace.put("{" + placeholder + "}", lookup.get(placeholder));
            else if (placeholder.equals("now"))
                toReplace.put("{now}", dtf.format(now));

        }
        return toReplace;
    }

    private static Map<String, String> generateReplacementMapForJson(String source, Map<String, String> lookup) {
        Map<String, String> toReplace = new HashMap<>();
        var dtf = DateTimeFormatter.ofPattern(DATE_FORMAT);
        var now = LocalDateTime.now();
        var m = Pattern.compile(TEMPLATE_PATTERN_JSON).matcher(source);
        while (m.find()) {
            String placeholder = m.group(1);
            if (lookup.get(placeholder) != null)
                toReplace.put("$" + placeholder + "$", lookup.get(placeholder));
            else if (placeholder.equals("now"))
                toReplace.put("$now$", dtf.format(now));
        }
        return toReplace;
    }
}
