import com.appacitive.sdk.AppacitiveContext;
import com.appacitive.sdk.AppacitivePushNotification;
import com.appacitive.sdk.infra.JavaPlatform;
import com.appacitive.sdk.model.Callback;
import com.appacitive.sdk.model.Environment;
import com.appacitive.sdk.model.PlatformType;
import com.appacitive.sdk.push.*;
import com.appacitive.sdk.query.BooleanOperator;
import com.appacitive.sdk.query.PropertyFilter;
import com.appacitive.sdk.query.Query;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
* Created by sathley.
*/
public class PushTest {

    @BeforeClass
    public static void oneTimeSetUp() {
        AppacitiveContext.initialize("up8+oWrzVTVIxl9ZiKtyamVKgBnV5xvmV95u1mEVRrM=", Environment.sandbox, new JavaPlatform());
    }

    @AfterClass
    public static void oneTimeTearDown() {
        // one-time cleanup code
    }

    @Test
    public void broadcastPushTest()
    {
        AppacitivePushNotification.Broadcast("helo world").withBadge("+1").withExpiry(500).withData(new HashMap<String, String>() {{
            put("a1", "v1");
            put("a2", "v2");
        }}).sendInBackground(new Callback<String>() {
            @Override
            public void success(String result) {
                assert Long.valueOf(result) > 0;
            }

            @Override
            public void failure(String result, Exception e) {
                Assert.fail(e.getMessage());
            }
        });
    }

    @Test
    public void sendToDevicesTest()
    {
        AppacitivePushNotification.ToDeviceIds("hello guys", new ArrayList<String>(){{add("1");add("2");}}).withData(new HashMap<String, String>(){{put("a1","v1");}}).sendInBackground(
                new Callback<String>() {
                    @Override
                    public void success(String result) {
                        assert Long.valueOf(result) > 0;
                    }

                    @Override
                    public void failure(String result, Exception e) {
                        Assert.fail(e.getMessage());
                    }
                }
        );
    }

    @Test
    public void sendToChannelsTest()
    {
        AppacitivePushNotification.ToChannels("hello guys", new ArrayList<String>() {{
            add("channel1");
            add("channel2");
        }}).withData(new HashMap<String, String>(){{put("a1","v1");}}).sendInBackground(
                new Callback<String>() {
                    @Override
                    public void success(String result) {
                        assert Long.valueOf(result) > 0;
                    }

                    @Override
                    public void failure(String result, Exception e) {
                        Assert.fail(e.getMessage());
                    }
                }
        );
    }

    @Test
    public void sendUsingQueryTest()
    {

        AppacitivePushNotification.ToQueryResult("hello guys", BooleanOperator.and(new ArrayList<Query>(){{add(new PropertyFilter("devicetype").isEqualTo("ios"));add(new PropertyFilter("isactive").isEqualTo(true));}}))
                .withData(new HashMap<String, String>() {{
        put("a1", "v1");
    }}).sendInBackground(
                new Callback<String>() {
                    @Override
                    public void success(String result) {
                        assert Long.valueOf(result) > 0;
                    }

                    @Override
                    public void failure(String result, Exception e) {
                        Assert.fail(e.getMessage());
                    }
                }
        );
    }

    @Test
    public void iosAndAndroidOptionsTest()
    {
        AppacitivePushNotification.Broadcast("hi ios & android")
                .withPlatformOptions(new IosOptions("soundfile"))
                .withPlatformOptions(new AndroidOptions("title"))
                .sendInBackground(new Callback<String>() {
                    @Override
                    public void success(String result) {
                        assert Long.valueOf(result) > 0;
                    }

                    @Override
                    public void failure(String result, Exception e) {
                        Assert.fail(e.getMessage());

                    }
                });
    }

    @Test
    public void wpToastOptionsTest()
    {
        WindowsPhoneOptions options = new WindowsPhoneOptions(new ToastNotification("text1", "text2", "path"));
        AppacitivePushNotification.Broadcast("hi ios & android")
                .withPlatformOptions(options)
                .sendInBackground(new Callback<String>() {
                    @Override
                    public void success(String result) {
                        assert Long.valueOf(result) > 0;
                    }

                    @Override
                    public void failure(String result, Exception e) {
                        Assert.fail(e.getMessage());

                    }
                });
    }

    @Test
    public void wpRawOptionsTest()
    {
        WindowsPhoneOptions options = new WindowsPhoneOptions(new RawNotification("raw data"));
        AppacitivePushNotification.Broadcast("hi ios & android")
                .withPlatformOptions(options)
                .withPlatformOptions(new AndroidOptions("title"))
                .withPlatformOptions(new IosOptions("soundfile")
                ).sendInBackground(new Callback<String>() {
            @Override
            public void success(String result) {
                assert Long.valueOf(result) > 0;
            }

            @Override
            public void failure(String result, Exception e) {
                Assert.fail(e.getMessage());

            }
        });
    }

