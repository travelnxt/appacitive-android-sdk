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
        headers.put("Appacitive-Apikey", AppacitiveContextBase.getApiKey());
        headers.put("Appacitive-Environment", AppacitiveContextBase.getEnvironment());
        headers.put("Content-Type", "application/json");

        if (AppacitiveContextBase.getLoggedInUserToken() != null && AppacitiveContextBase.getLoggedInUserToken().isEmpty() == false)
            headers.put("Appacitive-User-Auth", AppacitiveContextBase.getLoggedInUserToken());

        return headers;
    }
}
