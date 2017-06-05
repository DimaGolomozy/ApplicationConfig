package com.dg.applicationConfig.annotation;

import com.dg.applicationConfig.converters.Converter;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.Collection;
import java.util.Map;

/**
 * Annotation for Key-Value of type {@link Map}
 * @author dima.golomozy
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface MapProperty
{
    /**
     * Indicates if need to update this key's value
     * @return true if need to update the key's value if the properties get changed
     */
    boolean putToUpdate() default false;

    /**
     * @return delimiter to split the string with
     */
    String delimiter();

    /**
     * @return delimiter to split the value to get the key-value pair
     */
    String keyValueDelimiter();

    /**
     * Indicates the {@link Converter} to use to convert the key
     * @return the {@link Converter} to use to convert the key
     */
    Class<? extends Converter<?>> keyConverter();

    /**
     * Indicates the {@link Converter} to use to convert the value
     * @return the {@link Converter} to use to convert the value
     */
    Class<? extends Converter<?>> valueConverter();

    /**
     * Indicates the {@link Collection} class to create to store the converted values
     * @return {@link Collection} class to create to store the converted values
     */
    Class<? extends Map> map();
}
