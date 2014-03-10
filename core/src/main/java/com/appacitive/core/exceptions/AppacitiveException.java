package com.appacitive.core.exceptions;

import com.appacitive.core.model.AppacitiveStatus;

import java.io.Serializable;
import java.util.List;

/**
 * Created by sathley.
 */
public class AppacitiveException extends Exception implements Serializable {

    public AppacitiveException(AppacitiveStatus status) {
        super(status.message);
        if (status != null) {
            code = status.code;
            referenceId = status.referenceId;
            apiVersion = status.version;
            additionalMessages = status.additionalMessages;
        }
    }

    public AppacitiveException(String message) {
        super(message);
    }

    public AppacitiveException(Throwable throwable)
    {
        super(throwable);
    }

    public String code;

    public List<String> additionalMessages;

    public String referenceId;

    public String apiVersion;
}

