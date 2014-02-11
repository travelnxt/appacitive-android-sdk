package com.appacitive.sdk;

import com.appacitive.sdk.callbacks.Callback;
import com.appacitive.sdk.exceptions.AppacitiveException;
import com.appacitive.sdk.infra.AppacitiveHttp;
import com.appacitive.sdk.infra.Headers;
import com.appacitive.sdk.infra.Urls;

import java.io.Serializable;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by sathley.
 */
public class AppacitiveFile  implements Serializable {

    public final static Logger LOGGER = Logger.getLogger(AppacitiveFile.class.getName());

    public static void getUploadUrlInBackground(String contentType, String fileName, int expires, Callback<FileUploadUrlResponse> callback) {
        String url = Urls.ForFile.getUploadUrl(contentType).toString();
        final Map<String, String> headers = Headers.assemble();

        if(fileName != null && fileName.isEmpty() == false)
        {
            url += "&filename=";
            url += fileName;
        }

        if(expires >= 0)
        {
            url += "&expires=";
            url += String.valueOf(expires);
        }

        final String finalUrl = url;

        Future<Map<String, Object>> future = ExecutorServiceWrapper.submit(new Callable<Map<String, Object>>() {
            @Override
            public Map<String, Object> call() throws Exception {
                return AppacitiveHttp.get(finalUrl, headers);
            }
        });

        try {
            Map<String, Object> responseMap = future.get();
            AppacitiveStatus status = new AppacitiveStatus((Map<String, Object>) responseMap.get("status"));
            if (status.isSuccessful()) {
                if (callback != null) {
                    FileUploadUrlResponse resp = new FileUploadUrlResponse();
                    resp.fileId = (String) responseMap.get("id");
                    resp.url = (String) responseMap.get("url");
                    callback.success(resp);
                }

            } else {
                if (callback != null)
                    callback.failure(null, new AppacitiveException(status));
            }
        } catch (Exception e) {
            LOGGER.log(Level.ALL, e.getMessage());
        }
    }

    public static void getDownloadUrlInBackground(String fileId, int expires, Callback<String> callback) {
        String url = Urls.ForFile.getDownloadUrl(fileId).toString();
        final Map<String, String> headers = Headers.assemble();

        if(expires >= 0)
        {
            url += ("?expires=");
            url += (String.valueOf(expires));
        }
        final String finalUrl = url;

        Future<Map<String, Object>> future = ExecutorServiceWrapper.submit(new Callable<Map<String, Object>>() {
            @Override
            public Map<String, Object> call() throws Exception {
                return AppacitiveHttp.get(finalUrl, headers);
            }
        });

        try {
            Map<String, Object> responseMap = future.get();
            AppacitiveStatus status = new AppacitiveStatus((Map<String, Object>) responseMap.get("status"));
            if (status.isSuccessful()) {
                if (callback != null) {
                    callback.success((String)responseMap.get("uri"));
                }

            } else {
                if (callback != null)
                    callback.failure(null, new AppacitiveException(status));
            }
        } catch (Exception e) {
            LOGGER.log(Level.ALL, e.getMessage());
        }
    }

    public static void deleteFileInBackground(String fileId, Callback<Void> callback)
    {
        final String url = Urls.ForFile.getDeleteUrl(fileId).toString();
        final Map<String, String> headers = Headers.assemble();

        Future<Map<String, Object>> future = ExecutorServiceWrapper.submit(new Callable<Map<String, Object>>() {
            @Override
            public Map<String, Object> call() throws Exception {
                return AppacitiveHttp.delete(url, headers);
            }
        });

        try
        {
            Map<String, Object> responseMap = future.get();
            AppacitiveStatus status = new AppacitiveStatus((Map<String, Object>) responseMap.get("status"));
            if (status.isSuccessful()) {
                if (callback != null) {
                    callback.success(null);
                }

            } else {
                if (callback != null)
                    callback.failure(null, new AppacitiveException(status));
            }
        } catch (Exception e) {
            LOGGER.log(Level.ALL, e.getMessage());
        }
    }
}
