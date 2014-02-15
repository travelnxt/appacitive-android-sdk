package com.appacitive.sdk;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by sathley.
 */
public class PagedList<T>  implements Serializable {

    public PagedList(Map<String, Object> pagingInfo) {

        if (pagingInfo != null) {
            this.pagingInfo = new PagingInfo(pagingInfo);
        }
    }

    public PagedList(){}

    public List<T> results = new ArrayList<T>();

    public PagingInfo pagingInfo;
}
