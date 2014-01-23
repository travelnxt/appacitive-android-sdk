package com.appacitive.sdk.exceptions;

import com.appacitive.sdk.AppacitiveStatus;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by sathley.
 */
public class AppacitiveException extends Exception {

    public AppacitiveException(AppacitiveStatus status)
    {
        if(status != null)
        {
            code = status.code;
            message = status.message;
            referenceId = status.referenceId;
            apiVersion = status.version;
            additionalMessages = status.additionalMessages;
        }
    }

    public String code;

    public String message;

    public List<String> additionalMessages;

    public String referenceId;

    public String apiVersion;
}

