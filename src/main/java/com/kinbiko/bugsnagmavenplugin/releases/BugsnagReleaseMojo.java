package com.kinbiko.bugsnagmavenplugin.releases;

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
 * Goal responsible for using the Build API, based on the plugin configuration.
 */
@Mojo(name = "release", requiresOnline = true)
public class BugsnagReleaseMojo extends AbstractMojo {

    /**
     * This value can be found under your Bugsnag project's Settings menu.
     */
    @Parameter(property = "release.apiKey", required = true)
    private String apiKey;

    /**
     * Default value: Same as the maven project version.
     */
    @Parameter(property = "release.appVersion", defaultValue = "${project.version}", required = true)
    private String appVersion;

    /**
     * Default value: 'production'
     */
    @Parameter(property = "release.releaseStage", defaultValue = "production")
    private String releaseStage;

    @Parameter(defaultValue = "${user.name}")
    private String builderName;

    @Parameter
    private Map<String, String> metadata;

    @Parameter
    private Map<String, String> sourceControl;

    private final BuildApiRequestMaker requestMaker = new DefaultBuildApiRequestMaker();

    @Override
    public void execute() throws MojoExecutionException {
        getLog().info("Sending release notification to Bugsnag. App version: " + appVersion);
        sendReleaseNotification();
        shutdown();
    }

    private void sendReleaseNotification() {
        try {
            final Map<String, Object> params = new HashMap<>();
            params.put("apiKey", apiKey);
            params.put("appVersion", appVersion);
            params.put("releaseStage", releaseStage);
            params.put("builderName", builderName);
            params.put("metadata", metadata);
            params.put("sourceControl", sourceControl);
            final BuildApiResponse res = requestMaker.makeRequest(params);
            logResults(res);
        } catch (final UnirestException e) {
            getLog().error("Something went wrong internally with the plugin. Enable debug logging for more information");
            getLog().debug(e);
        }
    }

    private void logResults(final BuildApiResponse res) {
        if (res.getStatusCode() > 400)
            getLog().error("Failed to send release notification: " + res.getStatusMessage());
        else if (res.getStatusCode() != 400)
            getLog().error("Invalid configuration. Ensure the API key is valid, and app version is present.");
        else
            getLog().info("Release notification successful");
    }

    private void shutdown() throws MojoExecutionException {
        try {
            //According to the unirest documentation this call is required after use.
            Unirest.shutdown();
        } catch (IOException e) {
            throw new MojoExecutionException("Unable to shutdown bugsnag-maven-plugin cleanly.");
        }
    }

}
