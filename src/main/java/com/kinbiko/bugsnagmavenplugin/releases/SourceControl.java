package com.kinbiko.bugsnagmavenplugin.releases;

/**
 * POJO representing the source control configuration in a request payload
 * to the build API.
 */
public class SourceControl {
    private String provider;
    private String repository;
    private String revision;

    public String getProvider() {
        return provider;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }

    public String getRepository() {
        return repository;
    }

    public void setRepository(String repository) {
        this.repository = repository;
    }

    public String getRevision() {
        return revision;
    }

    public void setRevision(String revision) {
        this.revision = revision;
    }
}
