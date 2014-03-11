package com.appacitive.java;

import com.appacitive.core.infra.ObjectFactory;
import com.appacitive.core.interfaces.AsyncHttp;
import com.appacitive.core.interfaces.UserContextProvider;
import com.appacitive.core.model.Platform;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by sathley.
 */
public class JavaPlatform implements Platform {
    private static final Map<Class<?>, ObjectFactory<?>> registrations = new HashMap<Class<?>, ObjectFactory<?>>() {{

        put(AsyncHttp.class, new ObjectFactory<AsyncHttp>() {
            @Override
            public AsyncHttp get() {
                return new JavaAsyncHttp();
            }
        });

        put(com.appacitive.core.interfaces.Logger.class, new ObjectFactory<com.appacitive.core.interfaces.Logger>() {
            @Override
            public com.appacitive.core.interfaces.Logger get() {
                return new JavaLogger();
            }
        });

        put(UserContextProvider.class, new ObjectFactory<UserContextProvider>() {
            @Override
            public UserContextProvider get() {
                return new StaticUserContextProvider();
            }
        });
    }};

    public Map<Class<?>, ObjectFactory<?>> getRegistrations() {
        return registrations;
    }
}
