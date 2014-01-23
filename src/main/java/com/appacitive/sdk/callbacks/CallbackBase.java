package com.appacitive.sdk.callbacks;

import com.appacitive.sdk.exceptions.AppacitiveException;

/**
 * Created by sathley.
 */
public abstract class CallbackBase<T> {

        public void success(T result){}

        public void failure(T result, AppacitiveException e){}
}
