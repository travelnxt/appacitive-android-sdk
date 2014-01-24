package com.appacitive.sdk;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sathley.
 */
public class PagedList<T> {

    public List<T> results = new ArrayList<T>();

    public int pageNumber = 0;

    public int pageSize = 0;

    public int totalRecords = 0;
}
