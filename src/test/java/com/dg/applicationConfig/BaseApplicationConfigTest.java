package com.dg.applicationConfig;

import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.HashSet;
import java.util.Properties;
import java.util.Set;

/**
 * @author dima.golomozy
 */
public class BaseApplicationConfigTest
{
    protected ApplicationConfig applicationConfig;
    protected Properties properties;

    @Test
    public void testInteger() throws Throwable {
        int actual = applicationConfig.get(TestConfigConstants.integer1);
        int expected = Integer.parseInt(properties.getProperty(TestConfigConstants.integer1));
        Assert.assertEquals(actual, expected);
    }

    @Test
    public void testFloat() throws Throwable {
        float actual = applicationConfig.get(TestConfigConstants.float1);
        float expected = Float.parseFloat(properties.getProperty(TestConfigConstants.float1));
        Assert.assertEquals(actual, expected);
    }

    @Test
    public void testDouble() throws Throwable {
        double actual = applicationConfig.get(TestConfigConstants.double1);
        double expected = Double.parseDouble(properties.getProperty(TestConfigConstants.double1));
        Assert.assertEquals(actual, expected);
    }

    @Test
    public void testBoolean() throws Throwable {
        boolean actual = applicationConfig.get(TestConfigConstants.boolean1);
        boolean expected = Boolean.parseBoolean(properties.getProperty(TestConfigConstants.boolean1));
        Assert.assertEquals(actual, expected);
    }

    @Test
    public void testLong() throws Throwable {
        long actual = applicationConfig.get(TestConfigConstants.long1);
        long expected = Long.parseLong(properties.getProperty(TestConfigConstants.long1));
        Assert.assertEquals(actual, expected);
    }

    @Test
    public void testShort() throws Throwable {
        short actual = applicationConfig.get(TestConfigConstants.short1);
        short expected = Short.parseShort(properties.getProperty(TestConfigConstants.short1));
        Assert.assertEquals(actual, expected);
    }

    @Test
    public void testString() throws Throwable {
        String actual = applicationConfig.get(TestConfigConstants.string1);
        String expected = properties.getProperty(TestConfigConstants.string1);
        Assert.assertEquals(actual, expected);
    }

    @Test
    public void testEmptyString() throws Throwable {
        String actual = applicationConfig.get(TestConfigConstants.emptyString);
        Assert.assertTrue(actual.isEmpty());
    }

    @Test
    public void testEmptySet() throws Throwable {
        Set actual = applicationConfig.get(TestConfigConstants.emptySet);
        Assert.assertTrue(actual.isEmpty());
    }

    @Test
    public void testSet() throws Throwable {
        Set actual = applicationConfig.get(TestConfigConstants.set);
        Set<Integer> expected = new HashSet<>();
        for (String s : properties.getProperty(TestConfigConstants.set).split(","))
            expected.add(Integer.parseInt(s));
        Assert.assertEquals(actual, expected);
    }

    @Test
    public void testFolder() throws Throwable {
        int actual1 = applicationConfig.get(TestConfigConstants.folder + "int1");
        int expected1 = Integer.parseInt(properties.getProperty(TestConfigConstants.folder + "int1"));
        int actual2 = applicationConfig.get(TestConfigConstants.folder + "int2");
        int expected2 = Integer.parseInt(properties.getProperty(TestConfigConstants.folder + "int2"));

        Assert.assertEquals(actual1, expected1);
        Assert.assertEquals(actual2, expected2);
    }
}
