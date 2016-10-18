package com.dgol.applicationConfig.exceptions;

/**
 * Created by dima.golomozy on 25/05/2016.
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
