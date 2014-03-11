package com.appacitive.core;

import com.appacitive.core.apjson.APJSONObject;
import com.appacitive.core.exceptions.AppacitiveException;
import com.appacitive.core.infra.APCallback;
import com.appacitive.core.infra.APContainer;
import com.appacitive.core.infra.Headers;
import com.appacitive.core.infra.Urls;
import com.appacitive.core.interfaces.AsyncHttp;
import com.appacitive.core.model.AppacitiveStatus;
import com.appacitive.core.model.Callback;
import com.appacitive.core.model.FileUploadUrlResponse;

import java.io.Serializable;
import java.util.Map;
import java.util.logging.Logger;

/**
 * Created by sathley.
 */
public class AppacitiveFile implements Serializable {

    public final static Logger LOGGER = Logger.getLogger(AppacitiveFile.class.getName());

    public static void getUploadUrlInBackground(String contentType, String fileName, int expires, final Callback<FileUploadUrlResponse> callback) {
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
        AsyncHttp asyncHttp = APContainer.build(AsyncHttp.class);
        asyncHttp.get(finalUrl, headers, new APCallback() {
            @Override
            public void success(String result) {
                try {
                    APJSONObject jsonObject = new APJSONObject(result);
                    AppacitiveStatus status = new AppacitiveStatus(jsonObject.optJSONObject("status"));
                    if (status.isSuccessful()) {
                        FileUploadUrlResponse resp = new FileUploadUrlResponse();
                        resp.fileId = jsonObject.optString("id");
                        resp.url = jsonObject.optString("url");
                        if (callback != null)
                            callback.success(resp);
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
                    callback.failure(null, e);
            }
        });
    }

    public static void getDownloadUrlInBackground(String fileId, int expires, final Callback<String> callback) {
        String url = Urls.ForFile.getDownloadUrl(fileId).toString();
        final Map<String, String> headers = Headers.assemble();

        if (expires >= 0) {
            url += ("?expires=");
            url += (String.valueOf(expires));
        }
        final String finalUrl = url;
        AsyncHttp asyncHttp = APContainer.build(AsyncHttp.class);
        asyncHttp.get(finalUrl, headers, new APCallback() {
            @Override
            public void success(String result) {
                try {
                    APJSONObject jsonObject = new APJSONObject(result);
                    AppacitiveStatus status = new AppacitiveStatus(jsonObject.optJSONObject("status"));
                    if (status.isSuccessful()) {
                        String uri = jsonObject.optString("uri");
                        if (callback != null)
                            callback.success(uri);
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
                    callback.failure(null, e);
            }
        });
    }

    public static void deleteFileInBackground(String fileId, final Callback<Void> callback) {
        final String url = Urls.ForFile.getDeleteUrl(fileId).toString();
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
                    callback.failure(null, e);
            }
        });
    }
}
