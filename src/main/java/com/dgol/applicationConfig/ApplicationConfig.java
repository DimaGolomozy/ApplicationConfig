package com.dgol.applicationConfig;

import com.dgol.applicationConfig.exceptions.ConfigInitializeException;

/**
 * Created by dima.golomozy on 13/05/2016.
 */
public interface ApplicationConfig
{
    boolean initialize() throws ConfigInitializeException;

    <T> T get(String property);

    <T> T get(String property, Class<T> type);
}
