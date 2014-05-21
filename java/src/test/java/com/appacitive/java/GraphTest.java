package com.appacitive.java;

import com.appacitive.core.AppacitiveConnection;
import com.appacitive.core.AppacitiveContextBase;
import com.appacitive.core.AppacitiveGraphSearch;
import com.appacitive.core.AppacitiveObject;
import com.appacitive.core.exceptions.ValidationException;
import com.appacitive.core.model.AppacitiveGraphNode;
import com.appacitive.core.model.Callback;
import com.appacitive.core.model.Environment;
import com.jayway.awaitility.Awaitility;
import org.junit.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import static com.jayway.awaitility.Awaitility.await;

/**
 * Created by sathley.
 */
public class GraphTest {

    @BeforeClass
    public static void oneTimeSetUp() {
        AppacitiveContextBase.initialize(Keys.masterKey, Environment.sandbox, new JavaPlatform());
    }

    @AfterClass
    public static void oneTimeTearDown() {
        // one-time cleanup code
    }

    private String getRandomString() {
        return UUID.randomUUID().toString();
    }

    @Before
    public void beforeTest() {
        Awaitility.reset();
    }

    @Test
    public void filterQueryTest() throws ValidationException {
        Awaitility.setDefaultTimeout(20, TimeUnit.SECONDS);
        final AtomicBoolean somethingHappened = new AtomicBoolean(false);
        final AppacitiveObject parent = new AppacitiveObject("object");
        final String unique = getRandomString();

        AppacitiveObject child = new AppacitiveObject("object");
        child.setStringProperty("stringfield", unique);

        new AppacitiveConnection("link").fromNewObject("parent", parent).toNewObject("child", child).createInBackground(new Callback<AppacitiveConnection>() {
            @Override
            public void success(AppacitiveConnection result) {
                AppacitiveGraphSearch.filterQueryInBackground("sample_filter", new HashMap<String, String>() {{
                            put("search_value", unique);
                        }}, new Callback<List<Long>>() {
                            @Override
                            public void success(List<Long> result) {
                                assert result.size() == 1;
                                assert result.get(0) == parent.getId();
                                somethingHappened.set(true);
                            }

                            @Override
                            public void failure(List<Long> result, Exception e) {
                                Assert.fail(e.getMessage());
                            }
                        }
                );
            }
        });
        await().untilTrue(somethingHappened);
    }

    @Test
    public void projectQueryTest() throws ValidationException {
        Awaitility.setDefaultTimeout(20, TimeUnit.SECONDS);
        final AtomicBoolean somethingHappened = new AtomicBoolean(false);
        final String val1 = getRandomString();
        final String val2 = getRandomString();
        final AppacitiveObject root = new AppacitiveObject("object");
        final AppacitiveObject level1child = new AppacitiveObject("object");
        level1child.setStringProperty("stringfield", val1);

        final AppacitiveConnection level1edge = new AppacitiveConnection("link");
        level1edge.fromNewObject("parent", root).toNewObject("child", level1child);
        level1edge.createInBackground(new Callback<AppacitiveConnection>() {
            @Override
            public void success(AppacitiveConnection result) {
                final AppacitiveObject level2child = new AppacitiveObject("object");
                level2child.setStringProperty("stringfield", val2);
                final AppacitiveConnection level2edge = new AppacitiveConnection("link");
                level2edge.fromExistingObject("parent", level1child.getId()).toNewObject("child", level2child);
                try {
                    level2edge.createInBackground(new Callback<AppacitiveConnection>() {
                        @Override
                        public void success(AppacitiveConnection result) {
                            List<Long> ids = new ArrayList<Long>();
                            ids.add(root.getId());
                            AppacitiveGraphSearch.projectQueryInBackground("sample_project", ids, new HashMap<String, String>() {{
                                        put("level1_filter", val1);
                                        put("level2_filter", val2);
                                    }}, new Callback<List<AppacitiveGraphNode>>() {
                                        @Override
                                        public void success(List<AppacitiveGraphNode> result) {
                                            assert result.size() == 1;
                                            assert result.get(0).object != null;
                                            assert result.get(0).object.getId() == root.getId();
                                            List<AppacitiveGraphNode> level1children = result.get(0).getChildren("level1_children");
                                            assert level1children.size() == 1;
                                            assert level1children.get(0).object != null;
                                            assert level1children.get(0).object.getId() == level1child.getId();
                                            assert level1children.get(0).connection != null;
                                            assert level1children.get(0).connection.getId() == level1edge.getId();
                                            assert level1children.get(0).connection.endpointA.objectId == root.getId();
                                            assert level1children.get(0).connection.endpointB.objectId == level1child.getId();

                                            List<AppacitiveGraphNode> level2children = level1children.get(0).getChildren("level2_children");
                                            assert level2children.size() == 1;
                                            assert level2children.get(0).object != null;
                                            assert level2children.get(0).object.getId() == level2child.getId();
                                            assert level2children.get(0).connection != null;
                                            assert level2children.get(0).connection.getId() == level2edge.getId();
                                            assert level2children.get(0).connection.endpointA.objectId == level1child.getId();
                                            assert level2children.get(0).connection.endpointB.objectId == level2child.getId();
                                            somethingHappened.set(true);
                                        }

                                        @Override
                                        public void failure(List<AppacitiveGraphNode> result, Exception e) {
                                            Assert.fail(e.getMessage());
                                        }
                                    }
                            );
                        }
                    });
                } catch (ValidationException e) {
                    Assert.fail(e.getMessage());
                }
            }
        });
        await().untilTrue(somethingHappened);
    }
}
