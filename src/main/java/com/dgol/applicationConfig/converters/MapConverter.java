package com.dgol.applicationConfig.converters;

import com.dgol.applicationConfig.exceptions.ConvertException;

import java.util.Map;

/**
 * @author dima.golomozy
 */
public final class MapConverter implements Converter<Map>
{
    private Class<? extends Map> aClass;
    private Converter<?> keyConverter;
    private Converter<?> valueConverter;
    private String delimiter;
    private String keyValueDelimiter;

    MapConverter() { }

    public MapConverter withDelimiter(String delimiter) {
        this.delimiter = delimiter;
        return this;
    }

    public MapConverter withKeyValueDelimiter(String keyValueDelimiter) {
        this.keyValueDelimiter = keyValueDelimiter;
        return this;
    }

    public MapConverter withKeyConverter(Converter<?> keyConverter) {
        this.keyConverter = keyConverter;
        return this;
    }

    public MapConverter withValueConverter(Converter<?> valueConverter) {
        this.valueConverter = valueConverter;
        return this;
    }

    public MapConverter withMapClass(Class<? extends Map> aClass) {
        this.aClass = aClass;
        return this;
    }

    @Override
    @SuppressWarnings("unchecked")
    public Map convert(String str) throws ConvertException {
        Map map;
        try {
            map = aClass.getConstructor().newInstance();
        } catch (Exception e) {
            throw new ConvertException(e);
        }

        if (!str.isEmpty())
            for (String s : str.split(delimiter)) {
                String[] split = s.split(keyValueDelimiter);
                map.put(keyConverter.convert(split[0].trim()), valueConverter.convert(split[1].trim()));
            }

        return map;
    }
}
