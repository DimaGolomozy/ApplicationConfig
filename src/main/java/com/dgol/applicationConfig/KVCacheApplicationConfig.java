package com.dgol.applicationConfig;

import com.dgol.applicationConfig.exceptions.ConfigInitializeException;
import com.orbitz.consul.Consul;
import com.orbitz.consul.cache.ConsulCache;
import com.orbitz.consul.cache.KVCache;
import com.orbitz.consul.model.kv.Value;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Created by dima.golomozy on 04/05/2016.
 */
public class KVCacheApplicationConfig extends ApplicationConfig
{
    private final Logger logger = LoggerFactory.getLogger(KVCacheApplicationConfig.class);
    private final KVCache kvCache;

    public KVCacheApplicationConfig(KVCache kvCache, Class... propertiesClass)
    {
        super(propertiesClass);
        this.kvCache = initKVCache(kvCache);
    }

    public KVCacheApplicationConfig(Class... propertiesClass)
    {
        this(System.getProperty("propertiesConsulPath"),
                Integer.parseInt(System.getProperty("delayTimeInterval")),
                propertiesClass
        );
    }

    public KVCacheApplicationConfig(String propertiesConsulPath, int delayTimeInterval, Class... propertiesClass)
    {
        super(propertiesClass);
        Consul consul = Consul.builder().build();
//                    .withReadTimeoutMillis(10000 + delayTimeInterval * 1000)    // timeout is the delayTimeInterval + 10 sec
//                    .build();
        this.kvCache = initKVCache(KVCache.newCache(consul.keyValueClient(), propertiesConsulPath, delayTimeInterval));
    }

    private KVCache initKVCache(KVCache kvCache)
    {
        kvCache.addListener(new ConsulCache.Listener<String, Value>()
        {
            @Override
            public void notify(Map<String, Value> newValues)
            {
                if (isInitialized)
                {
                    if (!updateProperty2ObjectsMap(properties2Update))
                        logger.error("Fails in KVCacheApplicationConfig update. See log for details");

                    for (ApplicationConfigListener l : applicationConfigListeners)
                        l.notify(property2Object);

                    HashMap<String, Object> printedMap = new HashMap<>();
                    for (String property : properties2Update)
                    {
                        if (isFolder(property))
                            for (String key : getFolderKeysFromMapByProperty(property))
                                printedMap.put(property, property2Object.get(key));
                        else
                            printedMap.put(property, property2Object.get(property));
                    }
                    logger.info("Updated properties: {}", printedMap);
                }
            }
        });
        return kvCache;
    }

    public void stop()
    {
        try {
            kvCache.stop();
        } catch (Exception e) {
            logger.error("KVCache could not stop", e);
        }
    }

    @Override
    public String toString()
    {
        final StringBuilder sb = new StringBuilder("KVCacheApplicationConfig{");
        sb.append("property2Object=").append(property2Object);
        sb.append(", properties2Update=").append(properties2Update);
        sb.append('}');
        return sb.toString();
    }

    @Override
    protected final String getStringValueFromMapByProperty(String property)
    {
        Value value = kvCache.getMap().get(property);
        if (value == null)
            return null;

        String valueAsString = value.getValueAsString().orNull();
        if ("EMPTY_STRING".equalsIgnoreCase(valueAsString))
            return "";

        return valueAsString;
    }

    @Override
    protected List<String> getFolderKeysFromMapByProperty(String property)
    {
        List<String> folderValues = new LinkedList<>();
        for (String s : kvCache.getMap().keySet())
            if (s.startsWith(property))
                folderValues.add(s);
        return folderValues;
    }

    @Override
    public boolean initialize() throws ConfigInitializeException
    {
        if (isInitialized)
        {
            logger.warn("KVCacheApplicationConfig is already initialized");
            return true;
        }

        try {
            this.kvCache.start();
        } catch (Exception ex) {
            throw new ConfigInitializeException("Could not initialize KVCache", ex);
        }

        try {
            if (!kvCache.awaitInitialized(10, TimeUnit.SECONDS))    // wait for KVCache first response
                throw new ConfigInitializeException("Could not initialize KVCache within the given timeout");
        } catch (InterruptedException ie) {
            throw new ConfigInitializeException("Interrupted while initializing KVCache", ie);
        }

        if (!updateProperty2ObjectsMap(property2Converters.keySet()))
        {
            this.stop();
            logger.error("Failed to initialize KVCacheApplicationConfig. See log for details");
            throw new ConfigInitializeException("Failed to initialize KVCacheApplicationConfig. See log for details");
        }

        // in case of empty properties2Update, we don't need the kvCache
        if (properties2Update.isEmpty())
        {
            logger.warn("Stopping KVCache since there is no properties to update");
            this.stop();
        }

        return isInitialized = true;
    }
}
