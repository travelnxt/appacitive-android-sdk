package com.appacitive.sdk.infra;

import com.appacitive.sdk.interfaces.Http;
import com.appacitive.sdk.model.Platform;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by sathley.
 */
public class JavaPlatform implements Platform {
    private static final Map<Class<?>, ObjectFactory<?>> registrations = new HashMap<Class<?>, ObjectFactory<?>>(){{
        put(HttpClient.class, new ObjectFactory<HttpClient>() {
            @Override
            public HttpClient get() {
//                return HttpClientBuilder.create().build();
                return new DefaultHttpClient();
            }
        });
        put(Http.class, new ObjectFactory<Http>() {
            @Override
            public Http get() {
                return new AppacitiveHttp();
            }
        });
    }};

    public Map<Class<?>, ObjectFactory<?>> getRegistrations() {
        return registrations;
    }
}
