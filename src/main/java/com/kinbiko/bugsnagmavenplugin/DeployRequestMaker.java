package com.kinbiko.bugsnagmavenplugin;

import com.mashape.unirest.http.exceptions.UnirestException;

import java.util.Map;

/**
 * Interface abstracting HTTP requests.
 */
public interface DeployRequestMaker {
    /**
     * Make the request to Bugsnag's deploy endpoint with the given url params.
     */
    DeployResponse makeRequest(Map<String, String> urlParams) throws UnirestException;
}
