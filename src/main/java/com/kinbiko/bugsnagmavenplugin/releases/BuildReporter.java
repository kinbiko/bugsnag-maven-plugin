package com.kinbiko.bugsnagmavenplugin.releases;

import org.apache.maven.plugin.logging.Log;

/**
 * Class responsible for reporting the result of a request to the Build API.
 */
class BuildReporter {
    private final Log logger;

    BuildReporter(final Log logger) {
        this.logger = logger;
    }

    void logResponse(final BuildApiResponse res) {
        if ("ok".equals(res.getStatus())) {
            logger.info("Successfully reported release to Bugsnag!");
        } else {
            logger.error("Something went wrong reporting release to Bugsnag.");
            if (res.getErrors() != null) {
                res.getErrors().forEach(logger::error);
            } else {
                logger.error("Unknown failure when reporting release to Bugsnag.");
            }
        }
        if (res.getWarnings() != null) {
            res.getWarnings().forEach(logger::warn);
        }
    }
}
