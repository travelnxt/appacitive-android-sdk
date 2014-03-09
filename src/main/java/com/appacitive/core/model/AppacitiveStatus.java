package com.appacitive.core.model;

import com.appacitive.core.infra.APSerializable;
import com.appacitive.core.apjson.APJSONArray;
import com.appacitive.core.apjson.APJSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by sathley.
 */
public class AppacitiveStatus implements Serializable, APSerializable {

    public AppacitiveStatus() {
        additionalMessages = new ArrayList<String>();
    }

    public AppacitiveStatus(APJSONObject jsonObject) {
        this();
        this.setSelf(jsonObject);
    }

    public String code = null;

    public String message = null;

    public String referenceId = null;

    public String version = null;

    public List<String> additionalMessages = null;

    public boolean isSuccessful() {
        return code != null && code.matches("2..");
    }

    @Override
    public void setSelf(APJSONObject status) {
        if (status != null) {
            code = status.optString("code", null);
            message = status.optString("message", null);
            referenceId = status.optString("referenceid", null);
            version = status.optString("version", null);
            if (status.isNull("additionalmessages") == false) {
                this.additionalMessages = new ArrayList<String>();

                APJSONArray additionalMessagesArray = status.optJSONArray("additionalmessages");
                for (int i = 0; i < additionalMessagesArray.length(); i++)
                    this.additionalMessages.add(additionalMessagesArray.optString(i));
            }
        }
    }

    @Override
    public APJSONObject getMap() {
        return null;
    }
}
