package com.appacitive.sdk;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by sathley.
 */
public class AppacitiveEndpoint {
    public AppacitiveEndpoint(Map<String, Object> endpoint)
    {
        this.setSelf(endpoint);
    }

    public AppacitiveEndpoint()
    {}

    public Map<String, Object> getMap()
    {
        Map<String, Object> nativeMap = new HashMap<String, Object>();

        nativeMap.put("label", this.label);
        nativeMap.put("type", this.type);
        nativeMap.put("objectid", String.valueOf(this.objectId));
        if(this.object != null)
            nativeMap.put("object", this.object.getMap());

        return nativeMap;
    }

    public String label = null;

    public String type = null;

    public AppacitiveObject object = null;

    public long objectId = 0;

    public void setSelf(Map<String, Object> endpoint)
    {
        if(endpoint != null)
        {
            Object object = endpoint.get("label");
            if(object != null)
                this.label = (String)object;

            object = endpoint.get("type");
            if(object != null)
                this.type = (String)object;

            object = endpoint.get("objectid");
            if(object != null)
                this.objectId = Long.parseLong(object.toString());

            object = endpoint.get("object");
            if(object != null)
            {
                if(this.object == null)
                    this.object = new AppacitiveObject((Map<String, Object>)endpoint.get("object"));
                else
                    this.object.setSelf((Map<String, Object>)endpoint.get("object"));
            }
        }
    }
}
