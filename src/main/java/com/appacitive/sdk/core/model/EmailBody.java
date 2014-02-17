package com.appacitive.sdk.core.model;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by sathley.
 */
public abstract class EmailBody {

    public EmailBody(Map<String, Object> body) {
        Object object = body.get("ishtml");
        if(object != null)
            this.isHTML = Boolean.valueOf(object.toString());
    }

    public boolean isHTML() {
        return isHTML;
    }

    private boolean isHTML;

    public EmailBody(boolean isHTML)
    {
        this.isHTML = isHTML;
    }

    public Map<String, Object> getMap()
    {
        Map<String, Object> nativeMap = new HashMap<String, Object>();
        nativeMap.put("ishtml", this.isHTML);
        return nativeMap;
    }
}
