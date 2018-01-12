package com.kinbiko.bugsnagmavenplugin.releases;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

import java.util.Map;

import static com.kinbiko.bugsnagmavenplugin.releases.JSONConverter.makeBody;

/**
 * The default build API request maker using unirest.
 */
public class DefaultBuildApiRequestMaker implements BuildApiRequestMaker {

    @Override
    public BuildApiResponse makeRequest(final Map<String, Object> requestBody) throws UnirestException {
        final HttpResponse<JsonNode> res = Unirest.post("http://localhost:3000/")
                .header("Content-Type", "application/json")
                .body(makeBody(requestBody)).asJson();
        return convert(res);
    }

    private BuildApiResponse convert(HttpResponse<JsonNode> res) {
        final BuildApiResponse buildResponse = new BuildApiResponse();
        buildResponse.setStatusCode(res.getStatus());
        final String status = (String) res.getBody().getObject().get("status");
        buildResponse.setStatusMessage(status);
        return buildResponse;
    }

}
