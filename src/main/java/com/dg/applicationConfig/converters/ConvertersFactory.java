package com.dg.applicationConfig.converters;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Factory class to build {@link Converter}s
 * Primitive converters will be build as singleton.
 * {@link CollectionConverter} and other classes that implement {@link Converter} will be initialized with {@code new}
 *
 * @author dima.golomozy
 */
public class ConvertersFactory
{
    private static final Map<String, Converter<?>> convertersMap = Collections.unmodifiableMap(new HashMap<String, Converter<?>>() {{
        put(StringConverter.class.getCanonicalName(), StringConverter.getInstance());
        put(IntegerConverter.class.getCanonicalName(), IntegerConverter.getInstance());
        put(BooleanConverter.class.getCanonicalName(), BooleanConverter.getInstance());
        put(LongConverter.class.getCanonicalName(), LongConverter.getInstance());
        put(DoubleConverter.class.getCanonicalName(), DoubleConverter.getInstance());
        put(ShortConverter.class.getCanonicalName(), ShortConverter.getInstance());
        put(FloatConverter.class.getCanonicalName(), FloatConverter.getInstance());
    }});

    public static Converter<?> getConverter(Class<? extends Converter<?>> c)
    {
        String canonicalName = c.getCanonicalName();
        Converter<?> converter = convertersMap.get(canonicalName);
        if (converter != null)
            return converter;
        else
            return createConverter(c);
    }

    private static Converter<?> createConverter(Class<? extends Converter<?>> c)
    {
        try {
            return c.getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
