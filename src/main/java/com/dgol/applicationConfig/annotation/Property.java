package com.dgol.applicationConfig.annotation;

import com.dgol.applicationConfig.converters.Converter;
import com.dgol.applicationConfig.converters.primitives.StringConverter;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by dima.golomozy on 05/05/2016.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Property
{
    boolean putToUpdate() default false;
    Class<? extends Converter<?>> converter() default StringConverter.class;
}
