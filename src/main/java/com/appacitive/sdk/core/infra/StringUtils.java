package com.appacitive.sdk.core.infra;

import java.util.List;

/**
 * Created by sathley.
 */
public class StringUtils {

    public static String join(List<String> lst, String prefix)
    {
        final StringBuilder sb = new StringBuilder();
        String separator = "";
        for(String f : lst)
        {
            sb.append(separator);
            separator = prefix;
            sb.append(f);
        }
        return sb.toString();
    }
}
