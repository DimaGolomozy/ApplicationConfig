package com.dg.applicationConfig;

import com.dg.applicationConfig.converters.ConvertersFactory;
import com.dg.applicationConfig.converters.MapConverter;
import com.dg.applicationConfig.annotation.CollectionProperty;
import com.dg.applicationConfig.annotation.MapProperty;
import com.dg.applicationConfig.annotation.Property;
import com.dg.applicationConfig.converters.Converter;
import com.dg.applicationConfig.converters.CollectionConverter;
import com.dg.applicationConfig.exceptions.ApplicationConfigException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Abstract class of ApplicationConfig.
 * FileApplicationConfig and KVCacheApplicationConfig both extend this class in order to
 * use both of them in code with minimum change.
 *
 * If one desire to implement a new ApplicationConfig and extend this one,
 * {@code updateProperty2ObjectsMap} must be called to before using {@code get}.
 *
 * @author dima.golomozy
 */
public abstract class ApplicationConfig
{
    private final Logger logger = LoggerFactory.getLogger(ApplicationConfig.class);
    protected final CopyOnWriteArrayList<ApplicationConfigListener> configUpdateListeners;
    protected final CopyOnWriteArrayList<ApplicationConfigListener> configUpdateFailedListeners;
    protected final Map<String, Object> property2Object;
    protected final Map<String, Converter> property2Converters;
    protected final Set<String> properties2Update;
    protected boolean isInitialized = false;

    protected ApplicationConfig(Class... propertiesClasses)
    {
        if (propertiesClasses == null)
            throw new RuntimeException("Properties class can't be null");

        this.property2Object = new ConcurrentHashMap<>();
        this.property2Converters = new HashMap<>();
        this.properties2Update = new HashSet<>();
        this.configUpdateListeners = new CopyOnWriteArrayList<>();
        this.configUpdateFailedListeners = new CopyOnWriteArrayList<>();

        initPropertiesByClass(propertiesClasses);
    }

    /**
     * Uses reflection to read {@link Property} and {@link CollectionProperty} annotation
     * and add there value {@link String} and {@link Converter} to {@code property2Converters} map.
     * @param propertiesClasses - Array of classes that has {@link Property} or {@link CollectionProperty} annotations
     */
    private void initPropertiesByClass(Class... propertiesClasses)
    {
        for (Class propertiesClass : propertiesClasses) {
            for (Field field : propertiesClass.getFields()) {
                for (Annotation annotation : field.getDeclaredAnnotations()) {
                    if (annotation instanceof Property)
                        handleProperty(field, (Property) annotation);
                    else if (annotation instanceof CollectionProperty)
                        handleCollectionProperty(field, (CollectionProperty) annotation);
                    else if (annotation instanceof MapProperty)
                        handleMapProperty(field, (MapProperty) annotation);
                }
            }
        }
    }

    private void handleMapProperty(Field field, MapProperty annotation)
    {
        MapConverter mapConverter = (MapConverter) ConvertersFactory.getConverter(MapConverter.class);
        mapConverter.withCollectionClass(annotation.map())
                .withKeyConveter(ConvertersFactory.getConverter(annotation.keyConverter()))
                .withValueConveter(ConvertersFactory.getConverter(annotation.valueConverter()))
                .withDelimiter(annotation.delimiter())
                .withKeyValueDelimiter(annotation.keyValueDelimiter());
        insert(field, mapConverter, annotation.putToUpdate());
    }

    private void handleCollectionProperty(Field field, CollectionProperty annotation)
    {
        CollectionConverter collectionConverter = (CollectionConverter) ConvertersFactory.getConverter(CollectionConverter.class);
        collectionConverter.withCollectionClass(annotation.collection())
                .withConveter(ConvertersFactory.getConverter(annotation.converter()))
                .withDelimiter(annotation.delimiter());
        insert(field, collectionConverter, annotation.putToUpdate());
    }

    private void handleProperty(Field field, Property annotation)
    {
        insert(field, ConvertersFactory.getConverter(annotation.converter()), annotation.putToUpdate());
    }

    private void insert(Field field, Converter<?> converter, boolean putToUpdate)
    {
        try {
            property2Converters.put((String) field.get(field), converter);
            if (putToUpdate)
                properties2Update.add((String) field.get(field));
        } catch (IllegalAccessException iae) {
            logger.error("Error accessing field [{}]", field.getName(), iae);
        }
    }

    protected boolean isFolder(String property)
    {
        return property.charAt(property.length() - 1) == '/';
    }

