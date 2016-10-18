package com.dgol.applicationConfig;

import com.dgol.applicationConfig.exceptions.ConfigInitializeException;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.Set;

/**
 * Created by dima.golomozy on 08/05/2016.
 */
public class FileApplicationConfigTest
{
    private ApplicationConfig fileApplicationConfig;

    @BeforeClass
    public void before()
    {
        try {
            this.fileApplicationConfig = new FileApplicationConfig("FileApplictionProperties.properties", TestConfigConstants.class);
            this.fileApplicationConfig.initialize();
        } catch (ConfigInitializeException e) {
            e.printStackTrace();
            System.exit(0);
        }
    }

    @Test
    public void testFileApplicationConfig()
    {
        int integer1 = fileApplicationConfig.get(TestConfigConstants.integer1);
        float float1 = fileApplicationConfig.get(TestConfigConstants.float1);
        double double1 = fileApplicationConfig.get(TestConfigConstants.double1);
        boolean boolean1 = fileApplicationConfig.get(TestConfigConstants.boolean1);
        long long1 = fileApplicationConfig.get(TestConfigConstants.long1);
        short short1 = fileApplicationConfig.get(TestConfigConstants.short1);
        String string1 = fileApplicationConfig.get(TestConfigConstants.string1);
        String emptyString = fileApplicationConfig.get(TestConfigConstants.emptyString);
        Set set1 = fileApplicationConfig.get(TestConfigConstants.set);

        int int1 = fileApplicationConfig.get(TestConfigConstants.folder + "int1");
        int int2 = fileApplicationConfig.get(TestConfigConstants.folder + "int2");

        System.out.println(integer1);
        System.out.println(float1);
        System.out.println(double1);
        System.out.println(boolean1);
        System.out.println(long1);
        System.out.println(short1);

        System.out.println(set1);
        Assert.assertFalse(set1.isEmpty());

        System.out.println(string1);
        Assert.assertFalse(string1.isEmpty());

        System.out.println(emptyString);
        Assert.assertEquals(emptyString, "");

        System.out.println(int1);
        System.out.println(int2);
    }
}
