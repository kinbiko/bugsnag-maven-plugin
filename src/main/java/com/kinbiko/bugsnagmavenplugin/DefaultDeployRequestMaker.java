package com.kinbiko.bugsnagmavenplugin;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

import java.util.Map;

/**
 * The default deploy request maker using unirest.
 */
public class DefaultDeployRequestMaker implements DeployRequestMaker {
    public DeployResponse makeRequest(final Map<String, String> urlParams) throws UnirestException {
            return convert(
                    Unirest.post("https://notify.bugsnag.com/deploy")
                            .field("apiKey", urlParams.get("apiKey"))
                            .field("releaseStage", urlParams.get("releaseStage"))
                            .field("appVersion", urlParams.get("appVersion"))
                            .asString());
    }

    public DeployResponse convert(final HttpResponse<String> res) {
        return new DeployResponse(res.getStatus(), res.getStatusText());
    }
}
