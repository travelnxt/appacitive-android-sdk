package com.appacitive.core.push;

import com.appacitive.core.apjson.APJSONException;
import com.appacitive.core.apjson.APJSONObject;

import java.io.Serializable;

public class RawNotification extends WindowsPhoneNotification implements Serializable {
    public RawNotification(String rawData) {
        super(WindowsPhoneNotificationType.Raw);
        this.rawData = rawData;
    }

    public String rawData;

    @Override
    public APJSONObject getMap() throws APJSONException {
        APJSONObject map = super.getMap();
        map.put("notificationtype", "raw");
        map.put("data", rawData);
        return map;
    }
}
