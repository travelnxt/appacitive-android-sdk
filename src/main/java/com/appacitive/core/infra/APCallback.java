package com.appacitive.core.infra;

import java.io.Serializable;

/**
 * Created by sathley.
 */
public abstract class APCallback implements Serializable {

    public void success(String result) {
    }

    public void failure(Exception e) {
    }
}
