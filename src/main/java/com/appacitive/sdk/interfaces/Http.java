package com.appacitive.sdk.interfaces;

import java.io.IOException;
import java.util.Map;

/**
 * Created by sathley.
 */
public interface Http {
    public Map<String, Object> get(String url, Map<String, String> headers)  throws IOException;

    public Map<String, Object> delete(String url, Map<String, String> headers)  throws IOException;

    public Map<String, Object> put(String url, Map<String, String> headers, Map<String, Object> payload)  throws IOException;

    public Map<String, Object> post(String url, Map<String, String> headers, Map<String, Object> payload)  throws IOException;

}
