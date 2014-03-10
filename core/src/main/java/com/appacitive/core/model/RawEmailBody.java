package com.appacitive.core.model;

import com.appacitive.core.infra.APSerializable;
import com.appacitive.core.apjson.APJSONException;
import com.appacitive.core.apjson.APJSONObject;

import java.io.Serializable;

/**
 * Created by sathley.
 */
public class RawEmailBody extends EmailBody implements Serializable, APSerializable {

    public RawEmailBody(String content, boolean isHTML) {
        super(isHTML);
        this.content = content;
    }

    public RawEmailBody() {

    }

    public String getContent() {
        return content;
    }

    private String content = null;

    @Override
    public void setSelf(APJSONObject emailBody) {
        super.setSelf(emailBody);
        if (emailBody.isNull("content") == false)
            this.content = emailBody.optString("content");
    }

    @Override
    public APJSONObject getMap() throws APJSONException {
        APJSONObject nativeMap = super.getMap();
        nativeMap.put("content", this.content);
        return nativeMap;
    }
}
