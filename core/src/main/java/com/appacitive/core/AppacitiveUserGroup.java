package com.appacitive.core;

import com.appacitive.core.apjson.APJSONArray;
import com.appacitive.core.apjson.APJSONException;
import com.appacitive.core.apjson.APJSONObject;
import com.appacitive.core.exceptions.AppacitiveException;
import com.appacitive.core.infra.APCallback;
import com.appacitive.core.infra.APContainer;
import com.appacitive.core.infra.Headers;
import com.appacitive.core.infra.Urls;
import com.appacitive.core.interfaces.AsyncHttp;
import com.appacitive.core.interfaces.Logger;
import com.appacitive.core.model.AppacitiveStatus;
import com.appacitive.core.model.Callback;

import java.util.ArrayList;
import java.util.Map;

/**
 * Created by sathley on 5/19/2014.
 */
public class AppacitiveUserGroup {

    public final static Logger LOGGER = APContainer.build(Logger.class);

    public static void addUserInBackground(final String username, Callback<Void> callback) {
        addUsersInBackground(new ArrayList<String>() {{
            add(username);
        }}, callback);

    }

    public static void addUserInBackground(final long userId, Callback<Void> callback) {
        addUsersInBackground(new ArrayList<String>() {{
            add(String.valueOf(userId));
        }}, callback);
    }

    public static void removeUserInBackground(final String username, Callback<Void> callback) {
        removeUsersInBackground(new ArrayList<String>() {{
            add(username);
        }}, callback);
    }

    public static void removeUserInBackground(final long userId, Callback<Void> callback) {
        removeUsersInBackground(new ArrayList<String>() {{
            add(String.valueOf(userId));
        }}, callback);
    }

    public static void addUsersInBackground(ArrayList<String> userIds, Callback<Void> callback) {
        APJSONArray array = new APJSONArray(userIds);
        APJSONObject payload = new APJSONObject();

        try {
            payload.put("add", array);
        } catch (APJSONException e) {
            throw new RuntimeException(e);
        }
        fireCall(payload.toString(), callback);
    }

    public static void removeUsersInBackground(ArrayList<String> userIds, Callback<Void> callback) {
        APJSONArray array = new APJSONArray(userIds);
        APJSONObject payload = new APJSONObject();

        try {
            payload.put("remove", array);
        } catch (APJSONException e) {
            throw new RuntimeException(e);
        }
        fireCall(payload.toString(), callback);
    }

    private static void fireCall(String payload, final Callback<Void> callback) {
        String url = Urls.ForUserGroup.getUpdateMembersUrl().toString();
        Map<String, String> headers = Headers.assemble();
        AsyncHttp asyncHttp = APContainer.build(AsyncHttp.class);
        asyncHttp.post(url, headers, payload, new APCallback() {
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

                    if (callback != null) {
                        callback.success(null);
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
