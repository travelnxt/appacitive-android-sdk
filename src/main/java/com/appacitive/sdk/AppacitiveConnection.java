//package com.appacitive.sdk;
//
//import com.appacitive.sdk.callbacks.Callback;
//import com.appacitive.sdk.exceptions.AppacitiveException;
//import com.appacitive.sdk.exceptions.ValidationError;
//import com.appacitive.sdk.infra.AppacitiveHttp;
//import com.appacitive.sdk.infra.ExecutorServiceWrapper;
//import com.appacitive.sdk.infra.Headers;
//import com.appacitive.sdk.infra.Urls;
//
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//import java.util.concurrent.Callable;
//import java.util.concurrent.ExecutionException;
//import java.util.concurrent.Future;
//import java.util.logging.Logger;
//
///**
// * Created by sathley.
// */
//public class AppacitiveConnection extends AppacitiveEntity {
//
//    private final static Logger LOGGER = Logger.getLogger(AppacitiveConnection.class.getName());
//
//    public AppacitiveConnection(Map<String, Object> connection) {
//        this.setSelf(connection);
//    }
//
//    public AppacitiveConnection(String relationType)
//    {
//        this.relationType = relationType;
//    }
//
//    public AppacitiveConnection(long relationId)
//    {
//        this.relationId = relationId;
//    }
//
//    protected void setSelf(Map<String, Object> connection)
//    {
//        super.setSelf(connection);
//        if(connection != null)
//        {
//            this.relationId = Long.parseLong(connection.get("__relationid").toString());
//            this.relationType = connection.get("__relationtype").toString();
//            this.endpointA.setSelf((Map<String, Object>)connection.get("__endpointa"));
//            this.endpointB.setSelf((Map<String, Object>)connection.get("__endpointb"));
//        }
//    }
//
//    protected Map<String, Object> getMap()
//    {
//        Map<String, Object> nativeMap = super.getMap();
//        nativeMap.put("__relationtype", this.relationType);
//        nativeMap.put("__relationid", this.relationId);
//        nativeMap.put("__endpointa",  this.endpointA.getMap());
//        nativeMap.put("__endpointb",  this.endpointB.getMap());
//        return nativeMap;
//    }
//
//    private String relationType = null;
//
//    private long relationId = 0;
//
//    private AppacitiveEndpoint endpointA = new AppacitiveEndpoint();
//
//    private AppacitiveEndpoint endpointB = new AppacitiveEndpoint();
//
//    public String getRelationType() {
//        return relationType;
//    }
//
//    public long getRelationId() {
//        return relationId;
//    }
//
//    public void createInBackground(Callback<AppacitiveConnection> callback)
//    {
//        // validations
//
//        final String url = Urls.ForConnection.createConnectionUrl(this.relationType).toString();
//        final Map<String, String> headers = Headers.assemble();
//        final Map<String, Object> payload = this.getMap();
//        Future<Map<String, Object>> future = ExecutorServiceWrapper.submit(new Callable<Map<String, Object>>() {
//            @Override
//            public Map<String, Object> call() throws Exception {
//                Map<String, Object> map = AppacitiveHttp.put(url, headers, payload);
//                return (Map<String, Object>)map.get("connection");
//            }
//        });
//        try
//        {
//            Map<String, Object> map = future.get();
//            this.setSelf(map);
//            this.resetUpdateCommands();
//            callback.success(null);
//        }
//        catch (ExecutionException e)
//        {
//            if(e.getCause() instanceof AppacitiveException)
//            {
//                AppacitiveException appacitiveException = (AppacitiveException) e.getCause();
//                callback.failure(null, appacitiveException);
//            }
//        }
//        catch (InterruptedException e)
//        {
//
//        }
//    }
//
//    public static void getInBackground(String relationType, long id, List<String> fields, Callback<AppacitiveConnection> callback) throws ValidationError
//    {
//        if(relationType.isEmpty())
//            throw new ValidationError("RelationType cannot be empty.");
//        if(id <= 0)
//            throw new ValidationError("Connection id should be greater than equal to 0.");
//
//        final String url = Urls.ForConnection.getConnectionUrl(relationType, id, fields).toString();
//        final Map<String, String> headers = Headers.assemble();
//
//        Future<AppacitiveConnection> future = ExecutorServiceWrapper.submit(new Callable<AppacitiveConnection>() {
//            @Override
//            public AppacitiveConnection call() throws Exception {
//                Map<String, Object> response = AppacitiveHttp.get(url, headers);
//                return new AppacitiveConnection((Map<String, Object>)response.get("connection"));
//            }
//        });
//
//        try
//        {
//            AppacitiveConnection conn = future.get();
//            callback.success(conn);
//        }
//        catch (ExecutionException e)
//        {
//            if(e.getCause() instanceof AppacitiveException)
//            {
//                AppacitiveException appacitiveException = (AppacitiveException) e.getCause();
//                callback.failure(null, appacitiveException);
//            }
//            else
//            {
//
//            }
//        }
//        catch (InterruptedException e)
//        {
//
//        }
//    }
//
//    public void deleteInBackground(Callback<Void> callback)
//    {
//        final String url = Urls.ForConnection.deleteConnectionUrl(this.relationType, this.getId()).toString();
//        final Map<String, String> headers = Headers.assemble();
//        Future<Void> future = ExecutorServiceWrapper.submit(new Callable<Void>() {
//            @Override
//            public Void call() throws Exception {
//                AppacitiveHttp.delete(url, headers);
//                return null;
//            }
//        });
//
//        try
//        {
//            future.get();
//        }
//        catch (ExecutionException e)
//        {
//            if(e.getCause() instanceof AppacitiveException)
//            {
//                AppacitiveException appacitiveException = (AppacitiveException) e.getCause();
//                callback.failure(null, appacitiveException);
//            }
//        }
//        catch (InterruptedException e)
//        {
//
//        }
//    }
//
//    public static void bulkDeleteInBackground(String relationType, long[] connectionIds, Callback<Void> callback)
//    {
//        final String url = Urls.ForConnection.bulkDeleteConnectionUrl(relationType).toString();
//        final Map<String, String> headers = Headers.assemble();
//        final Map<String, Object> payload = new HashMap<String, Object>();
//        payload.put("idlist", connectionIds);
//
//        // API should accept ids without quotes
//        Future<Void> future = ExecutorServiceWrapper.submit(new Callable<Void>() {
//            @Override
//            public Void call() throws Exception {
//                AppacitiveHttp.post(url, headers, payload);
//                return null;
//            }
//        });
//
//        try
//        {
//            future.get();
//        }
//        catch (ExecutionException e)
//        {
//            if(e.getCause() instanceof AppacitiveException)
//            {
//                AppacitiveException appacitiveException = (AppacitiveException) e.getCause();
//                callback.failure(null, appacitiveException);
//            }
//        }
//        catch (InterruptedException e)
//        {
//
//        }
//
//    }
//
//    public void updateInBackground(boolean withRevision, Callback<Void> callback)
//    {
//        final String url = Urls.ForObject.updateObjectUrl(this.relationType, this.getId(), withRevision, this.getRevision()).toString();
//        final Map<String, String> headers = Headers.assemble();
//        final Map<String, Object> payload = new HashMap<String, Object>();
//        payload.putAll(super.getUpdateCommand());
//        Future<Map<String, Object>> future = ExecutorServiceWrapper.submit(new Callable<Map<String, Object>>() {
//            @Override
//            public Map<String, Object> call() throws Exception {
//                Map<String, Object> map = AppacitiveHttp.post(url, headers, payload);
//                return (Map<String, Object>)map.get("connection");
//            }
//        });
//
//        try
//        {
//            Map<String, Object> map = future.get();
//            this.setSelf(map);
//            this.resetUpdateCommands();
//        }
//        catch (ExecutionException e)
//        {
//            if(e.getCause() instanceof AppacitiveException)
//            {
//                AppacitiveException appacitiveException = (AppacitiveException) e.getCause();
//                callback.failure(null, appacitiveException);
//            }
//        }
//        catch (InterruptedException e)
//        {
//
//        }
//    }
//
//    public void fetchLatestInBackground(Callback<Void> callback)
//    {
//        final String url = Urls.ForConnection.getConnectionUrl(relationType, this.getId(), null).toString();
//        final Map<String, String> headers = Headers.assemble();
//
//        Future<Map<String, Object>> future = ExecutorServiceWrapper.submit(new Callable<Map<String, Object>>() {
//            @Override
//            public Map<String, Object> call() throws Exception {
//                return AppacitiveHttp.get(url, headers);
//            }
//        });
//
//        try
//        {
//            Map<String, Object> response = future.get();
//            this.setSelf((Map<String, Object>)response.get("connection"));
//            this.resetUpdateCommands();
//            callback.success(null);
//        }
//        catch (ExecutionException e)
//        {
//            if(e.getCause() instanceof AppacitiveException)
//            {
//                AppacitiveException appacitiveException = (AppacitiveException) e.getCause();
//                callback.failure(null, appacitiveException);
//            }
//            else
//            {
//
//            }
//        }
//        catch (InterruptedException e)
//        {
//
//        }
//    }
//
//    public static void multiGetInBackground(String relationType, long[] connectionIds, List<String> fields, Callback<List<AppacitiveConnection>> callback) throws ValidationError
//    {
//        if(relationType.isEmpty())
//            throw new ValidationError("Relation Type cannot be empty.");
//        final String url = Urls.ForConnection.multiGetConnectionUrl(relationType, connectionIds, fields).toString();
//        final Map<String, String> headers = Headers.assemble();
//
//        Future<List<AppacitiveConnection>> future = ExecutorServiceWrapper.submit(new Callable<List<AppacitiveConnection>>() {
//            @Override
//            public List<AppacitiveConnection> call() throws Exception {
//                Map<String, Object> response = AppacitiveHttp.get(url, headers);
//                ArrayList<Object> connections = (ArrayList<Object>)response.get("objects");
//                List<AppacitiveConnection> returnObjects = new ArrayList<AppacitiveConnection>();
//                for(Object conn : connections)
//                {
//                    returnObjects.add(new AppacitiveConnection((Map<String, Object>)conn ));
//                }
//                return returnObjects;
//            }
//        });
//
//        try
//        {
//            List<AppacitiveConnection> objs = future.get();
//            callback.success(objs);
//        }
//        catch (ExecutionException e)
//        {
//            if(e.getCause() instanceof AppacitiveException)
//            {
//                AppacitiveException appacitiveException = (AppacitiveException) e.getCause();
//                callback.failure(null, appacitiveException);
//            }
//            else
//            {
//
//            }
//        }
//        catch (InterruptedException e)
//        {
//
//        }
//    }
//
//}
