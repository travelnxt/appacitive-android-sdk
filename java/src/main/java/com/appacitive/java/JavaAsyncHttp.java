package com.appacitive.java;

import com.appacitive.core.infra.APCallback;
import com.appacitive.core.interfaces.AsyncHttp;
import com.ning.http.client.*;
import com.ning.http.client.listener.TransferCompletionHandler;

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
        return null;
    }

    @Override
    public void get(String url, Map<String, String> headers, final APCallback callback) {
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

//                    @Override
//                    public void onThrowable(Throwable throwable) {
//                        callback.failure(new Exception(throwable));
//                    }
            });
        } catch (IOException e) {
            callback.failure(e);
        }
    }

    @Override
    public Future<String> delete(String url, Map<String, String> headers) {
        return null;
    }

    @Override
    public void delete(String url, Map<String, String> headers, final APCallback callback) {
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

//                    @Override
//                    public void onThrowable(Throwable throwable) {
//                        callback.failure(new Exception(throwable));
//                    }
            });
        } catch (IOException e) {
            callback.failure(e);
        }
    }

    @Override
    public Future<String> put(String url, Map<String, String> headers, String request) {
        return null;
    }

    @Override
    public void put(String url, Map<String, String> headers, String request, final APCallback callback) {
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

//                    @Override
//                    public void onThrowable(Throwable throwable) {
//                        callback.failure(new Exception(throwable));
//                    }
            });
        } catch (IOException e) {
            callback.failure(e);
        }
    }

    @Override
    public Future<String> post(String url, Map<String, String> headers, String request) {
        return null;
    }

    @Override
    public void post(String url, Map<String, String> headers, String request, final APCallback callback) {
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

//                    @Override
//                    public void onThrowable(Throwable throwable) {
//                        throw throwable;
//                    }
            });
        } catch (IOException e) {
            callback.failure(e);
        }
    }

}
