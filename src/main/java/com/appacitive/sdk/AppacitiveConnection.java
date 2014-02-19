package com.appacitive.sdk;

import com.appacitive.sdk.exceptions.AppacitiveException;
import com.appacitive.sdk.exceptions.ValidationException;
import com.appacitive.sdk.infra.*;
import com.appacitive.sdk.model.AppacitiveEndpoint;
import com.appacitive.sdk.model.AppacitiveStatus;
import com.appacitive.sdk.model.Callback;
import com.appacitive.sdk.model.PagedList;
import com.appacitive.sdk.query.AppacitiveQuery;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by sathley.
 */
public class AppacitiveConnection extends AppacitiveEntity implements Serializable, APSerializable {

    public final static Logger LOGGER = Logger.getLogger(AppacitiveConnection.class.getName());

//    public AppacitiveConnection(Map<String, Object> connection) {
//        this.setSelf(connection);
//    }

    public AppacitiveConnection(String relationType) {
        this.relationType = relationType;
    }

    public AppacitiveConnection(long relationId) {
        this.relationId = relationId;
    }

    public void setSelf(Map<String, Object> connection) {
        super.setSelf(connection);
        if (connection != null) {
            Object object = connection.get(SystemDefinedProperties.relationId);
            if (object != null)
                this.relationId = Long.parseLong(object.toString());

            object = connection.get(SystemDefinedProperties.relationType);
            if (object != null)
                this.relationType = object.toString();

            object = connection.get(SystemDefinedProperties.endpointA);
            if (object != null)
                this.endpointA.setSelf((Map<String, Object>) object);

            object = connection.get(SystemDefinedProperties.endpointB);
            if (object != null)
                this.endpointB.setSelf((Map<String, Object>) object);
        }
    }

    public Map<String, Object> getMap() {
        Map<String, Object> nativeMap = super.getMap();
        nativeMap.put("__relationtype", this.relationType);
        nativeMap.put("__relationid", String.valueOf(this.relationId));
        nativeMap.put("__endpointa", this.endpointA.getMap());
        nativeMap.put("__endpointb", this.endpointB.getMap());
        return nativeMap;
    }

    public String relationType = null;

    public long relationId = 0;

    public AppacitiveEndpoint endpointA = new AppacitiveEndpoint();

    public AppacitiveEndpoint endpointB = new AppacitiveEndpoint();

    public String getRelationType() {
        return relationType;
    }

    public long getRelationId() {
        return relationId;
    }

    public AppacitiveConnection fromNewObject(String label, AppacitiveObject object) {
        this.endpointA.label = label;
        this.endpointA.object = object;
        return this;
    }

    public AppacitiveConnection toNewObject(String label, AppacitiveObject object) {
        this.endpointB.label = label;
        this.endpointB.object = object;
        return this;
    }

    public AppacitiveConnection fromNewUser(String label, AppacitiveUser user) {
        this.endpointA.label = label;
        this.endpointA.object = user;
        return this;
    }

    public AppacitiveConnection toNewUser(String label, AppacitiveUser user) {
        this.endpointB.label = label;
        this.endpointB.object = user;
        return this;
    }

    public AppacitiveConnection fromNewDevice(String label, AppacitiveDevice device) {
        this.endpointA.label = label;
        this.endpointA.object = device;
        return this;
    }

    public AppacitiveConnection toNewDevice(String label, AppacitiveDevice device) {
        this.endpointB.label = label;
        this.endpointB.object = device;
        return this;
    }

    public AppacitiveConnection fromExistingObject(String label, long objectId) {
        this.endpointA.label = label;
        this.endpointA.objectId = objectId;
        return this;
    }

    public AppacitiveConnection toExistingObject(String label, long objectId) {
        this.endpointB.label = label;
        this.endpointB.objectId = objectId;
        return this;
    }

    public AppacitiveConnection fromExistingUser(String label, long userId) {
        return this.fromExistingObject(label, userId);
    }

    public AppacitiveConnection toExistingUser(String label, long userId) {
        return this.toExistingObject(label, userId);
    }

    public AppacitiveConnection fromExistingDevice(String label, long deviceId) {
        return this.fromExistingObject(label, deviceId);
    }

    public AppacitiveConnection toExistingDevice(String label, long deviceId) {
        return this.toExistingObject(label, deviceId);
    }

