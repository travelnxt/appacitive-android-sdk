package com.appacitive.core;

import com.appacitive.core.apjson.APJSONArray;
import com.appacitive.core.apjson.APJSONException;
import com.appacitive.core.apjson.APJSONObject;
import com.appacitive.core.exceptions.AppacitiveException;
import com.appacitive.core.exceptions.ValidationException;
import com.appacitive.core.infra.*;
import com.appacitive.core.interfaces.AsyncHttp;
import com.appacitive.core.model.*;
import com.appacitive.core.query.AppacitiveQuery;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

/**
 * Created by sathley.
 */
public class AppacitiveObject extends AppacitiveEntity implements Serializable, APSerializable {

    public final static Logger LOGGER = Logger.getLogger(AppacitiveObject.class.getName());

    public void setSelf(APJSONObject object) {

        super.setSelf(object);

        if (object != null) {
            if (object.isNull(SystemDefinedProperties.typeId) == false)
                this.typeId = object.optLong(SystemDefinedProperties.typeId);
            if (object.isNull(SystemDefinedProperties.type) == false)
                this.type = object.optString(SystemDefinedProperties.type);
        }
    }

    public AppacitiveObject(String type) {
        this.type = type;
    }

    public AppacitiveObject(long typeId) {
        this.typeId = typeId;
    }

