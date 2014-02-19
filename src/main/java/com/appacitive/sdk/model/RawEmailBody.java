package com.appacitive.sdk.model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by sathley.
 */
public class RawEmailBody extends EmailBody implements Serializable {

    public RawEmailBody(String content, boolean isHTML)
    {
        super(isHTML);
        this.content = content;
    }

    public RawEmailBody(Map<String, Object> body) {
        super(body);
        Object object = body.get("content");
        if(object != null)
            this.content = object.toString();
    }

    public String getContent() {
        return content;
    }

    private String content = null;

    @Override
    public Map<String, Object> getMap() {
        Map<String, Object> nativeMap = new HashMap<String, Object>();
        nativeMap.putAll(super.getMap());
        nativeMap.put("content", this.content);
        return nativeMap;
    }
}
