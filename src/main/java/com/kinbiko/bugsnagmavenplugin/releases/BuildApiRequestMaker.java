package com.kinbiko.bugsnagmavenplugin.releases;

import com.mashape.unirest.http.exceptions.UnirestException;

import java.util.Map;

/**
 * Interface abstracting HTTP requests to the Build API.
 */
interface BuildApiRequestMaker {
    /**
     * Make the request to Bugsnag's build API with the given url params.
     */
    BuildApiResponse makeRequest(final Map<String, Object> urlParams) throws UnirestException;
}