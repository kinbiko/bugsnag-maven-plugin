package com.kinbiko;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;

import org.apache.maven.plugins.annotations.Mojo;

@Mojo( name = "deploy" )
public class BugsnagDeployMojo extends AbstractMojo {
    public void execute() throws MojoExecutionException {
        getLog().info("Hello, world!");
    }
}
