package com.dg.applicationConfig.exceptions;

/**
 * Thrown to indicate that the code had a failure during the
 * {@code Converter.convert()} operation.
 *
 * @author dima.golomozy
 */
public class ConvertException extends RuntimeException
{
    public ConvertException()
    {
    }

    public ConvertException(String message)
    {
        super(message);
    }

    public ConvertException(String message, Throwable cause)
    {
        super(message, cause);
    }

    public ConvertException(Throwable cause)
    {
        super(cause);
    }

    public ConvertException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace)
    {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
