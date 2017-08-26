package com.kinbiko;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;

import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

import java.io.IOException;

@Mojo(name = "deploy")
public class BugsnagDeployMojo extends AbstractMojo {

    private static final String DEFAULT_RELEASE_STAGE = "production";

    @Parameter(required = true, property = "deploy.apiKey")
    private String apiKey;

    @Parameter(property = "deploy.releaseStage", defaultValue = DEFAULT_RELEASE_STAGE)
    private String releaseStage;

    @Parameter(property = "appVersion", defaultValue = "${project.version}")
    private String appVersion;

    public void execute() throws MojoExecutionException {
        getLog().info("Sending deploy notification to Bugsnag. App version: " + appVersion);
        sendDeployNotification();
        shutdown();
    }

    private void sendDeployNotification() {
        try {
            final HttpResponse<String> res = makeHttpCall();
            if (res.getStatus() > 399)
                getLog().error("Failed to send deploy notification: "
                        + res.getStatusText()
                        + ". Ensure the API key is valid.");
            else
                getLog().info("Deploy notification successful");
        } catch (final UnirestException e) {
            getLog().error("Something went wrong internally with the plugin. Enable debug logging for more information");
            getLog().debug(e);
        }
    }

    private HttpResponse<String> makeHttpCall() throws UnirestException {
        return Unirest.post("https://notify.bugsnag.com/deploy")
                        .field("apiKey", apiKey)
                        .field("releaseStage", releaseStage)
                        .field("appVersion", appVersion)
                        .asString();
    }

    private void shutdown() throws MojoExecutionException {
        try {
            //According to their documentation this call is required after use.
            Unirest.shutdown();
        } catch (IOException e) {
            throw new MojoExecutionException("Unable to shutdown bugsnag-maven-plugin cleanly.");
        }
    }
}
