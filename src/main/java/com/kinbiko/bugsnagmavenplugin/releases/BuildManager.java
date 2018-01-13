package com.kinbiko.bugsnagmavenplugin.releases;

import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.logging.Log;

import java.io.IOException;

/**
 * Manager class responsible for the higher level orchestration of this plugin.
 */
class BuildManager {

    private final BuildApiRequestMaker requestMaker = new BuildApiRequestMaker();
    private final Log logger;
    private final ReleaseMojo config;
    private final BuildValidator buildValidator;
    private final BuildConverter buildConverter;
    private final BuildReporter buildReporter;

    BuildManager(final ReleaseMojo config) {
        this.logger = config.getLog();
        this.config = config;
        this.buildValidator = new BuildValidator(config);
        this.buildConverter = new BuildConverter(config);
        this.buildReporter = new BuildReporter(logger);
    }

    void orchestrateRelease() throws MojoExecutionException {
        logger.info("Sending release notification to Bugsnag for app version: " + config.appVersion);
        try {
            buildValidator.validateConfig();
            final BuildApiRequest req = buildConverter.makeBuildApiRequest();
            final BuildApiResponse res = requestMaker.makeRequest(req);
            buildReporter.logResponse(res);
        } catch (final UnirestException e) {
            logger.error("Something went wrong when sending release information to Bugsnag. Make sure you are connected to the internet.");
            logger.debug(e);
        }
        shutdown();
    }

    private void shutdown() throws MojoExecutionException {
        try {
            Unirest.shutdown(); //According to the unirest documentation this call is required after use.
        } catch (final IOException e) {
            throw new MojoExecutionException("Unable to shutdown bugsnag-maven-plugin cleanly.");
        }
    }
}
