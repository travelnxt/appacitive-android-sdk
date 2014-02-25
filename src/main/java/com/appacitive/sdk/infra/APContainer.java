package com.appacitive.sdk.infra;

import com.appacitive.sdk.interfaces.Http;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by sathley.
 */
public class APContainer {

    private static final Map<Class<?>, Class<?>> registrations = new HashMap<Class<?>, Class<?>>();

    public APContainer() {
    }

    public static final synchronized void register(Class<?> interfaceObject, Class<?> implementationObject) {
        if (interfaceObject.isInterface() == false)
            throw new IllegalArgumentException("interfaceObject is not a valid interface.");
        if (implementationObject.isAssignableFrom(interfaceObject) == false)
            throw new IllegalArgumentException("implementationObject should be assignable from interfaceObject.");
        registrations.put(interfaceObject, implementationObject);
    }

    public static final synchronized void registerAll(Map<Class<?>, Class<?>> registrations) {

        // TODO add validations

        registrations.putAll(registrations);
    }

    public static final <T> T build(Class<? super T> interfaceObject) {
        try {
            if (registrations.containsKey(interfaceObject))
                return (T) registrations.get(interfaceObject).newInstance();
        } catch (Exception e) {
            return null;
        }
        return null;
    }
}
