package com.kinbiko.bugsnagmavenplugin;

/**
 * Bugsnag's deploy HTTP response.
 */
public class DeployResponse {
    private int statusCode;
    private String statusMessage;

    public DeployResponse(int statusCode, String statusMessage) {
        this.statusCode = statusCode;
        this.statusMessage = statusMessage;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public String getStatusMessage() {
        return statusMessage;
    }
}
