package com.appacitive.sdk.model;

import com.appacitive.sdk.infra.ObjectFactory;

import java.util.Map;

/**
 * Created by sathley.
 */
public interface Platform {
    public Map<Class<?>, ObjectFactory<?>> getRegistrations();

}
