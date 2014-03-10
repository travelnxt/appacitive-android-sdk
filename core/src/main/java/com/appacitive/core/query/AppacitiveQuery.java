package com.appacitive.core.query;

import com.appacitive.core.infra.StringUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by sathley.
 */
public class AppacitiveQuery implements Serializable {

    public int pageNumber = 0;

    public int pageSize = 0;

    public String orderBy = null;

    public boolean isAscending;

    public List<String> freeTextTokens = new ArrayList<String>();

    public Query query;

    public Map<String, String> asQueryStringParameters() {
        Map<String, String> queryStringParameters = new HashMap<String, String>();

        if (this.pageNumber > 0)
            queryStringParameters.put("pNum", String.valueOf(pageNumber));

        if (this.pageSize > 0)
            queryStringParameters.put("pSize", String.valueOf(pageSize));

        if (this.orderBy != null) {
            queryStringParameters.put("orderBy=", orderBy);
            queryStringParameters.put("isAsc", String.valueOf(isAscending));
        }

        if (this.freeTextTokens != null && this.freeTextTokens.size() > 0) {
            queryStringParameters.put("freeText", StringUtils.join(freeTextTokens, ","));
        }

        if (this.query != null) {
            queryStringParameters.put("query", this.query.asString());
        }

        return queryStringParameters;
    }
}
