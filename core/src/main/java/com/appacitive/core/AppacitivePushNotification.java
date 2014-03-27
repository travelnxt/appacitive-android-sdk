package com.appacitive.core;

import com.appacitive.core.apjson.APJSONArray;
import com.appacitive.core.apjson.APJSONException;
import com.appacitive.core.apjson.APJSONObject;
import com.appacitive.core.exceptions.AppacitiveException;
import com.appacitive.core.infra.*;
import com.appacitive.core.interfaces.AsyncHttp;
import com.appacitive.core.interfaces.Logger;
import com.appacitive.core.model.AppacitiveStatus;
import com.appacitive.core.model.Callback;
import com.appacitive.core.push.AndroidOptions;
import com.appacitive.core.push.IosOptions;
import com.appacitive.core.push.PlatformOptions;
import com.appacitive.core.push.WindowsPhoneOptions;
import com.appacitive.core.query.BooleanOperator;
import com.appacitive.core.query.Query;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by sathley.
 */
public class AppacitivePushNotification implements Serializable, APSerializable {

    public final static Logger LOGGER = APContainer.build(Logger.class);

    public void setSelf(APJSONObject APEntity) {

    }

    public APJSONObject getMap() throws APJSONException {

        APJSONObject nativeMap = new APJSONObject();
        nativeMap.put("broadcast", this.isBroadcast);
        if (this.deviceIds.size() > 0)
            nativeMap.put("deviceids", new APJSONArray(this.deviceIds));
        if (this.channels.size() > 0)
            nativeMap.put("channels", new APJSONArray(this.channels));
        if (this.query != null)
            nativeMap.put("query", this.query);
        if (this.expiryInSeconds > 0)
            nativeMap.put("expireafter", this.expiryInSeconds);

        // write data
        if (this.alert != null && this.alert.isEmpty() == false)
            this.data.put("alert", this.alert);

        if (this.badge != null && this.badge.isEmpty() == false)
            this.data.put("badge", this.badge);

        nativeMap.put("data", new APJSONObject(this.data));

        // write platform options
        if (this.platformOptions != null) {
            nativeMap.put("platformoptions", this.platformOptions.getMap());
        }

        return nativeMap;
    }

    public static AppacitivePushNotification Broadcast(String message) {
        return new AppacitivePushNotification(message, true, null, null, null);
    }

    public static AppacitivePushNotification ToChannels(String message, List<String> channels) {
        return new AppacitivePushNotification(message, false, channels, null, null);
    }

    public static AppacitivePushNotification ToQueryResult(String message, BooleanOperator booleanOperator) {
        if (booleanOperator != null) {
            return new AppacitivePushNotification(message, false, null, null, booleanOperator.asString());
        }
        return new AppacitivePushNotification(message, false, null, null, null);
    }

    public static AppacitivePushNotification ToQueryResult(String message, Query query) {
        if (query != null) {
            return new AppacitivePushNotification(message, false, null, null, query.asString());
        }
        return new AppacitivePushNotification(message, false, null, null, null);
    }

    public static AppacitivePushNotification ToDeviceIds(String message, List<String> deviceIds) {
        return new AppacitivePushNotification(message, false, null, deviceIds, null);
    }

    private AppacitivePushNotification(String alert, boolean isBroadcast, List<String> channels, List<String> deviceIds, String query) {
        this.alert = alert;
        this.isBroadcast = isBroadcast;
        this.query = query;
        if (channels != null)
            this.channels.addAll(channels);
        if (deviceIds != null)
            this.deviceIds.addAll(deviceIds);
        this.expiryInSeconds = -1;
    }

    public String alert;

    public String badge;

    public boolean isBroadcast = false;

    public String query;

    public int expiryInSeconds;

    public List<String> deviceIds = new ArrayList<String>();

    public List<String> channels = new ArrayList<String>();

    public Map<String, String> data = new HashMap<String, String>();

    public PlatformOptions platformOptions;

    public AppacitivePushNotification withBadge(String badge) {
        this.badge = badge;
        return this;
    }

    public AppacitivePushNotification withPlatformOptions(IosOptions options) {
        if (this.platformOptions == null)
            this.platformOptions = new PlatformOptions();
        this.platformOptions.iOS = options;
        return this;
    }

    public AppacitivePushNotification withPlatformOptions(AndroidOptions options) {
        if (this.platformOptions == null)
            this.platformOptions = new PlatformOptions();
        this.platformOptions.android = options;
        return this;
    }

    public AppacitivePushNotification withPlatformOptions(WindowsPhoneOptions options) {
        if (this.platformOptions == null)
            this.platformOptions = new PlatformOptions();
        this.platformOptions.windowsPhone = options;
        return this;
    }

    public AppacitivePushNotification withData(Map<String, String> data) {
        this.data.putAll(data);
        return this;
    }

    public AppacitivePushNotification withExpiry(int seconds) {
        if (seconds <= 0)
            throw new IllegalArgumentException("Expiry time cannot be less than or equal to zero.");
        this.expiryInSeconds = seconds;
        return this;
    }

    public void sendInBackground(final Callback<String> callback) {
        LOGGER.info("Sending push notification(s).");
        final String url = Urls.Misc.sendPushUrl().toString();
        final Map<String, String> headers = Headers.assemble();
        final APJSONObject payload;
        try {
            payload = this.getMap();
        } catch (APJSONException e) {
            throw new RuntimeException(e);
        }
        AsyncHttp asyncHttp = APContainer.build(AsyncHttp.class);
        asyncHttp.post(url, headers, payload.toString(), new APCallback() {
            @Override
            public void success(String result) {

                APJSONObject jsonObject;
                try {
                    jsonObject = new APJSONObject(result);
                } catch (APJSONException e) {
                    throw new RuntimeException(e);
                }
                AppacitiveStatus status = new AppacitiveStatus(jsonObject.optJSONObject("status"));
                if (status.isSuccessful()) {
                    String id = jsonObject.optString("id");
                    if (callback != null) {
                        callback.success(id);
                    }
                } else {
                    if (callback != null)
                        callback.failure(null, new AppacitiveException(status));
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


