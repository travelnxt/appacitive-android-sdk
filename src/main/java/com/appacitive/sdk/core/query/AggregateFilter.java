package com.appacitive.sdk.core.query;

/**
 * Created by sathley.
 */
public class AggregateFilter extends Filter {

    public AggregateFilter(String propertyName)
    {
        this.key = propertyName;
    }

    public AggregateFilter isEqualTo(double value)
    {
        this.operator = "==";
        this.value = String.valueOf(value);
        return this;
    }

    public AggregateFilter isLessThan(double value)
    {
        this.operator = "<";
        this.value = String.valueOf(value);
        return this;
    }

    public AggregateFilter isGreaterThan(double value)
    {
        this.operator = ">";
        this.value = String.valueOf(value);
        return this;
    }

    public AggregateFilter isLessThanEqualTo(double value)
    {
        this.operator = "<=";
        this.value = String.valueOf(value);
        return this;
    }

    public AggregateFilter isGreaterThanEqualTo(double value)
    {
        this.operator = ">=";
        this.value = String.valueOf(value);
        return this;
    }

    public AggregateFilter between(double minValue, double maxValue)
    {
        this.operator = "between";
        this.value = String.format("(%s,%s)", String.valueOf(minValue), String.valueOf(maxValue));
        return this;
    }

    @Override
    public String asString() {
        return String.format("$%s %s %s", this.key, this.operator, this.value);
    }
}
