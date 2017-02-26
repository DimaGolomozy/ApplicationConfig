package com.dgol.applicationConfig;

import java.util.Map;

/**
 * Implementers can register a listener to receive
 * a new map when changes occur or the failures when they happen
 *
 * @author dima.golomozy
 */
public interface ApplicationConfigListener
{
    void notify(Map<String, Object> newValues);
}
