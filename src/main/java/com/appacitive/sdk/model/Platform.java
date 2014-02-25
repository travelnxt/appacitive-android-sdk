package com.appacitive.sdk.model;

import java.util.Map;

/**
 * Created by sathley.
 */
public abstract class Platform {
    public abstract Map<Class<?>, Class<?>> getRegistrations();

}
