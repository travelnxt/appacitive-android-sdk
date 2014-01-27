package com.appacitive.sdk;

import com.appacitive.sdk.callbacks.Callback;
import com.appacitive.sdk.exceptions.AppacitiveException;
import com.appacitive.sdk.infra.AppacitiveHttp;
import com.appacitive.sdk.infra.Headers;
import com.appacitive.sdk.infra.Urls;

import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.logging.Logger;

/**
 * Created by sathley.
 */
public class AppacitivePushNotification {

    private final static Logger LOGGER = Logger.getLogger(AppacitivePushNotification.class.getName());

    private Map<String, Object> getMap()
    {
        // TODO
        return new HashMap<String, Object>();
    }

    public String alert;

    public String badge;

    public boolean isBroadcast;

    public String query;

    public int expiryInSeconds;

    private List<String> deviceIds = new ArrayList<String>();

    private List<String> channels = new ArrayList<String>();

    public Map<String, String> data = new HashMap<String, String>();

    public PlatformOptions platformOptions;

    public AppacitivePushNotification withBadge(String badge)
    {
        this.badge = badge;
        return this;
    }

    public AppacitivePushNotification withPlatformOptions(AndroidOptions options)
    {
        if (this.platformOptions == null)
            this.platformOptions = new PlatformOptions();
        this.platformOptions.android = options;
        return this;
    }

    public AppacitivePushNotification withPlatformOptions(WindowsPhoneOptions options)
    {
        if (this.platformOptions == null)
            this.platformOptions = new PlatformOptions();
        this.platformOptions.windowsPhone = options;
        return this;
    }

    public AppacitivePushNotification withData(Map<String, String> data)
    {
        this.data.putAll(data);
        return this;
    }

    public AppacitivePushNotification withExpiry(int seconds)
    {
        if (seconds <= 0)
            throw new IllegalArgumentException("Expiry time cannot be less than or equal to zero.");
        this.expiryInSeconds = seconds;
        return this;
    }

