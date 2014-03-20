package com.appacitive.android;

import android.content.Context;
import com.android.volley.*;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.appacitive.core.infra.APCallback;
import com.appacitive.core.interfaces.AsyncHttp;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Future;

/**
 * Created by sathley.
 */
public class VolleyAsyncHttp implements AsyncHttp {

    private static RequestQueue requestQueue = null;

    private static final String UTF8_BOM = "\uFEFF";

    private static RequestQueue getRequestQueue() {
        if (requestQueue == null) {
            Context context = AppacitiveContext.getApplicationContext();
            if (context == null)
                throw new IllegalStateException("AppacitiveContext is not yet initialized. Initialize it before making any requests to appacitive.");
            requestQueue = Volley.newRequestQueue(context);
        }
        return requestQueue;
    }

    private static Map<String, String> processHeaders(Map<String, String> headers)
    {
        headers.remove("Content-Type");
        return headers;
    }

    private static String processResponse(String response)
    {
        return response.substring(response.indexOf("{"), response.lastIndexOf("}") + 1);
    }

    @Override
    public Future<String> get(String s, Map<String, String> stringStringMap) {
        return null;
    }

    @Override
    public void get(String url, final Map<String, String> headers, final APCallback callback) {
        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (callback != null)
                    callback.success(processResponse(response));
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (callback != null)
                    callback.failure(error);
            }
        }
        ) {
            @Override
            public HashMap<String, String> getHeaders() {
                return new HashMap<String, String>(processHeaders(headers));
            }
        };
        getRequestQueue().add(request);
    }

    @Override
    public Future<String> delete(String s, Map<String, String> stringStringMap) {
        return null;
    }

    @Override
    public void delete(String url, final Map<String, String> headers, final APCallback callback) {
        StringRequest request = new StringRequest(Request.Method.DELETE, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (callback != null)
                    callback.success(processResponse(response));
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (callback != null)
                    callback.failure(error);
            }
        }
        ) {
            @Override
            public HashMap<String, String> getHeaders() {
                return new HashMap<String, String>(processHeaders(headers));
            }
        };
        getRequestQueue().add(request);
    }

    @Override
    public Future<String> put(String s, Map<String, String> stringStringMap, String jsonObject) {
        return null;
    }

    @Override
    public void put(String url, final Map<String, String> headers, final String payload, final APCallback callback) {
        StringRequest request = new StringRequest(Request.Method.PUT, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (callback != null)
                    callback.success(processResponse(response));
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (callback != null)
                    callback.failure(error);
            }
        }
        ) {
            @Override
            public HashMap<String, String> getHeaders() {
                return new HashMap<String, String>(processHeaders(headers));
            }

            @Override
            public byte[] getBody() throws AuthFailureError {
                return payload.getBytes();
            }
        };
        getRequestQueue().add(request);
    }

    @Override
    public Future<String> post(String s, Map<String, String> stringStringMap, String jsonObject) {
        return null;
    }

    @Override
    public void post(String url, final Map<String, String> headers, final String payload, final APCallback callback) {

        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (callback != null)
                    callback.success(processResponse(response));
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (callback != null)
                    callback.failure(error);
            }
        }
        ) {
            @Override
            public HashMap<String, String> getHeaders() {
                return new HashMap<String, String>(processHeaders(headers));
            }

            @Override
            public byte[] getBody() throws AuthFailureError {
                return payload.getBytes();
            }
        };
        getRequestQueue().add(request);
    }
}
