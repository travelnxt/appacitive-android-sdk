package com.appacitive.core.push;

import com.appacitive.core.apjson.APJSONException;
import com.appacitive.core.apjson.APJSONObject;

import java.io.Serializable;
import java.util.Arrays;

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
    public APJSONObject getMap() throws APJSONException {
        APJSONObject nativeMap = super.getMap();
        nativeMap.put("tiletemplate", "cycle");
        nativeMap.put("tileid", tileId);
        nativeMap.put("title", frontTitle);
        for (int i = 0; i < 9; i++)
            nativeMap.put("cycleimage".concat(String.valueOf(i)), images.Get(i));
        return nativeMap;
    }
}
