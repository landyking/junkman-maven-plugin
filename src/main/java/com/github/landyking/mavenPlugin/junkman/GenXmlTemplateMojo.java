package com.github.landyking.mavenPlugin.junkman;

import com.github.landyking.mavenPlugin.junkman.utils.MyAssert;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Mojo;
import org.codehaus.plexus.util.FileUtils;
import org.codehaus.plexus.util.io.RawInputStreamFacade;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by landy on 2017/12/10.
 */
@Mojo(name = "genXmlTemplate")
public class GenXmlTemplateMojo extends JunkmanMojo {

    public static final String DATABASE_XML = "database.xml";
    public static final String DICT_XML = "dict.xml";

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        getLog().info("rootDir: " + getRootDir().getAbsolutePath());
        if (!getRootDir().exists()) {
            boolean rst = getRootDir().mkdirs();
            MyAssert.isTrue(rst);
        }
        File databaseXmlFile = new File(getRootDir(), DATABASE_XML);
        if (!databaseXmlFile.exists()) {
            getLog().info(DATABASE_XML + " not exist, will generate!");
            InputStream templateRes = getClass().getResourceAsStream("/xmlTemplate/v001/database.xml");
            MyAssert.notNull(templateRes);
            try {
                FileUtils.copyStreamToFile(new RawInputStreamFacade(templateRes), databaseXmlFile);
            } catch (IOException e) {
                throw new MojoFailureException("generate template file: " + DATABASE_XML + " failure: " + e.getMessage());
            }
        } else {
            getLog().info(DATABASE_XML + " is exist, will skip!");
        }

        File dictXmlFile = new File(getRootDir(), DICT_XML);
        if (!dictXmlFile.exists()) {
            getLog().info(DICT_XML + " not exist, will generate!");
            InputStream templateRes = getClass().getResourceAsStream("/xmlTemplate/v001/dict.xml");
            MyAssert.notNull(templateRes);
            try {
                FileUtils.copyStreamToFile(new RawInputStreamFacade(templateRes), dictXmlFile);
            } catch (IOException e) {
                throw new MojoFailureException("generate template file: " + DICT_XML + " failure: " + e.getMessage());
            }
        } else {
            getLog().info(DICT_XML + " is exist, will skip!");
        }

        getLog().info("Generate template success!");
    }
}
