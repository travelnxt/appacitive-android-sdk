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
    }

    private List<ObjectContainer> nodes;
    private List<ConnectionContainer> edges;

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
        return json;
    }

//    public ObjectCreateContainer addNewNode(AppacitiveObjectBase object)
//    {
//        ObjectCreateContainer container;
//        container = new ObjectCreateContainer();
//        container.object = object;
//        nodes.add(container);
//        return container;
//    }

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

//    public ObjectUpdateContainer updateExistingNode(AppacitiveObjectBase object, String name)
//    {
//        ObjectUpdateContainer container = new ObjectUpdateContainer();
//        container.object = object;
//        nodes.add(container);
//        return container;
//    }
//
//    public ObjectUpdateContainer updateExistingNodeWithRevision(AppacitiveObjectBase object, String name, int revision)
//    {
//        ObjectUpdateContainer container = new ObjectUpdateContainer();
//        container.object = object;
//        container.revision = revision;
//        nodes.add(container);
//        return container;
//    }

//    public ConnectionUpdateContainer updateExistingEdge(AppacitiveConnection connection, String name)
//    {
//        ConnectionUpdateContainer container = new ConnectionUpdateContainer();
//        container.connection = connection;
//        edges.add(container);
//        return container;
//    }
//
//    public ConnectionUpdateContainer updateExistingEdge(AppacitiveConnection connection, String name, int revision)
//    {
//        ConnectionUpdateContainer container = new ConnectionUpdateContainer();
//        container.connection = connection;
//        container.revision = revision;
//        edges.add(container);
//        return container;
//    }
//
//    public ConnectionCreateContainer addNewEdge(AppacitiveConnection connection)
//    {
//        ConnectionCreateContainer container = new ConnectionCreateContainer();
//        container.connection = connection;
//        edges.add(container);
//        return container;
//    }
//
//    public ConnectionCreateContainer addNewEdge(AppacitiveConnection connection, String name, String labelA, ObjectContainer endpointA, String labelB, ObjectContainer endpointB)
//    {
//        ConnectionCreateContainer container = new ConnectionCreateContainer();
//        container.connection = connection;
//        if(name != null)
//            container.name = name;
//
//        if(endpointA != null){
//            container.connection.endpointA.setName(endpointA.name);
//            container.connection.endpointA.label = labelA;
//        }
//        if(endpointB != null) {
//            container.connection.endpointB.setName(endpointB.name);
//            container.connection.endpointB.label = labelB;
//        }
//        edges.add(container);
//
//        return container;
//    }

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
