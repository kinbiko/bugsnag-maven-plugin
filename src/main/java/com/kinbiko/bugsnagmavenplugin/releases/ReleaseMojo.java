package com.kinbiko.bugsnagmavenplugin.releases;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

import java.util.Map;

/**
 * Goal responsible for using the Build API, based on the plugin configuration.
 */
@Mojo(name = "release", requiresOnline = true)
public class ReleaseMojo extends AbstractMojo {

    /**
     * The API key of your Bugsnag project.
     * This value can be found under your Bugsnag project's Settings menu.
     */
    @Parameter(property = "release.apiKey", required = true)
    String apiKey;

    /**
     * The version of your application.
     * Default value: Same as the maven project version.
     */
    @Parameter(property = "release.appVersion", defaultValue = "${project.version}", required = true)
    String appVersion;

    /**
     * The release stage to release to.
     * Is ignored if skipReleaseStage is true.
     * Default value: 'production'
     */
    @Parameter(property = "release.releaseStage", defaultValue = "production")
    String releaseStage;

    /**
     * Default value: Current user's username
     */
    @Parameter(defaultValue = "${user.name}")
    String builderName;

    /**
     * Any number of any String-String key-value pair.
     */
    @Parameter
    Map<String, String> metadata;

    /**
     * A nested config of:
     * <pre>
     *     provider: One of 'github', 'github-enterprise', 'bitbucket', 'bitbucket-server', 'gitlab', 'gitlab-onpremise'
     *     repository: The URL of your repository
     *     revision: The long or short SHA-1 commit hash
     * </pre>
     */
    @Parameter
    Map<String, String> sourceControl;

    /**
     * Whether to automatically associate this build with new any new error events.
     */
    @Parameter(property = "releases.autoAssignRelease")
    Boolean autoAssignRelease;

    /**
     * Flag for not setting the default value of the releaseStage.
     * Can be used either as an entry in pom.xml or as a command line arugment:
     * <pre>mvn bugsnag:release -Drelease.skipReleaseStage=true</pre>
     * Default value: false
     */
    @Parameter(property = "release.skipReleaseStage", defaultValue = "false")
    Boolean skipReleaseStage;

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        new BuildManager(getLog(), this).orchestrateRelease();
    }
}
