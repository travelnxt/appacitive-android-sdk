package com.appacitive.java;

import com.appacitive.core.*;
import com.appacitive.core.exceptions.AppacitiveException;
import com.appacitive.core.exceptions.ValidationException;
import com.appacitive.core.infra.ErrorCodes;
import com.appacitive.core.model.Callback;
import com.appacitive.core.model.Environment;
import com.jayway.awaitility.Awaitility;
import org.junit.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import static com.jayway.awaitility.Awaitility.await;
import static org.hamcrest.Matchers.equalTo;

/**
 * Created by sathley.
 */
public class ConnectionTest {

    @BeforeClass
    public static void oneTimeSetUp() {
        AppacitiveContextBase.initialize(Keys.masterKey, Environment.sandbox, new JavaPlatform());
    }


    @AfterClass
    public static void oneTimeTearDown() {
        // one-time cleanup code
    }

    @Before
    public void beforeTest() {
        Awaitility.reset();
    }

    private String getRandomString() {
        return UUID.randomUUID().toString();
    }

    @Test
    public void createConnectionBetweenNewObjects()  {
        final AtomicBoolean somethingHappened = new AtomicBoolean(false);
        final AppacitiveObject parent = new AppacitiveObject("object");
        final AppacitiveObject child = new AppacitiveObject("object");
        parent.createInBackground(new Callback<AppacitiveObject>() {
            @Override
            public void success(final AppacitiveObject p) {
                try {
                    child.createInBackground(new Callback<AppacitiveObject>() {
                        @Override
                        public void success(final AppacitiveObject c) {
                            AppacitiveConnection sibling = new AppacitiveConnection("sibling");
                            sibling.endpointA.label = "object";
                            sibling.endpointB.label = "object";
                            sibling.endpointA.objectId = p.getId();
                            sibling.endpointB.objectId = c.getId();
                            sibling.setStringProperty("field1", "hello");
                            sibling.setStringProperty("field2", "1520");

                            sibling.addTag("t1");
                            sibling.addTag("t2");

                            sibling.setAttribute("a1", "v1");
                            sibling.setAttribute("a2", "v2");
                            try {
                                sibling.createInBackground(new Callback<AppacitiveConnection>() {
                                    @Override
                                    public void success(AppacitiveConnection result) {
                                        assert result.getId() > 0;
                                        somethingHappened.set(true);

                                    }

                                    @Override
                                    public void failure(AppacitiveConnection result, Exception e) {
                                        assert false;
                                    }
                                });
                            } catch (ValidationException e) {
                                Assert.fail(e.getMessage());
                            }

                        }

                    });
                } catch (ValidationException e) {
                    Assert.fail(e.getMessage());
                }
            }
        });
        await().untilTrue(somethingHappened);
    }

    @Test
    public void createConnectionBetweenNewUserAndDeviceTest() throws ValidationException {
        final AtomicBoolean somethingHappened = new AtomicBoolean(false);
        AppacitiveUser user = new AppacitiveUser();
        user.setPassword(getRandomString());
        user.setUsername(getRandomString());
        user.setFirstName(getRandomString());
        user.setEmail(getRandomString().concat("@gmail.com"));

        AppacitiveDevice device = new AppacitiveDevice();
        device.setDeviceToken(getRandomString());
        device.setDeviceType("ios");

        new AppacitiveConnection("my_device").toNewUser("user", user).fromNewDevice("device", device).createInBackground(new Callback<AppacitiveConnection>() {
            @Override
            public void success(AppacitiveConnection result) {
                assert result.getId() > 0;
                assert result.endpointA.object instanceof AppacitiveDevice;
                assert result.endpointB.object instanceof AppacitiveUser;
                somethingHappened.set(true);
            }

            @Override
            public void failure(AppacitiveConnection result, Exception e) {
                Assert.fail(e.getMessage());
            }
        });
        await().untilTrue(somethingHappened);
    }

