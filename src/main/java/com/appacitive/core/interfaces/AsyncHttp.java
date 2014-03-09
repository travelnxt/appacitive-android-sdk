package com.appacitive.core.interfaces;

import com.appacitive.core.infra.APCallback;

import java.util.Map;
import java.util.concurrent.Future;

/**
 * Created by sathley.
 */
public interface AsyncHttp {

    public Future<String> get(String url, Map<String, String> headers);

    public void get(String url, Map<String, String> headers, APCallback callback);

    public Future<String> delete(String url, Map<String, String> headers);

    public void delete(String url, Map<String, String> headers, APCallback callback);

    public Future<String> put(String url, Map<String, String> headers, String request);

    public void put(String url, Map<String, String> headers, String request, APCallback callback);

    public Future<String> post(String url, Map<String, String> headers, String request);

    public void post(String url, Map<String, String> headers, String request, APCallback callback);
}
