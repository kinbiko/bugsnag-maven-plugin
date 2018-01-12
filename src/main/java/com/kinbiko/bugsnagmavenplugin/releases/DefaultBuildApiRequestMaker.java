package com.kinbiko.bugsnagmavenplugin.releases;

import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

/**
 * The default build API request maker using unirest.
 */
public class DefaultBuildApiRequestMaker implements BuildApiRequestMaker {

    private static final String BUILD_URL = "https://build.bugsnag.com/";

    DefaultBuildApiRequestMaker() {
        Unirest.setObjectMapper(new BuildsObjectMapper());
    }

    @Override
    public BuildApiResponse makeRequest(final BuildApiRequest requestBody) throws UnirestException {
        return Unirest.post(BUILD_URL)
                .header("Content-Type", "application/json")
                .body(requestBody).asObject(BuildApiResponse.class).getBody();
    }
}
