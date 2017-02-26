package com.dgol.applicationConfig.converters;

import com.dgol.applicationConfig.exceptions.ConvertException;

/**
 * Created by dima.golomozy on 01/08/2016.
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
