package com.appacitive.core.model;

import com.appacitive.core.infra.ObjectFactory;

import java.util.Map;

/**
 * Created by sathley.
 */
public interface Platform {
    public Map<Class<?>, ObjectFactory<?>> getRegistrations();

}
