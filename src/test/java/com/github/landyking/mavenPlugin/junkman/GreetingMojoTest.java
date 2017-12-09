package com.github.landyking.mavenPlugin.junkman;

import org.apache.maven.model.Resource;
import org.apache.maven.plugin.testing.MojoRule;
import org.apache.maven.plugin.testing.resources.TestResources;
import org.apache.maven.project.MavenProject;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;

import java.io.File;
import java.util.List;

/**
 * Created by landy on 2017/12/9.
 */
public class GreetingMojoTest {
    @Rule
    public MojoRule rule = new MojoRule();

    @Rule
    public TestResources resources = new TestResources();


    @Test
    public void checkSourcesDirectoriesAreUpdated() throws Exception {

        // Find the project
        File baseDir = this.resources.getBasedir( "project-invalid" );
        Assert.assertNotNull( baseDir );
        Assert.assertTrue( baseDir.exists());
        Assert.assertTrue( baseDir.isDirectory());

        File pom = new File( baseDir, "pom.xml" );
        Assert.assertTrue( pom.exists());
        GreetingMojo mojo = (GreetingMojo) this.rule.lookupMojo( "sayhi", pom );
        Assert.assertNotNull( mojo );

        // Create the Maven project by hand (...)
//        final MavenProject mvnProject = new MavenProject() ;
//        mvnProject.setFile( pom ) ;
//        this.rule.setVariableValueToO、bject( mojo, "project", mvnProject );
//        Assert.assertNotNull( this.rule.getVariableValueFromObject( mojo, "project" ));


        mojo.execute();


    }

}