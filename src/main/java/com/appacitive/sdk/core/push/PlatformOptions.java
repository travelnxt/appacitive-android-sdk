package com.appacitive.sdk.core.push;

import java.util.HashMap;
import java.util.Map;

public class PlatformOptions {

    public IosOptions iOS;

    public AndroidOptions android;

    public WindowsPhoneOptions windowsPhone;

    public boolean isEmpty() {
        return
                (this.iOS == null || this.iOS.isEmpty() == true) &&
                        (this.android == null || this.android.isEmpty() == true) &&
                        (this.windowsPhone == null || this.windowsPhone.isEmpty() == true);
    }

    public Map<String, Object> getMap()
    {
        Map<String, Object> nativeMap = new HashMap<String, Object>();
        if(this.isEmpty() == false);
        {
            if(this.iOS != null && this.iOS.isEmpty() == false)
                nativeMap.putAll(this.iOS.getMap());

            if(this.android != null && this.android.isEmpty() == false)
                nativeMap.putAll(this.android.getMap());

            if(this.windowsPhone != null && this.windowsPhone.isEmpty() == false)
                nativeMap.putAll(this.windowsPhone.getMap());
        }
        return nativeMap;
    }

}
