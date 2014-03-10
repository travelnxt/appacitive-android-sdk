package com.appacitive.core.push;

import com.appacitive.core.infra.APSerializable;
import com.appacitive.core.apjson.APJSONException;
import com.appacitive.core.apjson.APJSONObject;

import java.io.Serializable;

public class WindowsPhoneNotification implements Serializable, APSerializable {
    public WindowsPhoneNotification(WindowsPhoneNotificationType windowsPhoneNotificationType) {
        this.windowsPhoneNotificationType = windowsPhoneNotificationType;
    }

    public WindowsPhoneNotification() {
    }

    public WindowsPhoneNotificationType windowsPhoneNotificationType;

    public void setSelf(APJSONObject APEntity) {

    }

    public APJSONObject getMap() throws APJSONException {
        return new APJSONObject();
    }
}
