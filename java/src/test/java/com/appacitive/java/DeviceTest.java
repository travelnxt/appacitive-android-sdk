package com.appacitive.java;

import com.appacitive.core.AppacitiveContextBase;
import com.appacitive.core.AppacitiveDevice;
import com.appacitive.core.exceptions.ValidationException;
import com.appacitive.core.model.Callback;
import com.appacitive.core.model.Environment;
import com.appacitive.core.model.PagedList;
import com.appacitive.core.query.AppacitiveQuery;
import com.jayway.awaitility.Awaitility;
import org.junit.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;

import static com.jayway.awaitility.Awaitility.await;

/**
 * Created by sathley.
 */
public class DeviceTest {

    @BeforeClass
    public static void oneTimeSetUp() {
        AppacitiveContextBase.initialize("up8+oWrzVTVIxl9ZiKtyamVKgBnV5xvmV95u1mEVRrM=", Environment.sandbox, new JavaPlatform());
//        Awaitility.setDefaultTimeout(5, TimeUnit.MINUTES);
    }

    @AfterClass
    public static void oneTimeTearDown() {
        // one-time cleanup code
    }
//    private static AtomicBoolean somethingHappened;

    @Before
    public void beforeTest() {
        Awaitility.reset();
    }

    public static AppacitiveDevice getRandomDevice() {

        AppacitiveDevice device = new AppacitiveDevice();
        device.setDeviceToken(UUID.randomUUID().toString());
        device.setDeviceType("ios");
        device.setBadge(-12);
        device.setIsActive(true);
        double[] geo = new double[2];
        geo[0] = 10.11d;
        geo[1] = 20.22d;
        device.setLocation(geo);
        List<String> channels = new ArrayList<String>() {{
            add("channel1");
            add("channel2");
            add("channel3");
        }};
        device.setChannels(channels);

        device.setAttribute("a1", "v1");
        device.setAttribute("a1", "v1");
        device.setAttribute("a2", "v2");

        device.addTags(channels);
        return device;
    }

    @Test
    public void registerDeviceTest() throws ValidationException {
        final AtomicBoolean somethingHappened = new AtomicBoolean(false);
        AppacitiveDevice device = getRandomDevice();
        device.registerInBackground(new Callback<AppacitiveDevice>() {
            @Override
            public void success(AppacitiveDevice result) {
                assert result.getId() > 0;
                assert result.getAllAttributes().size() == 2;
                assert result.getAllTags().size() == 3;
                assert result.getChannels().size() == 3;
                assert result.getBadge() == -12;
                assert result.getIsActive() == true;
                assert result.getCreatedBy() != null && result.getCreatedBy().isEmpty() == false;
                assert result.getLastModifiedBy() != null && result.getLastModifiedBy().isEmpty() == false;
                somethingHappened.set(true);
            }

            @Override
            public void failure(AppacitiveDevice result, Exception e) {
                Assert.fail(e.getMessage());
            }
        });
        await().untilTrue(somethingHappened);
    }

    @Test
    public void getDeviceTest() throws ValidationException {
        final AtomicBoolean somethingHappened = new AtomicBoolean(false);
        AppacitiveDevice device = getRandomDevice();
        device.registerInBackground(new Callback<AppacitiveDevice>() {
            @Override
            public void success(final AppacitiveDevice result1) {

                AppacitiveDevice.getInBackground(result1.getId(), new Callback<AppacitiveDevice>() {
                    @Override
                    public void success(AppacitiveDevice result) {
                        assert result.getId() == result1.getId();
                        somethingHappened.set(true);
                    }

                    @Override
                    public void failure(AppacitiveDevice result, Exception e) {
                        Assert.fail(e.getMessage());
                    }
                });

            }
        });
        await().untilTrue(somethingHappened);
    }

