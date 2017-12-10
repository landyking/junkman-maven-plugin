package com.github.landyking.mavenPlugin.junkman;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.Mojo;

/**
 * 用来测试插件是否正常。打印出基本属性和关键变量的信息。
 */
@Mojo(name = "checker")
public class CheckerMojo extends JunkmanMojo {

    public void execute() throws MojoExecutionException {
        getLog().info("baseDir: " + getProject().getBasedir().getAbsolutePath());
        getLog().info("baseVersion: " + getProject().getVersion());
        getLog().info("buildDirectory: " + getProject().getBuild().getDirectory());
        getLog().info("buildOutputDirectory: " + getProject().getBuild().getOutputDirectory());
        getLog().info("buildFinalName: " + getProject().getBuild().getFinalName());
        getLog().info("buildSourceDirectory: " + getProject().getBuild().getSourceDirectory());
    }
}