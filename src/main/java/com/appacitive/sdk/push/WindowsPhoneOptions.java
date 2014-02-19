package com.appacitive.sdk.push;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class WindowsPhoneOptions implements Serializable {
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

        public Map<String, Object> getMap() {
            Map<String, Object> nativeMap = new HashMap<String, Object>();
            if(this.isEmpty() == false)
                nativeMap.put("wp", this.notification.getMap());

            return nativeMap;
        }
    }
