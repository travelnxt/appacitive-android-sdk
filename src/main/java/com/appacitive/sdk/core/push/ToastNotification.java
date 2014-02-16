package com.appacitive.sdk.core.push;

import java.util.HashMap;
import java.util.Map;

public class ToastNotification extends WindowsPhoneNotification {
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
        public Map<String, Object> getMap()
        {
            return new HashMap<String, Object>(){{
                put("notificationtype", "toast");
                put("text1", text1);
                put("text2", text2);
                put("navigatepath", path);
            }};
        }
    }
