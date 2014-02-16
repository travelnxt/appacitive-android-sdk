package com.appacitive.sdk.push;

import java.util.HashMap;
import java.util.Map;

public class IconicTile extends WindowsPhoneTile {

        public IconicTile() {
            super(WindowsPhoneTileType.Iconic);
        }

        public String tileId;

        public String frontTitle;

        public String iconImage;

        public String smallIconImage;

        public String backgroundColor;

        public String wideContent1;

        public String wideContent2;

        public String wideContent3;

        @Override
        public Map<String, Object> getMap()
        {
            return new HashMap<String, Object>()
            {{
                    put("tiletemplate", "iconic");
                    put("tileid", tileId);
                    put("title", frontTitle);
                    put("iconimage", iconImage);
                    put("smalliconimage", smallIconImage);
                    put("backgroundcolor", backgroundColor);
                    put("widecontent1", wideContent1);
                    put("widecontent2", wideContent2);
                    put("widecontent3", wideContent3);
                }};
        }
    }
