package com.dgol.applicationConfig;

import com.dgol.applicationConfig.annotation.CollectionProperty;
import com.dgol.applicationConfig.annotation.MapProperty;
import com.dgol.applicationConfig.annotation.Property;
import com.dgol.applicationConfig.converters.Converter;
import com.dgol.applicationConfig.converters.ConvertersFactory;
import com.dgol.applicationConfig.converters.CollectionConverter;
import com.dgol.applicationConfig.converters.MapConverter;
import com.dgol.applicationConfig.exceptions.ApplicationConfigException;
import com.dgol.applicationConfig.exceptions.ConvertException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @author dima.golomozy
 */
public abstract class ApplicationConfig
{
    private final Logger logger = LoggerFactory.getLogger(ApplicationConfig.class);
    protected final CopyOnWriteArrayList<ApplicationConfigListener> updateListeners;
    protected final CopyOnWriteArrayList<ApplicationConfigListener> failureListeners;
    protected final Map<String, Object> property2Object;
    protected final Map<String, Converter> property2Converters;
    protected final Set<String> properties2Update;
    protected boolean isInitialized = false;

    protected ApplicationConfig(Class... propertiesClasses) {
        if (propertiesClasses == null)
            throw new RuntimeException("Properties class can't be null");

        this.property2Object = new ConcurrentHashMap<>();
        this.property2Converters = new HashMap<>();
        this.properties2Update = new HashSet<>();
        this.updateListeners = new CopyOnWriteArrayList<>();
        this.failureListeners = new CopyOnWriteArrayList<>();

        initPropertiesByClass(propertiesClasses);
    }

    private void initPropertiesByClass(Class... propertiesClasses) {
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

    private void handleMapProperty(Field field, MapProperty annotation) {
        MapConverter mapConverter = (MapConverter) ConvertersFactory.getConverter(MapConverter.class);
        mapConverter.withMapClass(annotation.map())
                .withKeyConverter(ConvertersFactory.getConverter(annotation.keyConverter()))
                .withValueConverter(ConvertersFactory.getConverter(annotation.valueConverter()))
                .withDelimiter(annotation.delimiter())
                .withKeyValueDelimiter(annotation.keyValueDelimiter());

        insert(field, mapConverter, annotation.putToUpdate());
    }

    private void handleCollectionProperty(Field field, CollectionProperty annotation) {
        CollectionConverter collectionConverter = (CollectionConverter) ConvertersFactory.getConverter(CollectionConverter.class);
        collectionConverter.withCollectionClass(annotation.collection())
                .withConverter(ConvertersFactory.getConverter(annotation.converter()))
                .withDelimiter(annotation.delimiter());

        insert(field, collectionConverter, annotation.putToUpdate());
    }

    private void handleProperty(Field field, Property annotation) {
        insert(field, ConvertersFactory.getConverter(annotation.converter()), annotation.putToUpdate());
    }

    private void insert(Field field, Converter<?> converter, boolean putToUpdate) {
        try {
            property2Converters.put((String) field.get(field), converter);
            if (putToUpdate)
                properties2Update.add((String) field.get(field));
        } catch (IllegalAccessException iae) {
            logger.error("Error accessing field [{}]", field.getName(), iae);
        }
    }

    protected boolean isFolder(String property) {
        return property.charAt(property.length() - 1) == '/';
    }

    protected final Map<String, Object> updateProperty2ObjectsMap(Set<String> propertiesToUpdate) {
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

    private boolean tryAddProperty2Object(String property, String value, Converter converter) {
        try {
            property2Object.put(property, converter.convert(value));
            return true;
        } catch (ConvertException ce) {
            logger.error("Failed to convert value [{}] for property [{}]", value, property, ce);
            return false;
        }
    }

    public boolean addUpdateListener(ApplicationConfigListener applicationConfigListener) {
        boolean added = updateListeners.add(applicationConfigListener);
        if (isInitialized)
            applicationConfigListener.notify(property2Object);
        return added;
    }

    public boolean removeUpdateListener(ApplicationConfigListener applicationConfigListener) {
        return updateListeners.remove(applicationConfigListener);
    }

    public boolean addFailureListener(ApplicationConfigListener applicationConfigListener) {
        return failureListeners.add(applicationConfigListener);
    }

    public boolean removeFailureListener(ApplicationConfigListener applicationConfigListener) {
        return failureListeners.remove(applicationConfigListener);
    }

    public abstract boolean initialize() throws ApplicationConfigException;

    public abstract void stop() throws ApplicationConfigException;

    protected abstract List<String> getFolderKeysFromMapByProperty(String property);

    protected abstract String getStringValueFromMapByProperty(String property);

    public <T> T get(String property) {
        return (T) property2Object.get(property);
    }

    public <T> T get(String property, Class<T> type) {
        return (T) property2Object.get(property);
    }

    public <T> T getOrDefault(String property, T defaultValue) {
        T value = (T) property2Object.get(property);
        return value != null
                ? value
                : defaultValue;
    }
}
