package com.appacitive.sdk.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by sathley.
 */
public class Link implements Serializable {

    private static final List<String> knownFields = new ArrayList<String>() {{
        add("name");
        add("authtype");
        add("username");
    }};

    public void setSelf(Map<String, Object> link) {
        if (link != null) {
            Object object = link.get("name");
            if (object != null)
                this.name = object.toString();

            object = link.get("authtype");
            if (object != null)
                this.authType = object.toString();

            object = link.get("username");
            if (object != null)
                this.username = object.toString();

            for (Map.Entry<String, Object> field : link.entrySet()) {
                if (knownFields.contains(field.getKey()) == false)
                    this.attributes.put(field.getKey(), field.getValue().toString());
            }
        }
    }

    public String name = null;

    public String authType = null;

    public String username = null;

    public Map<String, String> attributes = new HashMap<String, String>();

}
