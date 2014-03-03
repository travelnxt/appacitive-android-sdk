package com.appacitive.sdk;

import com.appacitive.sdk.exceptions.AppacitiveException;
import com.appacitive.sdk.exceptions.ValidationException;
import com.appacitive.sdk.infra.APSerializable;
import com.appacitive.sdk.infra.APContainer;
import com.appacitive.sdk.infra.Headers;
import com.appacitive.sdk.infra.Urls;
import com.appacitive.sdk.interfaces.Http;
import com.appacitive.sdk.model.*;
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
public class AppacitiveObject extends AppacitiveEntity implements Serializable, APSerializable {

    public final static Logger LOGGER = Logger.getLogger(AppacitiveObject.class.getName());

    public void setSelf(Map<String, Object> entity) {

        super.setSelf(entity);

        if (entity != null) {

            Object object = entity.get("__typeid");
            if (object != null)
                this.typeId = Long.parseLong(object.toString());

            object = entity.get("__type");
            if (object != null)
                this.type = object.toString();

        }
    }

    public AppacitiveObject(String type) {
        this.type = type;
    }

    public AppacitiveObject(long typeId) {
        this.typeId = typeId;
    }

    public Map<String, Object> getMap() {
        Map<String, Object> nativeMap = super.getMap();
        nativeMap.put("__type", this.type);
        nativeMap.put("__typeid", String.valueOf(this.typeId));

        return nativeMap;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setTypeId(long typeId) {
        this.typeId = typeId;
    }

    private String type = null;

    private long typeId = 0;

    public String getType() {
        return type;
    }

    public long getTypeId() {
        return typeId;
    }

    public void createInBackground(Callback<AppacitiveObject> callback) throws ValidationException {
        if ((type == null || this.type.isEmpty()) && (typeId <= 0)) {
            throw new ValidationException("Type and TypeId both cannot be empty while creating an object.");
        }

        final String url = Urls.ForObject.createObjectUrl(this.type).toString();
        final Map<String, String> headers = Headers.assemble();
        final Map<String, Object> payload = this.getMap();
        Future<Map<String, Object>> future = ExecutorServiceWrapper.submit(new Callable<Map<String, Object>>() {
            @Override
            public Map<String, Object> call() throws Exception {
                return APContainer.build(Http.class).put(url, headers, payload);
            }
        });
        AppacitiveStatus status;
        boolean isSuccessful;
        AppacitiveException exception = null;
        try {
            Map<String, Object> responseMap = future.get();
            status = new AppacitiveStatus((Map<String, Object>) responseMap.get("status"));
            isSuccessful = status.isSuccessful();
            if (isSuccessful) {
                this.setSelf((Map<String, Object>) responseMap.get("object"));
                this.resetUpdateCommands();
            } else
                exception = new AppacitiveException(status);

        } catch (Exception e) {
            LOGGER.log(Level.ALL, e.getMessage());
            if (callback != null) callback.failure(null, e);
            return;
        }
        if (callback != null) {
            if (isSuccessful)
                callback.success(this);
            else
                callback.failure(null, exception);
        }
    }

    public static void getInBackground(String type, long id, List<String> fields, Callback<AppacitiveObject> callback) throws ValidationException {
        if (type == null || type.isEmpty())
            throw new ValidationException("Type cannot be empty.");
        if (id <= 0)
            throw new ValidationException("Object id should be greater than equal to 0.");

        final String url = Urls.ForObject.getObjectUrl(type, id, fields).toString();
        final Map<String, String> headers = Headers.assemble();

        Future<Map<String, Object>> future = ExecutorServiceWrapper.submit(new Callable<Map<String, Object>>() {
            @Override
            public Map<String, Object> call() throws Exception {
                return APContainer.build(Http.class).get(url, headers);
            }
        });
        AppacitiveStatus status;
        boolean isSuccessful;
        AppacitiveObject object = null;
        AppacitiveException exception = null;
        try {
            Map<String, Object> responseMap = future.get();
            status = new AppacitiveStatus((Map<String, Object>) responseMap.get("status"));
            isSuccessful = status.isSuccessful();
            if (isSuccessful) {
                object = new AppacitiveObject("");
                object.setSelf((Map<String, Object>) responseMap.get("object"));
            } else
                exception = new AppacitiveException(status);
        } catch (Exception e) {
            LOGGER.log(Level.ALL, e.getMessage());
            if (callback != null) callback.failure(null, e);
            return;
        }
        if (callback != null) {
            if (isSuccessful)
                callback.success(object);
            else
                callback.failure(null, exception);
        }

    }

    public void deleteInBackground(boolean deleteConnections, Callback<Void> callback) {
        final String url = Urls.ForObject.deleteObjectUrl(this.type, this.getId(), deleteConnections).toString();
        final Map<String, String> headers = Headers.assemble();
        Future<Map<String, Object>> future = ExecutorServiceWrapper.submit(new Callable<Map<String, Object>>() {
            @Override
            public Map<String, Object> call() throws Exception {
                return APContainer.build(Http.class).delete(url, headers);
            }
        });
        AppacitiveStatus status;
        boolean isSuccessful;
        AppacitiveException exception = null;
        try {
            Map<String, Object> responseMap = future.get();
            status = new AppacitiveStatus((Map<String, Object>) responseMap.get("status"));
            isSuccessful = status.isSuccessful();
            if (isSuccessful == false)
                exception = new AppacitiveException(status);

        } catch (Exception e) {
            LOGGER.log(Level.ALL, e.getMessage());
            if (callback != null) callback.failure(null, e);
            return;
        }
        if (callback != null) {
            if (isSuccessful)
                callback.success(null);
            else
                callback.failure(null, exception);
        }

    }

    public static void bulkDeleteInBackground(String type, List<Long> objectIds, Callback<Void> callback) {
        final String url = Urls.ForObject.bulkDeleteObjectUrl(type).toString();
        final Map<String, String> headers = Headers.assemble();
        final Map<String, Object> payload = new HashMap<String, Object>();
        List<String> strIds = new ArrayList<String>();
        for (long id : objectIds) {
            strIds.add(String.valueOf(id));
        }
        payload.put("idlist", strIds);

        // API should accept ids without quotes
        Future<Map<String, Object>> future = ExecutorServiceWrapper.submit(new Callable<Map<String, Object>>() {
            @Override
            public Map<String, Object> call() throws Exception {
                return APContainer.build(Http.class).post(url, headers, payload);
            }
        });
        AppacitiveStatus status;
        boolean isSuccessful;
        AppacitiveException exception = null;
        try {
            Map<String, Object> responseMap = future.get();
            status = new AppacitiveStatus((Map<String, Object>) responseMap.get("status"));
            isSuccessful = status.isSuccessful();
            if (isSuccessful == false)
                exception = new AppacitiveException(status);
        } catch (Exception e) {
            LOGGER.log(Level.ALL, e.getMessage());
            if (callback != null) callback.failure(null, e);
            return;
        }
        if (callback != null) {
            if (isSuccessful)
                callback.success(null);
            else
                callback.failure(null, exception);
        }
    }

    public void updateInBackground(boolean withRevision, Callback<AppacitiveObject> callback) {
        final String url = Urls.ForObject.updateObjectUrl(this.type, this.getId(), withRevision, this.getRevision()).toString();
        final Map<String, String> headers = Headers.assemble();
        final Map<String, Object> payload = new HashMap<String, Object>();
        payload.putAll(super.getUpdateCommand());
        Future<Map<String, Object>> future = ExecutorServiceWrapper.submit(new Callable<Map<String, Object>>() {
            @Override
            public Map<String, Object> call() throws Exception {
                return APContainer.build(Http.class).post(url, headers, payload);
            }
        });
        AppacitiveStatus status;
        boolean isSuccessful;
        AppacitiveException exception = null;
        try {
            Map<String, Object> responseMap = future.get();
            status = new AppacitiveStatus((Map<String, Object>) responseMap.get("status"));
            isSuccessful = status.isSuccessful();
            if (isSuccessful) {
                this.resetUpdateCommands();
                this.setSelf((Map<String, Object>) responseMap.get("object"));
            } else
                exception = new AppacitiveException(status);

        } catch (Exception e) {
            LOGGER.log(Level.ALL, e.getMessage());
            if (callback != null) callback.failure(null, e);
            return;
        }
        if (callback != null) {
            if (isSuccessful)
                callback.success(this);
            else
                callback.failure(null, exception);
        }
    }

    public void fetchLatestInBackground(Callback<Void> callback) {
        final String url = Urls.ForObject.getObjectUrl(type, this.getId(), null).toString();
        final Map<String, String> headers = Headers.assemble();

        Future<Map<String, Object>> future = ExecutorServiceWrapper.submit(new Callable<Map<String, Object>>() {
            @Override
            public Map<String, Object> call() throws Exception {
                return APContainer.build(Http.class).get(url, headers);
            }
        });
        AppacitiveStatus status;
        boolean isSuccessful;
        AppacitiveException exception = null;
        try {
            Map<String, Object> responseMap = future.get();
            status = new AppacitiveStatus((Map<String, Object>) responseMap.get("status"));
            isSuccessful = status.isSuccessful();
            if (isSuccessful) {
                this.setSelf((Map<String, Object>) responseMap.get("object"));
                this.resetUpdateCommands();
            } else
                exception = new AppacitiveException(status);


        } catch (Exception e) {
            LOGGER.log(Level.ALL, e.getMessage());
            if (callback != null) callback.failure(null, e);
            return;
        }
        if (callback != null) {
            if (isSuccessful)
                callback.success(null);
            else
                callback.failure(null, exception);
        }
    }

    public static void multiGetInBackground(String type, List<Long> ids, List<String> fields, Callback<List<AppacitiveObject>> callback) throws ValidationException {
        if (type.isEmpty())
            throw new ValidationException("Type cannot be empty.");
        final String url = Urls.ForObject.multiGetObjectUrl(type, ids, fields).toString();
        final Map<String, String> headers = Headers.assemble();
        Future<Map<String, Object>> future = ExecutorServiceWrapper.submit(new Callable<Map<String, Object>>() {
            @Override
            public Map<String, Object> call() throws Exception {
                return APContainer.build(Http.class).get(url, headers);
            }
        });
        AppacitiveStatus status;
        boolean isSuccessful;
        List<AppacitiveObject> returnObjects = null;
        AppacitiveException exception = null;
        try {
            Map<String, Object> responseMap = future.get();
            status = new AppacitiveStatus((Map<String, Object>) responseMap.get("status"));
            isSuccessful = status.isSuccessful();
            if (isSuccessful) {
                List<Object> objects = (ArrayList<Object>) responseMap.get("objects");
                returnObjects = new ArrayList<AppacitiveObject>();
                for (Object obj : objects) {
                    AppacitiveObject object = new AppacitiveObject("");
                    object.setSelf((Map<String, Object>) obj);
                    returnObjects.add(object);
                }
            } else
                exception = new AppacitiveException(status);
        } catch (Exception e) {
            LOGGER.log(Level.ALL, e.getMessage());
            if (callback != null) callback.failure(null, e);
            return;

        }
        if (callback != null) {
            if (isSuccessful)
                callback.success(returnObjects);
            else
                callback.failure(null, exception);
        }

    }

    public static void findInBackground(String type, AppacitiveQuery query, List<String> fields, Callback<PagedList<AppacitiveObject>> callback) {
        final String url = Urls.ForObject.findObjectsUrl(type, query, fields).toString();
        final Map<String, String> headers = Headers.assemble();

        Future<Map<String, Object>> future = ExecutorServiceWrapper.submit(new Callable<Map<String, Object>>() {
            @Override
            public Map<String, Object> call() throws Exception {
                return APContainer.build(Http.class).get(url, headers);
            }
        });
        AppacitiveStatus status;
        boolean isSuccessful;
        PagedList<AppacitiveObject> pagedResult = null;
        AppacitiveException exception = null;
        try {
            Map<String, Object> responseMap = future.get();
            status = new AppacitiveStatus((Map<String, Object>) responseMap.get("status"));
            isSuccessful = status.isSuccessful();
            if (isSuccessful) {
                ArrayList<Object> objects = (ArrayList<Object>) responseMap.get("objects");
                List<AppacitiveObject> returnObjects = new ArrayList<AppacitiveObject>();
                for (Object obj : objects) {
                    AppacitiveObject object = new AppacitiveObject("");
                    object.setSelf((Map<String, Object>) obj);
                    returnObjects.add(object);
                }
                pagedResult = new PagedList<AppacitiveObject>((Map<String, Object>) responseMap.get("paginginfo"));
                pagedResult.results = returnObjects;
            } else
                exception = new AppacitiveException(status);
        } catch (Exception e) {
            LOGGER.log(Level.ALL, e.getMessage());
            if (callback != null) callback.failure(null, e);
            return;
        }
        if (callback != null) {
            if (isSuccessful)
                callback.success(pagedResult);
            else
                callback.failure(null, exception);
        }
    }

    public static void findInBetweenTwoObjectsInBackground(String type, long objectAId, String relationA, String labelA, long objectBId, String relationB, String labelB, List<String> fields, Callback<PagedList<AppacitiveObject>> callback) {
        final String url = Urls.ForObject.findBetweenTwoObjectsUrl(type, objectAId, relationA, labelA, objectBId, relationB, labelB, fields).toString();
        final Map<String, String> headers = Headers.assemble();
        Future<Map<String, Object>> future = ExecutorServiceWrapper.submit(new Callable<Map<String, Object>>() {
            @Override
            public Map<String, Object> call() throws Exception {
                return APContainer.build(Http.class).get(url, headers);
            }
        });

        AppacitiveStatus status;
        boolean isSuccessful;
        PagedList<AppacitiveObject> pagedResult = null;
        AppacitiveException exception = null;
        try {
            Map<String, Object> responseMap = future.get();
            status = new AppacitiveStatus((Map<String, Object>) responseMap.get("status"));
            isSuccessful = status.isSuccessful();
            if (isSuccessful) {
                ArrayList<Object> objects = (ArrayList<Object>) responseMap.get("objects");
                List<AppacitiveObject> returnObjects = new ArrayList<AppacitiveObject>();
                for (Object obj : objects) {
                    AppacitiveObject object = new AppacitiveObject("");
                    object.setSelf((Map<String, Object>) obj);
                    returnObjects.add(object);
                }
                pagedResult = new PagedList<AppacitiveObject>((Map<String, Object>) responseMap.get("paginginfo"));
                pagedResult.results = returnObjects;
            } else
                exception = new AppacitiveException(status);
        } catch (Exception e) {
            LOGGER.log(Level.ALL, e.getMessage());
            if (callback != null) callback.failure(null, e);
            return;
        }
        if (callback != null) {
            if (isSuccessful)
                callback.success(pagedResult);
            else
                callback.failure(null, exception);
        }
    }

    public static void getConnectedObjectsInBackground(String relationType, String objectType, long objectId, AppacitiveQuery query, List<String> fields, Callback<ConnectedObjectsResponse> callback) {
        final String url = Urls.ForConnection.getConnectedObjectsUrl(relationType, objectType, objectId, query, fields).toString();
        final Map<String, String> headers = Headers.assemble();
        Future<Map<String, Object>> future = ExecutorServiceWrapper.submit(new Callable<Map<String, Object>>() {
            @Override
            public Map<String, Object> call() throws Exception {
                return APContainer.build(Http.class).get(url, headers);
            }
        });
        AppacitiveStatus status;
        boolean isSuccessful;
        ConnectedObjectsResponse response = null;
        AppacitiveException exception = null;
        try {
            Map<String, Object> responseMap = future.get();
            status = new AppacitiveStatus((Map<String, Object>) responseMap.get("status"));
            isSuccessful = status.isSuccessful();
            if (isSuccessful) {

                response = new ConnectedObjectsResponse(responseMap.get("parent").toString());
                response.pagingInfo = new PagingInfo((Map<String, Object>) responseMap.get("paginginfo"));
                List<Object> nodeObjects = (ArrayList<Object>) (responseMap.get("nodes"));
                response.results = new ArrayList<ConnectedObject>();
                if (nodeObjects != null)
                    for (Object n : nodeObjects) {
                        Map<String, Object> obj_n = (Map<String, Object>) n;
                        ConnectedObject connectedObject = new ConnectedObject();
                        if (obj_n.containsKey("__edge")) {
                            connectedObject.connection = new AppacitiveConnection("");
                            connectedObject.connection.setSelf((Map<String, Object>) obj_n.get("__edge"));
                            obj_n.remove("__edge");
                        }
                        connectedObject.object = new AppacitiveObject("");
                        connectedObject.object.setSelf(obj_n);
                        response.results.add(connectedObject);
                    }
            } else
                exception = new AppacitiveException(status);
        } catch (Exception e) {
            LOGGER.log(Level.ALL, e.getMessage());
            if (callback != null) callback.failure(null, e);
            return;
        }
        if (callback != null) {
            if (isSuccessful)
                callback.success(response);
            else
                callback.failure(null, exception);
        }
    }

}
