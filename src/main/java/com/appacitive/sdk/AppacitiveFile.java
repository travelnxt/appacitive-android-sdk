package com.appacitive.sdk;

import com.appacitive.sdk.exceptions.AppacitiveException;
import com.appacitive.sdk.infra.APContainer;
import com.appacitive.sdk.infra.Headers;
import com.appacitive.sdk.infra.Urls;
import com.appacitive.sdk.interfaces.Http;
import com.appacitive.sdk.model.AppacitiveStatus;
import com.appacitive.sdk.model.Callback;
import com.appacitive.sdk.model.FileUploadUrlResponse;

import java.io.Serializable;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
* Created by sathley.
*/
public class AppacitiveFile implements Serializable {

    public final static Logger LOGGER = Logger.getLogger(AppacitiveFile.class.getName());

    public static void getUploadUrlInBackground(String contentType, String fileName, int expires, Callback<FileUploadUrlResponse> callback) {
        String url = Urls.ForFile.getUploadUrl(contentType).toString();
        final Map<String, String> headers = Headers.assemble();

        if (fileName != null && fileName.isEmpty() == false) {
            url += "&filename=";
            url += fileName;
        }

        if (expires >= 0) {
            url += "&expires=";
            url += String.valueOf(expires);
        }

        final String finalUrl = url;

        Future<Map<String, Object>> future = ExecutorServiceWrapper.submit(new Callable<Map<String, Object>>() {
            @Override
            public Map<String, Object> call() throws Exception {
                return APContainer.build(Http.class).get(finalUrl, headers);
            }
        });
        AppacitiveStatus status;
        boolean isSuccessful;
        FileUploadUrlResponse resp = null;
        try {
            Map<String, Object> responseMap = future.get();
            status = new AppacitiveStatus((Map<String, Object>) responseMap.get("status"));
            isSuccessful = status.isSuccessful();
            if (isSuccessful) {
                resp = new FileUploadUrlResponse();
                resp.fileId = (String) responseMap.get("id");
                resp.url = (String) responseMap.get("url");
            }
        } catch (Exception e) {
            LOGGER.log(Level.ALL, e.getMessage());
            if (callback != null) callback.failure(null, e);
            return;
        }
        if (callback != null) {
            if (isSuccessful)
                callback.success(resp);
            else
                callback.failure(null, new AppacitiveException(status));
        }
    }

    public static void getDownloadUrlInBackground(String fileId, int expires, Callback<String> callback) {
        String url = Urls.ForFile.getDownloadUrl(fileId).toString();
        final Map<String, String> headers = Headers.assemble();

        if (expires >= 0) {
            url += ("?expires=");
            url += (String.valueOf(expires));
        }
        final String finalUrl = url;

        Future<Map<String, Object>> future = ExecutorServiceWrapper.submit(new Callable<Map<String, Object>>() {
            @Override
            public Map<String, Object> call() throws Exception {
                return APContainer.build(Http.class).get(finalUrl, headers);
            }
        });
        AppacitiveStatus status;
        boolean isSuccessful;
        String uri = null;
        try {
            Map<String, Object> responseMap = future.get();
            status = new AppacitiveStatus((Map<String, Object>) responseMap.get("status"));
            isSuccessful = status.isSuccessful();
            if (isSuccessful) {
                uri = (String) responseMap.get("uri");
            }
        } catch (Exception e) {
            LOGGER.log(Level.ALL, e.getMessage());
            if (callback != null) callback.failure(null, e);
            return;
        }
        if (callback != null) {
            if (isSuccessful)
                callback.success(uri);
            else
                callback.failure(null, new AppacitiveException(status));
        }
    }

    public static void deleteFileInBackground(String fileId, Callback<Void> callback) {
        final String url = Urls.ForFile.getDeleteUrl(fileId).toString();
        final Map<String, String> headers = Headers.assemble();

        Future<Map<String, Object>> future = ExecutorServiceWrapper.submit(new Callable<Map<String, Object>>() {
            @Override
            public Map<String, Object> call() throws Exception {
                return APContainer.build(Http.class).delete(url, headers);
            }
        });
        AppacitiveStatus status;
        boolean isSuccessful;
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
}
