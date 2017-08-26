package com.kinbiko;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;

import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

import java.io.IOException;

/**
 * Mojo responsible for sending deploy notifications to Bugsnag,
 * based on the plugin configuration.
 */
@Mojo(name = "deploy")
public class BugsnagDeployMojo extends AbstractMojo {

    @Parameter(property = "deploy.apiKey", required = true)
    private String apiKey;

    @Parameter(property = "deploy.releaseStage", defaultValue = "production")
    private String releaseStage;

    @Parameter(property = "deploy.appVersion", defaultValue = "${project.version}")
    private String appVersion;

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
            final HttpResponse<String> res = makeHttpCall();
            logResults(res);
        } catch (final UnirestException e) {
            getLog().error("Something went wrong internally with the plugin. Enable debug logging for more information");
            getLog().debug(e);
        }
    }

    private void logResults(final HttpResponse<String> res) {
        if (res.getStatus() > 400)
            getLog().error("Failed to send deploy notification: " + res.getStatusText());
        else if (res.getStatus() == 400)
            getLog().error("Invalid configuration. Ensure the API key is valid.");
        else
            getLog().info("Deploy notification successful");
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
