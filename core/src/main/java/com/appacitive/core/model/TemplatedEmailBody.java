package com.appacitive.core.model;

import com.appacitive.core.apjson.APJSONException;
import com.appacitive.core.apjson.APJSONObject;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by sathley.
 */
public class TemplatedEmailBody extends EmailBody implements Serializable {

    public TemplatedEmailBody(String templateName, Map<String, String> data, boolean isHtml) {
        super(isHtml);

        this.templateName = templateName;
        this.data = data;
    }

    public TemplatedEmailBody() {

        templateName = null;
        data = new HashMap<String, String>();
    }

    public String getTemplateName() {
        return templateName;
    }

    public Map<String, String> getData() {
        return data;
    }

    private String templateName;

    private Map<String, String> data;

    public void setSelf(APJSONObject emailBody) {
        super.setSelf(emailBody);
        if (emailBody.isNull("templatename") == false)
            this.templateName = emailBody.optString("templatename");

        if (emailBody.isNull("data") == false) {
            this.data = new HashMap<String, String>();
            APJSONObject dataObject = emailBody.optJSONObject("data");
            Iterator<String> iterator = dataObject.keys();
            while (iterator.hasNext()) {
                String key = iterator.next();
                String value = dataObject.optString(key);
                this.data.put(key, value);
            }
        }
    }

    public APJSONObject getMap() throws APJSONException {

        APJSONObject jsonObject = super.getMap();
        jsonObject.put("templatename", this.templateName);
        APJSONObject body = new APJSONObject(this.data);
        jsonObject.put("data", body);
        return jsonObject;
    }
}
