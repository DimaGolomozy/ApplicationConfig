package com.dgol.applicationConfig.annotation;

import com.dgol.applicationConfig.converters.Converter;
import com.dgol.applicationConfig.converters.StringConverter;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation for basic property;
 * Integer, String, Double, custom converters, etc..
 *
 * @author dima.golomozy
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Property
{
    /**
     * @return true if need to update the key's value if the properties get changed
     */
    boolean putToUpdate() default false;

    /**
     * @return the {@link Converter} to use to convert the key's value
     */
    Class<? extends Converter<?>> converter() default StringConverter.class;
}
