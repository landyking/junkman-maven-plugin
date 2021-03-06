package com.github.landyking.mavenPlugin.junkman;

import junit.framework.TestCase;
import org.apache.maven.execution.DefaultMavenExecutionRequest;
import org.apache.maven.execution.MavenExecutionRequest;
import org.apache.maven.plugin.testing.MojoRule;
import org.apache.maven.plugin.testing.resources.TestResources;
import org.apache.maven.project.MavenProject;
import org.apache.maven.project.ProjectBuilder;
import org.apache.maven.project.ProjectBuildingRequest;
import org.eclipse.aether.DefaultRepositorySystemSession;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;

import java.io.File;

/**
 * Created by landy on 2017/12/10.
 */
public class GenXmlTemplateMojoTest{
    @Rule
    public MojoRule rule = new MojoRule();

    @Rule
    public TestResources resources = new TestResources();


    @Test
    public void testExecute() throws Exception {

        // Find the project
        File baseDir = this.resources.getBasedir( "project-test" );
        Assert.assertNotNull( baseDir );
        Assert.assertTrue( baseDir.exists());
        Assert.assertTrue( baseDir.isDirectory());

        File pom = new File( baseDir, "pom.xml" );
        Assert.assertTrue( pom.exists());


        MavenExecutionRequest executionRequest = new DefaultMavenExecutionRequest();
        ProjectBuildingRequest configuration = executionRequest.getProjectBuildingRequest()
                .setRepositorySession(new DefaultRepositorySystemSession());
        MavenProject project = rule.lookup(ProjectBuilder.class).build(pom, configuration).getProject();
        System.out.println(project.getBasedir().getAbsolutePath());
        GenXmlTemplateMojo mojo = (GenXmlTemplateMojo) rule.lookupConfiguredMojo(project, "genXmlTemplate");
        Assert.assertNotNull(mojo);
        mojo.execute();

    }
}