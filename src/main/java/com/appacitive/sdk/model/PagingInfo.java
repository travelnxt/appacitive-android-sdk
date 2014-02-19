package com.appacitive.sdk.model;

import java.io.Serializable;
import java.util.Map;

/**
 * Created by sathley.
 */
public class PagingInfo implements Serializable {

    public PagingInfo(Map<String, Object> pagingInfo)
    {
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

    public long pageNumber = 0;

    public long pageSize = 0;

    public long totalRecords = 0;
}
