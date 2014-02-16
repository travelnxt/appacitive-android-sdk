package com.appacitive.sdk.core.push;

import java.util.HashMap;
import java.util.Map;

public class WindowsPhoneTile {
        protected WindowsPhoneTile(WindowsPhoneTileType type) {
            this.windowsPhoneTileType = type;
        }

        public WindowsPhoneTileType windowsPhoneTileType;

        public Map<String, Object> getMap()
        {
            return new HashMap<String, Object>();
        }
    }
