package com.dg.applicationConfig.exceptions;

/**
 * Thrown to indicate that the code had a failure during the
 * initialization operation.
 *
 * @author dima.golomozy
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
