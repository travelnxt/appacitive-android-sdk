package com.appacitive.sdk.exceptions;

import java.io.Serializable;

public class UserAuthException extends Exception implements Serializable
{
    public final static String message = "No logged in user found.";
}
