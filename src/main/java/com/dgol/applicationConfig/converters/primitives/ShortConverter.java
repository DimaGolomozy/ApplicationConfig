package com.dgol.applicationConfig.converters.primitives;

import com.dgol.applicationConfig.converters.Converter;
import com.dgol.applicationConfig.exceptions.ConvertException;

/**
 * Created by dima.golomozy on 01/08/2016.
 */
public final class ShortConverter implements Converter<Short>
{
    private static ShortConverter instance;

    public static ShortConverter getInstance()
    {
        if (instance == null)
        {
            synchronized (ShortConverter.class){
                if (instance == null)
                    instance = new ShortConverter();
            }
        }
        return instance;
    }

    @Override
    public Short convert(String str) throws ConvertException
    {
        try {
            return Short.parseShort(str);
        } catch (NumberFormatException e) {
            throw new ConvertException(e);
        }
    }
}
