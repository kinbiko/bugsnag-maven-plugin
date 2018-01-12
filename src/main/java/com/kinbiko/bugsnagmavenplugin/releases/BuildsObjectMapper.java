package com.kinbiko.bugsnagmavenplugin.releases;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

/**
 * Object mapper for serializing requests and deserializing responses for the build API.
 */
public class BuildsObjectMapper implements com.mashape.unirest.http.ObjectMapper {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Override
    public <T> T readValue(String value, Class<T> type) {
        try {
            return MAPPER.readValue(value, type);
        } catch (IOException ioe) {
            throw new RuntimeException(ioe);
        }
    }

    @Override
    public String writeValue(Object value) {
        try {
            return MAPPER.writeValueAsString(value);
        } catch (IOException ioe) {
            throw new RuntimeException(ioe);
        }
    }
}
