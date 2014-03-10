package com.appacitive.core.model;

import java.io.Serializable;

/**
 * Created by sathley.
 */
public class ConnectedObjectsResponse extends PagedList<ConnectedObject> implements Serializable {

    public String parent = null;

    public ConnectedObjectsResponse(String parent) {
        this.parent = parent;
    }

}
