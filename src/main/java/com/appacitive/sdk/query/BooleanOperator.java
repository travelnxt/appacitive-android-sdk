package com.appacitive.sdk.query;

import com.appacitive.sdk.infra.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sathley.
 */
public class BooleanOperator extends Object implements Query {

    public BooleanOperator(String operator, List<Query> filters)
    {
        this.operator = operator;
        this.filters = filters;
    }

    private String operator = null;

    private List<Query> filters = null;

    public BooleanOperator and(List<Query> filters)
    {
        return new BooleanOperator("and", filters);
    }

    public BooleanOperator or(List<Query> filters)
    {
        return new BooleanOperator("or", filters);
    }

    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        sb.append("( ");
        List<String> strFilters = new ArrayList<String>();
        for(Query qb : filters)
        {
            strFilters.add(qb.toString());
        }

        sb.append(StringUtils.join(strFilters, String.format(" %s ", this.operator)));
        sb.append(" )");
        return sb.toString();
    }
}
