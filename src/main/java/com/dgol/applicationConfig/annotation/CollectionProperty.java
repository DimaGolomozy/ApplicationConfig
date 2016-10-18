package com.dgol.applicationConfig.annotation;

import com.dgol.applicationConfig.converters.Converter;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.Collection;

/**
 * Created by dima.golomozy on 05/05/2016.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface CollectionProperty
{
    boolean putToUpdate() default false;
    String delimiter();
    Class<? extends Converter<?>> converter();
    Class<? extends Collection> collection();
}