    public APJSONObject getMap() throws APJSONException {
        APJSONObject nativeMap = super.getMap();
        nativeMap.put(SystemDefinedProperties.type, this.type);
        nativeMap.put(SystemDefinedProperties.typeId, String.valueOf(this.typeId));

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

    public void createInBackground(final Callback<AppacitiveObject> callback) throws ValidationException {
        if ((type == null || this.type.isEmpty()) && (typeId <= 0)) {
            throw new ValidationException("Type and TypeId, both cannot be missing while creating an object.");
        }

        final String url = Urls.ForObject.createObjectUrl(this.type).toString();
        final Map<String, String> headers = Headers.assemble();
        final APJSONObject payload;
        try {
            payload = this.getMap();
        } catch (APJSONException e) {
            throw new RuntimeException(e);
        }
        final AppacitiveObject object = this;
        AsyncHttp asyncHttp = APContainer.build(AsyncHttp.class);
        asyncHttp.put(url, headers, payload.toString(), new APCallback() {
            @Override
            public void success(String result) {
                try {
                    APJSONObject jsonResult = new APJSONObject(result);
                    AppacitiveStatus status = new AppacitiveStatus(jsonResult.optJSONObject("status"));
                    if (status.isSuccessful()) {
                        object.setSelf(jsonResult.optJSONObject("object"));
                        if (callback != null) {
                            callback.success(object);
                        }
                    } else {
                        if (callback != null)
                            callback.failure(null, new AppacitiveException(status));
                    }
                } catch (Exception e) {
                    if (callback != null)
                        callback.failure(null, e);
                }
            }

            @Override
            public void failure(Exception e) {
                if (callback != null)
                    callback.failure(null,e);
            }
        });
    }

    public static void getInBackground(String type, long id, List<String> fields, final Callback<AppacitiveObject> callback) throws ValidationException {
        if (type == null || type.isEmpty())
            throw new ValidationException("Type cannot be empty.");
        if (id <= 0)
            throw new ValidationException("Object id should be greater than or equal to 0.");

        final String url = Urls.ForObject.getObjectUrl(type, id, fields).toString();
        final Map<String, String> headers = Headers.assemble();
        AsyncHttp asyncHttp = APContainer.build(AsyncHttp.class);
        asyncHttp.get(url, headers, new APCallback() {
            @Override
            public void success(String result) {
                try {
                    APJSONObject jsonObject = new APJSONObject(result);
                    AppacitiveStatus status = new AppacitiveStatus(jsonObject.optJSONObject("status"));
                    if (status.isSuccessful()) {
                        AppacitiveObject object = new AppacitiveObject("");
                        object.setSelf(jsonObject.optJSONObject("object"));
                        if (callback != null)
                            callback.success(object);
                    } else {
                        if (callback != null)
                            callback.failure(null, new AppacitiveException(status));
                    }
                } catch (Exception e) {
                    if (callback != null)
                        callback.failure(null, e);
                }
            }

            @Override
            public void failure(Exception e) {
                if (callback != null)
                    callback.failure(null,e);
            }
        });
    }

    public void deleteInBackground(boolean deleteConnections, final Callback<Void> callback) {
        final String url = Urls.ForObject.deleteObjectUrl(this.type, this.getId(), deleteConnections).toString();
        final Map<String, String> headers = Headers.assemble();
        AsyncHttp asyncHttp = APContainer.build(AsyncHttp.class);
        asyncHttp.delete(url, headers, new APCallback() {
            @Override
            public void success(String result) {
                try {
                    APJSONObject jsonObject = new APJSONObject(result);
                    AppacitiveStatus status = new AppacitiveStatus(jsonObject.optJSONObject("status"));
                    if (status.isSuccessful()) {
                        if (callback != null)
                            callback.success(null);
                    } else {
                        if (callback != null)
                            callback.failure(null, new AppacitiveException(status));
                    }
                } catch (Exception e) {
                    if (callback != null)
                        callback.failure(null, e);
                }
            }

            @Override
            public void failure(Exception e) {
                if (callback != null)
                    callback.failure(null,e);
            }
        });
    }

    public static void bulkDeleteInBackground(String type, List<Long> objectIds, final Callback<Void> callback) {
        final String url = Urls.ForObject.bulkDeleteObjectUrl(type).toString();
        final Map<String, String> headers = Headers.assemble();

        final List<String> strIds = new ArrayList<String>();
        for (long id : objectIds) {
            strIds.add(String.valueOf(id));
        }

        final APJSONObject payload = new APJSONObject();
        try {
            payload.put("idlist", new APJSONArray(strIds));
        } catch (APJSONException e) {
            throw new RuntimeException(e);
        }
        AsyncHttp asyncHttp = APContainer.build(AsyncHttp.class);
        asyncHttp.post(url, headers, payload.toString(), new APCallback() {
            @Override
            public void success(String result) {
                try {
                    APJSONObject jsonObject = new APJSONObject(result);
                    AppacitiveStatus status = new AppacitiveStatus(jsonObject.optJSONObject("status"));
                    if (status.isSuccessful()) {
                        if (callback != null)
                            callback.success(null);
                    } else {
                        if (callback != null)
                            callback.failure(null, new AppacitiveException(status));
                    }
                } catch (Exception e) {
                    if (callback != null)
                        callback.failure(null, e);
                }
            }

            @Override
            public void failure(Exception e) {
                if (callback != null)
                    callback.failure(null,e);
            }
        });
    }

    public void updateInBackground(boolean withRevision, final Callback<AppacitiveObject> callback) {
        final String url = Urls.ForObject.updateObjectUrl(this.type, this.getId(), withRevision, this.getRevision()).toString();
        final Map<String, String> headers = Headers.assemble();
        final APJSONObject payload;
        try {
            payload = super.getUpdateCommand();
        } catch (APJSONException e) {
            throw new RuntimeException(e);
        }
        final AppacitiveObject object = this;
        AsyncHttp asyncHttp = APContainer.build(AsyncHttp.class);
        asyncHttp.post(url, headers, payload.toString(), new APCallback() {
            @Override
            public void success(String result) {
                try {
                    APJSONObject jsonObject = new APJSONObject(result);
                    AppacitiveStatus status = new AppacitiveStatus(jsonObject.optJSONObject("status"));
                    if (status.isSuccessful()) {
                        object.setSelf(jsonObject.optJSONObject("object"));
                        if (callback != null) {
                            callback.success(object);
                        }
                    } else {
                        if (callback != null)
                            callback.failure(null, new AppacitiveException(status));
                    }
                } catch (Exception e) {
                    if (callback != null)
                        callback.failure(null, e);
                }
            }

            @Override
            public void failure(Exception e) {
                if (callback != null)
                    callback.failure(null,e);
            }
        });
    }

    public void fetchLatestInBackground(final Callback<Void> callback) {
        final String url = Urls.ForObject.getObjectUrl(type, this.getId(), null).toString();
        final Map<String, String> headers = Headers.assemble();
        final AppacitiveObject object = this;
        AsyncHttp asyncHttp = APContainer.build(AsyncHttp.class);
        asyncHttp.get(url, headers, new APCallback() {
            @Override
            public void success(String result) {
                try {
                    APJSONObject jsonObject = new APJSONObject(result);
                    AppacitiveStatus status = new AppacitiveStatus(jsonObject.optJSONObject("status"));
                    if (status.isSuccessful()) {
                        object.setSelf(jsonObject.optJSONObject("object"));
                        if (callback != null) {
                            callback.success(null);
                        }
                    } else {
                        if (callback != null)
                            callback.failure(null, new AppacitiveException(status));
                    }
                } catch (Exception e) {
                    if (callback != null)
                        callback.failure(null, e);
                }
            }

            @Override
            public void failure(Exception e) {
                if (callback != null)
                    callback.failure(null,e);
            }
        });
    }

    public static void multiGetInBackground(String type, List<Long> ids, List<String> fields, final Callback<List<AppacitiveObject>> callback) throws ValidationException {
        if (type.isEmpty())
            throw new ValidationException("Type cannot be empty.");
        final String url = Urls.ForObject.multiGetObjectUrl(type, ids, fields).toString();
        final Map<String, String> headers = Headers.assemble();
        final List<AppacitiveObject> returnObjects = new ArrayList<AppacitiveObject>();
        AsyncHttp asyncHttp = APContainer.build(AsyncHttp.class);
        asyncHttp.get(url, headers, new APCallback() {
            @Override
            public void success(String result) {
                try {
                    APJSONObject jsonObject = new APJSONObject(result);
                    AppacitiveStatus status = new AppacitiveStatus(jsonObject.optJSONObject("status"));
                    if (status.isSuccessful()) {
                        APJSONArray objectsArray = jsonObject.optJSONArray("objects");
                        for (int i = 0; i < objectsArray.length(); i++) {
                            AppacitiveObject object = new AppacitiveObject("");
                            object.setSelf(objectsArray.optJSONObject(i));
                            returnObjects.add(object);
                        }
                        if (callback != null)
                            callback.success(returnObjects);
                    } else if (callback != null)
                        callback.failure(null, new AppacitiveException(status));
                } catch (Exception e) {
                    if (callback != null)
                        callback.failure(null, e);
                }
            }

            @Override
            public void failure(Exception e) {
                if (callback != null)
                    callback.failure(null,e);
            }
        });
    }

    public static void findInBackground(String type, AppacitiveQuery query, List<String> fields, final Callback<PagedList<AppacitiveObject>> callback) {
        final String url = Urls.ForObject.findObjectsUrl(type, query, fields).toString();
        final Map<String, String> headers = Headers.assemble();
        final List<AppacitiveObject> returnObjects = new ArrayList<AppacitiveObject>();
        final PagedList<AppacitiveObject> pagedResult = new PagedList<AppacitiveObject>();
        AsyncHttp asyncHttp = APContainer.build(AsyncHttp.class);
        asyncHttp.get(url, headers, new APCallback() {
            @Override
            public void success(String result) {
                try {
                    APJSONObject jsonObject = new APJSONObject(result);
                    AppacitiveStatus status = new AppacitiveStatus(jsonObject.optJSONObject("status"));
                    if (status.isSuccessful()) {
                        APJSONArray objectsArray = jsonObject.optJSONArray("objects");
                        for (int i = 0; i < objectsArray.length(); i++) {
                            AppacitiveObject object = new AppacitiveObject("");
                            object.setSelf(objectsArray.optJSONObject(i));
                            returnObjects.add(object);
                        }
                        pagedResult.results = returnObjects;
                        pagedResult.pagingInfo.setSelf(jsonObject.optJSONObject("paginginfo"));
                        if (callback != null)
                            callback.success(pagedResult);
                    } else if (callback != null)
                        callback.failure(null, new AppacitiveException(status));
                } catch (Exception e) {
                    if (callback != null)
                        callback.failure(null, e);
                }
            }

            @Override
            public void failure(Exception e) {
                if (callback != null)
                    callback.failure(null,e);
            }
        });
    }

    public static void findInBetweenTwoObjectsInBackground(String type, long objectAId, String relationA, String labelA, long objectBId, String relationB, String labelB, List<String> fields, final Callback<PagedList<AppacitiveObject>> callback) {
        final String url = Urls.ForObject.findBetweenTwoObjectsUrl(type, objectAId, relationA, labelA, objectBId, relationB, labelB, fields).toString();
        final Map<String, String> headers = Headers.assemble();
        final List<AppacitiveObject> returnObjects = new ArrayList<AppacitiveObject>();
        final PagedList<AppacitiveObject> pagedResult = new PagedList<AppacitiveObject>();
        AsyncHttp asyncHttp = APContainer.build(AsyncHttp.class);
        asyncHttp.get(url, headers, new APCallback() {
            @Override
            public void success(String result) {
                try {
                    APJSONObject jsonObject = new APJSONObject(result);
                    AppacitiveStatus status = new AppacitiveStatus(jsonObject.optJSONObject("status"));
                    if (status.isSuccessful()) {
                        APJSONArray objectsArray = jsonObject.optJSONArray("objects");
                        for (int i = 0; i < objectsArray.length(); i++) {
                            AppacitiveObject object = new AppacitiveObject("");
                            object.setSelf(objectsArray.optJSONObject(i));
                            returnObjects.add(object);
                        }
                        pagedResult.results = returnObjects;
                        pagedResult.pagingInfo.setSelf(jsonObject.optJSONObject("paginginfo"));
                        if (callback != null)
                            callback.success(pagedResult);
                    } else if (callback != null)
                        callback.failure(null, new AppacitiveException(status));
                } catch (Exception e) {
                    if (callback != null)
                        callback.failure(null, e);
                }
            }

            @Override
            public void failure(Exception e) {
                if (callback != null)
                    callback.failure(null,e);
            }
        });
    }

    public static void getConnectedObjectsInBackground(String relationType, String objectType, long objectId, AppacitiveQuery query, List<String> fields, final Callback<ConnectedObjectsResponse> callback) {
        final String url = Urls.ForConnection.getConnectedObjectsUrl(relationType, objectType, objectId, query, fields).toString();
        final Map<String, String> headers = Headers.assemble();
        AsyncHttp asyncHttp = APContainer.build(AsyncHttp.class);
        asyncHttp.get(url, headers, new APCallback() {
            @Override
            public void success(String result) {
                try {
                    APJSONObject jsonObject = new APJSONObject(result);
                    AppacitiveStatus status = new AppacitiveStatus(jsonObject.optJSONObject("status"));
                    if (status.isSuccessful()) {
                        ConnectedObjectsResponse connectedObjectsResponse = new ConnectedObjectsResponse(jsonObject.optString("parent"));
                        connectedObjectsResponse.pagingInfo.setSelf(jsonObject.optJSONObject("paginginfo"));
                        connectedObjectsResponse.results = new ArrayList<ConnectedObject>();
                        APJSONArray nodesArray = jsonObject.optJSONArray("nodes");
                        if (nodesArray != null) {
                            for (int i = 0; i < nodesArray.length(); i++) {
                                ConnectedObject connectedObject = new ConnectedObject();
                                APJSONObject nodeObject = nodesArray.optJSONObject(i);
                                if (nodeObject.isNull("__edge") == false) {
                                    connectedObject.connection = new AppacitiveConnection("");
                                    connectedObject.connection.setSelf(nodeObject.optJSONObject("__edge"));
                                    nodeObject.remove("__edge");
                                }
                                connectedObject.object = new AppacitiveObject("");
                                connectedObject.object.setSelf(nodeObject);
                                connectedObjectsResponse.results.add(connectedObject);
                            }
                        }
                        if (callback != null)
                            callback.success(connectedObjectsResponse);
                    } else {
                        if (callback != null)
                            callback.failure(null, new AppacitiveException(status));
                    }
                } catch (Exception e) {
                    if (callback != null)
                        callback.failure(null, e);
                }

            }

            @Override
            public void failure(Exception e) {
                if (callback != null)
                    callback.failure(null,e);
            }
        });
//        Future<Map<String, Object>> future = ExecutorServiceWrapper.submit(new Callable<Map<String, Object>>() {
//            @Override
//            public Map<String, Object> call() throws Exception {
//                return APContainer.build(Http.class).get(url, headers);
//            }
//        });
//        AppacitiveStatus status;
//        boolean isSuccessful;
//        ConnectedObjectsResponse response = null;
//        AppacitiveException exception = null;
//        try {
//            Map<String, Object> responseMap = future.get();
//            status = new AppacitiveStatus((Map<String, Object>) responseMap.get("status"));
//            isSuccessful = status.isSuccessful();
//            if (isSuccessful) {
//
//                response = new ConnectedObjectsResponse(responseMap.get("parent").toString());
//                response.pagingInfo = new PagingInfo((Map<String, Object>) responseMap.get("paginginfo"));
//                List<Object> nodeObjects = (ArrayList<Object>) (responseMap.get("nodes"));
//                response.results = new ArrayList<ConnectedObject>();
//                if (nodeObjects != null)
//                    for (Object n : nodeObjects) {
//                        Map<String, Object> obj_n = (Map<String, Object>) n;
//                        ConnectedObject connectedObject = new ConnectedObject();
//                        if (obj_n.containsKey("__edge")) {
//                            connectedObject.connection = new AppacitiveConnection("");
//                            connectedObject.connection.setSelf((Map<String, Object>) obj_n.get("__edge"));
//                            obj_n.remove("__edge");
//                        }
//                        connectedObject.object = new AppacitiveObject("");
//                        connectedObject.object.setSelf(obj_n);
//                        response.results.add(connectedObject);
//                    }
//            } else
//                exception = new AppacitiveException(status);
//        } catch (Exception e) {
//            LOGGER.log(Level.ALL, e.getMessage());
//            if (callback != null) callback.failure(null, e);
//            return;
//        }
//        if (callback != null) {
//            if (isSuccessful)
//                callback.success(response);
//            else
//                callback.failure(null, exception);
//        }
    }

}
