package com.dg.applicationConfig.converters;

import com.dg.applicationConfig.exceptions.ConvertException;

/**
 * @author dima.golomozy
 */
public final class FloatConverter implements Converter<Float>
{
    private static FloatConverter instance;

    private FloatConverter() { }

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
