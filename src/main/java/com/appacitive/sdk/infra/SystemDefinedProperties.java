package com.appacitive.sdk.infra;

import java.util.Arrays;
import java.util.List;

/**
 * Created by sathley.
 */
public class SystemDefinedProperties {
    public final static List<String> ConnectionSystemProperties = Arrays.asList("__relationtype", "__relationid", "__id", "__createdby", "__lastmodifiedby",
            "__utcdatecreated", "__utclastupdateddate", "__tags", "__attributes", "__properties",
            "__revision", "__endpointa", "__endpointb");

    public final static List<String> ObjectSystemProperties = Arrays.asList("__type", "__typeid", "__id", "__createdby", "__lastmodifiedby",
            "__utcdatecreated", "__utclastupdateddate", "__tags", "__attributes", "__properties",
            "__revision");
    public final static String id = "__id";

    public final static String relationType = "__relationtype";

    public final static String relationId = "__relationid";

    public final static String createdBy = "__createdby";

    public final static String lastModifiedBy = "__lastmodifiedby";

    public final static String utcDateCreated = "__utcdatecreated";

    public final static String utcLastUpdatedDate = "__utclastupdateddate";

    public final static String tags = "__tags";

    public final static String attributes = "__attributes";

    public final static String revision = "__revision";

    public final static String endpointA = "__endpointa";

    public final static String endpointB = "__endpointb";

    public final static String label = "label";
}
