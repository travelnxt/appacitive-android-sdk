package com.appacitive.sdk.push;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class FlipTile extends WindowsPhoneTile implements Serializable {
        public FlipTile() {
            super(WindowsPhoneTileType.Flip);
        }

        public String tileId;

        public String frontTitle;

        public String frontBackgroundImage;

        public String frontCount;

        public String smallBackgroundImage;

        public String wideBackgroundImage;

        public String backTitle;

        public String backContent;

        public String backBackgroundImage;

        public String wideBackContent;

        public String wideBackBackgroundImage;

        @Override
        public Map<String, Object> getMap()
        {
            return new HashMap<String, Object>(){{
                put("tiletemplate", "flip");
                put("tileid", tileId);
                put("title", frontTitle);
                put("count", frontCount);
                put("backgroundimage", frontBackgroundImage);
                put("smallbackgroundimage", smallBackgroundImage);
                put("widebackgroundimage", wideBackgroundImage);
                put("backtitle", backTitle);
                put("backbackgroundimage", backBackgroundImage);
                put("backcontent", backContent);
                put("widebackbackgroundimage", wideBackBackgroundImage);
                put("widebackcontent", wideBackContent);
            }};
        }
    }
