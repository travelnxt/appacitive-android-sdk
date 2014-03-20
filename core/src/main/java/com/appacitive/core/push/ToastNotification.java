package com.appacitive.core.push;

import com.appacitive.core.apjson.APJSONException;
import com.appacitive.core.apjson.APJSONObject;

import java.io.Serializable;

public class ToastNotification extends WindowsPhoneNotification implements Serializable {
    public ToastNotification(String text1, String text2, String path) {
        super(WindowsPhoneNotificationType.Toast);
        this.text1 = text1;
        this.text2 = text2;
        this.path = path;
    }

    public String text1;

    public String text2;

    public String path;

    @Override
    public synchronized APJSONObject getMap() throws APJSONException {
        APJSONObject map = super.getMap();
        map.put("notificationtype", "toast");
        map.put("text1", text1);
        map.put("text2", text2);
        map.put("navigatepath", path);
        return map;
    }
}
