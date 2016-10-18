package com.dgol.applicationConfig.converters;

import com.dgol.applicationConfig.exceptions.ConvertException;

/**
 * Created by dima.golomozy on 03/05/2016.
 */
public interface Converter<R>
{
    R convert(String str) throws ConvertException;
}
