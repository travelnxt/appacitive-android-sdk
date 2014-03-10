package com.appacitive.core.push;

import com.appacitive.core.infra.APSerializable;
import com.appacitive.core.apjson.APJSONException;
import com.appacitive.core.apjson.APJSONObject;

import java.io.Serializable;

public class WindowsPhoneTile implements Serializable, APSerializable {
    protected WindowsPhoneTile(WindowsPhoneTileType type) {
        this.windowsPhoneTileType = type;
    }

    public WindowsPhoneTileType windowsPhoneTileType;

    public void setSelf(APJSONObject APEntity) {

    }

    public APJSONObject getMap() throws APJSONException {
        return new APJSONObject();
    }
}
