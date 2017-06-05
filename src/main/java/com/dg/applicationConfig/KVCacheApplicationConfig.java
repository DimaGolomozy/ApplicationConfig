package com.dg.applicationConfig;

import com.dg.applicationConfig.annotation.CollectionProperty;
import com.dg.applicationConfig.annotation.Property;
import com.dg.applicationConfig.exceptions.ConfigInitializeException;
import com.orbitz.consul.Consul;
import com.orbitz.consul.cache.ConsulCache;
import com.orbitz.consul.cache.KVCache;
import com.orbitz.consul.model.kv.Value;
import com.dg.applicationConfig.exceptions.ApplicationConfigException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * ApplicationConfig that uses {@link KVCache} as the properties provider.
 *
 * @author dima.golomozy
 */
public class KVCacheApplicationConfig extends ApplicationConfig
{
    private final Logger logger = LoggerFactory.getLogger(KVCacheApplicationConfig.class);
    private final KVCache kvCache;

    /**
     * Constructor for KVCacheApplicationConfig
     * @param kvCache - the {@link KVCache} object
     * @param propertiesClasses classes that has {@link Property} or
     *                        {@link CollectionProperty} annotations
     */
    public KVCacheApplicationConfig(KVCache kvCache, Class... propertiesClasses)
    {
        super(propertiesClasses);
        this.kvCache = initKVCache(kvCache);
    }

    /**
     * This constructor construct default {@link KVCache}
     * using arguments from the system properties:
     *      propertiesConsulPath    - the path to the consul root directory
     *      delayTimeInterval       - the time that consul server will respond
     *
     * @param propertiesClasses classes that has {@link Property} or
     *                        {@link CollectionProperty} annotations
     */
    public KVCacheApplicationConfig(Class... propertiesClasses)
    {
        this(System.getProperty("propertiesConsulPath"),
                Integer.parseInt(System.getProperty("delayTimeInterval")),
                propertiesClasses
        );
    }

    /**
     * This constructor construct default {@link KVCache}
     * @param propertiesConsulPath the path to the consul root directory
     * @param delayTimeInterval the time that consul server will respond
     * @param propertiesClasses classes that has {@link Property} or
     *                        {@link CollectionProperty} annotations
     */
    public KVCacheApplicationConfig(String propertiesConsulPath, int delayTimeInterval, Class... propertiesClasses)
    {
        super(propertiesClasses);
        Consul consul = Consul.builder()
//                    .withReadTimeoutMillis(10000 + delayTimeInterval * 1000)    // timeout is the delayTimeInterval + 10 sec
                    .build();
        this.kvCache = initKVCache(KVCache.newCache(consul.keyValueClient(), propertiesConsulPath, delayTimeInterval));
    }

    private KVCache initKVCache(KVCache kvCache)
    {
        kvCache.addListener(new ConsulCache.Listener<String, Value>()
        {
            private Map<String, Object> failures;

            @Override
            public void notify(Map<String, Value> newValues)
            {
                if (isInitialized) {
                    // call the update on the new values
                    failures = updateProperty2ObjectsMap(properties2Update);
                    if (!failures.isEmpty()) {
                        // notify all "Failure" listeners on the failed keys/values
                        logger.error("Fails in KVCacheApplicationConfig update. See log for details");
                        for (ApplicationConfigListener l : configUpdateFailedListeners)
                            l.notify(failures);
                    }

                    // notify all "Update" listeners on new values
                    for (ApplicationConfigListener l : configUpdateListeners)
                        l.notify(property2Object);

                    // create a HashMap for printing the updated values
                    HashMap<String, Object> printedMap = new HashMap<>();
                    for (String property : properties2Update) {
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

    public void stop() throws ApplicationConfigException
    {
        try {
            kvCache.stop();
        } catch (Exception e) {
            throw new ApplicationConfigException("KVCache could not stop", e);
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
    public boolean initialize() throws ApplicationConfigException
    {
        if (isInitialized) {
            logger.warn("KVCacheApplicationConfig is already initialized");
            return true;
        }

        try {
            kvCache.start();
            if (!kvCache.awaitInitialized(10, TimeUnit.SECONDS))    // wait for KVCache first response
                throw new ConfigInitializeException("Could not initialize KVCache within the given timeout");
        } catch (InterruptedException ie) {
            throw new ConfigInitializeException("Interrupted while initializing KVCache", ie);
        } catch (Exception ex) {
            throw new ConfigInitializeException("Could not initialize KVCache", ex);
        }

        // first operation to update the properties2Objects map, all keys/values should not failed!
        Map<String, Object> failures = updateProperty2ObjectsMap(property2Converters.keySet());
        if (!failures.isEmpty())
            throw new ConfigInitializeException("Failed to initialize KVCacheApplicationConfig. See log for details");

        // in case of empty properties2Update, we don't need the kvCache
        if (properties2Update.isEmpty()) {
            logger.warn("Stopping KVCache since there is no properties to update");
            try {
                kvCache.stop();
            } catch (Exception e) {
                logger.error("Failed to stop KVCache", e);
            }
        }

        return isInitialized = true;
    }
}
