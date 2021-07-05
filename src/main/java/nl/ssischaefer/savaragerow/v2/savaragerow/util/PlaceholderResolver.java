package nl.ssischaefer.savaragerow.v2.savaragerow.util;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PlaceholderResolver {
    private static final String DATE_FORMAT = "dd/MM/yyy HH:mm:ss";
    public static final Pair<String, String> BRACKETS_FORMAT = new ImmutablePair<>("{", "}");
    public static final Pair<String, String> CASH_FORMAT = new ImmutablePair<>("$", "$");

    public static String resolve(String source, Map<String, String> lookup, Pair<String, String> format) {
        Map<String, String> replacementMap = generateReplacementMap(source, lookup, format);

        String transformedString = source;
        for (Map.Entry<String, String> placeholders : replacementMap.entrySet()) {
            transformedString = transformedString.replace(placeholders.getKey(), placeholders.getValue());
        }
        return transformedString;
    }

    private static Map<String, String> generateReplacementMap(String source, Map<String, String> lookup, Pair<String, String> format) {
        String start = format.getLeft();
        String end = format.getRight();

        Map<String, String> toReplace = new HashMap<>();
        String pattern = String.format("\\%s(.*?)\\%s", start, end);

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern(DATE_FORMAT);
        LocalDateTime now = LocalDateTime.now();
        Matcher m = Pattern.compile(pattern).matcher(source);

        while (m.find()) {
            String placeholder = m.group(1);
            if (lookup.get(placeholder) != null)
                toReplace.put(start + placeholder + end, lookup.get(placeholder));
            else if (placeholder.equals("now"))
                toReplace.put(start+"now"+end, dtf.format(now));

        }
        return toReplace;
    }
}
