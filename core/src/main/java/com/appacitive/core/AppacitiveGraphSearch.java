package com.appacitive.core;

import com.appacitive.core.apjson.APJSONArray;
import com.appacitive.core.apjson.APJSONException;
import com.appacitive.core.apjson.APJSONObject;
import com.appacitive.core.exceptions.AppacitiveException;
import com.appacitive.core.infra.*;
import com.appacitive.core.interfaces.AsyncHttp;
import com.appacitive.core.model.AppacitiveGraphNode;
import com.appacitive.core.model.AppacitiveStatus;
import com.appacitive.core.model.Callback;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

/**
 * Created by sathley.
 */
public class AppacitiveGraphSearch implements Serializable {

    public final static Logger LOGGER = Logger.getLogger(AppacitiveGraphSearch.class.getName());

    public static void filterQueryInBackground(String queryName, final Map<String, String> placeHolders, final Callback<List<Long>> callback) {
        final String url = Urls.Misc.filterQueryUrl(queryName).toString();
        final Map<String, String> headers = Headers.assemble();

        APJSONObject payload = new APJSONObject();
        try {
            if (placeHolders != null)
                payload.put("placeholders", new APJSONObject(placeHolders));
        } catch (APJSONException e) {
            throw new RuntimeException(e);
        }
        AsyncHttp asyncHttp = APContainer.build(AsyncHttp.class);
        asyncHttp.post(url, headers, payload.toString(), new APCallback() {
            @Override
            public void success(String result) {
                try {
                    APJSONObject jsonObject = new APJSONObject(result);
                    AppacitiveStatus status = new AppacitiveStatus(jsonObject.optJSONObject("status"));
                    if (status.isSuccessful()) {
                        List<Long> ids = new ArrayList<Long>();
                        APJSONArray idsArray = jsonObject.optJSONArray("ids");
                        for (int i = 0; i < idsArray.length(); i++) {
                            String id = idsArray.optString(i);
                            ids.add(Long.valueOf(id));
                        }
                        if (callback != null) {
                            callback.success(ids);
                        }
                    } else {
                        if (callback != null)
                            callback.failure(null, new AppacitiveException(status));
                    }
                } catch (Exception e) {
                    if (callback != null)
                        callback.failure(null, e);
                }
            }

            @Override
            public void failure(Exception e) {
                if (callback != null)
                    callback.failure(null, e);
            }
        });
    }

    public static void projectQueryInBackground(String queryName, final List<Long> ids, final Map<String, String> placeHolders, final Callback<List<AppacitiveGraphNode>> callback) {
        final String url = Urls.Misc.projectQueryUrl(queryName).toString();
        final Map<String, String> headers = Headers.assemble();
        final List<String> strIds = new ArrayList<String>();
        for (long id : ids) {
            strIds.add(String.valueOf(id));
        }

        APJSONObject payload = new APJSONObject();
        try {
            if (placeHolders != null)
                payload.put("placeholders", new APJSONObject(placeHolders));
            if (ids != null)
                payload.put("ids", new APJSONArray(strIds));
        } catch (APJSONException e) {
            throw new RuntimeException(e);
        }
        AsyncHttp asyncHttp = APContainer.build(AsyncHttp.class);
        asyncHttp.post(url, headers, payload.toString(), new APCallback() {
            @Override
            public void success(String result) {
                try {
                    APJSONObject jsonObject = new APJSONObject(result);
                    AppacitiveStatus status = new AppacitiveStatus(jsonObject.optJSONObject("status"));
                    if (status.isSuccessful()) {
                        List<AppacitiveGraphNode> nodes = new ArrayList<AppacitiveGraphNode>();
                        Iterator<String> iterator = jsonObject.keys();
                        while (iterator.hasNext()) {
                            String key = iterator.next();
                            if (key.equals("status") == false)
                                nodes = AppacitiveGraphSearch.parseProjectionResult(jsonObject.optJSONObject(key).optJSONArray("values"));
                        }
                        if (callback != null) {
                            callback.success(nodes);
                        }
                    } else {
                        if (callback != null)
                            callback.failure(null, new AppacitiveException(status));
                    }
                } catch (Exception e) {
                    if (callback != null)
                        callback.failure(null, e);
                }
            }

            @Override
            public void failure(Exception e) {
                if (callback != null)
                    callback.failure(null, e);
            }
        });
    }

    private static List<AppacitiveGraphNode> parseProjectionResult(APJSONArray values) {
        List<AppacitiveGraphNode> nodes = new ArrayList<AppacitiveGraphNode>();
        for (int i = 0; i < values.length(); i++) {
            nodes.add(NodeHelper.convertNode(values.optJSONObject(i)));
        }
        return nodes;
    }
}
