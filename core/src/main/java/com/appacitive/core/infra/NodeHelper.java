package com.appacitive.core.infra;

import com.appacitive.core.AppacitiveConnection;
import com.appacitive.core.AppacitiveObject;
import com.appacitive.core.apjson.APJSONArray;
import com.appacitive.core.apjson.APJSONException;
import com.appacitive.core.apjson.APJSONObject;
import com.appacitive.core.model.AppacitiveGraphNode;

import java.util.Iterator;

/**
 * Created by sathley.
 */
public class NodeHelper {

    public static AppacitiveGraphNode convertNode(APJSONObject value) {
        return parseGraphNode(null, null, null, value);
    }

    private static AppacitiveGraphNode parseGraphNode(AppacitiveGraphNode parent, String name, String parentLabel, APJSONObject node) {
        AppacitiveGraphNode current = new AppacitiveGraphNode();
        APJSONObject nodeClone;
        try {
            nodeClone = new APJSONObject(node.toString());
        } catch (APJSONException e) {
            throw new RuntimeException(e);
        }
        if (nodeClone.has("__edge"))
            nodeClone.remove("__edge");

        if (nodeClone.has("__children"))
            nodeClone.remove("__children");

        current.object = new AppacitiveObject();
        current.object.setSelf(nodeClone);

        if (parent != null && node.has("__edge")) {
            APJSONObject edge = node.optJSONObject("__edge");
            APJSONObject edgeClone;
            try {
                edgeClone = new APJSONObject(edge.toString());
            } catch (APJSONException e) {
                throw new RuntimeException(e);
            }
            current.connection = parseConnection(parentLabel, parent.object, current.object, edgeClone);
        }

        if (node.has("__children")) {
            APJSONObject children = node.optJSONObject("__children");
            Iterator<String> iterator = children.keys();
            while (iterator.hasNext()) {
                String key = iterator.next();
                APJSONObject child = children.optJSONObject(key);
                if (child != null)
                    parseChildNodes(key, child, current);
            }
        }

        if (parent != null) {
            parent.addChildNode(name, current);
        }

        return current;
    }

    private static void parseChildNodes(String key, APJSONObject value, AppacitiveGraphNode current) {
        String parentLabel = value.optString("parent");
        APJSONArray values = value.optJSONArray("values");
        if (values != null) {
            for (int i = 0; i < values.length(); i++) {
                parseGraphNode(current, key, parentLabel, values.optJSONObject(i));
            }
        }
    }

    private static AppacitiveConnection parseConnection(String parentLabel, AppacitiveObject parentObject, AppacitiveObject currentObject, APJSONObject edgeClone) {

        String label = edgeClone.optString("__label", null);
        String relationType = edgeClone.optString(SystemDefinedPropertiesHelper.relationType, null);
        long connectionId = Long.valueOf(edgeClone.optString(SystemDefinedPropertiesHelper.id, "0"));

        AppacitiveConnection connection = new AppacitiveConnection();
        connection.setSelf(edgeClone);
        connection.relationType = relationType;
        connection.setId(connectionId);
        connection.endpointA.object = parentObject;
        connection.endpointA.label = parentLabel;
        connection.endpointA.objectId = parentObject.getId();

        connection.endpointB.label = label;
        connection.endpointB.object = currentObject;
        connection.endpointB.objectId = currentObject.getId();

        return connection;
    }
}
