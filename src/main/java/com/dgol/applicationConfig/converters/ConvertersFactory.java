package com.dgol.applicationConfig.converters;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by dima.golomozy on 03/05/2016.
 *
 */
public class ConvertersFactory
{
    private static final Map<String, Converter<?>> singeltonsConvertes = Collections.unmodifiableMap(
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
        Converter<?> converter = singeltonsConvertes.get(canonicalName);
        return converter != null
                ? converter
                : createConverter(c);
    }

    private static Converter<?> createConverter(Class<? extends Converter<?>> c) {
        try {
            return c.getConstructor().newInstance();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
