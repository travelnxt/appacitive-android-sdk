package com.appacitive.core.push;

import com.appacitive.core.apjson.APJSONException;
import com.appacitive.core.apjson.APJSONObject;
import com.appacitive.core.infra.APSerializable;

import java.io.Serializable;

public class IosOptions implements Serializable, APSerializable {
    public String soundFile;

    public IosOptions(String soundFile) {
        this.soundFile = soundFile;
    }

    public boolean isEmpty() {
        return this.soundFile == null || this.soundFile.isEmpty() == true;
    }

    @Override
    public void setSelf(APJSONObject APEntity) {

    }

    public synchronized APJSONObject getMap() throws APJSONException {
        APJSONObject jsonObject = new APJSONObject();
        APJSONObject sound = new APJSONObject();
        sound.put("sound", soundFile);
        jsonObject.put("ios", sound);
        return jsonObject;
    }
}
