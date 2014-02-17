package com.appacitive.sdk.core.model;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by sathley.
 */
public class SmtpSettings {

    public SmtpSettings(String username, String password, String host, int port, boolean enableSSL)
    {
        this.username = username;
        this.password = password;
        this.host = host;
        this.port = port;
        this.enableSSL = enableSSL;
    }

    private String username = null;

    private String password = null;

    private String host = null;

    private int port;

    private boolean enableSSL;

    public SmtpSettings(Map<String, Object> smtp) {
        Object object = smtp.get("username");
        if(object != null)
            this.username = object.toString();

        object = smtp.get("password");
        if(object != null)
            this.password = object.toString();

        object = smtp.get("host");
        if(object != null)
            this.host = object.toString();

        object = smtp.get("port");
        if(object != null)
            this.port = Integer.valueOf(object.toString());

        object = smtp.get("enablessl");
        if(object != null)
            this.enableSSL = Boolean.valueOf(object.toString());
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

    public Map<String, Object> getMap()
    {
        Map<String, Object> nativeMap = new HashMap<String, Object>();
        nativeMap.put("username", this.username);
        nativeMap.put("password", this.password);
        nativeMap.put("host", this.host);
        nativeMap.put("port", this.port);
        nativeMap.put("enablessl", this.enableSSL);
        return nativeMap;
    }
}
