package com.appacitive.core.model;

import java.io.Serializable;

/**
 * Created by sathley.
 */
public abstract class Callback<T> implements Serializable {
    public void success(T result) {
    }

    public void failure(T result, Exception e) {
    }
}
