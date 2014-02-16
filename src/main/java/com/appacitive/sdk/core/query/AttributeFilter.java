package com.appacitive.sdk.core.query;

/**
 * Created by sathley.
 */
public class AttributeFilter extends Filter implements Query {

    public AttributeFilter(String propertyName)
    {
        this.key = propertyName;
    }

    public AttributeFilter isEqualTo(String value)
    {
        this.operator = "==";
        this.value = value;
        return this;
    }

    public AttributeFilter like(String value)
    {
        this.operator = "like";
        this.value = value;
        return this;
    }

    public AttributeFilter startsWith(String value)
    {
        this.operator = "like";
        this.value = value + "*";
        return this;
    }

    public AttributeFilter endsWith(String value)
    {
        this.operator = "like";
        this.value = "*" + value;
        return this;
    }

    @Override
    public String asString() {
        return String.format("@%s %s '%s'", this.key, this.operator, this.value);
    }
}
