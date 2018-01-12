package com.kinbiko.bugsnagmavenplugin.releases;

import org.json.JSONObject;

import java.util.Map;

public class JSONConverter {

    static JSONObject makeBody(final Map<String, Object> requestBody) {
        final JSONObject json = new JSONObject();
        requestBody.forEach(json::put);
        return json;
    }
}