    public void createInBackground(Callback<AppacitiveConnection> callback) throws ValidationException {
        // validations
        if ((this.relationType == null || this.relationType.isEmpty()) && (this.relationId <= 0)) {
            throw new ValidationException("Relation Type and Relation Id both cannot be empty while creating an object.");
        }

        if (this.endpointA == null || this.endpointA.label == null || this.endpointA.label.isEmpty() || (this.endpointA.object == null && this.endpointA.objectId <= 0))
            throw new ValidationException("Endpoint A is not correctly initialized.");

        if (this.endpointB == null || this.endpointB.label == null || this.endpointB.label.isEmpty() || (this.endpointB.object == null && this.endpointB.objectId <= 0))
            throw new ValidationException("Endpoint B is not correctly initialized.");


        final String url = Urls.ForConnection.createConnectionUrl(this.relationType).toString();
        final Map<String, String> headers = Headers.assemble();
        final Map<String, Object> payload = this.getMap();
        Future<Map<String, Object>> future = ExecutorServiceWrapper.submit(new Callable<Map<String, Object>>() {
            @Override
            public Map<String, Object> call() throws Exception {
                return AppacitiveHttp.put(url, headers, payload);
            }
        });
        AppacitiveStatus status;
        boolean isSuccessful;
        try {
            Map<String, Object> responseMap = future.get();
            status = new AppacitiveStatus((Map<String, Object>) responseMap.get("status"));
            isSuccessful = status.isSuccessful();
            if (isSuccessful) {
                this.setSelf((Map<String, Object>) responseMap.get("connection"));
                this.resetUpdateCommands();
            }

        } catch (Exception e) {
            LOGGER.log(Level.ALL, e.getMessage());
            if (callback != null) callback.failure(null, e);
            return;
        }
        if (callback != null) {
            if (isSuccessful)
                callback.success(this);
            else
                callback.failure(null, new AppacitiveException(status));
        }
    }

    public static void getInBackground(String relationType, long id, List<String> fields, Callback<AppacitiveConnection> callback) throws ValidationException {
        if (relationType == null || relationType.isEmpty())
            throw new ValidationException("RelationType cannot be null or empty.");
        if (id <= 0)
            throw new ValidationException("Connection id should be greater than equal to 0.");

        final String url = Urls.ForConnection.getConnectionUrl(relationType, id, fields).toString();
        final Map<String, String> headers = Headers.assemble();

        Future<Map<String, Object>> future = ExecutorServiceWrapper.submit(new Callable<Map<String, Object>>() {
            @Override
            public Map<String, Object> call() throws Exception {
                return AppacitiveHttp.get(url, headers);
            }
        });
        AppacitiveStatus status;
        boolean isSuccessful;
        AppacitiveConnection connection = null;
        try {
            Map<String, Object> responseMap = future.get();
            status = new AppacitiveStatus((Map<String, Object>) responseMap.get("status"));
            isSuccessful = status.isSuccessful();
            if (isSuccessful) {
                connection = new AppacitiveConnection("");
                connection.setSelf((Map<String, Object>) responseMap.get("connection"));
            }

        } catch (Exception e) {
            LOGGER.log(Level.ALL, e.getMessage());
            if (callback != null) callback.failure(null, e);
            return;
        }
        if (callback != null) {
            if (isSuccessful)
                callback.success(connection);
            else
                callback.failure(null, new AppacitiveException(status));
        }
    }

    public void deleteInBackground(Callback<Void> callback) {
        final String url = Urls.ForConnection.deleteConnectionUrl(this.relationType, this.getId()).toString();
        final Map<String, String> headers = Headers.assemble();
        Future<Map<String, Object>> future = ExecutorServiceWrapper.submit(new Callable<Map<String, Object>>() {
            @Override
            public Map<String, Object> call() throws Exception {
                return AppacitiveHttp.delete(url, headers);
            }
        });

        boolean isSuccessful;
        AppacitiveStatus status;
        try {
            Map<String, Object> responseMap = future.get();
            status = new AppacitiveStatus((Map<String, Object>) responseMap.get("status"));
            isSuccessful = status.isSuccessful();

        } catch (Exception e) {
            LOGGER.log(Level.ALL, e.getMessage());
            if (callback != null) callback.failure(null, e);
            return;
        }
        if (callback != null) {
            if (isSuccessful)
                callback.success(null);
            else
                callback.failure(null, new AppacitiveException(status));
        }
    }

