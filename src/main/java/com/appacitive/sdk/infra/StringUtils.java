package com.appacitive.sdk.infra;

import java.util.List;

/**
 * Created by sathley.
 */
public class StringUtils {

    public static String join(List<String> lst, String prefix)
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
