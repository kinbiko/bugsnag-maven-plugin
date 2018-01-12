package com.kinbiko.bugsnagmavenplugin.releases;

import com.mashape.unirest.http.exceptions.UnirestException;

/**
 * Interface abstracting HTTP requests to the Build API.
 */
interface BuildApiRequestMaker {
    /**
     * Make the request to Bugsnag's build API with the given url params.
     * @param urlParams
     */
    BuildApiResponse makeRequest(final BuildApiRequest urlParams) throws UnirestException;
}