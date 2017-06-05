# Application Config
by _Dima Golomozy_

Util for loading properties from Consul client 
https://github.com/OrbitzWorldwide/consul-client  
and change them in **Runtime**.  
(Also the properties can be loaded from a file, but there wont be a runtime update of the properties)

After loading the properties, there is no need to parse the String value.
The ApplicationConfig is doing it when it loads the values.

### Uses
Use one of the Annotation Properties:
1. CollectionProperty
2. MapProperty
3. Property
    
        @Property(converter = BooleanConverter.class)
        public final static String boolean1 = "booleanKey";

        @CollectionProperty(delimiter = ",", converter = IntegerConverter.class, collection = HashSet.class)
        public final static String set = "setKey";  
    
After that the value of the key "booleanKey" will be converted to Boolean.class.  
And then one can get the value by one of the methods:

    public <T> T get(String property);
    public <T> T get(String property, Class<T> type);
    
To load the all the keys to the ApplicationConfig, please define a class of static keys like so:

    public class TestConfigConstants
    {
        @Property
        public final static String string1 = "stringKey";
    
        @Property
        public final static String integer1 = "integerKey";
    }
and send it to the ApplicationConfig constructor method like so:

    new KVCacheApplicationConfig("dg/applicationConfigTest", 590, TestConfigConstants.class);
    new FileApplicationConfig(resourceFileName, TestConfigConstants.class);
    
    
### Value updates
If KVCacheApplicationConfig is been used, turn the flag: `putToUpdate=true` so the property  
will be updated each time a change is been made in the consul key/value store (only under the root path, in this case is "dg/applicationConfigTest")  
If all the keys `putToUpdate` flage will be false, the KVCache client will be closed and no properties will be updated in runtime 

