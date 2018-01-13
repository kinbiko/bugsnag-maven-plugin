package com.kinbiko.bugsnagmavenplugin.releases;

import org.apache.maven.plugin.logging.Log;

/**
 * Class for validation of the properties the user has configured.
 */
class BuildValidator {
    private static final String ADVICE = "See https://github.com/kinbiko/bugsnag-maven-plugin/blob/master/README.md#usage for more information on how to configure this setting.";

    private final Log logger;
    private final ReleaseMojo config;

    BuildValidator(final ReleaseMojo config) {
        this.config = config;
        this.logger = config.getLog();
    }

    void validateConfig() {
        if (config.apiKey == null || "".equals(config.apiKey)) {
            invalidate("API key must be set.");
        }

        if (config.sourceControl != null && !config.sourceControl.isEmpty()) {
            if (isEmpty(config.sourceControl.get("revision"))) {
                invalidate("The revision must be specified whenever source control is specified.");
            }

            if (isEmpty(config.sourceControl.get("repository"))) {
                invalidate("The repository must be specified whenever source control is specified.");
            }

            if (isEmpty(config.sourceControl.get("provider"))) {
                logger.warn("The provider may not be identifiable from the URL. Suggest configuring provider. " + ADVICE);
            }
        }
    }

    private boolean isEmpty(final String val) {
        return null == val || "".equals(val);
    }

    private void invalidate(final String message) {
        logger.error(message + " " + ADVICE);
        throw new RuntimeException("Validation error.");
    }
}
