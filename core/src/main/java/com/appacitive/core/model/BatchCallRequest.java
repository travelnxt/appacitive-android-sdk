package com.appacitive.core.model;

import com.appacitive.core.AppacitiveConnection;
import com.appacitive.core.apjson.APJSONArray;
import com.appacitive.core.apjson.APJSONException;
import com.appacitive.core.apjson.APJSONObject;
import com.appacitive.core.infra.APSerializable;
import com.appacitive.core.model.containers.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by sathley on 8/8/2014.
 */
public class BatchCallRequest implements Serializable, APSerializable {

    public BatchCallRequest()
    {
        nodes = new ArrayList<ObjectContainer>();
        edges = new ArrayList<ConnectionContainer>();
        nodeDeletions = new ArrayList<ObjectDeleteContainer>();
        edgeDeletions = new ArrayList<ConnectionDeleteContainer>();
    }

    private List<ObjectContainer> nodes;
    private List<ConnectionContainer> edges;
    private List<ObjectDeleteContainer> nodeDeletions;
    private List<ConnectionDeleteContainer> edgeDeletions;

    @Override
    public void setSelf(APJSONObject APEntity) {
        return;
    }

    @Override
    public APJSONObject getMap() throws APJSONException {
        APJSONObject json = new APJSONObject();
        if(nodes.size() > 0)
        {
            APJSONArray nodesArray = new APJSONArray();
            for (ObjectContainer container : nodes)
            {
                APJSONObject nodeEntry = new APJSONObject();
                nodeEntry.put("name", container.name);

                if(container.revision > 0)
                    nodeEntry.put("revision", container.revision);

                if(container.object.getId() > 0)
                    nodeEntry.put("object", container.object.getUpdateCommand());
                else
                    nodeEntry.put("object", container.object.getMap());

                nodesArray.put(nodeEntry);
            }
            json.put("nodes", nodesArray);
        }

        if(edges.size() > 0)
        {
            APJSONArray edgesArray = new APJSONArray();
            for(ConnectionContainer container : edges)
            {
                APJSONObject edgeEntry = new APJSONObject();
                edgeEntry.put("name", container.name);

                if(container.revision > 0)
                    edgeEntry.put("revision", container.revision);

                if(container.connection.getId() > 0)
                    edgeEntry.put("connection", container.connection.getUpdateCommand());
                else
                    edgeEntry.put("connection", container.connection.getMap());

                edgesArray.put(edgeEntry);
            }
            json.put("edges", edgesArray);
        }

        if(nodeDeletions.size() > 0)
        {
            APJSONArray nodeDeletionsArray = new APJSONArray();
            for (ObjectDeleteContainer container : nodeDeletions)
            {
                APJSONObject nodeEntry = new APJSONObject();
                nodeEntry.put("type", container.type);
                nodeEntry.put("id", container.id);
                nodeEntry.put("revision", container.revision);
                nodeEntry.put("deleteconnections", container.deleteConnections);

                nodeDeletionsArray.put(nodeEntry);
            }
            json.put("nodedeletions", nodeDeletionsArray);
        }

        if(nodeDeletions.size() > 0)
        {
            APJSONArray edgeDeletionsArray = new APJSONArray();
            for (ConnectionDeleteContainer container : edgeDeletions)
            {
                APJSONObject edgeEntry = new APJSONObject();
                edgeEntry.put("type", container.relationType);
                edgeEntry.put("id", container.id);
                edgeEntry.put("revision", container.revision);

                edgeDeletionsArray.put(edgeEntry);
            }
            json.put("edgedeletions", edgeDeletionsArray);
        }
        return json;
    }

    public ObjectDeleteContainer deleteNode(String type, long objectId, boolean deleteConnections)
    {
        ObjectDeleteContainer container = new ObjectDeleteContainer();
        container.id = objectId;
        container.type = type;
        container.deleteConnections = deleteConnections;
        nodeDeletions.add(container);
        return container;
    }

    public ObjectDeleteContainer deleteNode(String type, long objectId, boolean deleteConnections, long revision)
    {
        ObjectDeleteContainer container = new ObjectDeleteContainer();
        container.id = objectId;
        container.type = type;
        container.deleteConnections = deleteConnections;
        container.revision = revision;
        nodeDeletions.add(container);
        return container;
    }

    public ConnectionDeleteContainer deleteEdge(String relationType, long connectionId)
    {
        ConnectionDeleteContainer container = new ConnectionDeleteContainer();
        container.id = connectionId;
        container.relationType = relationType;
        edgeDeletions.add(container);
        return container;
    }

    public ConnectionDeleteContainer deleteEdge(String relationType, long connectionId, long revision)
    {
        ConnectionDeleteContainer container = new ConnectionDeleteContainer();
        container.id = connectionId;
        container.relationType = relationType;
        container.revision = revision;
        edgeDeletions.add(container);
        return container;
    }

    public ObjectContainer addNode(AppacitiveObjectBase object, String name)
    {
        ObjectContainer container = new ObjectContainer();
        container.object = object;
        if(name != null)
            container.name = name;
        nodes.add(container);
        return container;
    }

    public ObjectContainer addNodeWithRevision(AppacitiveObjectBase object, String name)
    {
        ObjectContainer container = new ObjectContainer();
        container.object = object;
        container.revision = object.getRevision();
        if(name != null)
            container.name = name;
        nodes.add(container);
        return container;
    }

    public ConnectionContainer addEdge(AppacitiveConnection connection, String name, String labelA, String nameA, String labelB, String nameB)
    {
        ConnectionContainer container = new ConnectionContainer();
        container.connection = connection;
        if(name != null)
            container.name = name;

        if(labelA != null)
            container.connection.endpointA.label = labelA;
        if(nameA != null){
            container.connection.endpointA.setName(nameA);
        }
        if(labelB != null)
            container.connection.endpointB.label = labelB;
        if(nameB != null){
            container.connection.endpointB.setName(nameB);
        }
        edges.add(container);

        return container;
    }

    public ConnectionContainer addEdgeWithRevision(AppacitiveConnection connection, String name, String labelA, String nameA, String labelB, String nameB)
    {
        ConnectionContainer container = new ConnectionContainer();
        container.connection = connection;
        container.revision = connection.getRevision();
        if(name != null)
            container.name = name;

        if(labelA != null)
            container.connection.endpointA.label = labelA;
        if(nameA != null){
            container.connection.endpointA.setName(nameA);
        }
        if(labelB != null)
            container.connection.endpointB.label = labelB;
        if(nameB != null){
            container.connection.endpointB.setName(nameB);
        }
        edges.add(container);

        return container;
    }
}
