package com.dg.applicationConfig.converters;

import com.dg.applicationConfig.exceptions.ConvertException;

/**
 * @author dima.golomozy
 */
public final class LongConverter implements Converter<Long>
{
    private static LongConverter instance;

    private LongConverter() { }

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
