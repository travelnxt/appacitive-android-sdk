package com.appacitive.sdk;

import com.appacitive.sdk.callbacks.Callback;
import com.appacitive.sdk.exceptions.AppacitiveException;
import com.appacitive.sdk.exceptions.ValidationError;
import com.appacitive.sdk.infra.AppacitiveHttp;
import com.appacitive.sdk.infra.ExecutorServiceWrapper;
import com.appacitive.sdk.infra.Headers;
import com.appacitive.sdk.infra.Urls;

import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.logging.Logger;

/**
 * Created by sathley.
 */
public class AppacitiveObject extends AppacitiveEntity {

    private final static Logger LOGGER = Logger.getLogger(AppacitiveObject.class.getName());

    public AppacitiveObject(Map<String, Object> entity) {
        this.setSelf(entity);
    }

    protected void setSelf(Map<String, Object> entity) {

        super.setSelf(entity);

        if (entity != null) {

            Object object = entity.get("__typeid");
            if(object != null)
                this.typeId = Long.parseLong(object.toString());

            object = entity.get("__type");
            if(object != null)
                this.type = object.toString();

        }
    }

    public AppacitiveObject(String type) {
        this.type = type;
    }

    public AppacitiveObject(long typeId) {
        this.typeId = typeId;
    }

    protected Map<String, Object> getMap() {
        Map<String, Object> nativeMap = super.getMap();
        nativeMap.put("__type", this.type);
        nativeMap.put("__typeid", String.valueOf(this.typeId));

        return nativeMap;
    }

    private String type = null;

    private long typeId = 0;

    public String getType() {
        return type;
    }

    public long getTypeId() {
        return typeId;
    }

    public void createInBackground(Callback<AppacitiveObject> callback) throws ValidationError {
        if ((type == null || this.type.isEmpty()) && (typeId <= 0)) {
            throw new ValidationError("Type and TypeId both cannot be empty while creating an object.");
        }

        final String url = Urls.ForObject.createObjectUrl(this.type).toString();
        final Map<String, String> headers = Headers.assemble();
        final Map<String, Object> payload = this.getMap();
        Future<Map<String, Object>> future = ExecutorServiceWrapper.submit(new Callable<Map<String, Object>>() {
            @Override
            public Map<String, Object> call() throws Exception {
                return AppacitiveHttp.put(url, headers, payload);
            }
        });
        try {
            Map<String, Object> responseMap = future.get();
            AppacitiveStatus status = new AppacitiveStatus((Map<String, Object>) responseMap.get("status"));
            if (status.isSuccessful()) {
                this.setSelf((Map<String, Object>) responseMap.get("object"));
                this.resetUpdateCommands();
                if (callback != null)
                    callback.success(this);
            } else {
                if (callback != null)
                    callback.failure(null, new AppacitiveException(status));
            }

        } catch (Exception e) {

        }
    }

    public static void getInBackground(String type, long id, List<String> fields, Callback<AppacitiveObject> callback) throws ValidationError {
        if (type == null || type.isEmpty())
            throw new ValidationError("Type cannot be empty.");
        if (id <= 0)
            throw new ValidationError("Object id should be greater than equal to 0.");

        final String url = Urls.ForObject.getObjectUrl(type, id, fields).toString();
        final Map<String, String> headers = Headers.assemble();

        Future<Map<String, Object>> future = ExecutorServiceWrapper.submit(new Callable<Map<String, Object>>() {
            @Override
            public Map<String, Object> call() throws Exception {
                return AppacitiveHttp.get(url, headers);
            }
        });

        try {
            Map<String, Object> responseMap = future.get();
            AppacitiveStatus status = new AppacitiveStatus((Map<String, Object>) responseMap.get("status"));
            if (status.isSuccessful()) {
                if (callback != null)
                    callback.success(new AppacitiveObject((Map<String, Object>) responseMap.get("object")));
            } else {
                if (callback != null)
                    callback.failure(null, new AppacitiveException(status));
            }

        } catch (Exception e) {

        }
    }

