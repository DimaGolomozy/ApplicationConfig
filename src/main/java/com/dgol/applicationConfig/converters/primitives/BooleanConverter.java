package com.dgol.applicationConfig.converters.primitives;

import com.dgol.applicationConfig.converters.Converter;
import com.dgol.applicationConfig.exceptions.ConvertException;

/**
 * Created by dima.golomozy on 01/08/2016.
 */
public final class BooleanConverter implements Converter<Boolean>
{
    private static BooleanConverter instance;

    public static BooleanConverter getInstance()
    {
        if (instance == null)
        {
            synchronized (BooleanConverter.class){
                if (instance == null)
                    instance = new BooleanConverter();
            }
        }
        return instance;
    }

    @Override
    public Boolean convert(String str) throws ConvertException
    {
        if ("true".equalsIgnoreCase(str) || "false".equalsIgnoreCase(str))
            return Boolean.parseBoolean(str);
        throw new ConvertException("For input string: \"" + str + "\"");
    }
}
