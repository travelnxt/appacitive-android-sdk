package com.appacitive.sdk.push;

import java.io.Serializable;
import java.util.Map;

public class WindowsPhoneNotification implements Serializable {
        public WindowsPhoneNotification(WindowsPhoneNotificationType windowsPhoneNotificationType) {
            this.windowsPhoneNotificationType = windowsPhoneNotificationType;
        }

        public WindowsPhoneNotification() {
        }

        public WindowsPhoneNotificationType windowsPhoneNotificationType;

        public Map<String, Object> getMap()
        {
            return null;
        }
    }
