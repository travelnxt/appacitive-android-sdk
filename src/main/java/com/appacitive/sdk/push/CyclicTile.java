package com.appacitive.sdk.push;

import java.io.Serializable;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class CyclicTile extends WindowsPhoneTile implements Serializable {
        public CyclicTile(String frontTitle, String[] images) {
            this();
            this.frontTitle = frontTitle;
            this.images = new FixedSizeImageList(Arrays.asList(images));
        }

        public CyclicTile() {
            super(WindowsPhoneTileType.Cyclic);
        }

        public String tileId;

        public String frontTitle;

        public FixedSizeImageList images;

        @Override
        public Map<String, Object> getMap()
        {
            Map<String, Object> nativeMap = new HashMap<String, Object>();
            nativeMap.put("tiletemplate", "cycle");
            nativeMap.put("tileid", tileId);
            nativeMap.put("title", frontTitle);
            for(int i = 0; i< 9; i++)
                nativeMap.put("cycleimage".concat(String.valueOf(i)), images.Get(i));
            return nativeMap;
        }
    }
