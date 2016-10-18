package com.dgol.applicationConfig;

import com.dgol.applicationConfig.annotation.CollectionProperty;
import com.dgol.applicationConfig.annotation.Property;
import com.dgol.applicationConfig.converters.Converter;
import com.dgol.applicationConfig.converters.ConvertersFactory;
import com.dgol.applicationConfig.converters.primitives.CollectionConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created by dima.golomozy on 10/05/2016.
 */
public abstract class AbstractApplicationConfig implements ApplicationConfig
{
    private final Logger logger = LoggerFactory.getLogger(AbstractApplicationConfig.class);
    protected final CopyOnWriteArrayList<ApplicationConfigListener> applicationConfigListeners = new CopyOnWriteArrayList<>();
    protected final Map<String, Object> property2Object;
    protected final Map<String, Converter> property2Converters;
    protected final Set<String> properties2Update;
    protected boolean isInitialized = false;

    protected AbstractApplicationConfig(Class... propertiesClasses)
    {
        if (propertiesClasses == null)
            throw new RuntimeException("Properties class can't be null");

        this.property2Object = new ConcurrentHashMap<>();
        this.property2Converters = new HashMap<>();
        this.properties2Update = new HashSet<>();

        initPropertiesByClass(propertiesClasses);
    }

    private void initPropertiesByClass(Class... propertiesClasses)
    {
        for (Class propertiesClass : propertiesClasses)
        {
            for (Field field : propertiesClass.getFields())
            {
                for (Annotation annotation : field.getDeclaredAnnotations())
                {
                    if (annotation instanceof Property)
                        handleProperty(field, (Property) annotation);
                    else if (annotation instanceof CollectionProperty)
                        handleCollectionProperty(field, (CollectionProperty) annotation);
                }
            }
        }
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

    protected final boolean updateProperty2ObjectsMap(Set<String> propertiesToUpdate)
    {
        boolean isSuccess = true;
        for (String property : propertiesToUpdate)
        {
            if (property2Converters.containsKey(property))
            {
                if (isFolder(property))
                    for (String key : getFolderKeysFromMapByProperty(property))
                        isSuccess &= tryAddProperty2Object(key, property2Converters.get(property));
                else
                    isSuccess &= tryAddProperty2Object(property, property2Converters.get(property));
            }
            else
            {
                isSuccess = false;
                logger.error("Property [{}] was not found", property);
            }
        }
        return isSuccess;
    }

    private boolean tryAddProperty2Object(String property, Converter converter)
    {
        try {
            property2Object.put(property, converter.convert(getStringValueFromMapByProperty(property)));
            return true;
        } catch (RuntimeException re) {
            logger.error("Failed to convert value for property [{}]", property, re);
            return false;
        }
    }

    public boolean addListener(ApplicationConfigListener applicationConfigListener) {
        boolean added = applicationConfigListeners.add(applicationConfigListener);
        if (isInitialized)
            applicationConfigListener.notify(property2Object);
        return added;
    }

    public boolean removeListener(ApplicationConfigListener applicationConfigListener) {
        return applicationConfigListeners.remove(applicationConfigListener);
    }

    protected abstract List<String> getFolderKeysFromMapByProperty(String property);

    protected abstract String getStringValueFromMapByProperty(String property);

    public <T> T get(String property)
    {
        return (T) property2Object.get(property);
    }

    public <T> T get(String property, Class<T> type)
    {
        return (T) property2Object.get(property);
    }
}
