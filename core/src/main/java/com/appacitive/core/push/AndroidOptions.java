package com.appacitive.core.push;

import com.appacitive.core.apjson.APJSONException;
import com.appacitive.core.apjson.APJSONObject;
import com.appacitive.core.infra.APSerializable;

import java.io.Serializable;

public class AndroidOptions implements Serializable, APSerializable {
    public String notificationTitle;

    public AndroidOptions(String notificationTitle) {
        this.notificationTitle = notificationTitle;
    }

    public boolean isEmpty() {
        return this.notificationTitle == null || this.notificationTitle.isEmpty() == true;
    }

    public void setSelf(APJSONObject APEntity) {

    }

    public APJSONObject getMap() throws APJSONException {
        APJSONObject jsonObject = new APJSONObject();
        APJSONObject title = new APJSONObject();
        title.put("title", notificationTitle);
        jsonObject.put("android", title);
        return jsonObject;
    }
}
