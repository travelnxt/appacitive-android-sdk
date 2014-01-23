package com.appacitive.sdk.callbacks;

import com.appacitive.sdk.exceptions.AppacitiveException;

/**
 * Created by sathley.
 */
public abstract class Callback<T>
{
    public abstract void success(T result);

    public abstract void failure(T result, AppacitiveException e);
}
