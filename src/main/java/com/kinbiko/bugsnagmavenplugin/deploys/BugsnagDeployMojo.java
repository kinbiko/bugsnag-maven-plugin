package com.kinbiko.bugsnagmavenplugin.deploys;

import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;

import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Goal responsible for sending deploy notifications to Bugsnag,
 * based on the plugin configuration.
 */
@Mojo(name = "deploy")
public class BugsnagDeployMojo extends AbstractMojo {

    /**
     * This value can be found under your Bugsnag project's Settings menu.
     */
    @Parameter(property = "deploy.apiKey", required = true)
    private String apiKey;

    /**
     * Default value: 'production'
     */
    @Parameter(property = "deploy.releaseStage", defaultValue = "production")
    private String releaseStage;

    /**
     * Default value: Same as the maven project version.
     */
    @Parameter(property = "deploy.appVersion", defaultValue = "${project.version}")
    private String appVersion;

    private DeployRequestMaker requestMaker = new DefaultDeployRequestMaker();;

    /**
     * If the API key has been properly configured, this method will send a deploy notification
     * to Bugsnag, otherwise an error message will be displayed.
     */
    public void execute() throws MojoExecutionException {
        getLog().info("Sending deploy notification to Bugsnag. App version: " + appVersion);
        sendDeployNotification();
        shutdown();
    }

    private void sendDeployNotification() {
        try {
            final Map<String, String> params = new HashMap<String, String>() {{
                put("apiKey", apiKey);
                put("releaseStage", releaseStage);
                put("appVersion", appVersion);
            }};
            final DeployResponse res = requestMaker.makeRequest(params);
            logResults(res);
        } catch (final UnirestException e) {
            getLog().error("Something went wrong internally with the plugin. Enable debug logging for more information");
            getLog().debug(e);
        }
    }

    private void logResults(final DeployResponse res) {
        if (res.getStatusCode() > 400)
            getLog().error("Failed to send deploy notification: " + res.getStatusMessage());
        else if (res.getStatusCode() == 400)
            getLog().error("Invalid configuration. Ensure the API key is valid.");
        else
            getLog().info("Deploy notification successful");
    }

    private void shutdown() throws MojoExecutionException {
        try {
            //According to their documentation this call is required after use.
            Unirest.shutdown();
        } catch (IOException e) {
            throw new MojoExecutionException("Unable to shutdown bugsnag-maven-plugin cleanly.");
        }
    }

    /**
     * For testing purposes only.
     */
    public void setRequestMaker(final DeployRequestMaker requestMaker) {
        this.requestMaker = requestMaker;
    }
}
