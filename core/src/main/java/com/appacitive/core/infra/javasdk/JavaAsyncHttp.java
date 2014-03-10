//package com.appacitive.core.infra.javasdk;
//
//import com.appacitive.core.exceptions.AppacitiveException;
//import com.appacitive.core.infra.APCallback;
//import com.appacitive.core.infra.APFuture;
//import com.appacitive.core.interfaces.AsyncHttp;
//import com.appacitive.core.apjson.APJSONException;
//import com.appacitive.core.apjson.APJSONObject;
//import com.mashape.unirest.http.HttpResponse;
//import com.mashape.unirest.http.Unirest;
//import com.mashape.unirest.http.exceptions.UnirestException;
//
//import java.util.Map;
//import java.util.concurrent.ExecutionException;
//import java.util.concurrent.Future;
//import java.util.concurrent.TimeUnit;
//import java.util.concurrent.TimeoutException;
//
///**
//* Created by sathley.
//*/
//public class JavaAsyncHttp implements AsyncHttp {
//
//    private static final String UTF8_BOM = "\uFEFF";
//
//    @Override
//    public Future<String> get(String url, Map<String, String> headers) {
//
//        final Future<HttpResponse<String>> future = Unirest.get(url).headers(headers).asStringAsync();
//        return new APFuture<String>() {
//            @Override
//            public boolean cancel(boolean mayInterruptIfRunning) {
//                return future.cancel(mayInterruptIfRunning);
//            }
//
//            @Override
//            public boolean isCancelled() {
//                return future.isCancelled();
//            }
//
//            @Override
//            public boolean isDone() {
//                return future.isDone();
//            }
//
//            @Override
//            public String get() throws InterruptedException, ExecutionException {
//                String response = future.get().getBody();
//                if (response.startsWith(UTF8_BOM)) {
//                    response = response.substring(1);
//                }
//                APJSONObject jsonObject;
//                try {
//                    jsonObject = new APJSONObject(response);
//                } catch (APJSONException e) {
//                    throw new RuntimeException(e);
//                }
//                return jsonObject.toString();
//            }
//
//            @Override
//            public String get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
//                APJSONObject jsonObject;
//                try {
//                    jsonObject = new APJSONObject(future.get(timeout, unit).getBody());
//                } catch (APJSONException e) {
//                    throw new RuntimeException(e);
//                }
//                return jsonObject.toString();
//            }
//        };
//    }
//
//    @Override
//    public void get(String url, Map<String, String> headers, final APCallback callback) {
//
//        Unirest.get(url).headers(headers).asStringAsync(new com.mashape.unirest.http.async.Callback<String>() {
//            @Override
//            public void completed(final HttpResponse<String> stringHttpResponse) {
//                String response = stringHttpResponse.getBody();
//                if (response.startsWith(UTF8_BOM)) {
//                    response = response.substring(1);
//                }
//                if (callback != null) {
//                    callback.success(response);
//                }
//            }
//
//            @Override
//            public void failed(final UnirestException e) {
//                if (callback != null)
//                    callback.failure(e);
//            }
//
//            @Override
//            public void cancelled() {
//                if (callback != null)
//                    callback.failure(new AppacitiveException("Request cancelled."));
//            }
//        });
//    }
//
//    @Override
//    public Future<String> delete(String url, Map<String, String> headers) {
//
//        final Future<HttpResponse<String>> future = Unirest.delete(url).headers(headers).asStringAsync();
//        return new APFuture<String>() {
//            @Override
//            public boolean cancel(boolean mayInterruptIfRunning) {
//                return future.cancel(mayInterruptIfRunning);
//            }
//
//            @Override
//            public boolean isCancelled() {
//                return future.isCancelled();
//            }
//
//            @Override
//            public boolean isDone() {
//                return future.isDone();
//            }
//
//            @Override
//            public String get() throws InterruptedException, ExecutionException {
//                String response = future.get().getBody();
//                if (response.startsWith(UTF8_BOM)) {
//                    response = response.substring(1);
//                }
//                APJSONObject jsonObject;
//                try {
//                    jsonObject = new APJSONObject(response);
//                } catch (APJSONException e) {
//                    throw new RuntimeException(e);
//                }
//                return jsonObject.toString();
//            }
//
//            @Override
//            public String get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
//                APJSONObject jsonObject;
//                try {
//                    jsonObject = new APJSONObject(future.get(timeout, unit).getBody());
//                } catch (APJSONException e) {
//                    throw new RuntimeException(e);
//                }
//                return jsonObject.toString();
//            }
//        };
//    }
//
//    @Override
//    public void delete(String url, Map<String, String> headers, final APCallback callback) {
//
//        Unirest.delete(url).headers(headers).asStringAsync(new com.mashape.unirest.http.async.Callback<String>() {
//            @Override
//            public void completed(final HttpResponse<String> stringHttpResponse) {
//                String response = stringHttpResponse.getBody();
//                if (response.startsWith(UTF8_BOM)) {
//                    response = response.substring(1);
//                }
//                if (callback != null) {
//                    callback.success(response);
//                }
//            }
//
//            @Override
//            public void failed(final UnirestException e) {
//                if (callback != null)
//                    callback.failure(e);
//            }
//
//            @Override
//            public void cancelled() {
//                if (callback != null)
//                    callback.failure(new AppacitiveException("Request cancelled."));
//            }
//        });
//    }
//
//    @Override
//    public Future<String> put(String url, Map<String, String> headers, String request) {
//
//        final Future<HttpResponse<String>> future = Unirest.put(url).headers(headers).body(request).asStringAsync();
//        return new APFuture<String>() {
//            @Override
//            public boolean cancel(boolean mayInterruptIfRunning) {
//                return future.cancel(mayInterruptIfRunning);
//            }
//
//            @Override
//            public boolean isCancelled() {
//                return future.isCancelled();
//            }
//
//            @Override
//            public boolean isDone() {
//                return future.isDone();
//            }
//
//            @Override
//            public String get() throws InterruptedException, ExecutionException {
//                String response = future.get().getBody();
//                if (response.startsWith(UTF8_BOM)) {
//                    response = response.substring(1);
//                }
//                APJSONObject jsonObject;
//                try {
//                    jsonObject = new APJSONObject(response);
//                } catch (APJSONException e) {
//                    throw new RuntimeException(e);
//                }
//                return jsonObject.toString();
//            }
//
//            @Override
//            public String get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
//                APJSONObject jsonObject;
//                try {
//                    jsonObject = new APJSONObject(future.get(timeout, unit).getBody());
//                } catch (APJSONException e) {
//                    throw new RuntimeException(e);
//                }
//                return jsonObject.toString();
//            }
//        };
//    }
//
//    @Override
//    public void put(String url, Map<String, String> headers, final String request, final APCallback callback) {
//
//        Unirest.put(url).headers(headers).body(request.toString()).asStringAsync(new com.mashape.unirest.http.async.Callback<String>() {
//            @Override
//            public void completed(final HttpResponse<String> stringHttpResponse) {
//                String response = stringHttpResponse.getBody();
//                if (response.startsWith(UTF8_BOM)) {
//                    response = response.substring(1);
//                }
//                if (callback != null) {
//                    callback.success(response);
//                }
//            }
//
//            @Override
//            public void failed(final UnirestException e) {
//                if (callback != null)
//                    callback.failure(e);
//            }
//
//            @Override
//            public void cancelled() {
//                if (callback != null)
//                    callback.failure(new AppacitiveException("Request cancelled."));
//            }
//        });
//    }
//
//    @Override
//    public Future<String> post(String url, Map<String, String> headers, String request) {
//
//        final Future<HttpResponse<String>> future = Unirest.post(url).headers(headers).body(request.toString()).asStringAsync();
//        return new APFuture<String>() {
//            @Override
//            public boolean cancel(boolean mayInterruptIfRunning) {
//                return future.cancel(mayInterruptIfRunning);
//            }
//
//            @Override
//            public boolean isCancelled() {
//                return future.isCancelled();
//            }
//
//            @Override
//            public boolean isDone() {
//                return future.isDone();
//            }
//
//            @Override
//            public String get() throws InterruptedException, ExecutionException {
//                String response = future.get().getBody();
//                if (response.startsWith(UTF8_BOM)) {
//                    response = response.substring(1);
//                }
//                APJSONObject jsonObject;
//                try {
//                    jsonObject = new APJSONObject(response);
//                } catch (APJSONException e) {
//                    throw new RuntimeException(e);
//                }
//                return jsonObject.toString();
//            }
//
//            @Override
//            public String get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
//                APJSONObject jsonObject;
//                try {
//                    jsonObject = new APJSONObject(future.get(timeout, unit).getBody());
//                } catch (APJSONException e) {
//                    throw new RuntimeException(e);
//                }
//                return jsonObject.toString();
//            }
//        };
//    }
//
//    @Override
//    public void post(String url, Map<String, String> headers, String request, final APCallback callback) {
//
//        Unirest.post(url).headers(headers).body(request.toString()).asStringAsync(new com.mashape.unirest.http.async.Callback<String>() {
//            @Override
//            public void completed(final HttpResponse<String> stringHttpResponse) {
//                String response = stringHttpResponse.getBody();
//                if (response.startsWith(UTF8_BOM)) {
//                    response = response.substring(1);
//                }
//                if (callback != null) {
//                    callback.success(response);
//                }
//            }
//
//            @Override
//            public void failed(final UnirestException e) {
//                if (callback != null)
//                    callback.failure(e);
//            }
//
//            @Override
//            public void cancelled() {
//                if (callback != null)
//                    callback.failure(new AppacitiveException("Request cancelled."));
//            }
//        });
//    }
//}
