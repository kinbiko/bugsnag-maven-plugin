package com.kinbiko.bugsnagmavenplugin.releases;

import java.util.Map;

/**
 * Representing the request payload of a call to the Build API.
 */
class BuildApiRequest {

    private String apiKey;
    private String appVersion;
    private String builderName;
    private Map<String, String> metadata;
    private SourceControl sourceControl;
    private String releaseStage;
    private Boolean autoAssignRelease;

    public String getBuildTool() {
        return "bugsnag-maven-plugin";
    }

    public String getApiKey() {
        return apiKey;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    public String getAppVersion() {
        return appVersion;
    }

    public void setAppVersion(String appVersion) {
        this.appVersion = appVersion;
    }

    public String getBuilderName() {
        return builderName;
    }

    public void setBuilderName(String builderName) {
        this.builderName = builderName;
    }

    public Map<String, String> getMetadata() {
        return metadata;
    }

    public void setMetadata(Map<String, String> metadata) {
        this.metadata = metadata;
    }

    public SourceControl getSourceControl() {
        return sourceControl;
    }

    public void setSourceControl(SourceControl sourceControl) {
        this.sourceControl = sourceControl;
    }

    public String getReleaseStage() {
        return releaseStage;
    }

    public void setReleaseStage(String releaseStage) {
        this.releaseStage = releaseStage;
    }

    public Boolean getAutoAssignRelease() {
        return autoAssignRelease;
    }

    public void setAutoAssignRelease(Boolean autoAssignRelease) {
        this.autoAssignRelease = autoAssignRelease;
    }
}