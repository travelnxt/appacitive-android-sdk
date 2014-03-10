package com.appacitive.core.infra;

import java.io.Serializable;
import java.util.Map;

/**
 * Created by sathley.
 */
public class FutureResult<T> implements Serializable {

    public FutureResult<T> withResult(T result) {
        this.result = result;
        return this;
    }

    public FutureResult<T> withException(Exception exception) {
        this.exception = exception;
        return this;
    }

    public FutureResult<T> withResponseMap(Map<String, Object> responseMap) {
        this.responseMap = responseMap;
        return this;
    }

    public boolean isSuccessful() {
        return exception == null;
    }

    private T result;

    private Exception exception;

    private Map<String, Object> responseMap;

    public Exception getException() {
        return exception;
    }

    public T getResult() {
        return result;
    }

    public Map<String, Object> getResponseMap() {
        return responseMap;
    }
}
