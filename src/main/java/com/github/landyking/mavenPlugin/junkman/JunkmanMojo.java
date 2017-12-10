package com.github.landyking.mavenPlugin.junkman;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugins.annotations.Component;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;

import java.io.File;

/**
 * Created by landy on 2017/12/10.
 */
public abstract class JunkmanMojo extends AbstractMojo {
    public enum DB {
        Oracle,
        Mysql
    }

    /**
     * 数据库类型
     */
    @Parameter(name = "databaseType", required = true,defaultValue = "Mysql")
    private DB databaseType;
    @Parameter(name = "rootDir", required = true, defaultValue = "${project.basedir}/docs/system_design/")
    private File rootDir;
    /**
     * 工程构建结果输出目录
     */
    @Parameter(name = "outputDirectory", required = true, defaultValue = "${project.build.directory}/junkman/")
    private File outputDirectory;
    @Component
    private MavenProject project;

    public File getRootDir() {
        return rootDir;
    }

    public File getOutputDirectory() {
        return outputDirectory;
    }

    public MavenProject getProject() {
        return project;
    }

    public DB getDatabaseType() {
        return databaseType;
    }
}
