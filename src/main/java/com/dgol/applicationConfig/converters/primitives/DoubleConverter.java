package com.dgol.applicationConfig.converters.primitives;

import com.dgol.applicationConfig.converters.Converter;
import com.dgol.applicationConfig.exceptions.ConvertException;

/**
 * Created by dima.golomozy on 01/08/2016.
 */
public final class DoubleConverter implements Converter<Double>
{
    private static DoubleConverter instance;

    public static DoubleConverter getInstance()
    {
        if (instance == null)
        {
            synchronized (DoubleConverter.class){
                if (instance == null)
                    instance = new DoubleConverter();
            }
        }
        return instance;
    }

    @Override
    public Double convert(String str) throws ConvertException
    {
        try {
            return Double.parseDouble(str);
        } catch (NumberFormatException e) {
            throw new ConvertException(e);
        }
    }
}
