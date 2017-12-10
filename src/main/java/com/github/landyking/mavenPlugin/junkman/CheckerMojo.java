package com.github.landyking.mavenPlugin.junkman;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

import java.io.File;

/**
 * 用来测试插件是否正常。打印出基本属性和关键变量的信息。
 */
@Mojo(name = "checker")
public class CheckerMojo extends AbstractMojo {
    /**
     * 工程根目录
     */
    @Parameter(name="baseDir",defaultValue = "${project.basedir}")
    private File baseDir;
    /**
     * 工程版本号
     */
    @Parameter(name="baseVersion",defaultValue = "${project.version}")
    private String baseVersion;
    /**
     * 工程构建目录
     */
    @Parameter(name="buildDirectory",defaultValue = "${project.build.directory}")
    private String buildDirectory;
    /**
     * 工程构建结果输出目录
     */
    @Parameter(name="buildOutputDirectory",defaultValue = "${project.build.outputDirectory}")
    private String buildOutputDirectory;
    /**
     * 工程最终打包文件名
     */
    @Parameter(name="buildFinalName",defaultValue = "${project.build.finalName}")
    private String buildFinalName;
    /**
     * 工程源码目录
     */
    @Parameter(name="buildSourceDirectory",defaultValue = "${project.build.sourceDirectory}")
    private String buildSourceDirectory;

    public void execute() throws MojoExecutionException {
        getLog().info("baseDir: "+baseDir.getAbsolutePath());
        getLog().info("baseVersion: "+baseVersion);
        getLog().info("buildDirectory: "+buildDirectory);
        getLog().info("buildOutputDirectory: "+buildOutputDirectory);
        getLog().info("buildFinalName: "+buildFinalName);
        getLog().info("buildSourceDirectory: "+buildSourceDirectory);
    }
}