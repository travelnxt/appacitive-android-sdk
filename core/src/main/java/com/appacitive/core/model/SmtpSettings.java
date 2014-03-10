package com.appacitive.core.model;

import com.appacitive.core.infra.APSerializable;
import com.appacitive.core.apjson.APJSONException;
import com.appacitive.core.apjson.APJSONObject;

import java.io.Serializable;

/**
 * Created by sathley.
 */
public class SmtpSettings implements Serializable, APSerializable {

    public SmtpSettings(String username, String password, String host, int port, boolean enableSSL) {
        this.username = username;
        this.password = password;
        this.host = host;
        this.port = port;
        this.enableSSL = enableSSL;
    }

    public SmtpSettings() {
    }

    private String username = null;

    private String password = null;

    private String host = null;

    private int port;

    private boolean enableSSL;

    public void setSelf(APJSONObject smtp) {
        if (smtp != null) {
            if (smtp.isNull("username") == false)
                this.username = smtp.optString("username");

            if (smtp.isNull("password") == false)
                this.password = smtp.optString("password");

            if (smtp.isNull("host") == false)
                this.host = smtp.optString("host");

            if (smtp.isNull("port") == false)
                this.port = smtp.optInt("port");

            if (smtp.isNull("enablessl") == false)
                this.enableSSL = smtp.optBoolean("enablessl");
        }
    }

    public boolean isEnableSSL() {
        return enableSSL;
    }

    public int getPort() {
        return port;
    }

    public String getHost() {
        return host;
    }

    public String getPassword() {
        return password;
    }

    public String getUsername() {
        return username;
    }

    public APJSONObject getMap() throws APJSONException {
        APJSONObject jsonObject = new APJSONObject();
        jsonObject.put("username", this.username);
        jsonObject.put("password", this.password);
        jsonObject.put("host", this.host);
        jsonObject.put("port", this.port);
        jsonObject.put("enablessl", this.enableSSL);
        return jsonObject;
    }
}
