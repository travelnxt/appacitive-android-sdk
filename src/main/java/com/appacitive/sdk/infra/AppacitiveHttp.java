package com.appacitive.sdk.infra;

import com.appacitive.sdk.exceptions.AppacitiveException;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

/**
 * Created by sathley.
 */
public class AppacitiveHttp {

    private final static Logger LOGGER = Logger.getLogger(Package.class.getName());

    public static final String UTF8_BOM = "\uFEFF";


    public static Map<String, Object> get(String url, Map<String, String> headers) throws IOException, AppacitiveException
    {
        HttpClient client = HttpClientBuilder.create().build();
        HttpGet request = new HttpGet(url);

        for(Map.Entry<String, String> header : headers.entrySet())
        {
            request.addHeader(header.getKey(), header.getValue());
        }
        HttpResponse response = null;
        response = client.execute(request);
        return getMap(response);
    }

    public static Map<String, Object> delete(String url, Map<String, String> headers) throws IOException, AppacitiveException
    {
        HttpClient client = HttpClientBuilder.create().build();
        HttpDelete request = new HttpDelete(url);

        for(Map.Entry<String, String> header : headers.entrySet())
        {
            request.addHeader(header.getKey(), header.getValue());
        }
        HttpResponse response = null;
        response = client.execute(request);
        return getMap(response);
    }

    public static Map<String, Object> put(String url, Map<String, String> headers, Map<String, Object> payload) throws IOException, AppacitiveException
    {
        HttpClient client = HttpClientBuilder.create().build();
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

    public static Map<String, Object> post(String url, Map<String, String> headers, Map<String, Object> payload) throws IOException, AppacitiveException
    {
        HttpClient client = HttpClientBuilder.create().build();
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

    private static Map<String, Object> getMap(HttpResponse response) throws IOException, AppacitiveException
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
        ObjectMapper mapper = new ObjectMapper();
        Map<String,Object> map = new HashMap<String,Object>();
            map = mapper.readValue(strResponse,
                    new TypeReference<HashMap<String,Object>>(){});
        Map<String, Object> status = (Map<String, Object>)map.get("status");
        if(((String) status.get("code")).equals("200") == false)
        {
            AppacitiveException e = new  AppacitiveException();
            e.code = (String)status.get("code");
            e.message = (String)status.get("message");
            e.referenceId = (String)status.get("referenceid");
            e.apiVersion = (String)status.get("version");
            e.additionalMessages = (ArrayList<String>)status.get("additionalmessages");

            throw e;
        }
        return map;
    }

    private static String getJson(Map<String, Object> request) throws IOException
    {
        Writer writer = new StringWriter();
        JsonGenerator jsonGenerator = new JsonFactory().createJsonGenerator(writer);
        ObjectMapper mapper = new ObjectMapper();
        mapper.writeValue(jsonGenerator, request);
        jsonGenerator.close();
        return writer.toString();
    }
}

