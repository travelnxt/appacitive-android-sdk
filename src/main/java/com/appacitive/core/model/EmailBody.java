package com.appacitive.core.model;

import com.appacitive.core.infra.APSerializable;
import com.appacitive.core.apjson.APJSONException;
import com.appacitive.core.apjson.APJSONObject;

import java.io.Serializable;

/**
 * Created by sathley.
 */
public abstract class EmailBody implements Serializable, APSerializable {

    public EmailBody() {
    }

    public boolean isHTML() {
        return isHTML;
    }

    private boolean isHTML;

    public EmailBody(boolean isHTML) {
        this.isHTML = isHTML;
    }

    public void setSelf(APJSONObject emailBody) {
        if (emailBody.isNull("ishtml") == false)
            this.isHTML = emailBody.optBoolean("ishtml");
    }

    public APJSONObject getMap() throws APJSONException {
        APJSONObject object = new APJSONObject();
        object.put("ishtml", isHTML());
        return object;
    }
}
