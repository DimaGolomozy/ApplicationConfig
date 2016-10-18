package com.dgol.applicationConfig.converters.primitives;

import com.dgol.applicationConfig.converters.Converter;
import com.dgol.applicationConfig.exceptions.ConvertException;

/**
 * Created by dima.golomozy on 01/08/2016.
 */
public final class IntegerConverter implements Converter<Integer>
{
    private static IntegerConverter instance;

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
