package com.appacitive.sdk.push;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class WindowsPhoneTile implements Serializable {
        protected WindowsPhoneTile(WindowsPhoneTileType type) {
            this.windowsPhoneTileType = type;
        }

        public WindowsPhoneTileType windowsPhoneTileType;

        public Map<String, Object> getMap()
        {
            return new HashMap<String, Object>();
        }
    }
