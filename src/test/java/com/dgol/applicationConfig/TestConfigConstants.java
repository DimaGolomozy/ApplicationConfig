package com.dgol.applicationConfig;

import com.dgol.applicationConfig.annotation.MapProperty;
import com.dgol.applicationConfig.converters.*;
import com.dgol.applicationConfig.annotation.CollectionProperty;
import com.dgol.applicationConfig.annotation.Property;

import java.util.HashMap;
import java.util.HashSet;

/**
 * Created by dima.golomozy on 05/05/2016.
 */
public class TestConfigConstants
{
    @Property
    public final static String string1 = "string";

    @Property
    public final static String emptyString = "emptyString";

    @Property(converter = IntegerConverter.class)
    public final static String integer1 = "integer";

    @Property(converter = LongConverter.class)
    public final static String long1 = "long";

    @Property(converter = ShortConverter.class)
    public final static String short1 = "short";

    @Property(converter = DoubleConverter.class)
    public final static String double1 = "double";

    @Property(converter = FloatConverter.class)
    public final static String float1 = "float";

    @Property(converter = BooleanConverter.class)
    public final static String boolean1 = "boolean";

    @CollectionProperty(delimiter = ",", converter = IntegerConverter.class, collection = HashSet.class)
    public final static String set = "set";

    @MapProperty(delimiter = ";", keyValueDelimiter = ",", keyConverter = StringConverter.class, valueConverter = IntegerConverter.class, map = HashMap.class)
    public final static String map = "map";

    @Property(converter = IntegerConverter.class)
    public final static String folder = "folder/";
}
