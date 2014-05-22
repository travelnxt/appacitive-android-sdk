package com.appacitive.core.infra;

import com.appacitive.core.AppacitiveContextBase;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by sathley.
 */
public class Headers implements Serializable {

    public static Map<String, String> assemble() {
        Map<String, String> headers = new HashMap<String, String>();
        String apiKey = AppacitiveContextBase.getApiKey();
        String environment = AppacitiveContextBase.getEnvironment();

        if(apiKey == null || environment == null)
            throw new RuntimeException("Appacitive context is not initialized.");

        headers.put("Appacitive-Apikey", apiKey);
        headers.put("Appacitive-Environment", environment);
        headers.put("Content-Type", "application/json");

        if (AppacitiveContextBase.getLoggedInUserToken() != null && AppacitiveContextBase.getLoggedInUserToken().isEmpty() == false)
            headers.put("Appacitive-User-Auth", AppacitiveContextBase.getLoggedInUserToken());

        return headers;
    }
}
