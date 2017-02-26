package com.dgol.applicationConfig.annotation;

import com.dgol.applicationConfig.converters.Converter;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.Map;

/**
 * Annotation for properties that need to be stored in a {@link Map}
 *
 * @author dima.golomozy
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface MapProperty
{
    /**
     * @return true if need to update the key's value if the properties get changed
     */
    boolean putToUpdate() default false;

    /**
     * @return the delimiter to split the value with
     */
    String delimiter();

    /**
     * @return the delimiter to split the value to get the key-value pair
     */
    String keyValueDelimiter();

    /**
     * @return the {@link Converter} to use to convert the key
     */
    Class<? extends Converter<?>> keyConverter();

    /**
     * @return the {@link Converter} to use to convert the value
     */
    Class<? extends Converter<?>> valueConverter();

    /**
     * @return the {@link Map} class to create to store the converted values
     */
    Class<? extends Map> map();
}
