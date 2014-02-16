package com.appacitive.sdk.core.exceptions;

import com.appacitive.sdk.core.model.AppacitiveStatus;

import java.io.Serializable;
import java.util.List;

/**
 * Created by sathley.
 */
public class AppacitiveException extends Exception implements Serializable {

    public AppacitiveException(AppacitiveStatus status)
    {
        super(status.message);
        if(status != null)
        {
            code = status.code;
            referenceId = status.referenceId;
            apiVersion = status.version;
            additionalMessages = status.additionalMessages;
        }
    }

    public String code;

    public List<String> additionalMessages;

    public String referenceId;

    public String apiVersion;
}