    /**
     * Main operation to load and store the keys and their converted values.
     * Applies {@code Converter.convert()} operation ONLY on keys that has {@link Converter}
     * that were loaded from the {@link Property} or {@link CollectionProperty} annotations.
     * One should call this method in the {@code initialize} method.
     *
     * @param propertiesToUpdate set of keys to to apply {@code Converter.convert()} on them
     * @return {@link Map} of FAILED keys with there values as {@link String}
     * that failed during {@code Converter.convert()} operation.
     */
    protected final Map<String, Object>  updateProperty2ObjectsMap(Set<String> propertiesToUpdate)
    {
        Map<String, Object> failures = new HashMap<>();

        String value;
        for (String property : propertiesToUpdate) {
            if (property2Converters.containsKey(property)) {
                if (isFolder(property)) {
                    for (String key : getFolderKeysFromMapByProperty(property)) {
                        value = getStringValueFromMapByProperty(key);
                        if (!tryAddProperty2Object(key, value, property2Converters.get(property)))
                            failures.put(key, value);
                    }
                } else {
                    value = getStringValueFromMapByProperty(property);
                    if (!tryAddProperty2Object(property, value, property2Converters.get(property)))
                        failures.put(property, value);
                }
            } else {
                failures.put(property, null);
                logger.error("Property [{}] was not found", property);
            }
        }

        return failures;
    }

    private boolean tryAddProperty2Object(String property, String value, Converter converter)
    {
        try {
            property2Object.put(property, converter.convert(value));
            return true;
        } catch (RuntimeException re) {
            logger.error("Failed to convert value [{}] for property [{}]", value, property, re);
            return false;
        }
    }

    public boolean addFailureListener(ApplicationConfigListener applicationConfigListener) {
        return configUpdateFailedListeners.add(applicationConfigListener);
    }

    public boolean removeFailureListener(ApplicationConfigListener applicationConfigListener) {
        return configUpdateFailedListeners.remove(applicationConfigListener);
    }

    public boolean addListener(ApplicationConfigListener applicationConfigListener) {
        boolean added = configUpdateListeners.add(applicationConfigListener);
        if (isInitialized)
            applicationConfigListener.notify(property2Object);
        return added;
    }

    public boolean removeListener(ApplicationConfigListener applicationConfigListener) {
        return configUpdateListeners.remove(applicationConfigListener);
    }

    /**
     * Initialize the ApplicationConfig.
     * Basically it can be done in the c'tor, but some ApplicationConfig logic can throw exception in the initialization
     * (like {@link KVCacheApplicationConfig}), this why, its better to use {@code initialize} rather throwing Exception
     * from c'tor.
     *
     * @return true on success and false otherwise
     * @throws ApplicationConfigException
     */
    public abstract boolean initialize() throws ApplicationConfigException;

    public abstract void stop() throws ApplicationConfigException;

    /**
     * Returns the {@link List<String>} values to which the specified key is mapped,
     * or {@code null} if this map contains no mapping for the key.
     * All the values that returns, will be the input for the same {@code Converter.convert()}
     *
     * @param property - the key whose associated value is to be returned
     * @return the value to which the specified key is mapped, or
     *         {@code null} if this map contains no mapping for the key
     */
    protected abstract List<String> getFolderKeysFromMapByProperty(String property);

    /**
     * Returns the {@link String} value to which the specified key is mapped,
     * or {@code null} if this map contains no mapping for the key.
     * The value that returns, will be the input for the {@code Converter.convert()}
     *
     * @param property - the key whose associated value is to be returned
     * @return the value to which the specified key is mapped, or
     *         {@code null} if this map contains no mapping for the key
     */
    protected abstract String getStringValueFromMapByProperty(String property);

    /**
     * Returns the value to which the specified key is mapped,
     * or {@code null} if this map contains no mapping for the key.
     *
     * @param property the key whose associated value is to be returned
     *                 Should be a key that is annotated with {@link Property} or {@link CollectionProperty}
     *                 Other keys may result with {@code null} value
     * @return the value to which the specified key is mapped, or
     *         {@code null} if this map contains no mapping for the key
     * @throws ClassCastException if the key is of an inappropriate type for
     *         this map
     * @throws NullPointerException if the specified key is null
     */
    public <T> T get(String property)
    {
        return (T) property2Object.get(property);
    }

    /**
     * Returns the value to which the specified key is mapped,
     * or {@code null} if this map contains no mapping for the key.
     *
     * @param property the key whose associated value is to be returned
     *                 Should be a key that is annotated with {@link Property} or {@link CollectionProperty}
     *                 Other keys may result with {@code null} value
     * @param type a class representing the type that should be to the return value
     * @return the value to which the specified key is mapped, or
     *         {@code null} if this map contains no mapping for the key
     * @throws ClassCastException if the key is of an inappropriate type for
     *         this map
     * @throws NullPointerException if the specified key is null
     */
    public <T> T get(String property, Class<T> type)
    {
        return (T) property2Object.get(property);
    }
}
