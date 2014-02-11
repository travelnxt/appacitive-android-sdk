package com.appacitive.sdk;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by sathley.
 */
public class AppacitiveStatus  implements Serializable {

    public AppacitiveStatus(Map<String, Object> status)
    {
        additionalMessages = new ArrayList<String>();
        if(status != null)
        {
            code = (String)status.get("code");
            message = (String)status.get("message");
            referenceId = (String)status.get("referenceid");
            version = (String)status.get("version");
            additionalMessages = (ArrayList<String>)status.get("additionalmessages");
        }
    }

    public String code = null;

    public String message = null;

    public String referenceId = null;

    public String version = null;

    public List<String> additionalMessages = null;

    public boolean isSuccessful()
    {
        return code != null && code.equals("200");
    }
}