    @Test
    public void createConnectionBetweenExistingUserAndDeviceTest() throws ValidationException {
        final AtomicBoolean somethingHappened = new AtomicBoolean(false);
        final AppacitiveUser user = new AppacitiveUser();
        user.setPassword(getRandomString());
        user.setUsername(getRandomString());
        user.setFirstName(getRandomString());
        user.setEmail(getRandomString().concat("@gmail.com"));
        user.signupInBackground(new Callback<AppacitiveUser>() {
            @Override
            public void success(AppacitiveUser result) {
                final AppacitiveDevice device = new AppacitiveDevice();
                device.setDeviceToken(getRandomString());
                device.setDeviceType("ios");
                try {
                    device.registerInBackground(new Callback<AppacitiveDevice>() {
                        @Override
                        public void success(AppacitiveDevice result) {
                            try {
                                new AppacitiveConnection("my_device").fromExistingUser("user", user.getId()).toExistingDevice("device", device.getId()).createInBackground(new Callback<AppacitiveConnection>() {
                                    @Override
                                    public void success(AppacitiveConnection result) {
                                        assert result.getId() > 0;
                                        somethingHappened.set(true);
                                    }

                                    @Override
                                    public void failure(AppacitiveConnection result, Exception e) {
                                        Assert.fail(e.getMessage());
                                    }
                                });
                            } catch (ValidationException e) {
                                Assert.fail(e.getMessage());
                            }
                        }
                    });
                } catch (ValidationException e) {
                    Assert.fail(e.getMessage());
                }
            }
        });
        await().untilTrue(somethingHappened);
    }

    @Test
    public void createConnectionBetweenNewObjectsUsingFluentSyntaxTest() throws ValidationException {
        final AtomicBoolean somethingHappened = new AtomicBoolean(false);
        final AppacitiveObject parent = new AppacitiveObject("object");
        final AppacitiveObject child = new AppacitiveObject("object");
        parent.createInBackground(new Callback<AppacitiveObject>() {
            @Override
            public void success(final AppacitiveObject p) {
                try {
                    child.createInBackground(new Callback<AppacitiveObject>() {
                        @Override
                        public void success(final AppacitiveObject c) {
                            AppacitiveConnection sibling = new AppacitiveConnection("sibling").fromExistingObject("object", p.getId()).toExistingObject("object", c.getId());
                            sibling.setStringProperty("field1", "hello");
                            sibling.setStringProperty("field2", "1520");

                            sibling.addTag("t1");
                            sibling.addTag("t2");

                            sibling.setAttribute("a1", "v1");
                            sibling.setAttribute("a2", "v2");
                            try {

                                sibling.createInBackground(new Callback<AppacitiveConnection>() {
                                    @Override
                                    public void success(AppacitiveConnection result) {
                                        assert result.getId() > 0;
                                        somethingHappened.set(true);
                                    }

                                    @Override
                                    public void failure(AppacitiveConnection result, Exception e) {
                                        Assert.fail(e.getMessage());
                                    }
                                });
                            } catch (ValidationException e) {
                                Assert.fail(e.getMessage());
                            }
                        }
                    });
                } catch (ValidationException e) {
                    Assert.fail(e.getMessage());
                }
            }
        });
        await().untilTrue(somethingHappened);
    }

    @Test
    public void createConnectionBetweenNewAndExistingObjectTest() throws ValidationException {
        final AtomicBoolean somethingHappened = new AtomicBoolean(false);
        final AppacitiveObject parent = new AppacitiveObject("object");
        parent.addTag("parent");
        final AppacitiveObject child = new AppacitiveObject("object");
        child.addTag("child");
        parent.createInBackground(new Callback<AppacitiveObject>() {
            @Override
            public void success(final AppacitiveObject p) {
                AppacitiveConnection sibling = new AppacitiveConnection("sibling").fromExistingObject("object", p.getId()).toNewObject("object", child);
                try {
                    sibling.createInBackground(new Callback<AppacitiveConnection>() {
                        @Override
                        public void success(AppacitiveConnection result) {
                            assert result.getId() > 0;
                            somethingHappened.set(true);
//                            assert child.getId() > 0;
//                            assert result.endpointB.objectId == child.getId();
                        }

                        @Override
                        public void failure(AppacitiveConnection result, Exception e) {
                            Assert.fail(e.getMessage());
                        }
                    });
                } catch (ValidationException e) {
                    Assert.fail(e.getMessage());
                }
            }
        });
        await().untilTrue(somethingHappened);
    }

    @Test
    public void createConnectionBetweenNewAndNewObjectsTest() throws ValidationException {
        final AtomicBoolean somethingHappened = new AtomicBoolean(false);
        final AppacitiveObject parent = new AppacitiveObject("object");
        final AppacitiveObject child = new AppacitiveObject("object");
        new AppacitiveConnection("sibling").fromNewObject("object", parent).toNewObject("object", child).createInBackground(new Callback<AppacitiveConnection>() {
            @Override
            public void success(AppacitiveConnection result) {
                assert result.getId() > 0;
                somethingHappened.set(true);
//                assert parent.getId() > 0;
//                assert child.getId() > 0;
//                assert result.endpointA.objectId == parent.getId();
//                assert result.endpointB.objectId == child.getId();
            }

            @Override
            public void failure(AppacitiveConnection result, Exception e) {
                Assert.fail(e.getMessage());
            }
        });
        await().untilTrue(somethingHappened);
    }

