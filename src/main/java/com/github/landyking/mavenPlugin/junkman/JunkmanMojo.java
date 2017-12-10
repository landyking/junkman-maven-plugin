package com.github.landyking.mavenPlugin.junkman;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugins.annotations.Parameter;

import java.io.File;

/**
 * Created by landy on 2017/12/10.
 */
public abstract class JunkmanMojo extends AbstractMojo {
    @Parameter(name = "rootDir",required = true,defaultValue = "${project.basedir}/docs/system_design/")
    private File rootDir;

    public File getRootDir() {
        return rootDir;
    }
}
