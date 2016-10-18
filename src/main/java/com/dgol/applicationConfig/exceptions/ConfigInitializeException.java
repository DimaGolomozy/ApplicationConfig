package com.dgol.applicationConfig.exceptions;

/**
 * Created by dima.golomozy on 25/05/2016.
 */
public class ConfigInitializeException extends ApplicationConfigException
{
    public ConfigInitializeException() {
        super();
    }

    public ConfigInitializeException(String s) {
        super(s);
    }

    public ConfigInitializeException(String message, Throwable cause) {
        super(message, cause);
    }
}
