package com.dg.applicationConfig.annotation;

import com.dg.applicationConfig.converters.StringConverter;
import com.dg.applicationConfig.converters.Converter;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.Collection;

/**
 * Indicates the contexts of a property key
 * Annotation for Key-Value of all primitive types
 * @author dima.golomozy
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Property
{
    /**
     * Indicates if need to update this key's value
     * @return true if need to update the key's value if the properties get changed
     */
    boolean putToUpdate() default false;

    /**
     * Indicates the {@link Converter} to use to convert the key's value
     * @return the {@link Converter} to use to convert the key's value
     */
    Class<? extends Converter<?>> converter() default StringConverter.class;
}
