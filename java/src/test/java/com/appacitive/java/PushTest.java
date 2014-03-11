package com.appacitive.java;

import com.appacitive.core.AppacitiveContextBase;
import com.appacitive.core.AppacitivePushNotification;
import com.appacitive.core.model.Callback;
import com.appacitive.core.model.Environment;
import com.appacitive.core.push.*;
import com.appacitive.core.query.BooleanOperator;
import com.appacitive.core.query.PropertyFilter;
import com.appacitive.core.query.Query;
import com.jayway.awaitility.Awaitility;
import org.junit.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import static com.jayway.awaitility.Awaitility.await;

/**
 * Created by sathley.
 */
public class PushTest {

    @BeforeClass
    public static void oneTimeSetUp() {
        AppacitiveContextBase.initialize("up8+oWrzVTVIxl9ZiKtyamVKgBnV5xvmV95u1mEVRrM=", Environment.sandbox, new JavaPlatform());
        Awaitility.setDefaultTimeout(5, TimeUnit.MINUTES);
    }

    @AfterClass
    public static void oneTimeTearDown() {
        // one-time cleanup code
    }

//    private static AtomicBoolean somethingHappened;

    @Before
    public void beforeTest() {
//        somethingHappened = new AtomicBoolean(false);
    }

    @Test
    public void broadcastPushTest() {
        final AtomicBoolean somethingHappened = new AtomicBoolean(false);
        AppacitivePushNotification.Broadcast("helo world").withBadge("+1").withExpiry(500).withData(new HashMap<String, String>() {{
            put("a1", "v1");
            put("a2", "v2");
        }}).sendInBackground(new Callback<String>() {
            @Override
            public void success(String result) {
                assert Long.valueOf(result) > 0;
                somethingHappened.set(true);
            }

            @Override
            public void failure(String result, Exception e) {
                Assert.fail(e.getMessage());
            }
        });
        await().untilTrue(somethingHappened);
    }

    @Test
    public void sendToDevicesTest() {
        final AtomicBoolean somethingHappened = new AtomicBoolean(false);
        AppacitivePushNotification.ToDeviceIds("hello guys", new ArrayList<String>() {{
            add("1");
            add("2");
        }}).withData(new HashMap<String, String>() {{
            put("a1", "v1");
        }}).sendInBackground(
                new Callback<String>() {
                    @Override
                    public void success(String result) {
                        assert Long.valueOf(result) > 0;
                        somethingHappened.set(true);
                    }

                    @Override
                    public void failure(String result, Exception e) {
                        Assert.fail(e.getMessage());
                    }
                }
        );
        await().untilTrue(somethingHappened);
    }

    @Test
    public void sendToChannelsTest() {
        final AtomicBoolean somethingHappened = new AtomicBoolean(false);
        AppacitivePushNotification.ToChannels("hello guys", new ArrayList<String>() {{
            add("channel1");
            add("channel2");
        }}).withData(new HashMap<String, String>() {{
            put("a1", "v1");
        }}).sendInBackground(
                new Callback<String>() {
                    @Override
                    public void success(String result) {
                        assert Long.valueOf(result) > 0;
                        somethingHappened.set(true);
                    }

                    @Override
                    public void failure(String result, Exception e) {
                        Assert.fail(e.getMessage());
                    }
                }
        );
        await().untilTrue(somethingHappened);
    }

    @Test
    public void sendUsingQueryTest() {
        final AtomicBoolean somethingHappened = new AtomicBoolean(false);

        AppacitivePushNotification.ToQueryResult("hello guys", BooleanOperator.and(new ArrayList<Query>() {{
            add(new PropertyFilter("devicetype").isEqualTo("ios"));
            add(new PropertyFilter("isactive").isEqualTo(true));
        }}))
                .withData(new HashMap<String, String>() {{
                    put("a1", "v1");
                }}).sendInBackground(
                new Callback<String>() {
                    @Override
                    public void success(String result) {
                        assert Long.valueOf(result) > 0;
                        somethingHappened.set(true);
                    }

                    @Override
                    public void failure(String result, Exception e) {
                        Assert.fail(e.getMessage());
                    }
                }
        );
        await().untilTrue(somethingHappened);
    }

    @Test
    public void iosAndAndroidOptionsTest() {
        final AtomicBoolean somethingHappened = new AtomicBoolean(false);
        AppacitivePushNotification.Broadcast("hi ios & android")
                .withPlatformOptions(new IosOptions("soundfile"))
                .withPlatformOptions(new AndroidOptions("title"))
                .sendInBackground(new Callback<String>() {
                    @Override
                    public void success(String result) {
                        assert Long.valueOf(result) > 0;
                        somethingHappened.set(true);
                    }

                    @Override
                    public void failure(String result, Exception e) {
                        Assert.fail(e.getMessage());

                    }
                });
        await().untilTrue(somethingHappened);
    }

    @Test
    public void wpToastOptionsTest() {
        final AtomicBoolean somethingHappened = new AtomicBoolean(false);
        WindowsPhoneOptions options = new WindowsPhoneOptions(new ToastNotification("text1", "text2", "path"));
        AppacitivePushNotification.Broadcast("hi ios & android")
                .withPlatformOptions(options)
                .sendInBackground(new Callback<String>() {
                    @Override
                    public void success(String result) {
                        assert Long.valueOf(result) > 0;
                        somethingHappened.set(true);
                    }

                    @Override
                    public void failure(String result, Exception e) {
                        Assert.fail(e.getMessage());

                    }
                });
        await().untilTrue(somethingHappened);
    }

    @Test
    public void wpRawOptionsTest() {
        final AtomicBoolean somethingHappened = new AtomicBoolean(false);
        WindowsPhoneOptions options = new WindowsPhoneOptions(new RawNotification("raw data"));
        AppacitivePushNotification.Broadcast("hi ios & android")
                .withPlatformOptions(options)
                .withPlatformOptions(new AndroidOptions("title"))
                .withPlatformOptions(new IosOptions("soundfile")
                ).sendInBackground(new Callback<String>() {
            @Override
            public void success(String result) {
                assert Long.valueOf(result) > 0;
                somethingHappened.set(true);
            }

            @Override
            public void failure(String result, Exception e) {
                Assert.fail(e.getMessage());

            }
        });
        await().untilTrue(somethingHappened);
    }

    @Test
    public void wpFlipTileOptionsTest() {
        final AtomicBoolean somethingHappened = new AtomicBoolean(false);
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
                somethingHappened.set(true);
            }

            @Override
            public void failure(String result, Exception e) {
                Assert.fail(e.getMessage());

            }
        });
        await().untilTrue(somethingHappened);
    }

    @Test
    public void wpIconicTileOptionsTest() {
        final AtomicBoolean somethingHappened = new AtomicBoolean(false);
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
                somethingHappened.set(true);
            }

            @Override
            public void failure(String result, Exception e) {
                Assert.fail(e.getMessage());

            }
        });
        await().untilTrue(somethingHappened);
    }

    @Test
    public void wpCyclicTileOptionsTest() {
        final AtomicBoolean somethingHappened = new AtomicBoolean(false);
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
        List<String> images = new ArrayList<String>() {{
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
                somethingHappened.set(true);
            }

            @Override
            public void failure(String result, Exception e) {
                Assert.fail(e.getMessage());

            }
        });
        await().untilTrue(somethingHappened);
    }

}
