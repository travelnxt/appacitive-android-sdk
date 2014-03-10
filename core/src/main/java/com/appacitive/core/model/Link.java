package com.appacitive.core.model;

import com.appacitive.core.infra.APSerializable;
import com.appacitive.core.apjson.APJSONException;
import com.appacitive.core.apjson.APJSONObject;

import java.io.Serializable;
import java.util.*;

/**
 * Created by sathley.
 */
public class Link implements Serializable, APSerializable {

    private static final List<String> knownFields = new ArrayList<String>() {{
        add("name");
        add("authtype");
        add("username");
    }};

    public void setSelf(APJSONObject link) {
        if (link != null) {
            if (link.isNull("name") == false)
                this.name = link.optString("name");

            if (link.isNull("authtype") == false)
                this.authType = link.optString("authtype");

            if (link.isNull("username") == false)
                this.username = link.optString("username");
            Iterator iterator = link.keys();
            String key;
            while (iterator.hasNext()) {
                key = (String) iterator.next();
                if (knownFields.contains(key) == false)
                    this.attributes.put(key, link.optString(key));
            }
        }
    }

    @Override
    public APJSONObject getMap() throws APJSONException {
        return null;
    }

    public String name = null;

    public String authType = null;

    public String username = null;

    public Map<String, String> attributes = new HashMap<String, String>();

}
