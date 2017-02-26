package com.dgol.applicationConfig.converters;

import com.dgol.applicationConfig.exceptions.ConvertException;

import java.util.Collection;

/**
 * Created by dima.golomozy on 05/05/2016.
 */
public final class CollectionConverter implements Converter<Collection>
{
    private Class<? extends Collection> aClass;
    private Converter<?> converter;
    private String delimiter;

    CollectionConverter() { }

    public CollectionConverter withDelimiter(String delimiter) {
        this.delimiter = delimiter;
        return this;
    }

    public CollectionConverter withConverter(Converter<?> converter) {
        this.converter = converter;
        return this;
    }

    public CollectionConverter withCollectionClass(Class<? extends Collection> aClass) {
        this.aClass = aClass;
        return this;
    }

    @Override
    @SuppressWarnings("unchecked")
    public Collection convert(String str) throws ConvertException {
        Collection collection;
        try {
            collection = aClass.getConstructor().newInstance();
        } catch (Exception e) {
            throw new ConvertException(e);
        }

        if (!str.isEmpty())
            for (String s : str.split(delimiter))
                collection.add(converter.convert(s.trim()));

        return collection;
    }
}
