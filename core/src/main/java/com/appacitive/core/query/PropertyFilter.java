package com.appacitive.core.query;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by sathley.
 */
public class PropertyFilter extends Filter {

    final static DateFormat dtf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSSS'Z'");

    DateTimeFormatter fmt = DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ss.SSSSSSS'Z'");

    public PropertyFilter(String propertyName) {
        this.key = propertyName;
    }

    public PropertyFilter isEqualTo(String value) {
        this.operator = "==";
        this.value = value;
        return this;
    }

    public PropertyFilter isEqualTo(long value) {
        this.operator = "==";
        this.value = String.valueOf(value);
        return this;
    }

    public PropertyFilter isEqualTo(double value) {
        this.operator = "==";
        this.value = String.valueOf(value);
        return this;
    }

    public PropertyFilter isEqualTo(boolean value) {
        this.operator = "==";
        this.value = String.valueOf(value);
        return this;
    }

//    public PropertyFilter isEqualToDate(Date value) {
//        this.operator = "==";
//        this.value = df.format(value);
//        return this;
//    }
//
//    public PropertyFilter isEqualToTime(Date value) {
//        this.operator = "==";
//        this.value = tf.format(value);
//        return this;
//    }

    public synchronized PropertyFilter isEqualTo(Date value) {
        this.operator = "==";
        this.value = dtf.format(value);
        return this;
    }

    public synchronized PropertyFilter isEqualTo(DateTime value) {
        this.operator = "==";
        this.value = fmt.print(value);
        return this;
    }

    public PropertyFilter isNotEqualTo(String value) {
        this.operator = "<>";
        this.value = value;
        return this;
    }

    public PropertyFilter isNotEqualTo(long value) {
        this.operator = "<>";
        this.value = String.valueOf(value);
        return this;
    }

    public PropertyFilter isNotEqualTo(double value) {
        this.operator = "<>";
        this.value = String.valueOf(value);
        return this;
    }

//    public PropertyFilter isNotEqualToDate(Date value) {
//        this.operator = "<>";
//        this.value = df.format(value);
//        return this;
//    }
//
//    public PropertyFilter isNotEqualToTime(Date value) {
//        this.operator = "<>";
//        this.value = tf.format(value);
//        return this;
//    }

    public synchronized PropertyFilter isNotEqualTo(Date value) {
        this.operator = "<>";
        this.value = dtf.format(value);
        return this;
    }

    public synchronized PropertyFilter isNotEqualTo(DateTime value) {
        this.operator = "<>";
        this.value = fmt.print(value);
        return this;
    }

    public PropertyFilter between(double minValue, double maxValue) {
        this.operator = "between";
        this.value = String.format("(%s,%s)", String.valueOf(minValue), String.valueOf(maxValue));
        return this;
    }

    public PropertyFilter between(long minValue, long maxValue) {
        this.operator = "between";
        this.value = String.format("(%s,%s)", String.valueOf(minValue), String.valueOf(maxValue));
        return this;
    }

    public synchronized PropertyFilter between(Date minValue, Date maxValue) {
        this.operator = "between";
        this.value = String.format("(%s,%s)", dtf.format(minValue), dtf.format(maxValue));
        return this;
    }

    public synchronized PropertyFilter between(DateTime minValue, DateTime maxValue) {
        this.operator = "between";
        this.value = String.format("(%s,%s)", fmt.print(minValue), fmt.print(maxValue));
        return this;
    }

//    public PropertyFilter betweenDate(Date minValue, Date maxValue) {
//        this.operator = "between";
//        this.value = String.format("(%s,%s)", df.format(minValue), df.format(maxValue));
//        return this;
//    }
//
//    public PropertyFilter betweenTime(Date minValue, Date maxValue) {
//        this.operator = "between";
//        this.value = String.format("(%s,%s)", tf.format(minValue), tf.format(maxValue));
//        return this;
//    }

    public PropertyFilter isGreaterThan(double value) {
        this.operator = ">";
        this.value = String.valueOf(value);
        return this;
    }

    public PropertyFilter isGreaterThan(long value) {
        this.operator = ">";
        this.value = String.valueOf(value);
        return this;
    }

