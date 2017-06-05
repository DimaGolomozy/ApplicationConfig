package com.dg.applicationConfig;

import com.dg.applicationConfig.annotation.CollectionProperty;
import com.dg.applicationConfig.annotation.Property;
import com.dg.applicationConfig.converters.*;

import java.util.HashSet;

/**
 * @author dima.golomozy
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

    @CollectionProperty(delimiter = ",", converter = IntegerConverter.class, collection = HashSet.class)
    public final static String emptySet = "emptySet";

    @Property(converter = IntegerConverter.class)
    public final static String folder = "folder/";
}
