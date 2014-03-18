package com.appacitive.java;

import com.appacitive.core.infra.APCallback;
import com.appacitive.core.interfaces.AsyncHttp;
import com.ning.http.client.AsyncCompletionHandler;
import com.ning.http.client.AsyncHttpClient;
import com.ning.http.client.Response;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.Future;

/**
 * Created by sathley.
 */
public class JavaAsyncHttp implements AsyncHttp {


    private static final String UTF8_BOM = "\uFEFF";

    private static String processResponse(String response) {
        return response.substring(response.indexOf("{"), response.lastIndexOf("}") + 1);
    }

    @Override
    public Future<String> get(String url, Map<String, String> headers) {
        return Instance.get(url, headers);
    }

    @Override
    public void get(String url, Map<String, String> headers, APCallback callback) {
        Instance.get(url, headers, callback);
    }

    @Override
    public Future<String> delete(String url, Map<String, String> headers) {
        return Instance.delete(url, headers);
    }

    @Override
    public void delete(String url, Map<String, String> headers, APCallback callback) {
        Instance.delete(url, headers, callback);
    }

    @Override
    public Future<String> put(String url, Map<String, String> headers, String request) {
        return Instance.put(url, headers, request);
    }

    @Override
    public void put(String url, Map<String, String> headers, String request, APCallback callback) {
        Instance.put(url, headers, request, callback);
    }

    @Override
    public Future<String> post(String url, Map<String, String> headers, String request) {
        return Instance.post(url, headers, request);
    }

