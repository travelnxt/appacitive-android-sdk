package com.appacitive.core;

import com.appacitive.core.apjson.APJSONArray;
import com.appacitive.core.apjson.APJSONException;
import com.appacitive.core.apjson.APJSONObject;
import com.appacitive.core.exceptions.AppacitiveException;
import com.appacitive.core.exceptions.ValidationException;
import com.appacitive.core.infra.*;
import com.appacitive.core.interfaces.AsyncHttp;
import com.appacitive.core.model.AppacitiveStatus;
import com.appacitive.core.model.Callback;
import com.appacitive.core.model.PagedList;
import com.appacitive.core.query.AppacitiveQuery;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

/**
 * Created by sathley.
 */
public class AppacitiveDevice extends AppacitiveEntity implements Serializable, APSerializable {

    public final static Logger LOGGER = Logger.getLogger(AppacitiveDevice.class.getName());

    public AppacitiveDevice() {
    }

    public void setSelf(APJSONObject device) {

        super.setSelf(device);

        if (device != null) {

            if (device.isNull(SystemDefinedProperties.typeId) == false)
                this.typeId = device.optLong(SystemDefinedProperties.typeId);
            if (device.isNull(SystemDefinedProperties.type) == false)
                this.type = device.optString(SystemDefinedProperties.type);

        }
    }

