package com.kinbiko.bugsnagmavenplugin.releases;

import org.apache.maven.plugin.logging.Log;

/**
 * Class for validation of the properties the user has configured.
 */
class BuildValidator {
    private static final String ADVICE = "See https://github.com/kinbiko/bugsnag-maven-plugin/blob/master/README.md#usage for more information on how to configure.";

    private final Log logger;
    private final ReleaseMojo config;

    BuildValidator(final Log logger, final ReleaseMojo config) {
        this.logger = logger;
        this.config = config;
    }

    void validateConfig() {
        if (config.apiKey == null || "".equals(config.apiKey)) {
            logger.error("API key must be set. " + ADVICE);
            invalidate();
        }

        if (config.sourceControl != null && !config.sourceControl.isEmpty()) {
            if (config.sourceControl.get("revision") == null) {
                logger.error("The revision must be specified whenever source control is specified. " + ADVICE);
                invalidate();
            }

            if (config.sourceControl.get("repository") == null) {
                logger.error("The repository must be specified whenever source control is specified. " + ADVICE);
                invalidate();
            }

            if (config.sourceControl.get("provider") == null) {
                logger.warn("The provider may not be identifiable from the URL. Suggest configuring provider. " + ADVICE);
            }
        }
    }

    private void invalidate() {
        throw new RuntimeException("Validation error.");
    }
}
