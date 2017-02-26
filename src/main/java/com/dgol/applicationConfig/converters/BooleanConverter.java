package com.dgol.applicationConfig.converters;

import com.dgol.applicationConfig.exceptions.ConvertException;

/**
 *
 * @author dima.golomozy
 */
public final class BooleanConverter implements Converter<Boolean>
{
    private static BooleanConverter instance;
    private BooleanConverter() { }

    public static BooleanConverter getInstance() {
        if (instance == null) {
            synchronized (BooleanConverter.class) {
                if (instance == null)
                    instance = new BooleanConverter();
            }
        }
        return instance;
    }

    @Override
    public Boolean convert(String str) throws ConvertException {
        if ("true".equalsIgnoreCase(str) || "false".equalsIgnoreCase(str))
            return Boolean.parseBoolean(str);
        throw new ConvertException("For input string: \"" + str + "\"");
    }
}
