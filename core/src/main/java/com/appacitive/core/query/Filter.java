package com.appacitive.core.query;

import java.io.Serializable;

/**
 * Created by sathley.
 */
public abstract class Filter implements Serializable, Query {

    protected String key = null;

    protected String value = null;

    protected String operator = null;
}
