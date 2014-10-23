package com.appacitive.core.infra;

import com.appacitive.core.AppacitiveContextBase;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by sathley.
 */
public class Url implements Serializable {

    public final static Logger LOGGER = Logger.getLogger(Url.class.getName());

    public Url(String baseUrl, String endpoint, String suffix, Map<String, String> queryStringParameters) {
        this.queryStringParameters = new HashMap<String, String>();
        this.baseUrl = baseUrl;
        this.endpoint = endpoint;
        this.suffix = suffix;
        if (queryStringParameters != null)
            this.queryStringParameters = queryStringParameters;
        double[] location = AppacitiveContextBase.getCurrentLocation();
        if (location[0] != 0 && location[1] != 0 && endpoint.equals("user")) {
            this.queryStringParameters.put("lat", String.valueOf(location[0]));
            this.queryStringParameters.put("long", String.valueOf(location[1]));
        }
    }

    public String baseUrl = null;

    public String endpoint = null;

    public String suffix = null;

    public Map<String, String> queryStringParameters = null;

    @Override
    public String toString() {
        StringBuilder urlBuilder = new StringBuilder();
        urlBuilder.append(baseUrl).append("/").append(endpoint);
        if(suffix != null)
            urlBuilder.append("/").append(suffix);
        if (queryStringParameters.size() > 0)
            urlBuilder.append("?");

        String separator = "";
        for (Map.Entry<String, String> qsp : queryStringParameters.entrySet()) {
            urlBuilder.append(separator);
            separator = "&";
            try {
                urlBuilder.append(qsp.getKey()).append("=").append(URLEncoder.encode(qsp.getValue(), "UTF-8"));
            } catch (UnsupportedEncodingException e) {
                LOGGER.log(Level.ALL, e.getMessage());
            }
        }
        return urlBuilder.toString();
    }
}