    public APJSONObject getMap() throws APJSONException {
        APJSONObject jsonObject = super.getMap();
        jsonObject.put(SystemDefinedProperties.type, this.type);
        jsonObject.put(SystemDefinedProperties.typeId, String.valueOf(this.typeId));
        return jsonObject;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setTypeId(long typeId) {
        this.typeId = typeId;
    }

    public String getType() {
        return type;
    }

    public long getTypeId() {
        return typeId;
    }

    private String type = null;

    private long typeId = 0;

    public String getDeviceType() {
        return this.getPropertyAsString("devicetype");
    }

    public void setDeviceType(String deviceType) {
        this.setStringProperty("devicetype", deviceType);

    }

    public String getDeviceToken() {
        return this.getPropertyAsString("devicetoken");
    }

    public void setDeviceToken(String deviceToken) {
        this.setStringProperty("devicetoken", deviceToken);

    }

    public double[] getLocation() {
        return this.getPropertyAsGeo("location");
    }

    public void setLocation(double[] coordinates) {
        this.setGeoProperty("location", coordinates);

    }

    public List<String> getChannels() {
        return this.getPropertyAsMultiValuedString("channels");
    }

    public void setChannels(List<String> channels) {
        this.setPropertyAsMultiValuedString("channels", channels);

    }

    public int getBadge() {
        return this.getPropertyAsInt("badge");
    }

    public void setBadge(int badge) {
        this.setIntProperty("badge", badge);

    }

    public String getTimeZone() {
        return this.getPropertyAsString("timezone");
    }

    public void setTimeZone(String timezone) {
        this.setStringProperty("timezone", timezone);

    }

    public boolean getIsActive() {
        return this.getPropertyAsBoolean("isactive");
    }

    public void setIsActive(boolean isActive) {
        this.setBoolProperty("isactive", isActive);

    }

    public void registerInBackground(final Callback<AppacitiveDevice> callback) throws ValidationException {
        final List<String> mandatoryFields = new ArrayList<String>() {{
            add("devicetype");
            add("devicetoken");
        }};
        List<String> missingFields = new ArrayList<String>();
        for (String field : mandatoryFields) {
            if (this.getPropertyAsString(field) == null) {
                missingFields.add(field);
            }
        }

        if (missingFields.size() > 0)
            throw new ValidationException("Following mandatory fields are missing. - " + missingFields);

        final String url = Urls.ForDevice.getRegisterUrl().toString();
        final Map<String, String> headers = Headers.assemble();
        final APJSONObject payload;
        try {
            payload = this.getMap();
        } catch (APJSONException e) {
            throw new RuntimeException(e);
        }
        final AppacitiveDevice device = this;
        AsyncHttp asyncHttp = APContainer.build(AsyncHttp.class);
        asyncHttp.put(url, headers, payload.toString(), new APCallback() {
            @Override
            public void success(String result) {
                try {
                    APJSONObject jsonObject = new APJSONObject(result);
                    AppacitiveStatus status = new AppacitiveStatus(jsonObject.optJSONObject("status"));
                    if (status.isSuccessful()) {
                        device.setSelf(jsonObject.optJSONObject("device"));
                        if (callback != null) {
                            callback.success(device);
                        }
                    } else {
                        if (callback != null)
                            callback.failure(null, new AppacitiveException(status));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void failure(Exception e) {
                if (callback != null)
                    callback.failure(null, e);
            }
        });
    }

    public static void getInBackground(long deviceId, final Callback<AppacitiveDevice> callback)  {

        final String url = Urls.ForDevice.getDeviceUrl(String.valueOf(deviceId)).toString();
        final Map<String, String> headers = Headers.assemble();
        AsyncHttp asyncHttp = APContainer.build(AsyncHttp.class);
        asyncHttp.get(url, headers, new APCallback() {
            @Override
            public void success(String result) {

                try {
                    APJSONObject jsonObject = new APJSONObject(result);
                    AppacitiveStatus status = new AppacitiveStatus(jsonObject.optJSONObject("status"));
                    if (status.isSuccessful()) {
                        AppacitiveDevice device = new AppacitiveDevice();
                        device.setSelf(jsonObject.optJSONObject("device"));
                        if (callback != null)
                            callback.success(device);
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

    public void fetchLatestInBackground(final Callback<Void> callback) {
        final String url = Urls.ForDevice.getDeviceUrl(String.valueOf(this.getId())).toString();
        final Map<String, String> headers = Headers.assemble();
        final AppacitiveDevice device = this;
        AsyncHttp asyncHttp = APContainer.build(AsyncHttp.class);
        asyncHttp.get(url, headers, new APCallback() {
            @Override
            public void success(String result) {
                try {
                    APJSONObject jsonObject = new APJSONObject(result);
                    AppacitiveStatus status = new AppacitiveStatus(jsonObject.optJSONObject("status"));
                    if (status.isSuccessful()) {
                        device.setSelf(jsonObject.optJSONObject("device"));
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

    public static void multiGetInBackground(List<Long> ids, List<String> fields, final Callback<List<AppacitiveDevice>> callback) {

        final String url = Urls.ForObject.multiGetObjectUrl("device", ids, fields).toString();
        final Map<String, String> headers = Headers.assemble();
        final List<AppacitiveDevice> returnDevices = new ArrayList<AppacitiveDevice>();
        AsyncHttp asyncHttp = APContainer.build(AsyncHttp.class);
        asyncHttp.get(url, headers, new APCallback() {
            @Override
            public void success(String result) {
                try {
                    APJSONObject jsonObject = new APJSONObject(result);
                    AppacitiveStatus status = new AppacitiveStatus(jsonObject.optJSONObject("status"));
                    if (status.isSuccessful()) {
                        APJSONArray objectsArray = jsonObject.optJSONArray("objects");
                        for (int i = 0; i < objectsArray.length(); i++) {
                            AppacitiveDevice device = new AppacitiveDevice();
                            device.setSelf(objectsArray.optJSONObject(i));
                            returnDevices.add(device);
                        }
                        if (callback != null)
                            callback.success(returnDevices);
                    } else if (callback != null)
                        callback.failure(null, new AppacitiveException(status));
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

    public void updateInBackground(boolean withRevision, final Callback<AppacitiveDevice> callback) {

        final String url = Urls.ForDevice.updateDeviceUrl(this.getId(), withRevision, this.getRevision()).toString();
        final Map<String, String> headers = Headers.assemble();
        final APJSONObject payload;
        try {
            payload = super.getUpdateCommand();
        } catch (APJSONException e) {
            throw new RuntimeException(e);
        }
        final AppacitiveDevice device = this;
        AsyncHttp asyncHttp = APContainer.build(AsyncHttp.class);
        asyncHttp.post(url, headers, payload.toString(), new APCallback() {
            @Override
            public void success(String result) {
                try {
                    APJSONObject jsonObject = new APJSONObject(result);
                    AppacitiveStatus status = new AppacitiveStatus(jsonObject.optJSONObject("status"));
                    if (status.isSuccessful()) {
                        device.setSelf(jsonObject.optJSONObject("device"));
                        if (callback != null) {
                            callback.success(device);
                        }
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

    public void deleteInBackground(boolean deleteConnections, final Callback<Void> callback) {
        final String url = Urls.ForDevice.deleteDeviceUrl(this.getId(), deleteConnections).toString();
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

    public static void findInBackground(AppacitiveQuery query, List<String> fields, final Callback<PagedList<AppacitiveDevice>> callback) {
        final String url = Urls.ForObject.findObjectsUrl("device", query, fields).toString();
        final Map<String, String> headers = Headers.assemble();
        final List<AppacitiveDevice> returnDevices = new ArrayList<AppacitiveDevice>();
        final PagedList<AppacitiveDevice> pagedResult = new PagedList<AppacitiveDevice>();
        AsyncHttp asyncHttp = APContainer.build(AsyncHttp.class);
        asyncHttp.get(url, headers, new APCallback() {
            @Override
            public void success(String result) {
                try {
                    APJSONObject jsonObject = new APJSONObject(result);
                    AppacitiveStatus status = new AppacitiveStatus(jsonObject.optJSONObject("status"));
                    if (status.isSuccessful()) {
                        APJSONArray objectsArray = jsonObject.optJSONArray("objects");
                        for (int i = 0; i < objectsArray.length(); i++) {
                            AppacitiveDevice device = new AppacitiveDevice();
                            device.setSelf(objectsArray.optJSONObject(i));
                            returnDevices.add(device);
                        }
                        pagedResult.results = returnDevices;
                        pagedResult.pagingInfo.setSelf(jsonObject.optJSONObject("paginginfo"));
                        if (callback != null)
                            callback.success(pagedResult);
                    } else if (callback != null)
                        callback.failure(null, new AppacitiveException(status));
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
