package com.dg.applicationConfig.converters;

import com.dg.applicationConfig.exceptions.ConvertException;

/**
 * @author dima.golomozy
 */
public final class StringConverter implements Converter<String>
{
    private static StringConverter instance;

    private StringConverter() { }

    public static StringConverter getInstance() {
        if (instance == null) {
            synchronized (StringConverter.class) {
                if (instance == null)
                    instance = new StringConverter();
            }
        }
        return instance;
    }

    @Override
    public String convert(String str) throws ConvertException
    {
        if (str == null)
            throw new ConvertException("String is null");

        return str;
    }
}
