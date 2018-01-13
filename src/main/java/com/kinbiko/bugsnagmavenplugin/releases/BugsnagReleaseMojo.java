package com.kinbiko.bugsnagmavenplugin.releases;

import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * Goal responsible for using the Build API, based on the plugin configuration.
 */
@Mojo(name = "release", requiresOnline = true)
public class BugsnagReleaseMojo extends AbstractMojo {

    private static final String ADVICE = "See https://github.com/kinbiko/bugsnag-maven-plugin/blob/master/README.md#usage for more information on how to configure.";

    /**
     * The API key of your Bugsnag project.
     * This value can be found under your Bugsnag project's Settings menu.
     */
    @Parameter(property = "release.apiKey", required = true)
    private String apiKey;

    /**
     * The version of your application.
     * Default value: Same as the maven project version.
     */
    @Parameter(property = "release.appVersion", defaultValue = "${project.version}", required = true)
    private String appVersion;

    /**
     * The release stage to release to.
     * Is ignored if skipReleaseStage is true.
     * Default value: 'production'
     */
    @Parameter(property = "release.releaseStage", defaultValue = "production")
    private String releaseStage;

    /**
     * Default value: Current user's username
     */
    @Parameter(defaultValue = "${user.name}")
    private String builderName;

    /**
     * Any number of any String-String key-value pair.
     */
    @Parameter
    private Map<String, String> metadata;

    /**
     * A nested config of:
     * <pre>
     *     provider: One of 'github', 'github-enterprise', 'bitbucket', 'bitbucket-server', 'gitlab', 'gitlab-onpremise'
     *     repository: The URL of your repository
     *     revision: The long or short SHA-1 commit hash
     * </pre>
     */
    @Parameter
    private Map<String, String> sourceControl;

    /**
     * Whether to automatically associate this build with new any new error events.
     */
    @Parameter(property = "releases.autoAssignRelease")
    private Boolean autoAssignRelease;

    /**
     * Flag for not setting the default value of the releaseStage.
     * Can be used either as an entry in pom.xml or as a command line arugment:
     * <pre>mvn bugsnag:release -Drelease.skipReleaseStage=true</pre>
     * Default value: false
     */
    @Parameter(property = "release.skipReleaseStage", defaultValue = "false")
    private Boolean skipReleaseStage;

    private final BuildApiRequestMaker requestMaker = new DefaultBuildApiRequestMaker();

    @Override
    public void execute() throws MojoExecutionException {
        getLog().info("Sending release notification to Bugsnag. App version: " + appVersion);
        sendReleaseNotification();
        shutdown();
    }

    private void sendReleaseNotification() {
        try {
            validateConfig();
            logResults(requestMaker.makeRequest(makeBuildApiRequest()));
        } catch (final UnirestException e) {
            getLog().error("Something went wrong internally with the plugin. Make sure you are connected to the internet.");
            getLog().debug(e);
        }
    }

    private BuildApiRequest makeBuildApiRequest() {
        final BuildApiRequest req = new BuildApiRequest();
        req.setApiKey(apiKey);
        req.setAppVersion(appVersion);
        if (skipReleaseStage) {
            getLog().info("Not specifying the release stage.");
        } else {
            req.setReleaseStage(releaseStage);
        }
        req.setBuilderName(builderName);
        req.setMetadata(metadata);
        req.setSourceControl(makeSourceControl(sourceControl));
        req.setAutoAssignRelease(autoAssignRelease);
        return req;
    }

    private SourceControl makeSourceControl(final Map<String, String> map) {
        if (!sourceControlPresent()) {
            return null;
        }
        final SourceControl sourceControl = new SourceControl();
        sourceControl.setProvider(map.get("provider"));
        sourceControl.setRepository(map.get("repository"));
        sourceControl.setRevision(map.get("revision"));
        return sourceControl;
    }

    private void validateConfig() {
        if (apiKey == null || "".equals(apiKey)) {
            getLog().error("API key must be set. " + ADVICE);
            invalidate();
        }

        if (sourceControlPresent()) {
            if (sourceControl.get("revision") == null) {
                getLog().error("The revision must be specified whenever source control is specified. " + ADVICE);
                invalidate();
            }

            if (sourceControl.get("repository") == null) {
                getLog().error("The repository must be specified whenever source control is specified. " + ADVICE);
                invalidate();
            }

            if (sourceControl.get("provider") == null) {
                getLog().warn("The provider may not be identifiable from the URL. Suggest configuring provider. " + ADVICE);
            }
        }
    }

    private void logResults(final BuildApiResponse res) {
        if ("ok".equals(res.getStatus())) {
            getLog().info("Successfully reported release to Bugsnag!");
        } else {
            getLog().error("Something went wrong reporting release to Bugsnag.");
            final List<String> errors = res.getErrors();
            if (errors != null) {
                errors.forEach(error -> getLog().error(error));
            } else {
                getLog().error("Unknown failure when reporting release to Bugsnag.");
            }
        }
        final List<String> warnings = res.getWarnings();
        if (warnings != null) {
            warnings.forEach(warning -> getLog().warn(warning));
        }
    }

    private boolean sourceControlPresent() {
        return sourceControl != null && !sourceControl.isEmpty();
    }

    private void invalidate() {
        throw new RuntimeException("Validation error.");
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