    public synchronized PropertyFilter isGreaterThan(Date value) {
        this.operator = ">";
        this.value = dtf.format(value);
        return this;
    }

    public synchronized PropertyFilter isGreaterThan(DateTime value) {
        this.operator = ">";
        this.value = fmt.print(value);
        return this;
    }

//    public PropertyFilter isGreaterThanDate(Date value) {
//        this.operator = ">";
//        this.value = df.format(value);
//        return this;
//    }
//
//    public PropertyFilter isGreaterThanTime(Date value) {
//        this.operator = ">";
//        this.value = tf.format(value);
//        return this;
//    }

    public PropertyFilter isGreaterThanEqualTo(double value) {
        this.operator = ">=";
        this.value = String.valueOf(value);
        return this;
    }

    public PropertyFilter isGreaterThanEqualTo(long value) {
        this.operator = ">=";
        this.value = String.valueOf(value);
        return this;
    }

    public synchronized PropertyFilter isGreaterThanEqualTo(Date value) {
        this.operator = ">=";
        this.value = dtf.format(value);
        return this;
    }

    public synchronized PropertyFilter isGreaterThanEqualTo(DateTime value) {
        this.operator = ">=";
        this.value = fmt.print(value);
        return this;
    }

//    public PropertyFilter isGreaterThanEqualToDate(Date value) {
//        this.operator = ">=";
//        this.value = df.format(value);
//        return this;
//    }
//
//    public PropertyFilter isGreaterThanEqualToTime(Date value) {
//        this.operator = ">=";
//        this.value = tf.format(value);
//        return this;
//    }

    public PropertyFilter isLessThan(double value) {
        this.operator = "<";
        this.value = String.valueOf(value);
        return this;
    }

    public PropertyFilter isLessThan(long value) {
        this.operator = "<";
        this.value = String.valueOf(value);
        return this;
    }

    public synchronized PropertyFilter isLessThan(Date value) {
        this.operator = "<";
        this.value = dtf.format(value);
        return this;
    }

    public synchronized PropertyFilter isLessThan(DateTime value) {
        this.operator = "<";
        this.value = fmt.print(value);
        return this;
    }

//    public PropertyFilter isLessThanDate(Date value) {
//        this.operator = "<";
//        this.value = df.format(value);
//        return this;
//    }
//
//    public PropertyFilter isLessThanTime(Date value) {
//        this.operator = "<";
//        this.value = tf.format(value);
//        return this;
//    }

    public PropertyFilter isLessThanEqualTo(double value) {
        this.operator = "<=";
        this.value = String.valueOf(value);
        return this;
    }

    public PropertyFilter isLessThanEqualTo(long value) {
        this.operator = "<=";
        this.value = String.valueOf(value);
        return this;
    }

    public synchronized PropertyFilter isLessThanEqualTo(Date value) {
        this.operator = "<=";
        this.value = dtf.format(value);
        return this;
    }

    public synchronized PropertyFilter isLessThanEqualTo(DateTime value) {
        this.operator = "<=";
        this.value = fmt.print(value);
        return this;
    }

//    public PropertyFilter isLessThanEqualToDate(Date value) {
//        this.operator = "<=";
//        this.value = df.format(value);
//        return this;
//    }
//
//    public PropertyFilter isLessThanEqualToTime(Date value) {
//        this.operator = "<=";
//        this.value = tf.format(value);
//        return this;
//    }

    public PropertyFilter like(String value) {
        this.operator = "like";
        this.value = "*" + value + "*";
        return this;
    }

    public PropertyFilter startsWith(String value) {
        this.operator = "like";
        this.value = value + "*";
        return this;
    }

    public PropertyFilter endsWith(String value) {
        this.operator = "like";
        this.value = "*" + value;
        return this;
    }

    public PropertyFilter match(String value)
    {
        this.operator = "match";
        this.value = value;
        return this;
    }


    @Override
    public synchronized String asString() {
        if (this.operator.equals("between"))
            return String.format("*%s %s %s", this.key, this.operator, this.value);
        return String.format("*%s %s '%s'", this.key, this.operator, this.value);
    }
}
