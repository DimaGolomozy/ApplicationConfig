package com.dg.applicationConfig.converters;

import com.dg.applicationConfig.exceptions.ConvertException;

/**
 * @author dima.golomozy
 */
public final class DoubleConverter implements Converter<Double>
{
    private static DoubleConverter instance;

    private DoubleConverter () { }

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