    public void deleteInBackground(boolean deleteConnections, Callback<Void> callback) {
        final String url = Urls.ForObject.deleteObjectUrl(this.type, this.getId(), deleteConnections).toString();
        final Map<String, String> headers = Headers.assemble();
        Future<Map<String, Object>> future = ExecutorServiceWrapper.submit(new Callable<Map<String, Object>>() {
            @Override
            public Map<String, Object> call() throws Exception {
                return AppacitiveHttp.delete(url, headers);
            }
        });

        try {
            Map<String, Object> responseMap = future.get();
            AppacitiveStatus status = new AppacitiveStatus((Map<String, Object>) responseMap.get("status"));
            if (status.isSuccessful()) {
                if (callback != null)
                    callback.success(null);
            } else {
                if (callback != null)
                    callback.failure(null, new AppacitiveException(status));
            }
        } catch (Exception e) {

        }
    }

    public static void bulkDeleteInBackground(String type, List<Long> objectIds, Callback<Void> callback) {
        final String url = Urls.ForObject.bulkDeleteObjectUrl(type).toString();
        final Map<String, String> headers = Headers.assemble();
        final Map<String, Object> payload = new HashMap<String, Object>();
        List<String> strIds = new ArrayList<String>();
        for(long id:objectIds)
        {
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

        try {
            Map<String, Object> responseMap = future.get();
            AppacitiveStatus status = new AppacitiveStatus((Map<String, Object>) responseMap.get("status"));
            if (status.isSuccessful()) {
                if (callback != null)
                    callback.success(null);
            } else {
                if (callback != null)
                    callback.failure(null, new AppacitiveException(status));
            }
        } catch (Exception e) {

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
                return AppacitiveHttp.post(url, headers, payload);
            }
        });

        try {
            Map<String, Object> responseMap = future.get();
            AppacitiveStatus status = new AppacitiveStatus((Map<String, Object>) responseMap.get("status"));
            if (status.isSuccessful()) {
                this.resetUpdateCommands();
                this.setSelf((Map<String, Object>)responseMap.get("object"));
                if (callback != null)
                    callback.success(this);
            } else {
                if (callback != null)
                    callback.failure(null, new AppacitiveException(status));
            }
        } catch (Exception e) {

        }
    }

    public void fetchLatestInBackground(Callback<Void> callback) {
        final String url = Urls.ForObject.getObjectUrl(type, this.getId(), null).toString();
        final Map<String, String> headers = Headers.assemble();

        Future<Map<String, Object>> future = ExecutorServiceWrapper.submit(new Callable<Map<String, Object>>() {
            @Override
            public Map<String, Object> call() throws Exception {
                return AppacitiveHttp.get(url, headers);
            }
        });

        try {
            Map<String, Object> responseMap = future.get();
            AppacitiveStatus status = new AppacitiveStatus((Map<String, Object>) responseMap.get("status"));
            if (status.isSuccessful()) {
                this.setSelf((Map<String, Object>) responseMap.get("object"));
                this.resetUpdateCommands();
                if (callback != null)
                    callback.success(null);
            } else {
                if (callback != null)
                    callback.failure(null, new AppacitiveException(status));
            }

        } catch (Exception e) {

        }
    }

    public static void multiGetInBackground(String type, List<Long> ids, List<String> fields, Callback<List<AppacitiveObject>> callback) throws ValidationError {
        if (type.isEmpty())
            throw new ValidationError("Type cannot be empty.");
        final String url = Urls.ForObject.multiGetObjectUrl(type, ids, fields).toString();
        final Map<String, String> headers = Headers.assemble();
        Future<Map<String, Object>> future = ExecutorServiceWrapper.submit(new Callable<Map<String, Object>>() {
            @Override
            public Map<String, Object> call() throws Exception {
                return AppacitiveHttp.get(url, headers);
            }
        });

        try {
            Map<String, Object> responseMap = future.get();
            AppacitiveStatus status = new AppacitiveStatus((Map<String, Object>) responseMap.get("status"));
            if (status.isSuccessful()) {
                ArrayList<Object> objects = (ArrayList<Object>) responseMap.get("objects");
                List<AppacitiveObject> returnObjects = new ArrayList<AppacitiveObject>();
                for (Object obj : objects) {
                    returnObjects.add(new AppacitiveObject((Map<String, Object>) obj));
                }
                callback.success(returnObjects);
            } else {
                if (callback != null)
                    callback.failure(null, new AppacitiveException(status));
            }
        } catch (Exception e) {

        }
    }
}
