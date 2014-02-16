package com.appacitive.sdk.core.push;

import java.util.HashMap;
import java.util.Map;

public class IosOptions {
        public String soundFile;

        public IosOptions(String soundFile) {
            this.soundFile = soundFile;
        }

        public boolean isEmpty() {
            return this.soundFile == null || this.soundFile.isEmpty() == true;
        }

        public Map<String, Object> getMap()
        {
            return new HashMap<String, Object>(){{
                put("ios", new HashMap<String, String>(){{put("sound", soundFile);}});
            }};
        }
    }
