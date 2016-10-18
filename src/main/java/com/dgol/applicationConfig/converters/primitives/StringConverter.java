package com.dgol.applicationConfig.converters.primitives;

import com.dgol.applicationConfig.converters.Converter;
import com.dgol.applicationConfig.exceptions.ConvertException;

/**
 * Created by dima.golomozy on 01/08/2016.
 */
public final class StringConverter implements Converter<String>
{
    private static StringConverter instance;

    public static StringConverter getInstance()
    {
        if (instance == null)
        {
            synchronized (StringConverter.class){
                if (instance == null)
                    instance = new StringConverter();
            }
        }
        return instance;
    }

    @Override
    public String convert(String str) throws ConvertException
    {
        if (str == null)
            throw new ConvertException("String is null");

        return str;
    }
}
