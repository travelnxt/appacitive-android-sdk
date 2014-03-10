package com.appacitive.core.infra;

import com.appacitive.core.apjson.APJSONException;
import com.appacitive.core.apjson.APJSONObject;

/**
 * Created by sathley.
 */
public interface APSerializable {

    public void setSelf(APJSONObject APEntity);

    public APJSONObject getMap() throws APJSONException;
}
