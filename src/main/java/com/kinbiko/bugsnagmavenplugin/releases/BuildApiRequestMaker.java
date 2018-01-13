package com.kinbiko.bugsnagmavenplugin.releases;

import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

/**
 * The default build API request maker using unirest.
 */
class BuildApiRequestMaker {

    private static final String BUILD_URL = "https://build.bugsnag.com/";

    BuildApiRequestMaker() {
        Unirest.setObjectMapper(new BuildsObjectMapper());
    }

    BuildApiResponse makeRequest(final BuildApiRequest requestBody) throws UnirestException {
        return Unirest.post(BUILD_URL)
                .header("Content-Type", "application/json")
                .body(requestBody).asObject(BuildApiResponse.class).getBody();
    }
}
