package nl.ssischaefer.savaragerow.v2.util;

import nl.ssischaefer.savaragerow.v2.util.exception.WorkspaceNotSetException;
import org.junit.Assert;
import org.junit.jupiter.api.Test;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public class PlaceholderResolverTest {
    private Map<String, String> getLookupMap() {
        Map<String, String> lookupMap = new HashMap<>();
        lookupMap.put("one", "1");
        lookupMap.put("two", "2");
        lookupMap.put("three", "3");
        return lookupMap;
    }

    @Test
    public void shouldResolvePlaceholdersWithCashFormat()  {
        String stringWithPlaceholders = "$one$, $two$, $three$";
        String expectedResult = "1, 2, 3";
        Map<String, String> lookupMap = getLookupMap();

        String result = PlaceholderResolver.resolve(stringWithPlaceholders, lookupMap, PlaceholderResolver.CASH_FORMAT);
        Assert.assertEquals(expectedResult, result);
    }

    @Test
    public void shouldResolvePlaceholdersWithBracketsFormat()  {
        String stringWithPlaceholders = "{one}, {two}, {three}";
        String expectedResult = "1, 2, 3";
        Map<String, String> lookupMap = getLookupMap();

        String result = PlaceholderResolver.resolve(stringWithPlaceholders, lookupMap, PlaceholderResolver.BRACKETS_FORMAT);
        Assert.assertEquals(expectedResult, result);
    }
}