    @Test
    public void wpFlipTileOptionsTest()
    {
        FlipTile tile = new FlipTile();
        tile.backBackgroundImage = "bbimage";
        tile.backContent = "back content";
        tile.backTitle = "back title";
        tile.frontBackgroundImage = "fbimage";
        tile.frontCount = "front count";
        tile.frontTitle = "front title";
        tile.smallBackgroundImage = "sbimage";
        tile.tileId = "id";
        tile.wideBackBackgroundImage = "wbbimg";
        tile.wideBackContent = "wide back content";
        tile.wideBackgroundImage = "wbimg";
        WindowsPhoneOptions options = new WindowsPhoneOptions(TileNotification.createNewFlipTile(tile));
        AppacitivePushNotification.Broadcast("hi ios & android")
                .withPlatformOptions(options)
                .withPlatformOptions(new AndroidOptions("title"))
                .withPlatformOptions(new IosOptions("soundfile")
                ).sendInBackground(new Callback<String>() {
            @Override
            public void success(String result) {
                assert Long.valueOf(result) > 0;
            }

            @Override
            public void failure(String result, Exception e) {
                Assert.fail(e.getMessage());

            }
        });
    }

    @Test
    public void wpIconicTileOptionsTest()
    {
        FlipTile flipTile = new FlipTile();
        flipTile.backBackgroundImage = "bbimage";
        flipTile.backContent = "back content";
        flipTile.backTitle = "back title";
        flipTile.frontBackgroundImage = "fbimage";
        flipTile.frontCount = "front count";
        flipTile.frontTitle = "front title";
        flipTile.smallBackgroundImage = "sbimage";
        flipTile.tileId = "id";
        flipTile.wideBackBackgroundImage = "wbbimg";
        flipTile.wideBackContent = "wide back content";
        flipTile.wideBackgroundImage = "wbimg";

        IconicTile iconicTile = new IconicTile();
        iconicTile.backgroundColor = "bg color";
        iconicTile.frontTitle = "front title";
        iconicTile.wideContent1 = "wc1";
        iconicTile.wideContent2 = "wc2";
        WindowsPhoneOptions options = new WindowsPhoneOptions(TileNotification.createNewIconicTile(iconicTile, flipTile));
        AppacitivePushNotification.Broadcast("hi ios & android")
                .withPlatformOptions(options)
                .withPlatformOptions(new AndroidOptions("title"))
                .withPlatformOptions(new IosOptions("soundfile")
                ).sendInBackground(new Callback<String>() {
            @Override
            public void success(String result) {
                assert Long.valueOf(result) > 0;
            }

            @Override
            public void failure(String result, Exception e) {
                Assert.fail(e.getMessage());

            }
        });
    }

    @Test
    public void wpCyclicTileOptionsTest()
    {
        FlipTile flipTile = new FlipTile();
        flipTile.backBackgroundImage = "bbimage";
        flipTile.backContent = "back content";
        flipTile.backTitle = "back title";
        flipTile.frontBackgroundImage = "fbimage";
        flipTile.frontCount = "front count";
        flipTile.frontTitle = "front title";
        flipTile.smallBackgroundImage = "sbimage";
        flipTile.tileId = "id";
        flipTile.wideBackBackgroundImage = "wbbimg";
        flipTile.wideBackContent = "wide back content";
        flipTile.wideBackgroundImage = "wbimg";

        CyclicTile cyclicTile = new CyclicTile();
        cyclicTile.frontTitle = "front title";
        cyclicTile.tileId = "id";
        List<String> images = new ArrayList<String>(){{
            add("img1");
            add("img2");
            add("img3");
        }};
        cyclicTile.images = new FixedSizeImageList(images);
        WindowsPhoneOptions options = new WindowsPhoneOptions(TileNotification.createNewCyclicTile(cyclicTile, flipTile));
        AppacitivePushNotification.Broadcast("hi ios & android")
                .withPlatformOptions(options)
                .withPlatformOptions(new AndroidOptions("title"))
                .withPlatformOptions(new IosOptions("soundfile")
                ).sendInBackground(new Callback<String>() {
            @Override
            public void success(String result) {
                assert Long.valueOf(result) > 0;
            }

            @Override
            public void failure(String result, Exception e) {
                Assert.fail(e.getMessage());

            }
        });
    }

}
