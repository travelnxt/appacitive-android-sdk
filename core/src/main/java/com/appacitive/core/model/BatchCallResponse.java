package com.appacitive.core.model;

import com.appacitive.core.AppacitiveConnection;
import com.appacitive.core.AppacitiveDevice;
import com.appacitive.core.AppacitiveObject;
import com.appacitive.core.AppacitiveUser;
import com.appacitive.core.apjson.APJSONArray;
import com.appacitive.core.apjson.APJSONObject;
import com.appacitive.core.infra.SystemDefinedPropertiesHelper;
import com.appacitive.core.model.containers.ConnectionContainer;
import com.appacitive.core.model.containers.ObjectContainer;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sathley on 10/22/2014.
 */
public class BatchCallResponse {

    public BatchCallResponse(APJSONArray nodesArray, APJSONArray edgesArray) {
        this.nodes = new ArrayList<ObjectContainer>();
        this.edges = new ArrayList<ConnectionContainer>();

        for (int i = 0; i < nodesArray.length(); i++) {
            this.nodes.add(processNode(nodesArray.optJSONObject(i)));
        }

        for (int i = 0; i < edgesArray.length(); i++) {
            this.edges.add(processEdge(edgesArray.optJSONObject(i)));
        }
    }

    public List<ObjectContainer> nodes;

    public List<ConnectionContainer> edges;

    private ConnectionContainer processEdge(APJSONObject edge) {
        ConnectionContainer container = new ConnectionContainer();
        container.name = edge.optString("name", null);
        APJSONObject connection = edge.optJSONObject("connection");
        if (connection != null) {
            String type = connection.optString(SystemDefinedPropertiesHelper.relationType);
            container.connection = new AppacitiveConnection(type);
            container.connection.setSelf(connection);
        }
        return container;
    }

    private ObjectContainer processNode(APJSONObject node)
    {
        ObjectContainer container = new ObjectContainer();
        container.name = node.optString("name", null);
        APJSONObject object = node.optJSONObject("object");
        if (object != null) {

                String type = object.optString(SystemDefinedPropertiesHelper.type);
                if (type.equals("user")) {
                    container.object = new AppacitiveUser();
                    container.object.setSelf(object);
                } else if (type.equals("device")) {
                    container.object = new AppacitiveDevice();
                    container.object.setSelf(object);
                } else {
                    container.object = new AppacitiveObject(type);
                    container.object.setSelf(object);
                }
        }
        return container;
    }
}
