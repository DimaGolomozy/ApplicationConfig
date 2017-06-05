package com.dg.applicationConfig;

import com.dg.applicationConfig.exceptions.ApplicationConfigException;
import org.testng.annotations.BeforeClass;

import java.io.InputStream;
import java.util.Properties;

/**
 * @author dima.golomozy
 */
public class FileApplicationConfigTest extends BaseApplicationConfigTest {

    @BeforeClass
    public void before() throws Throwable
    {
        String resourceFileName = "ApplicationProperties.properties";
        try {
            this.applicationConfig = new FileApplicationConfig(resourceFileName, TestConfigConstants.class);
            this.applicationConfig.initialize();
        } catch (ApplicationConfigException e) {
            e.printStackTrace();
            System.exit(0);
        }

        this.properties = new Properties();
        InputStream configStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(resourceFileName);
        this.properties.load(configStream);
        configStream.close();
    }


}
