package com.appacitive.sdk.query;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sathley.
 */
public class AppacitiveQuery  {

    public int pageNumber = 0;

    public int pageSize = 0;

    public String orderBy = null;

    public boolean isAscending;

    public List<String> freeTextTokens = new ArrayList<String>();

    public Query query;

    @Override
    public String toString()
    {
        List<String> items = new ArrayList<String>();

        if(this.pageNumber > 0)
            items.add("pNum=".concat(String.valueOf(pageNumber)));

        if(this.pageSize > 0)
            items.add("pSize=".concat(String.valueOf(pageSize)));

        if(this.orderBy != null){
            items.add("orderBy=".concat(orderBy));
            items.add("isAsc=".concat(String.valueOf(isAscending)));
        }

        if(this.pageSize > 0)
            items.add("pSize=".concat(String.valueOf(pageSize)));

        if(this.freeTextTokens != null && this.freeTextTokens.size() > 0)
        {
            items.add("freetext=".concat(join(freeTextTokens,",")));
        }

        if(this.query != null)
        {
            items.add(this.query.toString());
        }

        return join(items, "&");
    }

    private static String join(List<String> lst, String prefix)
    {
        final StringBuilder sb = new StringBuilder();
        String separator = "";
        for(Object f : lst)
        {
            sb.append(separator);
            separator = prefix;
            sb.append(f);
        }
        return sb.toString();
    }

}
