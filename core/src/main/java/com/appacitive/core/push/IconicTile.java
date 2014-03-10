package com.appacitive.core.push;

import com.appacitive.core.apjson.APJSONException;
import com.appacitive.core.apjson.APJSONObject;

import java.io.Serializable;

public class IconicTile extends WindowsPhoneTile implements Serializable {

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
    public APJSONObject getMap() throws APJSONException {
        APJSONObject map = super.getMap();

        map.put("tiletemplate", "iconic");
        map.put("tileid", tileId);
        map.put("title", frontTitle);
        map.put("iconimage", iconImage);
        map.put("smalliconimage", smallIconImage);
        map.put("backgroundcolor", backgroundColor);
        map.put("widecontent1", wideContent1);
        map.put("widecontent2", wideContent2);
        map.put("widecontent3", wideContent3);
        return map;
    }
}
