package appacitive.utilities;

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
import java.util.HashMap;
import java.util.Map;

/**
 * Created by sathley.
 */
public class AppacitiveHttp {

    public static final String UTF8_BOM = "\uFEFF";
    private final static String baseURL = "https://apis.appacitive.com/v1.0";

    public Map<String, Object> Get(String url, Map<String, String> headers) throws IOException
    {
        HttpClient client = HttpClientBuilder.create().build();
        HttpGet request = new HttpGet(url);

        for(Map.Entry<String, String> header : headers.entrySet())
        {
            request.addHeader(header.getKey(), header.getValue());
        }
        HttpResponse response = null;
        response = client.execute(request);
        return this.GetMap(response);
    }

    public Map<String, Object> Delete(String url, Map<String, String> headers) throws IOException
    {
        HttpClient client = HttpClientBuilder.create().build();
        HttpDelete request = new HttpDelete(url);

        for(Map.Entry<String, String> header : headers.entrySet())
        {
            request.addHeader(header.getKey(), header.getValue());
        }
        HttpResponse response = null;
        response = client.execute(request);
        return this.GetMap(response);
    }

    public Map<String, Object> Put(String url, Map<String, String> headers, Map<String, Object> payload) throws IOException
    {
        HttpClient client = HttpClientBuilder.create().build();
        HttpPut request = new HttpPut(url);

        for(Map.Entry<String, String> header : headers.entrySet()){

            request.addHeader(header.getKey(), header.getValue());
        }
        String PUTText = this.GetJson(payload);
        StringEntity entity = new StringEntity(PUTText, "UTF-8");
        entity.setContentType("application/json");
        request.setEntity(entity);
        HttpResponse response = null;
        response = client.execute(request);
        return this.GetMap(response);
    }

    public Map<String, Object> Post(String url, Map<String, String> headers, Map<String, Object> payload) throws IOException
    {
        HttpClient client = HttpClientBuilder.create().build();
        HttpPost request = new HttpPost(url);

        for(Map.Entry<String, String> header : headers.entrySet())
        {
            request.addHeader(header.getKey(), header.getValue());
        }
        String PUTText = this.GetJson(payload);
        StringEntity entity = new StringEntity(PUTText, "UTF-8");
        entity.setContentType("application/json");
        request.setEntity(entity);
        HttpResponse response = null;
        response = client.execute(request);
        return this.GetMap(response);
    }

    private Map<String, Object> GetMap(HttpResponse response) throws IOException
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
        return map;
    }

    private String GetJson(Map<String, Object> request) throws IOException
    {
        Writer writer = new StringWriter();
        JsonGenerator jsonGenerator = new JsonFactory().createJsonGenerator(writer);
        ObjectMapper mapper = new ObjectMapper();
        mapper.writeValue(jsonGenerator, request);
        jsonGenerator.close();
        return writer.toString();
    }
}

