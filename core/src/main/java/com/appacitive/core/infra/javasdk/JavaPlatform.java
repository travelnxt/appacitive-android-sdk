//package com.appacitive.core.infra.javasdk;
//
//import com.appacitive.core.infra.ObjectFactory;
//import com.appacitive.core.interfaces.AsyncHttp;
//import com.appacitive.core.model.Platform;
//
//import java.util.HashMap;
//import java.util.Map;
//
///**
//* Created by sathley.
//*/
//public class JavaPlatform implements Platform {
//    private static final Map<Class<?>, ObjectFactory<?>> registrations = new HashMap<Class<?>, ObjectFactory<?>>() {{
//
//        put(AsyncHttp.class, new ObjectFactory<AsyncHttp>() {
//            @Override
//            public AsyncHttp get() {
//                return new JavaAsyncHttp();
//            }
//        });
//    }};
//
//    public Map<Class<?>, ObjectFactory<?>> getRegistrations() {
//        return registrations;
//    }
//}
