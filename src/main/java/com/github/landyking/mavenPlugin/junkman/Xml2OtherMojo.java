package com.github.landyking.mavenPlugin.junkman;

import com.github.landyking.mavenPlugin.junkman.db.OracleSqlGen;
import com.github.landyking.mavenPlugin.junkman.utils.MyAssert;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

import java.io.File;

/**
 * 将xml文档转换为其他文件。比如sql。
 */
@Mojo(name = "xml2other")
public class Xml2OtherMojo extends JunkmanMojo {

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        if (getDatabaseType() == DB.Oracle) {
            File databaseXmlFile = new File(getRootDir(), GenXmlTemplateMojo.DATABASE_XML);
            MyAssert.notNull(databaseXmlFile);
            if (!getOutputDirectory().exists()) {
                boolean operate = getOutputDirectory().mkdirs();
                MyAssert.isTrue(operate);
            }
            File ddlSqlFile = new File(getOutputDirectory(), "database.sql");
            try {
                new OracleSqlGen().xml2DDL(getLog(), databaseXmlFile, ddlSqlFile);
            } catch (Exception e) {
                throw new MojoFailureException("Xml to ddl failure: " + e.getMessage());
            }
        } else {
            throw new MojoFailureException("Unsupported databaseType: " + getDatabaseType());
        }
        getLog().info("Xml convert to other success!");
    }
}
