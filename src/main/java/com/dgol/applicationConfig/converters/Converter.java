package com.dgol.applicationConfig.converters;

import com.dgol.applicationConfig.exceptions.ConvertException;

/**
 * Interface for Converters
 * @author dima.golomozy
 */
public interface Converter<R>
{
    R convert(String str) throws ConvertException;
}
