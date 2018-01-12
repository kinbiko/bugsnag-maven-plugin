package com.kinbiko.bugsnagmavenplugin.releases;

import java.util.List;

/**
 * Representing the result of a call to the Build API.
 */
class BuildApiResponse {
    private String status;
    private List<String> errors;
    private List<String> warnings;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<String> getErrors() {
        return errors;
    }

    public void setErrors(List<String> errors) {
        this.errors = errors;
    }

    public List<String> getWarnings() {
        return warnings;
    }

    public void setWarnings(List<String> warnings) {
        this.warnings = warnings;
    }
}