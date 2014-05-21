package com.appacitive.core.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by sathley.
 */
public class PagedList<T> implements Serializable {

    public PagedList() {
    }

    public List<T> results = new ArrayList<T>();

    public PagingInfo pagingInfo = new PagingInfo();


}
