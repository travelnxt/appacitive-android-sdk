package com.appacitive.sdk.infra;

import com.appacitive.sdk.AppacitiveConnection;
import com.appacitive.sdk.AppacitiveGraphNode;
import com.appacitive.sdk.AppacitiveObject;

import java.util.*;

/**
 * Created by sathley.
 */
public class NodeHelper {

    public static AppacitiveGraphNode convertNode(Map<String, Object> value) {
        return parseGraphNode(null, null, null, value);
    }

    private static AppacitiveGraphNode parseGraphNode(AppacitiveGraphNode parent, String name, String parentLabel, Map<String, Object> node) {
        AppacitiveGraphNode current = new AppacitiveGraphNode();
        Map<String, Object> nodeClone = new HashMap<String, Object>(node);
        Iterator<Map.Entry<String, Object>> iter = nodeClone.entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry<String, Object> entry = iter.next();
            if ("__edge".equalsIgnoreCase(entry.getKey()) || "__children".equalsIgnoreCase(entry.getKey())) {
                iter.remove();
            }
        }
        current.object = new AppacitiveObject(nodeClone);

        if (parent != null && node.containsKey("__edge")) {
            Map<String, Object> edge = (Map<String, Object>) node.get("__edge");
            Map<String, Object> edgeClone = new HashMap<String, Object>(edge);
            current.connection = parseConnection(parentLabel, parent.object, current.object, edgeClone);
        }

        if(node.containsKey("__children"))
        {
            for (Map.Entry<String, Object> property : ((Map<String, Object>)node.get("__children")).entrySet())
            {
                if(property.getValue() instanceof Map)
                {
                    parseChildNodes(property.getKey(), (Map<String, Object>)property.getValue(), current);
                }
            }
        }

        if(parent != null)
        {
            parent.addChildNode(name, current);
        }

        return current;
    }

    private static void parseChildNodes(String key, Map<String, Object> value, AppacitiveGraphNode current) {
        String parentLabel = (String)value.get("parent");
        if(value.get("values") instanceof List)
        {
            List<Object> values = (ArrayList<Object>) value.get("values");
            for(Object val : values)
            {
                parseGraphNode(current, key, parentLabel, (Map<String, Object>) val);
            }
        }
    }

    private static AppacitiveConnection parseConnection(String parentLabel, AppacitiveObject parentObject, AppacitiveObject currentObject, Map<String, Object> edgeClone) {

        String label = (String)edgeClone.get("__label");
        String relationType = (String)edgeClone.get(SystemDefinedProperties.relationType);
        long connectionId = Long.parseLong((String) edgeClone.get(SystemDefinedProperties.id));

        AppacitiveConnection connection = new AppacitiveConnection(edgeClone);
        connection.relationType = relationType;
        connection.id = connectionId;
        connection.endpointA.object = parentObject;
        connection.endpointA.label = parentLabel;
        connection.endpointA.objectId = parentObject.id;

        connection.endpointB.label = label;
        connection.endpointB.object = currentObject;
        connection.endpointB.objectId = currentObject.id;

        return connection;
    }
}
