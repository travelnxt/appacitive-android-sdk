package com.appacitive.sdk.exceptions;

public class ValidationError extends Exception
{
    public ValidationError(String message)
    {
        this.message = message;
    }
    public String message;
}

