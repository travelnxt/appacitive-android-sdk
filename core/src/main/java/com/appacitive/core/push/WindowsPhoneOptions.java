package com.appacitive.core.push;

import com.appacitive.core.apjson.APJSONException;
import com.appacitive.core.apjson.APJSONObject;
import com.appacitive.core.infra.APSerializable;

import java.io.Serializable;

public class WindowsPhoneOptions implements Serializable, APSerializable {
    public WindowsPhoneOptions withToast(ToastNotification notification) {
        return new WindowsPhoneOptions(notification);
    }

    public WindowsPhoneOptions withTile(TileNotification notification) {
        return new WindowsPhoneOptions(notification);
    }

    public WindowsPhoneOptions withRaw(RawNotification notification) {
        return new WindowsPhoneOptions(notification);
    }

    public WindowsPhoneOptions(WindowsPhoneNotification notification) {
        this.notification = notification;
    }

    public WindowsPhoneNotification notification;

    public boolean isEmpty() {
        return this.notification == null;
    }

    public void setSelf(APJSONObject APEntity) {

    }

    public APJSONObject getMap() throws APJSONException {
        APJSONObject nativeMap = new APJSONObject();
        if (this.isEmpty() == false)
            nativeMap.put("wp", this.notification.getMap());

        return nativeMap;
    }
}
