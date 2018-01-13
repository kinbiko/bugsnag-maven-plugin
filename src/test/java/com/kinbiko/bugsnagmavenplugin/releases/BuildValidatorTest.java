package com.kinbiko.bugsnagmavenplugin.releases;

import org.junit.Test;

import java.util.HashMap;

public class BuildValidatorTest {

    private ReleaseMojo config = new ReleaseMojo();
    private BuildValidator target = new BuildValidator(config);

    @Test(expected = RuntimeException.class)
    public void invalidatesWhenApiKeyIsAbsent() {
        config.sourceControl = new HashMap<String, String>() {{
            put("provider", "github");
            put("repository", "https://github.com/kinbiko/bugsnag-maven-plugin");
            put("revision", "dab212b");
        }};
        config.apiKey = null; //Invalid

        target.validateConfig();
    }

    @Test(expected = RuntimeException.class)
    public void sourceControlPresentButRevisionAbsent() {
        config.apiKey = "api key";
        config.sourceControl = new HashMap<String, String>() {{
            put("provider", "github");
            put("repository", "https://github.com/kinbiko/bugsnag-maven-plugin");
            put("revision", ""); //Invalid
        }};

        target.validateConfig();
    }

    @Test(expected = RuntimeException.class)
    public void sourceControlPresentButRepositoryAbsent() {
        config.apiKey = "api key";
        config.sourceControl = new HashMap<String, String>() {{
            put("provider", "github");
            put("repository", ""); //Invalid
            put("revision", "dab212b");
        }};

        target.validateConfig();
    }

    @Test
    public void happyPath() {
        config.sourceControl = new HashMap<String, String>() {{
            put("provider", "github");
            put("repository", "https://github.com/kinbiko/bugsnag-maven-plugin");
            put("revision", "dab212b");
        }};
        config.apiKey = "api key";

        target.validateConfig();
    }


}
