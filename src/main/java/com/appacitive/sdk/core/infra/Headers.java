package com.appacitive.sdk.core.infra;

import com.appacitive.sdk.core.AppacitiveContext;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by sathley.
 */
public class Headers {

    public static Map<String, String> assemble()
    {
        Map<String, String> headers = new HashMap<String, String>();
        headers.put("Appacitive-Apikey", AppacitiveContext.apiKey);
        headers.put("Appacitive-Environment", AppacitiveContext.environment);
        headers.put("Content-Type", "application/json");

        if (AppacitiveContext.getLoggedInUserToken() != null && AppacitiveContext.getLoggedInUserToken().isEmpty() == false)
            headers.put("Appacitive-User-Auth", AppacitiveContext.getLoggedInUserToken());

        return headers;
    }
}
