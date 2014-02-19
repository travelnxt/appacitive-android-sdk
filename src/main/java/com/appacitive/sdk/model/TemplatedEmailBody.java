package com.appacitive.sdk.model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by sathley.
 */
public class TemplatedEmailBody extends EmailBody implements Serializable {

    public TemplatedEmailBody(String templateName, Map<String, String> data, boolean isHtml)
    {
        super(isHtml);
        this.templateName = templateName;
        this.data = data;
    }

    public TemplatedEmailBody(Map<String, Object> body) {
        super(body);
        Object object = body.get("templatename");
        if(object != null)
            this.templateName = object.toString();
        object = body.get("data");
        if(object != null)
            this.data = (Map<String, String>) object;
    }

    public String getTemplateName() {
        return templateName;
    }

    public Map<String, String> getData() {
        return data;
    }

    private String templateName = null;

    private Map<String, String> data = null;

    @Override
    public Map<String, Object> getMap() {

        Map<String, Object> nativeMap = new HashMap<String, Object>();
        nativeMap.putAll(super.getMap());
        nativeMap.put("templatename", this.templateName);
        nativeMap.put("data", this.data);
        return nativeMap;
    }
}
