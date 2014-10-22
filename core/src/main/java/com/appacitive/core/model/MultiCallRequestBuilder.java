package com.appacitive.core.model;

import com.appacitive.core.AppacitiveConnection;
import com.appacitive.core.AppacitiveEntity;
import com.appacitive.core.AppacitiveObject;
import com.appacitive.core.apjson.APJSONArray;
import com.appacitive.core.apjson.APJSONException;
import com.appacitive.core.apjson.APJSONObject;
import com.appacitive.core.infra.APSerializable;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by sathley on 8/8/2014.
 */
public class MultiCallRequestBuilder implements Serializable, APSerializable {

    public MultiCallRequestBuilder()
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
                if(container instanceof ObjectCreateContainer)
                {
                    nodeEntry.put("object", container.object.getMap());
                }
                else if(container instanceof ObjectUpdateContainer)
                {
                    if(((ObjectUpdateContainer) container).revision > 0)
                        nodeEntry.put("revision", ((ObjectUpdateContainer) container).revision);
                    nodeEntry.put("object", container.object.getUpdateCommand());
                }
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
                if(container instanceof ConnectionUpdateContainer)
                {
                    if(((ConnectionUpdateContainer) container).revision>0)
                        edgeEntry.put("revision", ((ConnectionUpdateContainer) container).revision);
                    edgeEntry.put("connection", container.connection.getUpdateCommand());
                }
                else if(container instanceof ConnectionCreateContainer)
                {
                    edgeEntry.put("connection", container.connection.getMap());
                }
                edgesArray.put(edgeEntry);
            }
            json.put("edges", edgesArray);
        }
        return json;
    }

    public abstract class ObjectContainer
    {
        public AppacitiveObjectBase object = null;
        public String name;
        public ObjectContainer()
        {
            name = UUID.randomUUID().toString().replaceAll("-", "");
        }
    }

    public abstract class ConnectionContainer
    {
        public AppacitiveConnection connection = null;
    }

    public class ObjectCreateContainer extends ObjectContainer{

    }

    public class ObjectUpdateContainer extends ObjectContainer
    {
        public int revision = 0;
    }

    public class ConnectionCreateContainer extends ConnectionContainer
    {
        String nameA;
        String nameB;
    }

    public class ConnectionUpdateContainer extends ConnectionContainer
    {
        public int revision = 0;
    }

    public ObjectCreateContainer addNewNode(AppacitiveObjectBase object)
    {
        ObjectCreateContainer container;
        container = new ObjectCreateContainer();
        container.object = object;
        nodes.add(container);
        return container;
    }

    public ObjectUpdateContainer addExistingNode(AppacitiveObjectBase object)
    {
        ObjectUpdateContainer container = new ObjectUpdateContainer();
        container.object = object;
        nodes.add(container);
        return container;
    }

    public ObjectUpdateContainer addExistingNode(AppacitiveObjectBase object, int revision)
    {
        ObjectUpdateContainer container = new ObjectUpdateContainer();
        container.object = object;
        container.revision = revision;
        nodes.add(container);
        return container;
    }

    public void addExistingEdge(AppacitiveConnection connection)
    {
        ConnectionUpdateContainer container = new ConnectionUpdateContainer();
        container.connection = connection;
        edges.add(container);
    }

    public void addExistingEdge(AppacitiveConnection connection, int revision)
    {
        ConnectionUpdateContainer container = new ConnectionUpdateContainer();
        container.connection = connection;
        container.revision = revision;
        edges.add(container);
    }

    public void addNewEdge(AppacitiveConnection connection, ObjectContainer endpointA, ObjectContainer endpointB)
    {
        ConnectionCreateContainer container = new ConnectionCreateContainer();
        container.connection = connection;
        if(endpointA != null)
            container.connection.endpointA.setName(endpointA.name);
        if(endpointB != null)
            container.connection.endpointB.setName(endpointB.name);
    }
}