    public static void bulkDeleteInBackground(String relationType, List<Long> connectionIds, Callback<Void> callback) {
        final String url = Urls.ForConnection.bulkDeleteConnectionUrl(relationType).toString();
        final Map<String, String> headers = Headers.assemble();
        final Map<String, Object> payload = new HashMap<String, Object>();
        List<String> strIds = new ArrayList<String>();
        for (long id : connectionIds) {
            strIds.add(String.valueOf(id));
        }
        payload.put("idlist", strIds);

        // API should accept ids without quotes
        Future<Map<String, Object>> future = ExecutorServiceWrapper.submit(new Callable<Map<String, Object>>() {
            @Override
            public Map<String, Object> call() throws Exception {
                return AppacitiveHttp.post(url, headers, payload);
            }
        });

        boolean isSuccessful;
        AppacitiveStatus status;
        try {
            Map<String, Object> responseMap = future.get();
            status = new AppacitiveStatus((Map<String, Object>) responseMap.get("status"));
            isSuccessful = status.isSuccessful();

        } catch (Exception e) {
            LOGGER.log(Level.ALL, e.getMessage());
            if (callback != null) callback.failure(null, e);
            return;
        }
        if (callback != null) {
            if (isSuccessful)
                callback.success(null);
            else
                callback.failure(null, new AppacitiveException(status));
        }
    }

    public void updateInBackground(boolean withRevision, Callback<AppacitiveConnection> callback) {
        final String url = Urls.ForConnection.updateConnectionUrl(this.relationType, this.getId(), withRevision, this.getRevision()).toString();
        final Map<String, String> headers = Headers.assemble();
        final Map<String, Object> payload = new HashMap<String, Object>();
        payload.putAll(super.getUpdateCommand());
        Future<Map<String, Object>> future = ExecutorServiceWrapper.submit(new Callable<Map<String, Object>>() {
            @Override
            public Map<String, Object> call() throws Exception {
                return AppacitiveHttp.post(url, headers, payload);
            }
        });

        AppacitiveStatus status;
        boolean isSuccessful;
        try {
            Map<String, Object> responseMap = future.get();
            status = new AppacitiveStatus((Map<String, Object>) responseMap.get("status"));
            isSuccessful = status.isSuccessful();
            if (isSuccessful) {
                this.setSelf((Map<String, Object>) responseMap.get("connection"));
                this.resetUpdateCommands();
            }

        } catch (Exception e) {
            LOGGER.log(Level.ALL, e.getMessage());
            if (callback != null) callback.failure(null, e);
            return;
        }
        if (callback != null) {
            if (isSuccessful)
                callback.success(this);
            else
                callback.failure(null, new AppacitiveException(status));
        }
    }

    public void fetchLatestInBackground(Callback<Void> callback) {
        final String url = Urls.ForConnection.getConnectionUrl(relationType, this.getId(), null).toString();
        final Map<String, String> headers = Headers.assemble();

        Future<Map<String, Object>> future = ExecutorServiceWrapper.submit(new Callable<Map<String, Object>>() {
            @Override
            public Map<String, Object> call() throws Exception {
                return AppacitiveHttp.get(url, headers);
            }
        });

        AppacitiveStatus status;
        boolean isSuccessful;
        try {
            Map<String, Object> responseMap = future.get();
            status = new AppacitiveStatus((Map<String, Object>) responseMap.get("status"));
            isSuccessful = status.isSuccessful();
            if (isSuccessful) {
                this.setSelf((Map<String, Object>) responseMap.get("connection"));
                this.resetUpdateCommands();
            }

        } catch (Exception e) {
            LOGGER.log(Level.ALL, e.getMessage());
            if (callback != null) callback.failure(null, e);
            return;
        }
        if (callback != null) {
            if (isSuccessful)
                callback.success(null);
            else
                callback.failure(null, new AppacitiveException(status));
        }
    }

    public static void multiGetInBackground(String relationType, List<Long> ids, List<String> fields, Callback<List<AppacitiveConnection>> callback) throws ValidationException {
        if (relationType.isEmpty())
            throw new ValidationException("Relation Type cannot be empty.");
        final String url = Urls.ForConnection.multiGetConnectionUrl(relationType, ids, fields).toString();
        final Map<String, String> headers = Headers.assemble();

        Future<Map<String, Object>> future = ExecutorServiceWrapper.submit(new Callable<Map<String, Object>>() {
            @Override
            public Map<String, Object> call() throws Exception {
                return AppacitiveHttp.get(url, headers);
            }
        });
        AppacitiveStatus status;
        boolean isSuccessful;
        List<AppacitiveConnection> returnConnections = null;
        try {
            Map<String, Object> responseMap = future.get();
            status = new AppacitiveStatus((Map<String, Object>) responseMap.get("status"));
            isSuccessful = status.isSuccessful();
            if (isSuccessful) {
                ArrayList<Object> connections = (ArrayList<Object>) responseMap.get("connections");
                returnConnections = new ArrayList<AppacitiveConnection>();
                for (Object conn : connections) {
                    AppacitiveConnection connection = new AppacitiveConnection("");
                    connection.setSelf((Map<String, Object>) conn);
                    returnConnections.add(connection);
                }
            }
        } catch (Exception e) {
            LOGGER.log(Level.ALL, e.getMessage());
            if (callback != null) callback.failure(null, e);
            return;
        }
        if (callback != null) {
            if (isSuccessful)
                callback.success(returnConnections);
            else
                callback.failure(null, new AppacitiveException(status));
        }
    }

