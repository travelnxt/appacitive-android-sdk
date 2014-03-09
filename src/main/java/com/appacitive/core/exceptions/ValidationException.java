package com.appacitive.core.exceptions;

import java.io.Serializable;

public class ValidationException extends Exception implements Serializable {
    public ValidationException(String message) {
        this.message = message;
    }

    public String message;
}

