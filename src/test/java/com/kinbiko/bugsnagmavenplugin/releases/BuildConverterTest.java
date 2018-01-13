package com.kinbiko.bugsnagmavenplugin.releases;

import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

public class BuildConverterTest {

    private ReleaseMojo config = new ReleaseMojo();
    private BuildConverter target = new BuildConverter(config);

    @Before
    public void setUp() {
        config.skipReleaseStage = false; //mimic the default value
    }

    @Test
    public void configuresBasics() {
        config.apiKey = "api key";
        config.appVersion = "app version";
        config.autoAssignRelease = true;
        config.builderName = "bob";
        Map<String, String> hashMap = new HashMap<>();
        config.metadata = hashMap;

        final BuildApiRequest req = target.makeBuildApiRequest();

        assertEquals("api key", req.getApiKey());
        assertEquals("app version", req.getAppVersion());
        assertTrue(req.getAutoAssignRelease());
        assertEquals("bob", req.getBuilderName());
        assertEquals(hashMap, req.getMetadata());
        assertNull(req.getSourceControl());
    }

    @Test
    public void hasBuildToolConfigured() {
        final BuildApiRequest req = target.makeBuildApiRequest();
        assertEquals("bugsnag-maven-plugin", req.getBuildTool());
    }

    @Test
    public void skipsReleaseStageIfSoConfigured() {
        config.releaseStage = "I am actually set";
        config.skipReleaseStage = true;
        final BuildApiRequest req = target.makeBuildApiRequest();
        assertNull(req.getReleaseStage());
    }

    @Test
    public void correctlySetsSourceControl() {
        config.sourceControl = new HashMap<String, String>() {{
            put("provider", "github");
            put("repository", "https://github.com/kinbiko/bugsnag-maven-plugin");
            put("revision", "dab236d");
        }};

        final BuildApiRequest req = target.makeBuildApiRequest();

        assertEquals("github", req.getSourceControl().getProvider());
        assertEquals("https://github.com/kinbiko/bugsnag-maven-plugin", req.getSourceControl().getRepository());
        assertEquals("dab236d", req.getSourceControl().getRevision());
    }
}