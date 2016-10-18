package com.dgol.applicationConfig.converters.primitives;

import com.dgol.applicationConfig.converters.Converter;
import com.dgol.applicationConfig.exceptions.ConvertException;

/**
 * Created by dima.golomozy on 01/08/2016.
 */
public final class LongConverter implements Converter<Long>
{
    private static LongConverter instance;

    public static LongConverter getInstance()
    {
        if (instance == null)
        {
            synchronized (LongConverter.class){
                if (instance == null)
                    instance = new LongConverter();
            }
        }
        return instance;
    }

    @Override
    public Long convert(String str) throws ConvertException
    {
        try {
            return Long.parseLong(str);
        } catch (NumberFormatException e) {
            throw new ConvertException(e);
        }
    }
}
