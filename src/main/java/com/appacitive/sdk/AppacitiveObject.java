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

    protected void setSelf(Map<String, Object> entity)
    {
        super.setSelf(entity);
        if(entity != null)
        {
            this.typeId = Long.parseLong(entity.get("__typeid").toString());
            this.type = entity.get("__type").toString();
        }
    }

    public AppacitiveObject(String type)
    {
        this.type = type;
    }

    public AppacitiveObject(long typeId)
    {
        this.typeId = typeId;
    }

    protected Map<String, Object> getMap()
    {
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

    public void createInBackground(Callback<Void> callback) throws ValidationError
    {
        if((type == null || this.type.isEmpty()) && (typeId == 0))
        {
            throw new ValidationError("Type and TypeId both cannot be empty while creating an object.");
        }

        final String url = Urls.ForObject.createObjectUrl(this.type);
        final Map<String, String> headers = Headers.assemble();
        final Map<String, Object> payload = this.getMap();
        Future<Map<String, Object>> future = ExecutorServiceWrapper.submit(new Callable<Map<String, Object>>() {
            @Override
            public Map<String, Object> call() throws Exception {
                Map<String, Object> map = AppacitiveHttp.put(url, headers, payload);
                return (Map<String, Object>)map.get("object");
            }
        });
        try
        {
            Map<String, Object> map = future.get();
            this.setSelf(map);
            this.resetUpdateCommands();
            callback.success(null);
        }
        catch (ExecutionException e)
        {
            if(e.getCause() instanceof AppacitiveException)
            {
                AppacitiveException appacitiveException = (AppacitiveException) e.getCause();
                callback.failure(null, appacitiveException);
            }
        }
        catch (InterruptedException e)
        {

        }
    }

    public static void getInBackground(String type, long id, List<String> fields, Callback<AppacitiveObject> callback) throws ValidationError
    {
        if(type.isEmpty())
            throw new ValidationError("Type cannot be empty.");
        if(id <= 0)
            throw new ValidationError("Object id should be greater than equal to 0.");

        final List<String> innerFields = fields;
        final String url = Urls.ForObject.getObjectUrl(type, id);
        final Map<String, String> headers = Headers.assemble();

        Future<AppacitiveObject> future = ExecutorServiceWrapper.submit(new Callable<AppacitiveObject>() {
        @Override
        public AppacitiveObject call() throws Exception {
            Map<String, Object> response = AppacitiveHttp.get(url, headers);
            return new AppacitiveObject((Map<String, Object>)response.get("object"));
            }
        });

        try
        {
            AppacitiveObject obj = future.get();
            callback.success(obj);
        }
        catch (ExecutionException e)
        {
            if(e.getCause() instanceof AppacitiveException)
            {
                AppacitiveException appacitiveException = (AppacitiveException) e.getCause();
                callback.failure(null, appacitiveException);
            }
            else
            {

            }
        }
        catch (InterruptedException e)
        {

        }
    }

    public void deleteInBackground(boolean deleteConnections, Callback<Void> callback)
    {
        final String url = Urls.ForObject.deleteObjectUrl(this.type, this.getId(), deleteConnections);
        final Map<String, String> headers = Headers.assemble();
        Future<Void> future = ExecutorServiceWrapper.submit(new Callable<Void>() {
            @Override
            public Void call() throws Exception {
                AppacitiveHttp.delete(url, headers);
                return null;
            }
        });

        try
        {
            future.get();
        }
        catch (ExecutionException e)
        {
            if(e.getCause() instanceof AppacitiveException)
            {
                AppacitiveException appacitiveException = (AppacitiveException) e.getCause();
                callback.failure(null, appacitiveException);
            }
        }
        catch (InterruptedException e)
        {

        }
    }

    public static void bulkDeleteInBackground(String type, long[] objectIds, Callback<Void> callback)
    {
        final String url = Urls.ForObject.bulkDeleteObjectUrl(type);
        final Map<String, String> headers = Headers.assemble();
        final Map<String, Object> payload = new HashMap<String, Object>();
        payload.put("idlist", objectIds);

        // API should accept ids without quotes
        Future<Void> future = ExecutorServiceWrapper.submit(new Callable<Void>() {
            @Override
            public Void call() throws Exception {
                AppacitiveHttp.post(url, headers, payload);
                return null;
            }
        });

        try
        {
            future.get();
        }
        catch (ExecutionException e)
        {
            if(e.getCause() instanceof AppacitiveException)
            {
                AppacitiveException appacitiveException = (AppacitiveException) e.getCause();
                callback.failure(null, appacitiveException);
            }
        }
        catch (InterruptedException e)
        {

        }

    }

    public void updateInBackground(boolean withRevision, Callback<Void> callback)
    {
        final String url = Urls.ForObject.updateObjectUrl(this.type, this.getId());
        final Map<String, String> headers = Headers.assemble();
        final Map<String, Object> payload = new HashMap<String, Object>();
        payload.putAll(super.getUpdateCommand());
        Future<Void> future = ExecutorServiceWrapper.submit(new Callable<Void>() {
            @Override
            public Void call() throws Exception {
                AppacitiveHttp.post(url, headers, payload);
                return null;
            }
        });

        try
        {
            future.get();
            this.resetUpdateCommands();
        }
        catch (ExecutionException e)
        {
            if(e.getCause() instanceof AppacitiveException)
            {
                AppacitiveException appacitiveException = (AppacitiveException) e.getCause();
                callback.failure(null, appacitiveException);
            }
        }
        catch (InterruptedException e)
        {

        }
    }

    public void fetchLatestInBackground(Callback<Void> callback)
    {
        final String url = Urls.ForObject.getObjectUrl(type, this.getId());
        final Map<String, String> headers = Headers.assemble();

        Future<Map<String, Object>> future = ExecutorServiceWrapper.submit(new Callable<Map<String, Object>>() {
            @Override
            public Map<String, Object> call() throws Exception {
                return AppacitiveHttp.get(url, headers);
            }
        });

        try
        {
            Map<String, Object> response = future.get();
            this.setSelf((Map<String, Object>)response.get("object"));
            this.resetUpdateCommands();
            callback.success(null);
        }
        catch (ExecutionException e)
        {
            if(e.getCause() instanceof AppacitiveException)
            {
                AppacitiveException appacitiveException = (AppacitiveException) e.getCause();
                callback.failure(null, appacitiveException);
            }
            else
            {

            }
        }
        catch (InterruptedException e)
        {

        }
    }

    public static void multiGetInBackground(String type, long[] ids, String[] fields, Callback<List<AppacitiveObject>> callback) throws ValidationError
    {
        if(type.isEmpty())
            throw new ValidationError("Type cannot be empty.");
        final String url = Urls.ForObject.multiGetObjectUrl(type, ids);
        final Map<String, String> headers = Headers.assemble();
        Future<List<AppacitiveObject>> future = ExecutorServiceWrapper.submit(new Callable<List<AppacitiveObject>>() {
            @Override
            public List<AppacitiveObject> call() throws Exception {
                Map<String, Object> response = AppacitiveHttp.get(url, headers);
                ArrayList<Object> objects = (ArrayList<Object>)response.get("objects");
                List<AppacitiveObject> returnObjects = new ArrayList<AppacitiveObject>();
                for(Object obj : objects)
                {
                    returnObjects.add(new AppacitiveObject((Map<String, Object>)obj));
                }
                return returnObjects;
            }
        });

        try
        {
            List<AppacitiveObject> objs = future.get();
            callback.success(objs);
        }
        catch (ExecutionException e)
        {
            if(e.getCause() instanceof AppacitiveException)
            {
                AppacitiveException appacitiveException = (AppacitiveException) e.getCause();
                callback.failure(null, appacitiveException);
            }
            else
            {

            }
        }
        catch (InterruptedException e)
        {

        }
    }
}
