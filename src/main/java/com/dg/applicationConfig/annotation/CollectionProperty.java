package com.dg.applicationConfig.annotation;

import com.dg.applicationConfig.converters.Converter;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.Collection;

/**
 * Annotation for Key-Value of type {@link Collection}
 * @author dima.golomozy
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface CollectionProperty
{
    /**
     * Indicates if need to update this key's value
     * @return true if need to update the key's value if the properties get changed
     */
    boolean putToUpdate() default false;

    /**
     * @return delimiter to split the value with
     */
    String delimiter();

    /**
     * Indicates the {@link Converter} to use to convert the key's value
     * @return the {@link Converter} to use to convert the key's value
     */
    Class<? extends Converter<?>> converter();

    /**
     * Indicates the {@link Collection} class to create to store the converted values
     * @return {@link Collection} class to create to store the converted values
     */
    Class<? extends Collection> collection();
}
