package com.appacitive.sdk.core.model;

import com.appacitive.sdk.core.AppacitiveDevice;
import com.appacitive.sdk.core.AppacitiveEntity;
import com.appacitive.sdk.core.AppacitiveObject;
import com.appacitive.sdk.core.AppacitiveUser;
import com.appacitive.sdk.core.infra.APSerializable;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by sathley.
 */
public class AppacitiveEndpoint implements Serializable, APSerializable {

    public AppacitiveEndpoint(Map<String, Object> endpoint) {
        this.setSelf(endpoint);
    }

    public AppacitiveEndpoint() {
    }

    public Map<String, Object> getMap() {
        Map<String, Object> nativeMap = new HashMap<String, Object>();

        nativeMap.put("label", this.label);
        nativeMap.put("type", this.type);
        nativeMap.put("objectid", String.valueOf(this.objectId));
        if (this.object != null)
            nativeMap.put("object", this.object.getMap());

        return nativeMap;
    }

    public String label = null;

    public String type = null;

    public AppacitiveEntity object = null;

    public long objectId = 0;

    public void setSelf(Map<String, Object> endpoint) {
        if (endpoint != null) {
            Object object = endpoint.get("label");
            if (object != null)
                this.label = (String) object;

            object = endpoint.get("type");
            if (object != null)
                this.type = (String) object;

            object = endpoint.get("objectid");
            if (object != null)
                this.objectId = Long.parseLong(object.toString());

            object = endpoint.get("object");
            if (object != null) {
                Map<String, Object> objectMap = (Map<String, Object>) endpoint.get("object");
                if (this.object == null) {
                    String type = objectMap.get("__type").toString();

                    if (type.equals("user"))
                        this.object = new AppacitiveUser(objectMap);
                    else if (type.equals("device"))
                        this.object = new AppacitiveDevice(objectMap);
                    else
                        this.object = new AppacitiveObject(objectMap);
                } else
                    this.object.setSelf(objectMap);
            }
        }
    }
}
