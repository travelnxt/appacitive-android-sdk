package com.appacitive.java;

import com.appacitive.core.*;
import com.appacitive.core.exceptions.AppacitiveException;
import com.appacitive.core.model.BatchCallRequest;
import com.appacitive.core.model.BatchCallResponse;
import com.appacitive.core.model.Callback;
import com.appacitive.core.model.Environment;
import com.appacitive.core.model.containers.ObjectContainer;
import com.jayway.awaitility.Awaitility;
import com.jayway.awaitility.Duration;
import org.junit.*;

import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;

import static com.jayway.awaitility.Awaitility.await;

/**
 * Created by sathley on 10/23/2014.
 */
public class BatchTest {

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
    public void createObjectBatchTest()
    {
        final AtomicBoolean somethingHappened = new AtomicBoolean(false);
        AppacitiveObject  entityA = new AppacitiveObject("entity");
        entityA.setStringProperty("type", "entity a");

        AppacitiveObject  entityB = new AppacitiveObject("entity");
        entityB.setStringProperty("type", "entity b");

        BatchCallRequest request = new BatchCallRequest();
        request.addNode(entityA, null);
        request.addNode(entityB, "entityb");

        AppacitiveBatchCall.Fire(request, new Callback<BatchCallResponse>() {
            @Override
            public void success(BatchCallResponse result) {
                assert result.nodes.size() == 2;
                for (ObjectContainer container : result.nodes)
                {
                    assert container.object != null;
                    assert container.object.getId() > 0;
                }
                assert result.nodes.get(1).name.equals("entityb");
                somethingHappened.set(true);
            }

            @Override
            public void failure(BatchCallResponse result, Exception e) {
                Assert.fail(e.getMessage());
            }
        });

        await().untilTrue(somethingHappened);
    }

    @Test
    public void createConnectionBatchTest()
    {
        final AtomicBoolean somethingHappened= new AtomicBoolean(false);

        final String nameA = "siblingA";
        final String nameB = "siblingB";
        AppacitiveObject siblingA = new AppacitiveObject("object");
        siblingA.setStringProperty("stringfield", "siblingA");

        AppacitiveObject siblingB = new AppacitiveObject("object");
        siblingB.setStringProperty("stringfield", "siblingB");

        AppacitiveConnection connection = new AppacitiveConnection("sibling");
        connection.setStringProperty("field1", "random value");

        final BatchCallRequest request = new BatchCallRequest();
        request.addNode(siblingA, nameA);
        request.addNode(siblingB, nameB);

        request.addEdge(connection, "sibling", "object", nameA, "object", nameB);
        AppacitiveBatchCall.Fire(request, new Callback<BatchCallResponse>() {
            @Override
            public void success(BatchCallResponse result) {
                assert result.nodes.size() == 2;
                assert result.edges.size() == 1;
                assert result.edges.get(0).connection.getId() > 0;
                assert result.edges.get(0).name.equals("sibling");
                somethingHappened.set(true);
            }

            @Override
            public void failure(BatchCallResponse result, Exception e) {
                Assert.fail(e.getMessage());
            }
        });

        await().untilTrue(somethingHappened);
    }

    @Test
    public void updateObjectBatchTest()
    {
        final AtomicBoolean somethingHappened= new AtomicBoolean(false);
        final AppacitiveObject newObject = new AppacitiveObject("object");
        newObject.setIntProperty("intfield", 1111);

        newObject.createInBackground(new Callback<AppacitiveObject>() {
            @Override
            public void success(final AppacitiveObject createdObject) {
                BatchCallRequest request = new BatchCallRequest();
                createdObject.setIntProperty("intfield", 2222);

                request.addNode(createdObject, "obj");
                AppacitiveBatchCall.Fire(request, new Callback<BatchCallResponse>() {
                    @Override
                    public void success(BatchCallResponse result) {
                        assert result.nodes.size() == 1;
                        AppacitiveObject updatedObject = (AppacitiveObject) result.nodes.get(0).object;
                        assert updatedObject.getId() == createdObject.getId();
                        assert updatedObject.getPropertyAsInt("intfield").equals(2222);
                        assert updatedObject.getRevision() == 2;
                        somethingHappened.set(true);
                    }

                    @Override
                    public void failure(BatchCallResponse result, Exception e) {
                        Assert.fail(e.getMessage());

                    }
                });
            }
        });
        await().untilTrue(somethingHappened);
    }

