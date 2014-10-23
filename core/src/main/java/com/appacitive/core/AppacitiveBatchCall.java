package com.appacitive.core;

import com.appacitive.core.apjson.APJSONException;
import com.appacitive.core.apjson.APJSONObject;
import com.appacitive.core.exceptions.AppacitiveException;
import com.appacitive.core.infra.*;
import com.appacitive.core.interfaces.AsyncHttp;
import com.appacitive.core.interfaces.Logger;
import com.appacitive.core.model.AppacitiveStatus;
import com.appacitive.core.model.BatchCallRequest;
import com.appacitive.core.model.BatchCallResponse;
import com.appacitive.core.model.Callback;

import java.io.Serializable;
import java.util.Map;

/**
 * Created by sathley on 10/22/2014.
 */
public class AppacitiveBatchCall implements Serializable {

    public final static Logger LOGGER = APContainer.build(Logger.class);
    public static void Fire(BatchCallRequest request, final Callback<BatchCallResponse> callback)
    {
        LOGGER.info("Multi call");
        final String url = Urls.Misc.batchCallUrl().toString();
        final Map<String, String> headers = Headers.assemble();
        final APJSONObject payload;
        try {
            payload = request.getMap();
        } catch (APJSONException e) {
            throw new RuntimeException(e);
        }

        AsyncHttp asyncHttp = APContainer.build(AsyncHttp.class);
        asyncHttp.put(url, headers, payload.toString(), new APCallback() {
            @Override
            public void success(String result) {
                APJSONObject jsonObject;
                try {
                    jsonObject = new APJSONObject(result);
                } catch (APJSONException e) {
                    throw new RuntimeException(e);
                }
                AppacitiveStatus status = new AppacitiveStatus(jsonObject.optJSONObject("status"));
                if (status.isSuccessful()) {
                    BatchCallResponse response = new BatchCallResponse(jsonObject.optJSONArray("nodes"), jsonObject.optJSONArray("edges"));
                    if (callback != null) {
                        callback.success(response);
                    }
                } else {
                    if (callback != null)
                        callback.failure(null, new AppacitiveException(status));
                }
            }

            @Override
            public void failure(Exception e) {
                if (callback != null)
                    callback.failure(null, e);
            }
        });
    }
}
