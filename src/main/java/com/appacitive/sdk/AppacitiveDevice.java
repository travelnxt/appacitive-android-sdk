package com.appacitive.sdk;

import com.appacitive.sdk.exceptions.AppacitiveException;
import com.appacitive.sdk.exceptions.ValidationException;
import com.appacitive.sdk.infra.APSerializable;
import com.appacitive.sdk.infra.APContainer;
import com.appacitive.sdk.interfaces.Http;
import com.appacitive.sdk.model.Callback;
import com.appacitive.sdk.infra.Headers;
import com.appacitive.sdk.infra.Urls;
import com.appacitive.sdk.model.AppacitiveStatus;
import com.appacitive.sdk.model.PagedList;
import com.appacitive.sdk.query.AppacitiveQuery;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
* Created by sathley.
*/
public class AppacitiveDevice extends AppacitiveEntity implements Serializable, APSerializable {

    public final static Logger LOGGER = Logger.getLogger(AppacitiveDevice.class.getName());

    public AppacitiveDevice() {
    }

    public void setSelf(Map<String, Object> device) {

        super.setSelf(device);

        if (device != null) {

            Object object = device.get("__typeid");
            if (object != null)
                this.typeId = Long.parseLong(object.toString());

            object = device.get("__type");
            if (object != null)
                this.type = object.toString();

        }
    }