    @Test
    public void updateConnectionBatchTest()
    {
        final AtomicBoolean somethingHappened = new AtomicBoolean(false);
        AppacitiveUser user = new AppacitiveUser();
        user.setEmail(getRandomString().concat("@gmail.com"));
        user.setFirstName(getRandomString());
        user.setPassword(getRandomString());
        user.setUsername(getRandomString());

        AppacitiveDevice device = new AppacitiveDevice();
        device.setDeviceToken(getRandomString());
        device.setDeviceType("ios");

        final AppacitiveConnection newConnection = new AppacitiveConnection("my_device")
                .fromNewUser("user", user)
                .toNewDevice("device", device);
        newConnection.setStringProperty("stringfield", "aaaaa");

        newConnection.createInBackground(new Callback<AppacitiveConnection>() {
            @Override
            public void success(final AppacitiveConnection createdConnection) {
                createdConnection.setStringProperty("stringfield", "bbbbb");
                final BatchCallRequest request = new BatchCallRequest();
                request.addEdge(createdConnection, "conn", "user", null, "device", null);

                AppacitiveBatchCall.Fire(request, new Callback<BatchCallResponse>() {
                    @Override
                    public void success(BatchCallResponse result) {
                        assert result.edges.size() == 1;
                        assert result.edges.get(0).name.equals("conn");

                        AppacitiveConnection updatedConnection = result.edges.get(0).connection;
                        assert updatedConnection.getId() == createdConnection.getId();
                        assert updatedConnection.getRevision() == 2;
                        assert updatedConnection.getPropertyAsString("stringfield").equals("bbbbb");
                        somethingHappened.set(true);

                    }

                    @Override
                    public void failure(BatchCallResponse result, Exception e) {
                        Assert.fail(e.getMessage());
                    }
                });
            }
        });
        await().untilTrue(somethingHappened);
    }

    @Test
    public void updateConnectionWithRevisionBatchTest()
    {
        final AtomicBoolean somethingHappened = new AtomicBoolean(false);
        AppacitiveUser user = new AppacitiveUser();
        user.setEmail(getRandomString().concat("@gmail.com"));
        user.setFirstName(getRandomString());
        user.setPassword(getRandomString());
        user.setUsername(getRandomString());

        AppacitiveDevice device = new AppacitiveDevice();
        device.setDeviceToken(getRandomString());
        device.setDeviceType("ios");

        final AppacitiveConnection newConnection = new AppacitiveConnection("my_device")
                .fromNewUser("user", user)
                .toNewDevice("device", device);
        newConnection.setStringProperty("stringfield", "aaaaa");

        newConnection.createInBackground(new Callback<AppacitiveConnection>() {
            @Override
            public void success(final AppacitiveConnection createdConnection) {
                createdConnection.setStringProperty("stringfield", "bbbbb");
                createdConnection.updateInBackground(false, new Callback<AppacitiveConnection>() {
                    @Override
                    public void success(AppacitiveConnection updatedConnection) {
                        createdConnection.setStringProperty("stringfield", "ccccc");
                        createdConnection.setRevision(1);
                        BatchCallRequest request = new BatchCallRequest();
                        request.addEdgeWithRevision(createdConnection, "conn", "user", null, "device", null);

                        AppacitiveBatchCall.Fire(request, new Callback<BatchCallResponse>() {
                            @Override
                            public void success(BatchCallResponse result) {
                                Assert.fail();
                            }

                            @Override
                            public void failure(BatchCallResponse result, Exception e) {
                                assert e instanceof AppacitiveException;
                                somethingHappened.set(true);
                            }
                        });
                    }
                });
            }
        });
        await().untilTrue(somethingHappened);
    }
}
