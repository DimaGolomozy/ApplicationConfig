package com.dgol.applicationConfig;

import com.dgol.applicationConfig.exceptions.ApplicationConfigException;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.Set;

/**
 * Created by dima.golomozy on 08/05/2016.
 */
public class KVCacheApplicationConfigTest
{
    private ApplicationConfig kvCacheApplicationConfig;

    @BeforeClass
    public void before()
    {
        new KVCacheApplicationConfig();
        try {
            this.kvCacheApplicationConfig = new KVCacheApplicationConfig(TestConfigConstants.class);
            this.kvCacheApplicationConfig.initialize();
        } catch (ApplicationConfigException e) {
            e.printStackTrace();
            System.exit(0);
        }
    }

    @Test
    public void testKVCacheApplicationConfig()
    {

        int integer1 = kvCacheApplicationConfig.get(TestConfigConstants.integer1);
        float float1 = kvCacheApplicationConfig.get(TestConfigConstants.float1);
        double double1 = kvCacheApplicationConfig.get(TestConfigConstants.double1);
        boolean boolean1 = kvCacheApplicationConfig.get(TestConfigConstants.boolean1);
        long long1 = kvCacheApplicationConfig.get(TestConfigConstants.long1);
        short short1 = kvCacheApplicationConfig.get(TestConfigConstants.short1);
        String string1 = kvCacheApplicationConfig.get(TestConfigConstants.string1);
        String emptyString = kvCacheApplicationConfig.get(TestConfigConstants.emptyString);
        Set<String> set1 = kvCacheApplicationConfig.get(TestConfigConstants.set);

        int int1 = kvCacheApplicationConfig.get(TestConfigConstants.folder + "int1");
        int int2 = kvCacheApplicationConfig.get(TestConfigConstants.folder + "int2");

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