    public static void findInBackground(String relationType, AppacitiveQuery query, List<String> fields, Callback<PagedList<AppacitiveConnection>> callback) {
        final String url = Urls.ForConnection.findConnectionsUrl(relationType, query, fields).toString();
        final Map<String, String> headers = Headers.assemble();

        Future<Map<String, Object>> future = ExecutorServiceWrapper.submit(new Callable<Map<String, Object>>() {
            @Override
            public Map<String, Object> call() throws Exception {
                return AppacitiveHttp.get(url, headers);
            }
        });
        AppacitiveStatus status;
        boolean isSuccessful;
        PagedList<AppacitiveConnection> pagedResult = null;
        try {
            Map<String, Object> responseMap = future.get();
            status = new AppacitiveStatus((Map<String, Object>) responseMap.get("status"));
            isSuccessful = status.isSuccessful();
            if (isSuccessful) {
                ArrayList<Object> connections = (ArrayList<Object>) responseMap.get("connections");
                List<AppacitiveConnection> returnConnections = new ArrayList<AppacitiveConnection>();
                for (Object conn : connections) {
                    AppacitiveConnection connection = new AppacitiveConnection("");
                    connection.setSelf((Map<String, Object>) conn);
                    returnConnections.add(connection);
                }
                pagedResult = new PagedList<AppacitiveConnection>((Map<String, Object>) responseMap.get("paginginfo"));
                pagedResult.results = returnConnections;
            }

        } catch (Exception e) {
            LOGGER.log(Level.ALL, e.getMessage());
            if (callback != null) callback.failure(null, e);
            return;
        }
        if (callback != null) {
            if (isSuccessful)
                callback.success(pagedResult);
            else
                callback.failure(null, new AppacitiveException(status));
        }
    }

    public static void findByObjectsInBackground(long objectId1, long objectId2, List<String> fields, Callback<PagedList<AppacitiveConnection>> callback) {
        final String url = Urls.ForConnection.findForObjectsUrl(objectId1, objectId2, fields).toString();
        final Map<String, String> headers = Headers.assemble();

        Future<Map<String, Object>> future = ExecutorServiceWrapper.submit(new Callable<Map<String, Object>>() {
            @Override
            public Map<String, Object> call() throws Exception {
                return AppacitiveHttp.get(url, headers);
            }
        });

        AppacitiveStatus status;
        boolean isSuccessful;
        PagedList<AppacitiveConnection> pagedResult = null;
        try {
            Map<String, Object> responseMap = future.get();
            status = new AppacitiveStatus((Map<String, Object>) responseMap.get("status"));
            isSuccessful = status.isSuccessful();
            if (isSuccessful) {
                ArrayList<Object> connections = (ArrayList<Object>) responseMap.get("connections");
                List<AppacitiveConnection> returnConnections = new ArrayList<AppacitiveConnection>();
                for (Object conn : connections) {
                    AppacitiveConnection connection = new AppacitiveConnection("");
                    connection.setSelf((Map<String, Object>) conn);
                    returnConnections.add(connection);
                }
                pagedResult = new PagedList<AppacitiveConnection>((Map<String, Object>) responseMap.get("paginginfo"));
                pagedResult.results = returnConnections;
            }

        } catch (Exception e) {
            LOGGER.log(Level.ALL, e.getMessage());
            if (callback != null) callback.failure(null, e);
            return;
        }
        if (callback != null) {
            if (isSuccessful)
                callback.success(pagedResult);
            else
                callback.failure(null, new AppacitiveException(status));
        }
    }

