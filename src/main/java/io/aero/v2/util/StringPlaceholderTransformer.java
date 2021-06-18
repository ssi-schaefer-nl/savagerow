package io.aero.v2.util;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringPlaceholderTransformer {
    public static String transformPlaceholders(String source, Map<String, String> lookup) {
        Map<String, String> replacementMap = generateReplacementMap(source, lookup);
        String transformedString = source;
        for (Map.Entry<String, String> placeholders : replacementMap.entrySet()) {
            transformedString = transformedString.replace(placeholders.getKey(), placeholders.getValue());
        }
        return transformedString;
    }

    public static String transformPlaceholdersJson(String source, Map<String, String> lookup) {
        Map<String, String> replacementMap = generateReplacementMapForJson(source, lookup);
        String transformedString = source;
        for (Map.Entry<String, String> placeholders : replacementMap.entrySet()) {
            transformedString = transformedString.replace(placeholders.getKey(), placeholders.getValue());
        }
        return transformedString;
    }

    private static Map<String, String> generateReplacementMap(String source, Map<String, String> lookup) {
        Map<String, String> toReplace = new HashMap<>();
        Matcher m = Pattern.compile("\\{(.*?)\\}").matcher(source);
        while (m.find()) {
            String placeholder = m.group(1);
            if (lookup.get(placeholder) != null)
                toReplace.put("{" + placeholder + "}", lookup.get(placeholder));
        }
        return toReplace;
    }

    private static Map<String, String> generateReplacementMapForJson(String source, Map<String, String> lookup) {
        Map<String, String> toReplace = new HashMap<>();
        Matcher m = Pattern.compile("\\$(.*?)\\$").matcher(source);
        while (m.find()) {
            String placeholder = m.group(1);
            if (lookup.get(placeholder) != null)
                toReplace.put("$" + placeholder + "$", lookup.get(placeholder));
        }
        return toReplace;
    }
}
