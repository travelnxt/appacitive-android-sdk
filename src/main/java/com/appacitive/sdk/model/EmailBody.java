package com.appacitive.sdk.model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by sathley.
 */
public abstract class EmailBody implements Serializable {

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
        return new HashMap<String, Object>(){{
            put("ishtml", isHTML());
        }};
    }
}
