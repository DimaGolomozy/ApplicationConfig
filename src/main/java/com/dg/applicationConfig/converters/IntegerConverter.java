package com.dg.applicationConfig.converters;

import com.dg.applicationConfig.exceptions.ConvertException;

/**
 * @author dima.golomozy
 */
public final class IntegerConverter implements Converter<Integer>
{
    private static IntegerConverter instance;

    private IntegerConverter() { }

    public static IntegerConverter getInstance()
    {
        if (instance == null)
        {
            synchronized (IntegerConverter.class){
                if (instance == null)
                    instance = new IntegerConverter();
            }
        }
        return instance;
    }

    @Override
    public Integer convert(String str) throws ConvertException
    {
        try {
            return Integer.parseInt(str);
        } catch (NumberFormatException e) {
            throw new ConvertException(e);
        }
    }
}
