package com.dgol.applicationConfig;

import com.dgol.applicationConfig.exceptions.ConfigInitializeException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;

/**
 * Created by dima.golomozy on 10/05/2016.
 */
public class FileApplicationConfig extends AbstractApplicationConfig
{
    private final Logger logger = LoggerFactory.getLogger(FileApplicationConfig.class);
    private final String filePath;
    private final Properties properties;

    public FileApplicationConfig(Class... propertiesClasses)
    {
        this(System.getProperty("propertiesFilePath"), propertiesClasses);
    }

    public FileApplicationConfig(String filePath, Class... propertiesClasses)
    {
        super(propertiesClasses);
        this.properties = new Properties();
        this.filePath = filePath;
    }

    @Override
    protected String getStringValueFromMapByProperty(String property)
    {
        return this.properties.getProperty(property);
    }

    @Override
    protected List<String> getFolderKeysFromMapByProperty(String property)
    {
        List<String> folderValues = new LinkedList<>();
        for (Object o : this.properties.keySet())
            if (((String)o).startsWith(property))
                folderValues.add((String) o);
        return folderValues;
    }

    @Override
    public boolean initialize() throws ConfigInitializeException
    {
        if (isInitialized)
        {
            logger.warn("FileApplicationConfig is already initialized");
            return true;
        }

        InputStream configStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(filePath);
        if (configStream == null)
            throw new ConfigInitializeException("Could not find resource " + filePath + " in the classpath");

        try {
            properties.load(configStream);
            configStream.close();
        } catch (IOException e) {
            throw new ConfigInitializeException("Could not load properties file " + filePath + " from classpath.", e);
        }

        if (!updateProperty2ObjectsMap(property2Converters.keySet()))
            throw new ConfigInitializeException("Failed to initialize FileApplicationConfig. See log file for details");

        return isInitialized = true;
    }

    @Override
    public boolean addListener(ApplicationConfigListener applicationConfigListener)
    {
        logger.warn("Listeners are not implemented in FileApplicationConfig");
        return true;
    }

    @Override
    public boolean removeListener(ApplicationConfigListener applicationConfigListener)
    {
        logger.warn("Listeners are not implemented in FileApplicationConfig");
        return true;
    }
}