    @Test
    public void getConnectionTest() throws ValidationException {
        final AtomicBoolean somethingHappened = new AtomicBoolean(false);
        final AppacitiveObject parent = new AppacitiveObject("object");
        final AppacitiveObject child = new AppacitiveObject("object");
        new AppacitiveConnection("sibling").fromNewObject("object", parent).toNewObject("object", child).createInBackground(new Callback<AppacitiveConnection>() {
            @Override
            public void success(final AppacitiveConnection result1) {
                AppacitiveConnection.getInBackground("sibling", result1.getId(), null, new Callback<AppacitiveConnection>() {
                    @Override
                    public void success(AppacitiveConnection result2) {
                        assert result1.getId() == result2.getId();
                        somethingHappened.set(true);
                    }

                    @Override
                    public void failure(AppacitiveConnection result, Exception e) {
                        Assert.fail(e.getMessage());
                    }
                });
            }
        });
        await().untilTrue(somethingHappened);
    }

    @Test
    public void multiGetConnectionTest() throws ValidationException, InterruptedException {
        final AtomicBoolean somethingHappened = new AtomicBoolean(false);
        final AtomicInteger count = new AtomicInteger(0);
        final List<Long> ids = new ArrayList<Long>();
        for (int i = 0; i < 3; i++) {
            final AppacitiveObject parent = new AppacitiveObject("object");
            final AppacitiveObject child = new AppacitiveObject("object");
            new AppacitiveConnection("sibling").fromNewObject("object", parent).toNewObject("object", child).createInBackground(new Callback<AppacitiveConnection>() {
                @Override
                public void success(AppacitiveConnection result) {
                    ids.add(result.getId());
                    count.incrementAndGet();
                }
            });
        }
        await().untilAtomic(count, equalTo(3));
        AppacitiveConnection.multiGetInBackground("sibling", ids, null, new Callback<List<AppacitiveConnection>>() {
            @Override
            public void success(List<AppacitiveConnection> result) {
                assert result.size() == 3;
                somethingHappened.set(true);
            }

            @Override
            public void failure(List<AppacitiveConnection> result, Exception e) {
                Assert.fail(e.getMessage());
            }
        });
        await().untilTrue(somethingHappened);
    }

    @Test
    public void deleteConnectionTest() throws ValidationException {
        final AtomicBoolean somethingHappened = new AtomicBoolean(false);
        final AppacitiveObject parent = new AppacitiveObject("object");
        final AppacitiveObject child = new AppacitiveObject("object");
        new AppacitiveConnection("sibling").fromNewObject("object", parent).toNewObject("object", child).createInBackground(new Callback<AppacitiveConnection>() {
            @Override
            public void success(AppacitiveConnection result) {
                result.deleteInBackground(new Callback<Void>() {
                    @Override
                    public void success(Void result) {
                        assert true;
                        somethingHappened.set(true);
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
    public void multiDeleteConnectionsTest() throws ValidationException, InterruptedException {
        final AtomicBoolean somethingHappened = new AtomicBoolean(false);
        final AtomicInteger count = new AtomicInteger(0);
        final List<Long> ids = new ArrayList<Long>();
        for (int i = 0; i < 3; i++) {
            final AppacitiveObject parent = new AppacitiveObject("object");
            final AppacitiveObject child = new AppacitiveObject("object");
            new AppacitiveConnection("sibling").fromNewObject("object", parent).toNewObject("object", child).createInBackground(new Callback<AppacitiveConnection>() {
                @Override
                public void success(AppacitiveConnection result) {
                    ids.add(result.getId());
                    count.incrementAndGet();
                }
            });
        }
        await().untilAtomic(count, equalTo(3));
        AppacitiveConnection.bulkDeleteInBackground("sibling", ids, new Callback<Void>() {
            @Override
            public void success(Void result) {
                for (long id : ids) {
                    AppacitiveConnection.getInBackground("sibling", id, null, new Callback<AppacitiveConnection>() {
                        @Override
                        public void success(AppacitiveConnection result) {
                            assert false;
                        }

                        @Override
                        public void failure(AppacitiveConnection result, Exception e) {
                            AppacitiveException ae = (AppacitiveException) e;
                            assert ae.getCode().equals(ErrorCodes.NOT_FOUND);
                            count.decrementAndGet();
                        }
                    });
                }
            }

            @Override
            public void failure(Void result, Exception e) {
                Assert.fail(e.getMessage());
            }
        });
        await().untilAtomic(count, equalTo(0));
    }
}
