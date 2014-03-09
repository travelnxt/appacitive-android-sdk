package com.appacitive.core.model;

import com.appacitive.core.infra.APSerializable;
import com.appacitive.core.apjson.APJSONObject;

import java.io.Serializable;

/**
 * Created by sathley.
 */
public class PagingInfo implements Serializable, APSerializable {

    public PagingInfo() {
    }

    public long pageNumber = 0;

    public long pageSize = 0;

    public long totalRecords = 0;

    @Override
    public void setSelf(APJSONObject pagingInfo) {
        if (pagingInfo != null) {

            if (pagingInfo.isNull("pagenumber") == false)
                this.pageNumber = pagingInfo.optLong("pagenumber");

            if (pagingInfo.isNull("pagesize") == false)
                this.pageSize = pagingInfo.optLong("pagesize");

            if (pagingInfo.isNull("totalrecords") == false)
                this.totalRecords = pagingInfo.optLong("totalrecords");
        }
    }

    @Override
    public APJSONObject getMap() {
        return null;
    }
}
