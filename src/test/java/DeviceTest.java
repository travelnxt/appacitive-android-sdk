import com.appacitive.sdk.AppacitiveContext;
import com.appacitive.sdk.AppacitiveDevice;
import com.appacitive.sdk.PagedList;
import com.appacitive.sdk.callbacks.Callback;
import com.appacitive.sdk.exceptions.ValidationException;
import com.appacitive.sdk.infra.Environment;
import com.appacitive.sdk.query.AppacitiveQuery;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by sathley.
 */
public class DeviceTest {

    @BeforeClass
    public static void oneTimeSetUp() {
        AppacitiveContext.initialize("up8+oWrzVTVIxl9ZiKtyamVKgBnV5xvmV95u1mEVRrM=", Environment.sandbox);
    }

    @AfterClass
    public static void oneTimeTearDown() {
        // one-time cleanup code
    }

    private AppacitiveDevice getRandomDevice() {

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
            }

            @Override
            public void failure(AppacitiveDevice result, Exception e) {
                assert false;
            }
        });
    }

    @Test
    public void getDeviceTest() throws ValidationException {
        AppacitiveDevice device = getRandomDevice();
        device.registerInBackground(new Callback<AppacitiveDevice>() {
            @Override
            public void success(final AppacitiveDevice result1) {
                try {
                    AppacitiveDevice.getInBackground(result1.getId(), new Callback<AppacitiveDevice>() {
                        @Override
                        public void success(AppacitiveDevice result) {
                            assert result.getId() == result1.getId();
                        }

                        @Override
                        public void failure(AppacitiveDevice result, Exception e) {
                            assert false;
                        }
                    });
                } catch (ValidationException e) {
                }
            }
        });
    }

    @Test
    public void updateDeviceTest() throws ValidationException {
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
                    }

                    @Override
                    public void failure(AppacitiveDevice result, Exception e) {
                        super.failure(result, e);
                    }
                });
            }
        });
    }

    @Test
    public void fetchLatestTest() throws ValidationException {
        AppacitiveDevice device = getRandomDevice();
        final String newToken = UUID.randomUUID().toString();
        device.registerInBackground(new Callback<AppacitiveDevice>() {
            @Override
            public void success(final AppacitiveDevice result) {
                try {
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
                                        }

                                        @Override
                                        public void failure(Void result, Exception e) {
                                            assert false;
                                        }
                                    });
                                }
                            });

                        }
                    });
                } catch (ValidationException e) {
                }
            }
        });
    }

    @Test
    public void deleteDeviceTest() throws ValidationException {
        final AppacitiveDevice device = getRandomDevice();
        device.registerInBackground(new Callback<AppacitiveDevice>() {
            @Override
            public void success(AppacitiveDevice result) {
                device.deleteInBackground(false, new Callback<Void>() {
                    @Override
                    public void success(Void result) {
                        try {
                            AppacitiveDevice.getInBackground(device.getId(), new Callback<AppacitiveDevice>() {
                                @Override
                                public void success(AppacitiveDevice result) {
                                    assert false;
                                }

                                @Override
                                public void failure(AppacitiveDevice result, Exception e) {
                                    assert true;
                                }
                            });
                        } catch (ValidationException e) {
                        }
                    }

                    @Override
                    public void failure(Void result, Exception e) {
                        assert false;
                    }
                });
            }
        });

    }

    @Test
    public void findDeviceTest() throws ValidationException
    {
        AppacitiveDevice device = getRandomDevice();
        device.registerInBackground(new Callback<AppacitiveDevice>() {
            @Override
            public void success(AppacitiveDevice result) {
                AppacitiveQuery query = new AppacitiveQuery();

                AppacitiveDevice.findInBackground(query, null, new Callback<PagedList<AppacitiveDevice>>() {
                    @Override
                    public void success(PagedList<AppacitiveDevice> result) {
                        assert result.results.size() > 0;
                    }

                    @Override
                    public void failure(PagedList<AppacitiveDevice> result, Exception e) {
                        assert false;
                    }
                });
            }
        });
    }

}
