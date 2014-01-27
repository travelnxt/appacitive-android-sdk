package com.appacitive.sdk;

import com.appacitive.sdk.callbacks.Callback;
import com.appacitive.sdk.exceptions.AppacitiveException;
import com.appacitive.sdk.infra.AppacitiveHttp;
import com.appacitive.sdk.infra.Headers;
import com.appacitive.sdk.infra.NodeHelper;
import com.appacitive.sdk.infra.Urls;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.logging.Logger;

/**
 * Created by sathley.
 */
public class AppacitiveGraphSearch {

    private final static Logger LOGGER = Logger.getLogger(AppacitiveGraphSearch.class.getName());

    public static void filterQueryInBackground(String queryName, final Map<String, String> placeHolders, Callback<List<Long>> callback) {
        final String url = Urls.Misc.filterQueryUrl(queryName).toString();
        final Map<String, String> headers = Headers.assemble();
        final Map<String, Object> payload = new HashMap<String, Object>() {{
            put("placeholders", placeHolders);
        }};

        Future<Map<String, Object>> future = ExecutorServiceWrapper.submit(new Callable<Map<String, Object>>() {
            @Override
            public Map<String, Object> call() throws Exception {
                return AppacitiveHttp.post(url, headers, payload);
            }
        });

        try {
            Map<String, Object> responseMap = future.get();
            AppacitiveStatus status = new AppacitiveStatus((Map<String, Object>) responseMap.get("status"));
            if (status.isSuccessful()) {
                if (callback != null) {
                    List<Long> ids = new ArrayList<Long>();
                    List<String> strIds = (ArrayList<String>) responseMap.get("ids");
                    for (String id : strIds) {
                        ids.add(Long.parseLong(id));
                    }
                    callback.success(ids);
                }

            } else {
                if (callback != null)
                    callback.failure(null, new AppacitiveException(status));
            }

        } catch (Exception e) {

        }

    }

    public static void projectQueryInBackground(String queryName, final List<Long> ids, final Map<String, String> placeHolders, Callback<List<AppacitiveGraphNode>> callback) {
        final String url = Urls.Misc.projectQueryUrl(queryName).toString();
        final Map<String, String> headers = Headers.assemble();
        final List<String> strIds = new ArrayList<String>();
        for (long id : ids) {
            strIds.add(String.valueOf(id));
        }
        final Map<String, Object> payload = new HashMap<String, Object>() {{
            put("placeholders", placeHolders);
            put("ids", strIds);
        }};
        Future<Map<String, Object>> future = ExecutorServiceWrapper.submit(new Callable<Map<String, Object>>() {
            @Override
            public Map<String, Object> call() throws Exception {
                return AppacitiveHttp.post(url, headers, payload);
            }
        });

        try {
            Map<String, Object> responseMap = future.get();
            AppacitiveStatus status = new AppacitiveStatus((Map<String, Object>) responseMap.get("status"));
            if (status.isSuccessful()) {
                if (callback != null) {
                    for (Map.Entry<String, Object> property : responseMap.entrySet()) {
                        if (property.getKey().equals("status") == false) {
                            callback.success(AppacitiveGraphSearch.parseProjectionResult((ArrayList<Object>) ((Map<String, Object>)property.getValue()).get("values")));
                        }
                    }
                } else {
                    if (callback != null)
                        callback.failure(null, new AppacitiveException(status));
                }
            }
        } catch (Exception e) {

        }
    }

    private static List<AppacitiveGraphNode> parseProjectionResult(List<Object> values) {
        List<AppacitiveGraphNode> nodes = new ArrayList<AppacitiveGraphNode>();
        for (Object value : values) {
            nodes.add(NodeHelper.convertNode((Map<String, Object>)value));
        }
        return nodes;
    }
}
