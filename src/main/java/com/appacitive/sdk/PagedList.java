package com.appacitive.sdk;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by sathley.
 */
public class PagedList<T> {

    public PagedList(Map<String, Object> pagingInfo) {
        if (pagingInfo != null) {

            Object object = pagingInfo.get("pagenumber");
            if (object != null)
                this.pageNumber = (Long) object;

            object = pagingInfo.get("pagesize");
            if (object != null)
                this.pageSize = (Long) object;

            object = pagingInfo.get("totalrecords");
            if (object != null)
                this.totalRecords = (Long) object;
        }
    }

    public List<T> results = new ArrayList<T>();

    public long pageNumber = 0;

    public long pageSize = 0;

    public long totalRecords = 0;
}
