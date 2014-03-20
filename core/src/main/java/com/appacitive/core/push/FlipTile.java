package com.appacitive.core.push;

import com.appacitive.core.apjson.APJSONException;
import com.appacitive.core.apjson.APJSONObject;

import java.io.Serializable;

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
    public synchronized APJSONObject getMap() throws APJSONException {
        APJSONObject nativeMap = super.getMap();

        nativeMap.put("tiletemplate", "flip");
        nativeMap.put("tileid", tileId);
        nativeMap.put("title", frontTitle);
        nativeMap.put("count", frontCount);
        nativeMap.put("backgroundimage", frontBackgroundImage);
        nativeMap.put("smallbackgroundimage", smallBackgroundImage);
        nativeMap.put("widebackgroundimage", wideBackgroundImage);
        nativeMap.put("backtitle", backTitle);
        nativeMap.put("backbackgroundimage", backBackgroundImage);
        nativeMap.put("backcontent", backContent);
        nativeMap.put("widebackbackgroundimage", wideBackBackgroundImage);
        nativeMap.put("widebackcontent", wideBackContent);
        return nativeMap;
    }
}
