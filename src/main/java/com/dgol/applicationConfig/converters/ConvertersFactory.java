package com.dgol.applicationConfig.converters;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Converters factory class.
 * heals to create {@link Converter} instances
 * or reuse primitive converters as StringConverter, IntegerConverter, etc...
 *
 * @author dima.golomozy
 */
public class ConvertersFactory
{
    private static final Map<String, Converter<?>> singletonsConverters = Collections.unmodifiableMap(
            new HashMap<String, Converter<?>>() {{
                put(StringConverter.class.getCanonicalName(), StringConverter.getInstance());
                put(IntegerConverter.class.getCanonicalName(), IntegerConverter.getInstance());
                put(BooleanConverter.class.getCanonicalName(), BooleanConverter.getInstance());
                put(LongConverter.class.getCanonicalName(), LongConverter.getInstance());
                put(DoubleConverter.class.getCanonicalName(), DoubleConverter.getInstance());
                put(ShortConverter.class.getCanonicalName(), ShortConverter.getInstance());
                put(FloatConverter.class.getCanonicalName(), FloatConverter.getInstance());
    }});

    public static Converter<?> getConverter(Class<? extends Converter<?>> c) {
        String canonicalName = c.getCanonicalName();
        Converter<?> converter = singletonsConverters.get(canonicalName);

        if (converter != null)
            return converter;
        else if (canonicalName.equalsIgnoreCase(CollectionConverter.class.getCanonicalName()))
            return new CollectionConverter();
        else if (canonicalName.equalsIgnoreCase(MapConverter.class.getCanonicalName()))
            return new MapConverter();
        else
            return createConverter(c);
    }

    private static Converter<?> createConverter(Class<? extends Converter<?>> c) {
        try {
            return c.getConstructor().newInstance();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
