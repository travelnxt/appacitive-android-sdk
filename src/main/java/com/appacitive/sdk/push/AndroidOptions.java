package com.appacitive.sdk.push;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class AndroidOptions implements Serializable {
        public String notificationTitle;

        public AndroidOptions(String notificationTitle) {
            this.notificationTitle = notificationTitle;
        }

        public boolean isEmpty() {
            return this.notificationTitle == null || this.notificationTitle.isEmpty() == true;
        }

        public Map<String, Object> getMap()
        {
            return new HashMap<String, Object>(){{
                put("android", new HashMap<String, String>(){{put("title", notificationTitle);}});
            }};
        }
    }
