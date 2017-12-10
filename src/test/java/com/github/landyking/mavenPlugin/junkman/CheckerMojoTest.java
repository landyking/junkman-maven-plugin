package com.github.landyking.mavenPlugin.junkman;

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
 * 必须执行过package之后才能执行该测试用例。不然MojoDescriptor未生成
 * Created by landy on 2017/12/9.
 */
public class CheckerMojoTest {
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


        MavenExecutionRequest executionRequest = new DefaultMavenExecutionRequest();
        ProjectBuildingRequest configuration = executionRequest.getProjectBuildingRequest()
                .setRepositorySession(new DefaultRepositorySystemSession());
        MavenProject project = rule.lookup(ProjectBuilder.class).build(pom, configuration).getProject();
        System.out.println(project.getBasedir().getAbsolutePath());
        CheckerMojo mojo = (CheckerMojo) rule.lookupConfiguredMojo(project, "checker");
        Assert.assertNotNull(mojo);
        mojo.execute();

    }

}