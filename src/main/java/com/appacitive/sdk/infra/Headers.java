package com.appacitive.sdk.infra;

import com.appacitive.sdk.AppacitiveContext;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by sathley.
 */
public class Headers implements Serializable {

    public static Map<String, String> assemble()
    {
        Map<String, String> headers = new HashMap<String, String>();
        headers.put("Appacitive-Apikey", AppacitiveContext.getApiKey());
        headers.put("Appacitive-Environment", AppacitiveContext.getEnvironment());
        headers.put("Content-Type", "application/json");

        if (AppacitiveContext.getLoggedInUserToken() != null && AppacitiveContext.getLoggedInUserToken().isEmpty() == false)
            headers.put("Appacitive-User-Auth", AppacitiveContext.getLoggedInUserToken());

        return headers;
    }
}
