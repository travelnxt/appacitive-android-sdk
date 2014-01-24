package com.appacitive.sdk.query;

/**
 * Created by sathley.
 */
public class PropertyFilter implements Query {

    private String key = null;

    private String value = null;

    private String operator = null;

    public PropertyFilter(String propertyName)
    {
        this.key = propertyName;
    }


}
