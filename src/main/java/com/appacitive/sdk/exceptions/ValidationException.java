package com.appacitive.sdk.exceptions;

public class ValidationException extends Exception
{
    public ValidationException(String message)
    {
        this.message = message;
    }
    public String message;
}

