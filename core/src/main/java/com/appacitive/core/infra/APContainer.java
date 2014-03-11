package com.appacitive.core.infra;

import java.io.Serializable;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by sathley.
 */
public class APContainer implements Serializable {

    private static final Map<Class<?>, ObjectFactory<?>> registrations = new HashMap<Class<?>, ObjectFactory<?>>();

    public APContainer() {
    }

    public static final synchronized void register(Class<?> interfaceObject, ObjectFactory<?> implementationObject) {
        if (interfaceObject.isInterface() == false && Modifier.isAbstract(interfaceObject.getModifiers()) == false)
            throw new IllegalArgumentException("interfaceObject is not a valid interface.");
        if (interfaceObject.isAssignableFrom(implementationObject.get().getClass()) == false)
            throw new IllegalArgumentException("implementationObject should be assignable from interfaceObject.");
        registrations.put(interfaceObject, implementationObject);
    }

    public static final synchronized void registerAll(Map<Class<?>, ObjectFactory<?>> registrations) {

        for (Class<?> interfaceObject : registrations.keySet()) {
            register(interfaceObject, registrations.get(interfaceObject));
        }
    }

    public static final <T> T build(Class<? super T> interfaceObject) {
        try {
            if (registrations.containsKey(interfaceObject))
                return (T) registrations.get(interfaceObject).get();
        } catch (Exception e) {
            return null;
        }
        return null;
    }
}
