package com.dg.applicationConfig.converters;

import com.dg.applicationConfig.exceptions.ConvertException;

/**
 * Interface for Converters
 * @author dima.golomozy
 */
public interface Converter<R>
{
    R convert(String str) throws ConvertException;
}
