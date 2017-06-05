package com.dg.applicationConfig.exceptions;

/**
 * General class for all ApplicationConfig Exceptions
 *
 * @author dima.golomozy
 */
public class ApplicationConfigException extends Exception
{
    public ApplicationConfigException() {
        super();
    }

    public ApplicationConfigException(String s) {
        super(s);
    }

    public ApplicationConfigException(String message, Throwable cause) {
        super(message, cause);
    }
}
