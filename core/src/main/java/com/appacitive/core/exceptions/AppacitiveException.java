package com.appacitive.core.exceptions;

import com.appacitive.core.model.AppacitiveStatus;

import java.io.Serializable;
import java.util.List;

/**
 * Created by sathley.
 */
public class AppacitiveException extends Exception implements Serializable {

    public AppacitiveException(AppacitiveStatus status) {
        super(status.getMessage());
        if (status != null) {
            code = status.getCode();
            referenceId = status.getReferenceId();
            apiVersion = status.getVersion();
            additionalMessages = status.getAdditionalMessages();
        }
    }

    public AppacitiveException(String message) {
        super(message);
    }

    public AppacitiveException(Throwable throwable) {
        super(throwable);
    }

    private String code;

    private List<String> additionalMessages;

    private String referenceId;

    public String getApiVersion() {
        return apiVersion;
    }

    public String getCode() {
        return code;
    }

    public List<String> getAdditionalMessages() {
        return additionalMessages;
    }

    public String getReferenceId() {
        return referenceId;
    }

    private String apiVersion;
}