    @Test
    public void updateDeviceTest() throws ValidationException {
        final AtomicBoolean somethingHappened = new AtomicBoolean(false);
        AppacitiveDevice device = getRandomDevice();
        device.registerInBackground(new Callback<AppacitiveDevice>() {
            @Override
            public void success(final AppacitiveDevice result) {
                result.setDeviceType("android");
                final String newToken = UUID.randomUUID().toString();
                result.setDeviceToken(newToken);
                result.addTag("newTag");
                result.setAttribute("new_a", "new_v");
                result.updateInBackground(false, new Callback<AppacitiveDevice>() {
                    @Override
                    public void success(AppacitiveDevice result1) {
                        assert result.getDeviceType().equals("android");
                        assert result.getDeviceToken().equals(newToken);

                        assert result1.getDeviceType().equals("android");
                        assert result1.getDeviceToken().equals(newToken);

                        somethingHappened.set(true);
                    }

                    @Override
                    public void failure(AppacitiveDevice result, Exception e) {
                        Assert.fail(e.getMessage());
                    }
                });
            }
        });
        await().untilTrue(somethingHappened);
    }

    @Test
    public void fetchLatestTest() throws ValidationException {
        final AtomicBoolean somethingHappened = new AtomicBoolean(false);
        AppacitiveDevice device = getRandomDevice();
        final String newToken = UUID.randomUUID().toString();
        device.registerInBackground(new Callback<AppacitiveDevice>() {
            @Override
            public void success(final AppacitiveDevice result) {

                AppacitiveDevice.getInBackground(result.getId(), new Callback<AppacitiveDevice>() {
                    @Override
                    public void success(final AppacitiveDevice result1) {
                        result1.setDeviceType("android");
                        result1.setDeviceToken(newToken);
                        result1.updateInBackground(false, new Callback<AppacitiveDevice>() {
                            @Override
                            public void success(AppacitiveDevice result2) {
                                result.fetchLatestInBackground(new Callback<Void>() {
                                    @Override
                                    public void success(Void result3) {
                                        assert result.getDeviceToken().equals(newToken);
                                        assert result.getDeviceType().equals("android");
                                        somethingHappened.set(true);
                                    }

                                    @Override
                                    public void failure(Void result, Exception e) {
                                        Assert.fail(e.getMessage());
                                    }
                                });
                            }

                            @Override
                            public void failure(AppacitiveDevice result, Exception e) {
                                Assert.fail(e.getMessage());
                            }
                        });

                    }
                });

            }
        });
        await().untilTrue(somethingHappened);
    }

    @Test
    public void deleteDeviceTest() throws ValidationException {
        final AtomicBoolean somethingHappened = new AtomicBoolean(false);
        final AppacitiveDevice device = getRandomDevice();
        device.registerInBackground(new Callback<AppacitiveDevice>() {
            @Override
            public void success(AppacitiveDevice result) {
                device.deleteInBackground(false, new Callback<Void>() {
                    @Override
                    public void success(Void result) {

                        AppacitiveDevice.getInBackground(device.getId(), new Callback<AppacitiveDevice>() {
                            @Override
                            public void success(AppacitiveDevice result) {
                                assert false;
                            }

                            @Override
                            public void failure(AppacitiveDevice result, Exception e) {
                                assert true;
                                somethingHappened.set(true);
                            }
                        });

                    }

                    @Override
                    public void failure(Void result, Exception e) {
                        Assert.fail(e.getMessage());
                    }
                });
            }
        });
        await().untilTrue(somethingHappened);
    }

    @Test
    public void findDeviceTest() throws ValidationException {
        final AtomicBoolean somethingHappened = new AtomicBoolean(false);
        AppacitiveDevice device = getRandomDevice();
        device.registerInBackground(new Callback<AppacitiveDevice>() {
            @Override
            public void success(AppacitiveDevice result) {
                AppacitiveQuery query = new AppacitiveQuery();

                AppacitiveDevice.findInBackground(query, null, new Callback<PagedList<AppacitiveDevice>>() {
                    @Override
                    public void success(PagedList<AppacitiveDevice> result) {
                        assert result.results.size() > 0;
                        somethingHappened.set(true);
                    }

                    @Override
                    public void failure(PagedList<AppacitiveDevice> result, Exception e) {
                        Assert.fail(e.getMessage());
                    }
                });
            }
        });
        await().untilTrue(somethingHappened);
    }

}
