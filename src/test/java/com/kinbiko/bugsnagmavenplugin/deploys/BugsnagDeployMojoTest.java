package com.kinbiko.bugsnagmavenplugin.deploys;

import com.mashape.unirest.http.exceptions.UnirestException;
import org.junit.Test;

import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class BugsnagDeployMojoTest {

    private final TestRequestMaker testRequestMaker = new TestRequestMaker();
    private final BugsnagDeployMojo target = new BugsnagDeployMojo();
    { target.setRequestMaker(testRequestMaker); }

    @Test
    public void propertiesGetsPickedUp() throws Exception {
        target.execute();
        final Map<String, String> params = testRequestMaker.getParams();
        assertTrue(params.containsKey("appVersion"));
        assertTrue(params.containsKey("apiKey"));
        assertTrue(params.containsKey("releaseStage"));
        assertEquals(3, params.size());
    }

}

class TestRequestMaker implements DeployRequestMaker {
    private Map<String, String> params;
    public DeployResponse makeRequest(final Map<String, String> urlParams) throws UnirestException {
        this.params = urlParams;
        return new DeployResponse(200, "OK");
    }
    Map<String, String> getParams() {
        return params;
    }
}