    public void sendInBackground(Callback<String> callback)
    {
        final String url = Urls.Misc.sendPushUrl().toString();
        final Map<String, String> headers = Headers.assemble();
        final Map<String, Object> payload = this.getMap();
        Future<Map<String, Object>> future = ExecutorServiceWrapper.submit(new Callable<Map<String, Object>>() {
            @Override
            public Map<String, Object> call() throws Exception {
                return AppacitiveHttp.post(url, headers, payload);
            }
        });
        try {
            Map<String, Object> responseMap = future.get();
            AppacitiveStatus status = new AppacitiveStatus((Map<String, Object>) responseMap.get("status"));
            if (status.isSuccessful()) {
                if (callback != null)
                    callback.success((String)responseMap.get("id"));
            } else {
                if (callback != null)
                    callback.failure(null, new AppacitiveException(status));
            }

        } catch (Exception e) {

        }
    }


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

    }

    public class IosOptions {
        public String soundFile;

        public IosOptions(String soundFile) {
            this.soundFile = soundFile;
        }

        public boolean isEmpty() {
            return this.soundFile == null || this.soundFile.isEmpty() == true;
        }
    }

    public class AndroidOptions {
        public String notificationTitle;

        public AndroidOptions(String notificationTitle) {
            this.notificationTitle = notificationTitle;
        }

        public boolean isEmpty() {
            return this.notificationTitle == null || this.notificationTitle.isEmpty() == true;
        }
    }

    public enum WindowsPhoneNotificationType {
        Toast,
        Tile,
        Raw
    }

    public class WindowsPhoneNotification {
        public WindowsPhoneNotification(WindowsPhoneNotificationType windowsPhoneNotificationType) {
            this.windowsPhoneNotificationType = windowsPhoneNotificationType;
        }

        public WindowsPhoneNotification() {
        }

        public WindowsPhoneNotificationType windowsPhoneNotificationType;
    }

    public class WindowsPhoneOptions {
        public WindowsPhoneOptions withToast(ToastNotification notification) {
            return new WindowsPhoneOptions(notification);
        }

        public WindowsPhoneOptions withTile(TileNotification notification) {
            return new WindowsPhoneOptions(notification);
        }

        public WindowsPhoneOptions withRaw(RawNotification notification) {
            return new WindowsPhoneOptions(notification);
        }

        public WindowsPhoneOptions(WindowsPhoneNotification notification) {
            this.notification = notification;
        }

        public WindowsPhoneNotification notification;

        public boolean isEmpty() {
            return this.notification == null;
        }
    }

    public class ToastNotification extends WindowsPhoneNotification {
        public ToastNotification(String text1, String text2, String path) {
            super(WindowsPhoneNotificationType.Toast);
            this.text1 = text1;
            this.text2 = text2;
            this.path = path;
        }

        public String text1;

        public String text2;

        public String path;
    }

    public class TileNotification extends WindowsPhoneNotification {
        public TileNotification() {
            super(WindowsPhoneNotificationType.Tile);
        }

        public TileNotification createNewFlipTile(FlipTile tile) {
            TileNotification notification = new TileNotification();
            notification.setWp75Tile(tile);
            notification.setWp7Tile(tile);
            notification.setWp8Tile(tile);
            return notification;
        }

        public TileNotification createNewIconicTile(IconicTile tile, FlipTile tileForWP75AndBelow) {
            TileNotification notification = new TileNotification();
            notification.setWp75Tile(tileForWP75AndBelow);
            notification.setWp7Tile(tileForWP75AndBelow);
            notification.setWp8Tile(tile);
            return notification;
        }

        public TileNotification createNewCyclicTile(CyclicTile tile, FlipTile tileForWP75AndBelow) {
            TileNotification notification = new TileNotification();
            notification.setWp75Tile(tileForWP75AndBelow);
            notification.setWp7Tile(tileForWP75AndBelow);
            notification.setWp8Tile(tile);
            return notification;
        }

        private WindowsPhoneTile _wp8Tile;

        public WindowsPhoneTile getWp8Tile() {
            return _wp8Tile;
        }

        public void setWp8Tile(WindowsPhoneTile tile) {
            _wp8Tile = tile;
        }

        private WindowsPhoneTile _wp75Tile;

        public WindowsPhoneTile getWp75Tile() {
            return _wp75Tile;
        }

        public void setWp75Tile(WindowsPhoneTile tile) {
            if (tile != null && tile.windowsPhoneTileType != WindowsPhoneTileType.Flip) {
                throw new IllegalArgumentException("Only flip tiles are supported for Windows Phone v7.");
            }
            _wp75Tile = tile;
        }

        private WindowsPhoneTile _wp7Tile;

        public WindowsPhoneTile getWp7Tile() {
            return _wp7Tile;
        }

        public void setWp7Tile(WindowsPhoneTile tile) {
            if (tile != null && tile.windowsPhoneTileType != WindowsPhoneTileType.Flip) {
                throw new IllegalArgumentException("Only flip tiles are supported for Windows Phone v7.");
            }
            _wp7Tile = tile;
        }
    }

    public class RawNotification extends WindowsPhoneNotification {
        public RawNotification() {
            super(WindowsPhoneNotificationType.Raw);
        }

        public String rawData;
    }

    public enum WindowsPhoneTileType {
        Flip,
        Cyclic,
        Iconic
    }

    public class WindowsPhoneTile {
        protected WindowsPhoneTile(WindowsPhoneTileType type) {
            this.windowsPhoneTileType = type;
        }

        public WindowsPhoneTileType windowsPhoneTileType;
    }

    public class FlipTile extends WindowsPhoneTile {
        public FlipTile() {
            super(WindowsPhoneTileType.Flip);
        }

        public String tileId;

        public String frontTitle;

        public String frontBackgroundImage;

        public String frontCount;

        public String smallBackgroundImage;

        public String wideBackgroundImage;

        public String backTitle;

        public String backContent;

        public String backBackgroundImage;

        public String wideBackContent;

        public String wideBackBackgroundImage;
    }

    public class CyclicTile extends WindowsPhoneTile {
        public CyclicTile(String frontTitle, String[] images) {
            this();
            this.frontTitle = frontTitle;
            this.images = new FixedSizeImageList(Arrays.asList(images));
        }

        public CyclicTile() {
            super(WindowsPhoneTileType.Cyclic);
        }

        public String tileId;

        public String frontTitle;

        public FixedSizeImageList images;
    }

    public class FixedSizeImageList {
        public FixedSizeImageList(List<String> images) {
            int index = 0;
            for (String image : images) {
                if (index == 9)
                    break;
                if (image != null && image.isEmpty() == false)
                    _images[index++] = image;
            }
        }

        private String[] _images = new String[9];

//        public String[] toArray()
//        {
//
//            return _images.Where(x => string.IsNullOrWhiteSpace(x) == false).ToArray();
//        }

        private void Set(int index, String image) {
            _images[index] = image;
        }

        private String Get(int index) {
            return _images[index];
        }

        public String getImage1() {
            return this.Get(0);
        }

        public void setImage1(String image1) {
            this.Set(0, image1);
        }

        public String getImage2() {
            return this.Get(1);
        }

        public void setImage2(String image2) {
            this.Set(1, image2);
        }

        public String getImage3() {
            return this.Get(2);
        }

        public void setImage3(String image3) {
            this.Set(2, image3);
        }

        public String getImage4() {
            return this.Get(3);
        }

        public void setImage4(String image4) {
            this.Set(3, image4);
        }

        public String getImage5() {
            return this.Get(4);
        }

        public void setImage5(String image5) {
            this.Set(4, image5);
        }

        public String getImage6() {
            return this.Get(5);
        }

        public void setImage6(String image6) {
            this.Set(5, image6);
        }

        public String getImage7() {
            return this.Get(6);
        }

        public void setImage7(String image7) {
            this.Set(6, image7);
        }

        public String getImage8() {
            return this.Get(7);
        }

        public void setImage8(String image8) {
            this.Set(7, image8);
        }

        public String getImage9() {
            return this.Get(8);
        }

        public void setImage9(String image9) {
            this.Set(8, image9);
        }
    }

    public class IconicTile extends WindowsPhoneTile {

        public IconicTile() {
            super(WindowsPhoneTileType.Iconic);
        }

        public String tileId;

        public String frontTitle;

        public String iconImage;

        public String smallIconImage;

        public String backgroundColor;

        public String wideContent1;

        public String wideContent2;

        public String wideContent3;
    }

}
