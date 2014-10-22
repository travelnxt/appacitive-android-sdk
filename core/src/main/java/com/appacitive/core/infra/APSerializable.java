package com.appacitive.core.infra;

import com.appacitive.core.apjson.APJSONException;
import com.appacitive.core.apjson.APJSONObject;

/**
 * Created by sathley.
 */
public interface APSerializable {

    void setSelf(APJSONObject APEntity);

    APJSONObject getMap() throws APJSONException;
}
