package com.appacitive.sdk.infra;

import com.appacitive.sdk.interfaces.Http;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Map;
import java.util.logging.Logger;

/**
 * Created by sathley.
 */
public class AppacitiveHttp implements Http {

    public static final String UTF8_BOM = "\uFEFF";
    private final static Logger LOGGER = Logger.getLogger(Package.class.getName());

    private final static HttpClient getHttpClient() {
        return new DefaultHttpClient();
    }

    private static Map<String, Object> getMap(HttpResponse response) throws IOException {
        BufferedReader rd = null;
        rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "UTF8"));
        StringBuilder result = new StringBuilder();
        String line = "";
        while ((line = rd.readLine()) != null) {
            result.append(line);
        }
        String strResponse = result.toString();

        if (strResponse.startsWith(UTF8_BOM)) {
            strResponse = strResponse.substring(1);
        }
        return (Map<String, Object>) JSONValue.parse(strResponse);
    }

    private static String getJson(Map<String, Object> request) throws IOException {
        return new JSONObject(request).toString();
    }

    public Map<String, Object> get(String url, Map<String, String> headers) throws IOException {
        HttpClient client = getHttpClient();
        HttpGet request = new HttpGet(url);
        HttpResponse response = null;

        for (Map.Entry<String, String> header : headers.entrySet()) {
            request.addHeader(header.getKey(), header.getValue());
        }
        try {
            response = client.execute(request);
        } finally {
            request.releaseConnection();
        }
        return getMap(response);
    }

    public Map<String, Object> delete(String url, Map<String, String> headers) throws IOException {
        HttpClient client = getHttpClient();
        HttpDelete request = new HttpDelete(url);
        HttpResponse response = null;

        for (Map.Entry<String, String> header : headers.entrySet()) {
            request.addHeader(header.getKey(), header.getValue());
        }
        try {
            response = client.execute(request);
        } finally {
            request.releaseConnection();
        }
        return getMap(response);
    }

    public Map<String, Object> put(String url, Map<String, String> headers, Map<String, Object> payload) throws IOException {
        HttpClient client = getHttpClient();
        HttpPut request = new HttpPut(url);
        HttpResponse response = null;
        String PUTText = getJson(payload);
        StringEntity entity = new StringEntity(PUTText, "UTF-8");
        entity.setContentType("application/json");

        for (Map.Entry<String, String> header : headers.entrySet()) {

            request.addHeader(header.getKey(), header.getValue());
        }

        request.setEntity(entity);
        try {
            response = client.execute(request);
        } finally {
            request.releaseConnection();
        }
        return getMap(response);
    }

    public Map<String, Object> post(String url, Map<String, String> headers, Map<String, Object> payload) throws IOException {
        HttpClient client = getHttpClient();
        HttpPost request = new HttpPost(url);
        HttpResponse response = null;
        String PUTText = getJson(payload);
        StringEntity entity = new StringEntity(PUTText, "UTF-8");
        entity.setContentType("application/json");
        for (Map.Entry<String, String> header : headers.entrySet()) {
            request.addHeader(header.getKey(), header.getValue());
        }

        request.setEntity(entity);
        try {
            response = client.execute(request);
        } finally {
            request.releaseConnection();
        }
        return getMap(response);
    }
}

