package com.appacitive.core.exceptions;

import java.io.Serializable;

public class UserAuthException extends RuntimeException implements Serializable {
    public final static String message = "No logged in user found.";
}
