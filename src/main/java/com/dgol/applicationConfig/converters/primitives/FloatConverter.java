package com.dgol.applicationConfig.converters.primitives;

import com.dgol.applicationConfig.converters.Converter;
import com.dgol.applicationConfig.exceptions.ConvertException;

/**
 * Created by dima.golomozy on 01/08/2016.
 */
public final class FloatConverter implements Converter<Float>
{
    private static FloatConverter instance;

    public static FloatConverter getInstance()
    {
        if (instance == null)
        {
            synchronized (FloatConverter.class){
                if (instance == null)
                    instance = new FloatConverter();
            }
        }
        return instance;
    }

    @Override
    public Float convert(String str) throws ConvertException
    {
        try {
            return Float.parseFloat(str);
        } catch (NumberFormatException e) {
            throw new ConvertException(e);
        }
    }
}
