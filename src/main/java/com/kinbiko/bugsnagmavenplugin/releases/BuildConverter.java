package com.kinbiko.bugsnagmavenplugin.releases;

import org.apache.maven.plugin.logging.Log;

import java.util.Map;

/**
 * Class for converting the {@link ReleaseMojo} configuration to a {@link BuildApiRequest}.
 */
class BuildConverter {

    private final Log logger;
    private final ReleaseMojo config;

    BuildConverter(ReleaseMojo config) {
        this.config = config;
        this.logger = config.getLog();
    }

    BuildApiRequest makeBuildApiRequest() {
        final BuildApiRequest req = new BuildApiRequest();
        req.setApiKey(config.apiKey);
        req.setAppVersion(config.appVersion);
        if (config.skipReleaseStage) {
            logger.info("Not specifying the release stage.");
        } else {
            req.setReleaseStage(config.releaseStage);
        }
        req.setBuilderName(config.builderName);
        req.setMetadata(config.metadata);
        req.setSourceControl(makeSourceControl(config.sourceControl));
        req.setAutoAssignRelease(config.autoAssignRelease);
        return req;
    }

    private SourceControl makeSourceControl(final Map<String, String> map) {
        if (config.sourceControl == null || config.sourceControl.isEmpty()) {
            return null;
        }
        final SourceControl sourceControl = new SourceControl();
        sourceControl.setProvider(map.get("provider"));
        sourceControl.setRepository(map.get("repository"));
        sourceControl.setRevision(map.get("revision"));
        return sourceControl;
    }
}