    public Map<String, Object> getMap() {
        Map<String, Object> nativeMap = super.getMap();
        nativeMap.put("__type", this.type);
        nativeMap.put("__typeid", String.valueOf(this.typeId));

        return nativeMap;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setTypeId(long typeId) {
        this.typeId = typeId;
    }

    public String type = null;

    public long typeId = 0;

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

    public void registerInBackground(Callback<AppacitiveDevice> callback) throws ValidationException {
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
        final Map<String, Object> payload = this.getMap();
        Future<Map<String, Object>> future = ExecutorServiceWrapper.submit(new Callable<Map<String, Object>>() {
            @Override
            public Map<String, Object> call() throws Exception {
                return APContainer.build(Http.class).put(url, headers, payload);
            }
        });
        AppacitiveStatus status;
        boolean isSuccessful;
        try {
            Map<String, Object> responseMap = future.get();
            status = new AppacitiveStatus((Map<String, Object>) responseMap.get("status"));
            isSuccessful = status.isSuccessful();
            if (isSuccessful) {
                this.setSelf((Map<String, Object>) responseMap.get("device"));
                this.resetUpdateCommands();
            }
        } catch (Exception e) {
            LOGGER.log(Level.ALL, e.getMessage());
            if (callback != null) callback.failure(null, e);
            return;
        }
        if (callback != null) {
            if (isSuccessful)
                callback.success(this);
            else callback.failure(null, new AppacitiveException(status));
        }
    }

    public static void getInBackground(long deviceId, Callback<AppacitiveDevice> callback) throws ValidationException {

        final String url = Urls.ForDevice.getDeviceUrl(String.valueOf(deviceId)).toString();
        final Map<String, String> headers = Headers.assemble();

        Future<Map<String, Object>> future = ExecutorServiceWrapper.submit(new Callable<Map<String, Object>>() {
            @Override
            public Map<String, Object> call() throws Exception {
                return APContainer.build(Http.class).get(url, headers);
            }
        });
        AppacitiveStatus status;
        boolean isSuccessful;
        AppacitiveDevice device = null;
        try {
            Map<String, Object> responseMap = future.get();
            status = new AppacitiveStatus((Map<String, Object>) responseMap.get("status"));
            isSuccessful = status.isSuccessful();
            if (isSuccessful) {
                device = new AppacitiveDevice();
                device.setSelf((Map<String, Object>) responseMap.get("device"));
            }

        } catch (Exception e) {
            LOGGER.log(Level.ALL, e.getMessage());
            if (callback != null) callback.failure(null, e);
            return;
        }
        if (callback != null) {
            if (isSuccessful)
                callback.success(device);
            else
                callback.failure(null, new AppacitiveException(status));
        }
    }

    public void fetchLatestInBackground(Callback<Void> callback) {
        final String url = Urls.ForDevice.getDeviceUrl(String.valueOf(this.getId())).toString();
        final Map<String, String> headers = Headers.assemble();

        Future<Map<String, Object>> future = ExecutorServiceWrapper.submit(new Callable<Map<String, Object>>() {
            @Override
            public Map<String, Object> call() throws Exception {
                return APContainer.build(Http.class).get(url, headers);
            }
        });
        AppacitiveStatus status;
        boolean isSuccessful;
        try {
            Map<String, Object> responseMap = future.get();
            status = new AppacitiveStatus((Map<String, Object>) responseMap.get("status"));
            isSuccessful = status.isSuccessful();
            if (isSuccessful) {
                this.setSelf((Map<String, Object>) responseMap.get("device"));
                this.resetUpdateCommands();
            }

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

    public static void multiGetInBackground(List<Long> ids, List<String> fields, Callback<List<AppacitiveDevice>> callback) throws ValidationException {

        final String url = Urls.ForObject.multiGetObjectUrl("device", ids, fields).toString();
        final Map<String, String> headers = Headers.assemble();
        Future<Map<String, Object>> future = ExecutorServiceWrapper.submit(new Callable<Map<String, Object>>() {
            @Override
            public Map<String, Object> call() throws Exception {
                return APContainer.build(Http.class).get(url, headers);
            }
        });
        AppacitiveStatus status;
        boolean isSuccessful;
        List<AppacitiveDevice> returnDevices = null;
        try {
            Map<String, Object> responseMap = future.get();
            status = new AppacitiveStatus((Map<String, Object>) responseMap.get("status"));
            isSuccessful = status.isSuccessful();
            if (isSuccessful) {
                ArrayList<Object> objects = (ArrayList<Object>) responseMap.get("objects");
                returnDevices = new ArrayList<AppacitiveDevice>();
                for (Object device : objects) {
                    AppacitiveDevice device1 = new AppacitiveDevice();
                    device1.setSelf((Map<String, Object>) device);
                    returnDevices.add(device1);
                }

            }
        } catch (Exception e) {
            LOGGER.log(Level.ALL, e.getMessage());
            if (callback != null) callback.failure(null, e);
            return;
        }
        if (callback != null) {
            if (isSuccessful)
                callback.success(returnDevices);
            else
                callback.failure(null, new AppacitiveException(status));
        }
    }

    public void updateInBackground(boolean withRevision, Callback<AppacitiveDevice> callback) {

        final String url = Urls.ForDevice.updateDeviceUrl(this.getId(), withRevision, this.getRevision()).toString();
        final Map<String, String> headers = Headers.assemble();
        final Map<String, Object> payload = new HashMap<String, Object>();
        payload.putAll(super.getUpdateCommand());
        Future<Map<String, Object>> future = ExecutorServiceWrapper.submit(new Callable<Map<String, Object>>() {
            @Override
            public Map<String, Object> call() throws Exception {
                return APContainer.build(Http.class).post(url, headers, payload);
            }
        });
        AppacitiveStatus status;
        boolean isSuccessful;
        AppacitiveDevice device = null;
        try {
            Map<String, Object> responseMap = future.get();
            status = new AppacitiveStatus((Map<String, Object>) responseMap.get("status"));
            isSuccessful = status.isSuccessful();
            if (isSuccessful) {
                this.resetUpdateCommands();
                this.setSelf((Map<String, Object>) responseMap.get("object"));

            }
        } catch (Exception e) {
            LOGGER.log(Level.ALL, e.getMessage());
            if (callback != null) callback.failure(null, e);
            return;
        }
        if (callback != null) {
            if (isSuccessful)
                callback.success(this);
            else
                callback.failure(null, new AppacitiveException(status));
        }
    }

    public void deleteInBackground(boolean deleteConnections, Callback<Void> callback) {
        final String url = Urls.ForDevice.deleteDeviceUrl(this.getId(), deleteConnections).toString();
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

    public static void findInBackground(AppacitiveQuery query, List<String> fields, Callback<PagedList<AppacitiveDevice>> callback) {
        final String url = Urls.ForObject.findObjectsUrl("device", query, fields).toString();
        final Map<String, String> headers = Headers.assemble();

        Future<Map<String, Object>> future = ExecutorServiceWrapper.submit(new Callable<Map<String, Object>>() {
            @Override
            public Map<String, Object> call() throws Exception {
                return APContainer.build(Http.class).get(url, headers);
            }
        });
        AppacitiveStatus status;
        boolean isSuccessful;
        PagedList<AppacitiveDevice> pagedResult = null;
        try {
            Map<String, Object> responseMap = future.get();
            status = new AppacitiveStatus((Map<String, Object>) responseMap.get("status"));
            isSuccessful = status.isSuccessful();
            if (isSuccessful) {
                ArrayList<Object> devices = (ArrayList<Object>) responseMap.get("objects");
                List<AppacitiveDevice> returnDevices = new ArrayList<AppacitiveDevice>();
                for (Object device : devices) {
                    AppacitiveDevice device1 = new AppacitiveDevice();
                    device1.setSelf((Map<String, Object>) device);
                    returnDevices.add(device1);
                }
                pagedResult = new PagedList<AppacitiveDevice>((Map<String, Object>) responseMap.get("paginginfo"));
                pagedResult.results = returnDevices;
            }

        } catch (Exception e) {
            LOGGER.log(Level.ALL, e.getMessage());
            if (callback != null) callback.failure(null, e);
            return;
        }
        if (callback != null) {
            if (isSuccessful)
                callback.success(pagedResult);
            else
                callback.failure(null, new AppacitiveException(status));
        }
    }
}
