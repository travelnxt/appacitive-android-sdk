package com.appacitive.sdk.infra;

import com.appacitive.sdk.interfaces.Http;
import com.appacitive.sdk.model.Platform;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by sathley.
 */
public class JavaPlatform extends Platform {
    private static final Map<Class<?>, Class<?>> registrations = new HashMap<Class<?>, Class<?>>(){{
        put(Http.class, AppacitiveHttp.class);
    }};


    @Override
    public Map<Class<?>, Class<?>> getRegistrations() {
        return registrations;
    }
}
