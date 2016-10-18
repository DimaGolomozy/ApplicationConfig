package com.dgol.applicationConfig.converters;

import com.dgol.applicationConfig.converters.primitives.*;

/**
 * Created by dima.golomozy on 03/05/2016.
 */
public class ConvertersFactory
{
    public static Converter<?> getConverter(Class<? extends Converter<?>> c)
    {
        switch (c.getCanonicalName())
        {
            case "com.dgol.applicationConfig.converters.primitives.StringConverter":
                return StringConverter.getInstance();
            case "com.dgol.applicationConfig.converters.primitives.IntegerConverter":
                return IntegerConverter.getInstance();
            case "com.dgol.applicationConfig.converters.primitives.BooleanConverter":
                return BooleanConverter.getInstance();
            case "com.dgol.applicationConfig.converters.primitives.CollectionConverter":
                return new CollectionConverter();
            case "com.dgol.applicationConfig.converters.primitives.LongConverter":
                return LongConverter.getInstance();
            case "com.dgol.applicationConfig.converters.primitives.DoubleConverter":
                return DoubleConverter.getInstance();
            case "com.dgol.applicationConfig.converters.primitives.ShortConverter":
                return ShortConverter.getInstance();
            case "com.dgol.applicationConfig.converters.primitives.FloatConverter":
                return FloatConverter.getInstance();
            default:
                return createConverter(c);
        }
    }

    private static Converter<?> createConverter(Class<? extends Converter<?>> c)
    {
        try {
            return c.getConstructor().newInstance();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