    @Override
    public void post(String url, Map<String, String> headers, String request, APCallback callback) {
        Instance.post(url, headers, request, callback);
    }

//    private static class Instance {
//        public static Future<String> get(String url, Map<String, String> headers) {
//
//            final Future<HttpResponse<String>> future = Unirest.get(url).headers(headers).asStringAsync();
//            return new APFuture<String>() {
//                @Override
//                public boolean cancel(boolean mayInterruptIfRunning) {
//                    return future.cancel(mayInterruptIfRunning);
//                }
//
//                @Override
//                public boolean isCancelled() {
//                    return future.isCancelled();
//                }
//
//                @Override
//                public boolean isDone() {
//                    return future.isDone();
//                }
//
//                @Override
//                public String get() throws InterruptedException, ExecutionException {
//                    String response = future.get().getBody();
//                    if (response.startsWith(UTF8_BOM)) {
//                        response = response.substring(1);
//                    }
//                    JSONObject jsonObject;
//                    try {
//                        jsonObject = new JSONObject(response);
//                    } catch (JSONException e) {
//                        throw new RuntimeException(e);
//                    }
//                    return jsonObject.toString();
//                }
//
//                @Override
//                public String get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
//                    JSONObject jsonObject;
//                    try {
//                        jsonObject = new JSONObject(future.get(timeout, unit).getBody());
//                    } catch (JSONException e) {
//                        throw new RuntimeException(e);
//                    }
//                    return jsonObject.toString();
//                }
//            };
//        }
//
//        public static void get(String url, Map<String, String> headers, final APCallback callback) {
//
//            Unirest.get(url).headers(headers).asStringAsync(new com.mashape.unirest.http.async.Callback<String>() {
//                @Override
//                public void completed(final HttpResponse<String> stringHttpResponse) {
//                    String response = stringHttpResponse.getBody();
//                    if (response.startsWith(UTF8_BOM)) {
//                        response = response.substring(1);
//                    }
//                    if (callback != null) {
//                        callback.success(response);
//                    }
//                }
//
//                @Override
//                public void failed(final UnirestException e) {
//                    if (callback != null)
//                        callback.failure(e);
//                }
//
//                @Override
//                public void cancelled() {
//                    if (callback != null)
//                        callback.failure(new AppacitiveException("Request cancelled."));
//                }
//            });
//        }
//
//        public static Future<String> delete(String url, Map<String, String> headers) {
//
//            final Future<HttpResponse<String>> future = Unirest.delete(url).headers(headers).asStringAsync();
//            return new APFuture<String>() {
//                @Override
//                public boolean cancel(boolean mayInterruptIfRunning) {
//                    return future.cancel(mayInterruptIfRunning);
//                }
//
//                @Override
//                public boolean isCancelled() {
//                    return future.isCancelled();
//                }
//
//                @Override
//                public boolean isDone() {
//                    return future.isDone();
//                }
//
//                @Override
//                public String get() throws InterruptedException, ExecutionException {
//                    String response = future.get().getBody();
//                    if (response.startsWith(UTF8_BOM)) {
//                        response = response.substring(1);
//                    }
//                    JSONObject jsonObject;
//                    try {
//                        jsonObject = new JSONObject(response);
//                    } catch (JSONException e) {
//                        throw new RuntimeException(e);
//                    }
//                    return jsonObject.toString();
//                }
//
//                @Override
//                public String get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
//                    JSONObject jsonObject;
//                    try {
//                        jsonObject = new JSONObject(future.get(timeout, unit).getBody());
//                    } catch (JSONException e) {
//                        throw new RuntimeException(e);
//                    }
//                    return jsonObject.toString();
//                }
//            };
//        }
//
//        public static void delete(String url, Map<String, String> headers, final APCallback callback) {
//
//            Unirest.delete(url).headers(headers).asStringAsync(new com.mashape.unirest.http.async.Callback<String>() {
//                @Override
//                public void completed(final HttpResponse<String> stringHttpResponse) {
//                    String response = stringHttpResponse.getBody();
//                    if (response.startsWith(UTF8_BOM)) {
//                        response = response.substring(1);
//                    }
//                    if (callback != null) {
//                        callback.success(response);
//                    }
//                }
//
//                @Override
//                public void failed(final UnirestException e) {
//                    if (callback != null)
//                        callback.failure(e);
//                }
//
//                @Override
//                public void cancelled() {
//                    if (callback != null)
//                        callback.failure(new AppacitiveException("Request cancelled."));
//                }
//            });
//        }
//
//        public static Future<String> put(String url, Map<String, String> headers, String request) {
//
//            final Future<HttpResponse<String>> future = Unirest.put(url).headers(headers).body(request).asStringAsync();
//            return new APFuture<String>() {
//                @Override
//                public boolean cancel(boolean mayInterruptIfRunning) {
//                    return future.cancel(mayInterruptIfRunning);
//                }
//
//                @Override
//                public boolean isCancelled() {
//                    return future.isCancelled();
//                }
//
//                @Override
//                public boolean isDone() {
//                    return future.isDone();
//                }
//
//                @Override
//                public String get() throws InterruptedException, ExecutionException {
//                    String response = future.get().getBody();
//                    if (response.startsWith(UTF8_BOM)) {
//                        response = response.substring(1);
//                    }
//                    JSONObject jsonObject;
//                    try {
//                        jsonObject = new JSONObject(response);
//                    } catch (JSONException e) {
//                        throw new RuntimeException(e);
//                    }
//                    return jsonObject.toString();
//                }
//
//                @Override
//                public String get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
//                    JSONObject jsonObject;
//                    try {
//                        jsonObject = new JSONObject(future.get(timeout, unit).getBody());
//                    } catch (JSONException e) {
//                        throw new RuntimeException(e);
//                    }
//                    return jsonObject.toString();
//                }
//            };
//        }
//
//        public static void put(String url, Map<String, String> headers, final String request, final APCallback callback) {
//
//            Unirest.put(url).headers(headers).body(request.toString()).asStringAsync(new com.mashape.unirest.http.async.Callback<String>() {
//                @Override
//                public void completed(final HttpResponse<String> stringHttpResponse) {
//                    String response = stringHttpResponse.getBody();
//                    if (response.startsWith(UTF8_BOM)) {
//                        response = response.substring(1);
//                    }
//                    if (callback != null) {
//                        callback.success(response);
//                    }
//                }
//
//                @Override
//                public void failed(final UnirestException e) {
//                    if (callback != null)
//                        callback.failure(e);
//                }
//
//                @Override
//                public void cancelled() {
//                    if (callback != null)
//                        callback.failure(new AppacitiveException("Request cancelled."));
//                }
//            });
//        }
//
//        public static Future<String> post(String url, Map<String, String> headers, String request) {
//
//            final Future<HttpResponse<String>> future = Unirest.post(url).headers(headers).body(request.toString()).asStringAsync();
//            return new APFuture<String>() {
//                @Override
//                public boolean cancel(boolean mayInterruptIfRunning) {
//                    return future.cancel(mayInterruptIfRunning);
//                }
//
//                @Override
//                public boolean isCancelled() {
//                    return future.isCancelled();
//                }
//
//                @Override
//                public boolean isDone() {
//                    return future.isDone();
//                }
//
//                @Override
//                public String get() throws InterruptedException, ExecutionException {
//                    String response = future.get().getBody();
//                    if (response.startsWith(UTF8_BOM)) {
//                        response = response.substring(1);
//                    }
//                    JSONObject jsonObject;
//                    try {
//                        jsonObject = new JSONObject(response);
//                    } catch (JSONException e) {
//                        throw new RuntimeException(e);
//                    }
//                    return jsonObject.toString();
//                }
//
//                @Override
//                public String get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
//                    JSONObject jsonObject;
//                    try {
//                        jsonObject = new JSONObject(future.get(timeout, unit).getBody());
//                    } catch (JSONException e) {
//                        throw new RuntimeException(e);
//                    }
//                    return jsonObject.toString();
//                }
//            };
//        }
//
//        public static void post(String url, Map<String, String> headers, String request, final APCallback callback) {
//
//            Unirest.post(url).headers(headers).body(request.toString()).asStringAsync(new com.mashape.unirest.http.async.Callback<String>() {
//                @Override
//                public void completed(final HttpResponse<String> stringHttpResponse) {
//                    String response = stringHttpResponse.getBody();
//                    if (response.startsWith(UTF8_BOM)) {
//                        response = response.substring(1);
//                    }
//                    if (callback != null) {
//                        callback.success(response);
//                    }
//                }
//
//                @Override
//                public void failed(final UnirestException e) {
//                    if (callback != null)
//                        callback.failure(e);
//                }
//
//                @Override
//                public void cancelled() {
//                    if (callback != null)
//                        callback.failure(new AppacitiveException("Request cancelled."));
//                }
//            });
//        }
//    }

