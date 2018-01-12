package com.kinbiko.bugsnagmavenplugin.releases;

import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

import java.util.Map;

import static com.kinbiko.bugsnagmavenplugin.releases.JSONConverter.makeBody;

/**
 * The default build API request maker using unirest.
 */
public class DefaultBuildApiRequestMaker implements BuildApiRequestMaker {

    private static final BuildsObjectMapper OBJECT_MAPPER = new BuildsObjectMapper();

    DefaultBuildApiRequestMaker() {
        Unirest.setObjectMapper(OBJECT_MAPPER);
    }

    private static final String BUILD_URL = "https://build.bugsnag.com/";

    @Override
    public BuildApiResponse makeRequest(final Map<String, Object> requestBody) throws UnirestException {
        return Unirest.post(BUILD_URL)
                .header("Content-Type", "application/json")
                .body(makeBody(requestBody)).asObject(BuildApiResponse.class).getBody();
    }
}
