//package com.appacitive.android;
//
//import android.util.Log;
//import com.appacitive.core.AppacitiveObject;
//import com.appacitive.core.model.Callback;
//import com.appacitive.core.model.PagedList;
//import com.appacitive.core.query.AppacitiveQuery;
//import com.appacitive.core.query.PropertyFilter;
//
//import java.util.List;
//
///**
// * Created by sathley.
// */
//public class SampleTest {
//
//    public void docTest() throws Exception {
//
//
//        //  Build the query
//        AppacitiveQuery appacitiveQuery = new AppacitiveQuery();
//        appacitiveQuery.query = new PropertyFilter("firstname").isEqualTo("John");
//
//        //  Fire the query
//        List<String> fields = null;
//        AppacitiveObject.findInBackground("player", appacitiveQuery, fields, new Callback<PagedList<AppacitiveObject>>() {
//            @Override
//            public void success(PagedList<AppacitiveObject> result) {
//                Log.v("TAG", String.format("%s Johns found.", result.pagingInfo.totalRecords));
//            }
//
//            @Override
//            public void failure(PagedList<AppacitiveObject> result, Exception e) {
//            }
//        });
//
//    }
//}