    private static class Instance {
        public static Future<String> get(String url, Map<String, String> headers) {

            return null;
        }

        public static void get(String url, Map<String, String> headers, final APCallback callback) {

            AsyncHttpClient client = new AsyncHttpClient();
            AsyncHttpClient.BoundRequestBuilder builder = client.prepareGet(url);
            for (Map.Entry<String, String> header : headers.entrySet()) {
                builder.addHeader(header.getKey(), header.getValue());
            }

            try {
                builder.execute(new AsyncCompletionHandler<String>() {
                    @Override
                    public String onCompleted(Response response) throws Exception {

                        String responseString = response.getResponseBody();
                        if (responseString.startsWith(UTF8_BOM)) {
                            responseString = responseString.substring(1);
                        }
                        callback.success(processResponse(responseString));
                        return responseString;
                    }

                    @Override
                    public void onThrowable(Throwable throwable) {
                        callback.failure(new Exception(throwable));
                    }
                });
            } catch (IOException e) {
                callback.failure(e);
            }
        }

        public static Future<String> delete(String url, Map<String, String> headers) {

            return null;
        }

        public static void delete(String url, Map<String, String> headers, final APCallback callback) {

            AsyncHttpClient client = new AsyncHttpClient();
            AsyncHttpClient.BoundRequestBuilder builder = client.prepareDelete(url);
            for (Map.Entry<String, String> header : headers.entrySet()) {
                builder.addHeader(header.getKey(), header.getValue());
            }

            try {
                builder.execute(new AsyncCompletionHandler<String>() {
                    @Override
                    public String onCompleted(Response response) throws Exception {

                        String responseString = response.getResponseBody();
                        if (responseString.startsWith(UTF8_BOM)) {
                            responseString = responseString.substring(1);
                        }
                        callback.success(processResponse(responseString));
                        return responseString;
                    }

                    @Override
                    public void onThrowable(Throwable throwable) {
                        callback.failure(new Exception(throwable));
                    }
                });
            } catch (IOException e) {
                callback.failure(e);
            }
        }

        public static Future<String> put(String url, Map<String, String> headers, String request) {

            return null;
        }

        public static void put(String url, Map<String, String> headers, final String request, final APCallback callback) {

            AsyncHttpClient client = new AsyncHttpClient();
            AsyncHttpClient.BoundRequestBuilder builder = client.preparePut(url);
            builder.setBody(request);
            for (Map.Entry<String, String> header : headers.entrySet()) {
                builder.addHeader(header.getKey(), header.getValue());
            }

            try {
                builder.execute(new AsyncCompletionHandler<String>() {
                    @Override
                    public String onCompleted(Response response) throws Exception {

                        String responseString = response.getResponseBody();
                        if (responseString.startsWith(UTF8_BOM)) {
                            responseString = responseString.substring(1);
                        }
                        callback.success(processResponse(responseString));
                        return responseString;
                    }

                    @Override
                    public void onThrowable(Throwable throwable) {
                        callback.failure(new Exception(throwable));
                    }
                });
            } catch (IOException e) {
                callback.failure(e);
            }
        }

        public static Future<String> post(String url, Map<String, String> headers, String request) {

            return null;
        }

        public static void post(String url, Map<String, String> headers, String request, final APCallback callback) {

            AsyncHttpClient client = new AsyncHttpClient();
            AsyncHttpClient.BoundRequestBuilder builder = client.preparePost(url);
            builder.setBody(request);
            for (Map.Entry<String, String> header : headers.entrySet()) {
                builder.addHeader(header.getKey(), header.getValue());
            }

            try {
                builder.execute(new AsyncCompletionHandler<String>() {
                    @Override
                    public String onCompleted(Response response) throws Exception {

                        String responseString = response.getResponseBody();
                        if (responseString.startsWith(UTF8_BOM)) {
                            responseString = responseString.substring(1);
                        }
                        callback.success(processResponse(responseString));
                        return responseString;
                    }

                    @Override
                    public void onThrowable(Throwable throwable) {
                        callback.failure(new Exception(throwable));
                    }
                });
            } catch (IOException e) {
                callback.failure(e);
            }
        }
    }

}