    public static void findByObjectsAndRelationInBackground(String relationType, long objectId1, long objectId2, List<String> fields, Callback<AppacitiveConnection> callback) throws ValidationException {
        final String url = Urls.ForConnection.findForObjectsAndRelationUrl(relationType, objectId1, objectId2, fields).toString();
        final Map<String, String> headers = Headers.assemble();

        Future<Map<String, Object>> future = ExecutorServiceWrapper.submit(new Callable<Map<String, Object>>() {
            @Override
            public Map<String, Object> call() throws Exception {
                return AppacitiveHttp.get(url, headers);
            }
        });

        AppacitiveStatus status;
        boolean isSuccessful;
        AppacitiveConnection connection = null;
        try {
            Map<String, Object> responseMap = future.get();
            status = new AppacitiveStatus((Map<String, Object>) responseMap.get("status"));
            isSuccessful = status.isSuccessful();
            if (isSuccessful) {
                connection = new AppacitiveConnection("");
                connection.setSelf((Map<String, Object>) responseMap.get("connection"));
            }

        } catch (Exception e) {
            LOGGER.log(Level.ALL, e.getMessage());
            if (callback != null) callback.failure(null, e);
            return;
        }
        if (callback != null) {
            if (isSuccessful)
                callback.success(connection);
            else
                callback.failure(null, new AppacitiveException(status));
        }
    }

    public static void findInterconnectsInBackground(Long object1Id, List<Long> object2Ids, List<String> fields, Callback<PagedList<AppacitiveConnection>> callback) {
        final String url = Urls.ForConnection.findInterconnectsUrl(fields).toString();
        final Map<String, String> headers = Headers.assemble();
        final Map<String, Object> payload = new HashMap<String, Object>();
        List<String> strIds = new ArrayList<String>();
        for (long id : object2Ids)
            strIds.add(String.valueOf(id));
        payload.put("object1id", String.valueOf(object1Id));
        payload.put("object2ids", strIds);
        Future<Map<String, Object>> future = ExecutorServiceWrapper.submit(new Callable<Map<String, Object>>() {
            @Override
            public Map<String, Object> call() throws Exception {
                return AppacitiveHttp.post(url, headers, payload);
            }
        });

        AppacitiveStatus status;
        boolean isSuccessful;
        PagedList<AppacitiveConnection> pagedResult = null;
        try {
            Map<String, Object> responseMap = future.get();
            status = new AppacitiveStatus((Map<String, Object>) responseMap.get("status"));
            isSuccessful = status.isSuccessful();
            if (isSuccessful) {
                ArrayList<Object> connections = (ArrayList<Object>) responseMap.get("connections");
                List<AppacitiveConnection> returnConnections = new ArrayList<AppacitiveConnection>();
                for (Object conn : connections) {
                    AppacitiveConnection connection = new AppacitiveConnection("");
                    connection.setSelf((Map<String, Object>) conn);
                    returnConnections.add(connection);
                }
                pagedResult = new PagedList<AppacitiveConnection>((Map<String, Object>) responseMap.get("paginginfo"));
                pagedResult.results = returnConnections;
            }

        } catch (Exception e) {
            LOGGER.log(Level.ALL, e.getMessage());
            if (callback != null) callback.failure(null, e);
            return;
        }
        if (callback != null) {
            if (isSuccessful)
                callback.success(pagedResult);
            else
                callback.failure(null, new AppacitiveException(status));
        }
    }

    public static void findByObjectAndLabelInBackground(String relationType, long objectId, String label, List<String> fields, Callback<PagedList<AppacitiveConnection>> callback) {
        final String url = Urls.ForConnection.findByObjectAndLabelUrl(relationType, objectId, label, fields).toString();
        final Map<String, String> headers = Headers.assemble();

        Future<Map<String, Object>> future = ExecutorServiceWrapper.submit(new Callable<Map<String, Object>>() {
            @Override
            public Map<String, Object> call() throws Exception {
                return AppacitiveHttp.get(url, headers);
            }
        });

        AppacitiveStatus status;
        boolean isSuccessful;
        PagedList<AppacitiveConnection> pagedResult = null;
        try {
            Map<String, Object> responseMap = future.get();
            status = new AppacitiveStatus((Map<String, Object>) responseMap.get("status"));
            isSuccessful = status.isSuccessful();
            if (isSuccessful) {
                ArrayList<Object> connections = (ArrayList<Object>) responseMap.get("connections");
                List<AppacitiveConnection> returnConnections = new ArrayList<AppacitiveConnection>();
                for (Object conn : connections) {
                    AppacitiveConnection connection = new AppacitiveConnection("");
                    connection.setSelf((Map<String, Object>) conn);
                    returnConnections.add(connection);
                }
                pagedResult = new PagedList<AppacitiveConnection>((Map<String, Object>) responseMap.get("paginginfo"));
                pagedResult.results = returnConnections;
            }

        } catch (Exception e) {
            LOGGER.log(Level.ALL, e.getMessage());
            if (callback != null) callback.failure(null, e);
            return;
        }
        if (callback != null) {
            if (isSuccessful)
                callback.success(pagedResult);
            else
                callback.failure(null, new AppacitiveException(status));
        }
    }
}
