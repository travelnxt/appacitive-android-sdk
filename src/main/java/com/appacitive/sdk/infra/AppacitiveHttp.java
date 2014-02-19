package com.appacitive.sdk.infra;

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

//import org.apache.http.impl.client.HttpClientBuilder;
//import org.apache.http.impl.client.HttpClients;
//import org.json.JSONObject;

/**
 * Created by sathley.
 */
public class AppacitiveHttp {

    private final static Logger LOGGER = Logger.getLogger(Package.class.getName());

    public static final String UTF8_BOM = "\uFEFF";

    private final static HttpClient getHttpClient()
    {
//        return AndroidHttpClient.newInstance(null);
        return new DefaultHttpClient();
    }


    public static Map<String, Object> get(String url, Map<String, String> headers) throws IOException
    {
//        HttpClient client = HttpClientBuilder.create().build();
        HttpClient client = getHttpClient();
        HttpGet request = new HttpGet(url);

        for(Map.Entry<String, String> header : headers.entrySet())
        {
            request.addHeader(header.getKey(), header.getValue());
        }
        HttpResponse response = null;
        response = client.execute(request);
        return getMap(response);
    }

    public static Map<String, Object> delete(String url, Map<String, String> headers) throws IOException
    {
        HttpClient client = getHttpClient();
        HttpDelete request = new HttpDelete(url);

        for(Map.Entry<String, String> header : headers.entrySet())
        {
            request.addHeader(header.getKey(), header.getValue());
        }
        HttpResponse response = null;
        response = client.execute(request);
        return getMap(response);
    }

    public static Map<String, Object> put(String url, Map<String, String> headers, Map<String, Object> payload) throws IOException
    {
        HttpClient client = getHttpClient();
        HttpPut request = new HttpPut(url);

        for(Map.Entry<String, String> header : headers.entrySet()){

            request.addHeader(header.getKey(), header.getValue());
        }
        String PUTText = getJson(payload);
        StringEntity entity = new StringEntity(PUTText, "UTF-8");
        entity.setContentType("application/json");
        request.setEntity(entity);
        HttpResponse response = null;
        response = client.execute(request);
        return getMap(response);
    }

    public static Map<String, Object> post(String url, Map<String, String> headers, Map<String, Object> payload) throws IOException
    {
        HttpClient client = getHttpClient();
        HttpPost request = new HttpPost(url);

        for(Map.Entry<String, String> header : headers.entrySet())
        {
            request.addHeader(header.getKey(), header.getValue());
        }
        String PUTText = getJson(payload);
        StringEntity entity = new StringEntity(PUTText, "UTF-8");
        entity.setContentType("application/json");
        request.setEntity(entity);
        HttpResponse response = null;
        response = client.execute(request);
        return getMap(response);
    }

    private static Map<String, Object> getMap(HttpResponse response) throws IOException
    {
        BufferedReader rd = null;
        rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "UTF8"));
        StringBuilder result = new StringBuilder();
        String line = "";
        while ((line = rd.readLine()) != null)
        {
            result.append(line);
        }
        String strResponse = result.toString();

        if (strResponse.startsWith(UTF8_BOM)) {
            strResponse = strResponse.substring(1);
        }
        return (Map<String, Object>)JSONValue.parse(strResponse);
    }

    private static String getJson(Map<String, Object> request) throws IOException
    {
        return new JSONObject(request).toString();
    }
}

