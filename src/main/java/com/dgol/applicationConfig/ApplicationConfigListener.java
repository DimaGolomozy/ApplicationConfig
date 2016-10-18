package com.dgol.applicationConfig;

import java.util.Map;

/**
 * Created by dima.golomozy on 30/07/2016.
 */
public interface ApplicationConfigListener
{
    void notify(Map<String, Object> newValues);
}
