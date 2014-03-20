package com.appacitive.core.push;

import com.appacitive.core.apjson.APJSONException;
import com.appacitive.core.apjson.APJSONObject;
import com.appacitive.core.infra.APSerializable;

import java.io.Serializable;
import java.util.Iterator;

public class PlatformOptions implements Serializable, APSerializable {

    public IosOptions iOS;

    public AndroidOptions android;

    public WindowsPhoneOptions windowsPhone;

    public boolean isEmpty() {
        return
                (this.iOS == null || this.iOS.isEmpty() == true) &&
                        (this.android == null || this.android.isEmpty() == true) &&
                        (this.windowsPhone == null || this.windowsPhone.isEmpty() == true);
    }

    public void setSelf(APJSONObject APEntity) {

    }

    public synchronized APJSONObject getMap() throws APJSONException {
        APJSONObject nativeMap = new APJSONObject();
        if (this.isEmpty() == false) ;
        {
            if (this.iOS != null && this.iOS.isEmpty() == false) {
                APJSONObject ios = this.iOS.getMap();
                Iterator iosIterator = ios.keys();
                String iosKey;
                while (iosIterator.hasNext()) {
                    iosKey = (String) iosIterator.next();
                    nativeMap.put(iosKey, ios.get(iosKey));
                }
            }

            if (this.android != null && this.android.isEmpty() == false) {
                APJSONObject android = this.android.getMap();
                Iterator androidIterator = android.keys();
                String androidKey;
                while (androidIterator.hasNext()) {
                    androidKey = (String) androidIterator.next();
                    nativeMap.put(androidKey, android.get(androidKey));
                }
            }

            if (this.windowsPhone != null && this.windowsPhone.isEmpty() == false) {
                APJSONObject windowsPhone = this.windowsPhone.getMap();
                Iterator windowsPhoneIterator = windowsPhone.keys();
                String windowsPhoneKey;
                while (windowsPhoneIterator.hasNext()) {
                    windowsPhoneKey = (String) windowsPhoneIterator.next();
                    nativeMap.put(windowsPhoneKey, windowsPhone.get(windowsPhoneKey));
                }
            }
        }
        return nativeMap;
    }
}
