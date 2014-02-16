package com.appacitive.sdk.core.push;

import java.util.Map;

public class WindowsPhoneNotification {
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
