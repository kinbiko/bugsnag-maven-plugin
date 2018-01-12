package com.kinbiko.bugsnagmavenplugin.releases;

import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

public class JSONConverterTest {

    @Test
    public void empty() {
        final String converted = runTest(new HashMap<>());
        assertEquals("{}", converted);
    }

    @Test
    public void singleField() {
         final String converted = runTest(new HashMap<String, Object>(){{
             put("apiKey", "abcd1234abcd1234abcd1234abcd1234");
         }});
        assertEquals("{\"apiKey\":\"abcd1234abcd1234abcd1234abcd1234\"}", converted);
    }

    @Test
    public void mapOfMaps() {
          final String converted = runTest(new HashMap<String, Object>(){{
             put("apiKey", "abcd1234abcd1234abcd1234abcd1234");
             put("map", new HashMap<String, String>(){{
                 put("test", "value");
             }});
          }});
        assertEquals("{\"apiKey\":\"abcd1234abcd1234abcd1234abcd1234\",\"map\":{\"test\":\"value\"}}", converted);

    }

    private String runTest(final Map<String, Object> map) {
        return JSONConverter.makeBody(map).toString();
    }
}