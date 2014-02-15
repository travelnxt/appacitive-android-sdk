package com.appacitive.sdk;

import java.util.Map;

/**
 * Created by sathley.
 */
public class PagingInfo {

    public PagingInfo(Map<String, Object> pagingInfo)
    {
        if (pagingInfo != null) {
            Object object = pagingInfo.get("pagenumber");
            if (object != null)
                this.pageNumber = (Integer) object;

            object = pagingInfo.get("pagesize");
            if (object != null)
                this.pageSize = (Integer) object;

            object = pagingInfo.get("totalrecords");
            if (object != null)
                this.totalRecords = (Integer) object;
        }
    }

    public int pageNumber = 0;

    public int pageSize = 0;

    public int totalRecords = 0;
}
