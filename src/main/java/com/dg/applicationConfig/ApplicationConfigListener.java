package com.dg.applicationConfig;

import java.util.Map;

/**
 * Implementers can register a listener to receive
 * a new map when it changes or the failures when it fails
 *
 * @author dima.golomozy
 */
public interface ApplicationConfigListener
{
    void notify(Map<String, Object> newValues);
}
