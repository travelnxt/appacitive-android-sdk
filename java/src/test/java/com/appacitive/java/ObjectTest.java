package com.appacitive.java;

import com.appacitive.core.*;
import com.appacitive.core.exceptions.AppacitiveException;
import com.appacitive.core.exceptions.ValidationException;
import com.appacitive.core.infra.ErrorCodes;
import com.appacitive.core.infra.SystemDefinedProperties;
import com.appacitive.core.model.Callback;
import com.appacitive.core.model.ConnectedObjectsResponse;
import com.appacitive.core.model.Environment;
import com.appacitive.core.model.PagedList;
import com.appacitive.core.query.*;
import com.jayway.awaitility.Awaitility;
import org.junit.*;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import static com.jayway.awaitility.Awaitility.await;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertTrue;

/**
 * Created by sathley.
 */
//@Ignore
public class ObjectTest {

    @BeforeClass
    public static void oneTimeSetUp() {
        AppacitiveContextBase.initialize("up8+oWrzVTVIxl9ZiKtyamVKgBnV5xvmV95u1mEVRrM=", Environment.sandbox, new JavaPlatform());
//        Awaitility.setDefaultTimeout(10, TimeUnit.SECONDS);
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
    public void createFullObjectTest() throws ValidationException, ParseException {
        final AtomicBoolean somethingHappened = new AtomicBoolean(false);

        AppacitiveObject newObject = new AppacitiveObject("object");
        newObject.setIntProperty("intfield", 100);
        newObject.setDoubleProperty("decimalfield", 20.251100);
        newObject.setBoolProperty("boolfield", true);
        newObject.setStringProperty("stringfield", "hello world");
        newObject.setStringProperty("textfield", "Objects represent your data stored inside the Appacitive platform. Every object is mapped to the type that you create via the designer in your management console. If we were to use conventional databases as a metaphor, then a type would correspond to a table and an object would correspond to one row inside that table. object api allows you to store, retrieve and manage all the data that you store inside Appacitive. You can retrieve individual records or lists of records based on a specific filter criteria.");
        final Date date = new Date();
        TimeZone tz = TimeZone.getTimeZone("UTC");
        final DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        final DateFormat tf = new SimpleDateFormat("HH:mm:ss.SSSSSSS");
        final DateFormat dtf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSSS'Z'");
        final String nowAsISODate = df.format(date);
        final String nowAsISOTime = tf.format(date);
        final String nowAsISODateTime = dtf.format(date);
        newObject.setStringProperty("datefield", nowAsISODate);
        newObject.setStringProperty("timefield", nowAsISOTime);
        newObject.setStringProperty("datetimefield", nowAsISODateTime);
        newObject.setStringProperty("geofield", "10.11, 20.22");
        newObject.setPropertyAsMultiValuedString("multifield", new ArrayList<String>() {{
            add("val1");
            add("500");
            add("false");
        }});

        newObject.addTag("t1");
        List<String> tags = new ArrayList<String>();
        tags.add("t2");
        tags.add("t3");

        newObject.addTags(tags);

        newObject.setAttribute("a1", "v1");
        newObject.setAttribute("a2", "v2");
        newObject.createInBackground(new Callback<AppacitiveObject>() {
            @Override
            public void success(AppacitiveObject result) {
                assertTrue(result.getId() > 0);
                assertTrue(result.getPropertyAsInt("intfield") == 100);
                assertTrue(result.getPropertyAsDouble("decimalfield") == 20.2511d);
                assertTrue(result.getPropertyAsBoolean("boolfield"));
                try {
                    assertTrue(df.format(result.getPropertyAsDate("datefield")).equals(nowAsISODate));
                    assertTrue(tf.format(result.getPropertyAsTime("timefield")).equals(nowAsISOTime));
                    assertTrue(dtf.format(result.getPropertyAsDateTime("datetimefield")).equals(nowAsISODateTime));
                } catch (ParseException pe) {
                    Assert.fail(pe.getMessage());
                }
                assertTrue(result.getPropertyAsGeo("geofield")[0] == 10.11d);
                assertTrue(result.getPropertyAsGeo("geofield")[1] == 20.22d);
                assertTrue(result.getPropertyAsMultiValuedString("multifield").size() == 3);
                assertTrue((result.getPropertyAsMultiValuedString("multifield").get(0).equals("val1")));
                assertTrue((result.getPropertyAsMultiValuedString("multifield").get(1).equals("500")));
                assertTrue((result.getPropertyAsMultiValuedString("multifield").get(2).equals("false")));
                somethingHappened.set(true);
            }

            @Override
            public void failure(AppacitiveObject result, Exception e) {
                Assert.fail(e.getMessage());
            }
        });

        await().untilTrue(somethingHappened);
    }


    @Test
    public void multiLingualObjectCreateTest() throws ValidationException {
        final AtomicBoolean somethingHappened = new AtomicBoolean(false);
        AppacitiveObject newObject = new AppacitiveObject("object");
        final String randomString1 = " 以下便是有关此问题的所有信息";
        final String randomString2 = " ä»¥ä¸ä¾¿æ¯æå³æ­¤é®é¢çææä¿¡æ¯";
        newObject.setStringProperty("stringfield", randomString1);
        newObject.setStringProperty("textfield", randomString2);
        newObject.createInBackground(new Callback<AppacitiveObject>() {
            public void success(AppacitiveObject result) {
                assertTrue(result.getId() > 0);
                assertTrue(result.getPropertyAsString("stringfield").toString().equals(randomString1));
                assertTrue(result.getPropertyAsString("textfield").toString().equals(randomString2));

                somethingHappened.set(true);
            }

            public void failure(AppacitiveObject result, AppacitiveException e) {
                Assert.fail(e.getMessage());
            }
        });
        await().untilTrue(somethingHappened);
    }

    private static AppacitiveObject getRandomObject() {
        AppacitiveObject newObject = new AppacitiveObject("object");
        newObject.setIntProperty("intfield", 100);
        newObject.setDoubleProperty("decimalfield", 20.251100);
        newObject.setBoolProperty("boolfield", true);
        newObject.setStringProperty("stringfield", "hello world");
        newObject.setStringProperty("textfield", "Objects represent your data stored inside the Appacitive platform. Every object is mapped to the type that you create via the designer in your management console. If we were to use conventional databases as a metaphor, then a type would correspond to a table and an object would correspond to one row inside that table. object api allows you to store, retrieve and manage all the data that you store inside Appacitive. You can retrieve individual records or lists of records based on a specific filter criteria.");
        final Date date = new Date();
        TimeZone tz = TimeZone.getTimeZone("UTC");
        final DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        final DateFormat tf = new SimpleDateFormat("HH:mm:ss.SSSSSSS");
        final DateFormat dtf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSSS'Z'");
        final String nowAsISODate = df.format(date);
        final String nowAsISOTime = tf.format(date);
        final String nowAsISODateTime = dtf.format(date);
        newObject.setStringProperty("datefield", nowAsISODate);
        newObject.setStringProperty("timefield", nowAsISOTime);
        newObject.setStringProperty("datetimefield", nowAsISODateTime);
        newObject.setStringProperty("geofield", "10.11, 20.22");
        newObject.setPropertyAsMultiValuedString("multifield", new ArrayList<String>() {{
            add("val1");
            add("500");
            add("false");
        }});

        newObject.addTag("t1");
        List<String> tags = new ArrayList<String>();
        tags.add("t2");
        tags.add("t3");

        newObject.addTags(tags);

        newObject.setAttribute("a1", "v1");
        newObject.setAttribute("a2", "v2");
        return newObject;
    }

    @Test
    public void updateObjectTest() throws Exception {
        final AtomicBoolean somethingHappened = new AtomicBoolean(false);
        AppacitiveObject object = getRandomObject();
        getRandomObject().createInBackground(new Callback<AppacitiveObject>() {
            public void success(AppacitiveObject result) {
                result.setIntProperty("intfield", 200);
                result.setDoubleProperty("decimalfield", 40.50200);
                result.setBoolProperty("boolfield", false);
                result.setStringProperty("stringfield", "hello world again !!");
                final Date date = new Date();
                TimeZone tz = TimeZone.getTimeZone("UTC");
                final DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                final DateFormat tf = new SimpleDateFormat("HH:mm:ss.SSSSSSS");
                final DateFormat dtf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSSS'Z'");
                final String nowAsISODate = df.format(date);
                final String nowAsISOTime = tf.format(date);
                final String nowAsISODateTime = dtf.format(date);
                result.setStringProperty("datefield", nowAsISODate);
                result.setStringProperty("timefield", nowAsISOTime);
                result.setStringProperty("datetimefield", nowAsISODateTime);
                result.setStringProperty("geofield", "15.55, 33.88");
                result.setPropertyAsMultiValuedString("multifield", new ArrayList<String>() {{
                    add("val2");
                    add("800");
                    add("true");
                }});
                result.updateInBackground(false, new Callback<AppacitiveObject>() {
                    @Override
                    public void success(AppacitiveObject result) {
                        assertTrue(result.getRevision() == 2);
                        assertTrue(result.getPropertyAsInt("intfield") == 200);
                        assertTrue(result.getPropertyAsDouble("decimalfield") == 40.502d);
                        assertTrue(!result.getPropertyAsBoolean("boolfield"));
                        assertTrue((result.getPropertyAsString("datefield")).equals(nowAsISODate));
                        assertTrue((result.getPropertyAsString("timefield")).equals(nowAsISOTime));
                        assertTrue((result.getPropertyAsString("datetimefield")).equals(nowAsISODateTime));
                        assertTrue(result.getPropertyAsGeo("geofield")[0] == 15.55d);
                        assertTrue(result.getPropertyAsGeo("geofield")[1] == 33.88d);
                        assertTrue(result.getPropertyAsMultiValuedString("multifield").size() == 3);
                        assertTrue((result.getPropertyAsMultiValuedString("multifield").get(0).equals("val2")));
                        assertTrue((result.getPropertyAsMultiValuedString("multifield").get(1).equals("800")));
                        assertTrue((result.getPropertyAsMultiValuedString("multifield").get(2).equals("true")));
                        somethingHappened.set(true);
                    }

                    @Override
                    public void failure(AppacitiveObject result, Exception e) {
                        Assert.fail(e.getMessage());
                    }
                });
            }
        });

        await().untilTrue(somethingHappened);
    }

//    @Test
//    public void updateObjectPropertyAsNull() throws Exception {
//        AppacitiveObject appacitiveObject = getRandomObject();
//        appacitiveObject.createInBackground(new Callback<AppacitiveObject>() {
//            @Override
//            public void success(AppacitiveObject result) {
//                result.setIntProperty("intfield", null);
//                result.setDoubleProperty("decimalfield", null);
//                result.setBoolProperty("boolfield", null);
//                result.setStringProperty("stringfield", null);
//                result.setStringProperty("textfield", null);
//                result.setDateProperty("datefield", null);
//                result.setTimeProperty("timefield", null);
//                result.setDateTimeProperty("datetimefield", null);
//                result.setGeoProperty("geofield", null);
//                result.setPropertyAsMultiValuedString("multifield", null);
//                result.updateInBackground(false, new Callback<AppacitiveObject>() {
//                    @Override
//                    public void success(AppacitiveObject result) {
//                        assert result.getRevision() == 2;
//                    }
//
//                    @Override
//                    public void failure(AppacitiveObject result, Exception e) {
//                        Assert.fail(e.getMessage());
//                    }
//                });
//            }
//        });
//    }

    @Test
    public void updateEmptyObjectTest() throws ValidationException {
        final AtomicBoolean somethingHappened = new AtomicBoolean(false);
        AppacitiveObject appacitiveObject = new AppacitiveObject("object");
        appacitiveObject.createInBackground(new Callback<AppacitiveObject>() {
            @Override
            public void success(AppacitiveObject result) {
                result.setIntProperty("intfield", 200);
                result.setDoubleProperty("decimalfield", 40.50200);
                result.setBoolProperty("boolfield", false);
                result.setStringProperty("stringfield", "hello world again !!");
                final Date date = new Date();
                TimeZone tz = TimeZone.getTimeZone("UTC");
                final DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                final DateFormat tf = new SimpleDateFormat("HH:mm:ss.SSSSSSS");
                final DateFormat dtf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSSS'Z'");
                final String nowAsISODate = df.format(date);
                final String nowAsISOTime = tf.format(date);
                final String nowAsISODateTime = dtf.format(date);
                result.setStringProperty("datefield", nowAsISODate);
                result.setStringProperty("timefield", nowAsISOTime);
                result.setStringProperty("datetimefield", nowAsISODateTime);
                result.setStringProperty("geofield", "15.55, 33.88");
                result.setPropertyAsMultiValuedString("multifield", new ArrayList<String>() {{
                    add("val2");
                    add("800");
                    add("true");
                }});

                result.updateInBackground(false, new Callback<AppacitiveObject>() {
                    @Override
                    public void success(AppacitiveObject result) {
                        assertTrue(result.getRevision() == 2);
                        assertTrue(result.getPropertyAsInt("intfield") == 200);
                        assertTrue(result.getPropertyAsDouble("decimalfield") == 40.502d);
                        assertTrue(!result.getPropertyAsBoolean("boolfield"));


                        assertTrue((result.getPropertyAsString("datefield")).equals(nowAsISODate));
                        assertTrue((result.getPropertyAsString("timefield")).equals(nowAsISOTime));
                        assertTrue((result.getPropertyAsString("datetimefield")).equals(nowAsISODateTime));

                        assertTrue(result.getPropertyAsGeo("geofield")[0] == 15.55d);
                        assertTrue(result.getPropertyAsGeo("geofield")[1] == 33.88d);
                        assertTrue(result.getPropertyAsMultiValuedString("multifield").size() == 3);
                        assertTrue((result.getPropertyAsMultiValuedString("multifield").get(0).equals("val2")));
                        assertTrue((result.getPropertyAsMultiValuedString("multifield").get(1).equals("800")));
                        assertTrue((result.getPropertyAsMultiValuedString("multifield").get(2).equals("true")));
                        somethingHappened.set(true);
                    }

                    @Override
                    public void failure(AppacitiveObject result, Exception e) {
                        Assert.fail(e.getMessage());
                    }
                });
            }
        });

        await().untilTrue(somethingHappened);
    }

    @Test
    public void updateWithValidRevisionTest() throws ValidationException {
        final AtomicBoolean somethingHappened = new AtomicBoolean(false);
        AppacitiveObject appacitiveObject = new AppacitiveObject("object");
        appacitiveObject.createInBackground(new Callback<AppacitiveObject>() {
            @Override
            public void success(AppacitiveObject result) {
                result.addTag("t1");
                result.addTag("t2");
                result.setAttribute("a1", "v1");
                result.setAttribute("a2", "v2");
                result.updateInBackground(true, new Callback<AppacitiveObject>() {
                    @Override
                    public void success(AppacitiveObject result) {
                        assert result.getRevision() == 2;
                        somethingHappened.set(true);
                    }

                    @Override
                    public void failure(AppacitiveObject result, Exception e) {
                        Assert.fail(e.getMessage());
                    }
                });
            }
        });

        await().untilTrue(somethingHappened);
    }

    @Test
    public void updateWithInvalidRevisionTest() throws Exception {
        final AtomicBoolean somethingHappened = new AtomicBoolean(false);
        AppacitiveObject appacitiveObject = new AppacitiveObject("object");

        appacitiveObject.createInBackground(new Callback<AppacitiveObject>() {
            @Override
            public void success(final AppacitiveObject origResult) {
                final long id = origResult.getId();
                try {
                    AppacitiveObject.getInBackground("object", id, null, new Callback<AppacitiveObject>() {
                        @Override
                        public void success(AppacitiveObject returnOrigResult) {
                            returnOrigResult.updateInBackground(true, new Callback<AppacitiveObject>() {
                                @Override
                                public void success(AppacitiveObject result) {
                                    origResult.updateInBackground(true, new Callback<AppacitiveObject>() {
                                        @Override
                                        public void success(AppacitiveObject result) {
                                            assert false;
                                        }

                                        @Override
                                        public void failure(AppacitiveObject result, Exception e) {
                                            AppacitiveException ae = (AppacitiveException) e;
                                            assert ae.code.equals(ErrorCodes.INCORRECT_REVISION);
                                            somethingHappened.set(true);
                                        }
                                    });
                                }
                            });
                        }
                    });
                } catch (ValidationException e) {
                    Assert.fail(e.getMessage());
                }
            }
        });
        await().untilTrue(somethingHappened);

    }

//    @Test
//    public void updateExistingNullValuesToNullTest() throws ValidationException {
//        AppacitiveObject appacitiveObject = new AppacitiveObject("object");
//        appacitiveObject.setIntProperty("intfield", 0);
//        appacitiveObject.setProperty("decimalfield", null);
//        appacitiveObject.setProperty("boolfield", null);
//        appacitiveObject.setProperty("stringfield", null);
//        appacitiveObject.setProperty("textfield", null);
//        appacitiveObject.setProperty("datefield", null);
//        appacitiveObject.setProperty("timefield", null);
//        appacitiveObject.setProperty("datetimefield", null);
//        appacitiveObject.setProperty("geofield", null);
//        appacitiveObject.setProperty("multifield", null);
//        appacitiveObject.createInBackground(new Callback<AppacitiveObject>() {
//            @Override
//            public void success(AppacitiveObject result) {
//                result.setProperty("intfield", null);
//                result.setProperty("decimalfield", null);
//                result.setProperty("boolfield", null);
//                result.setProperty("stringfield", null);
//                result.setProperty("textfield", null);
//                result.setProperty("datefield", null);
//                result.setProperty("timefield", null);
//                result.setProperty("datetimefield", null);
//                result.setProperty("geofield", null);
//                result.setProperty("multifield", null);
//                result.updateInBackground(false, new Callback<AppacitiveObject>() {
//                    @Override
//                    public void success(AppacitiveObject result) {
//                        assert result.getRevision() == 2;
//                    }
//
//                    @Override
//                    public void failure(AppacitiveObject result, Exception e) {
//                        Assert.fail(e.getMessage());
//                    }
//                });
//            }
//        });
//    }

    @Test
    public void updateAttributesTest() throws ValidationException {
        final AtomicBoolean somethingHappened = new AtomicBoolean(false);
        AppacitiveObject appacitiveObject = new AppacitiveObject("object");
        appacitiveObject.setAttribute("a1", "vx");
        appacitiveObject.setAttribute("a2", "v2");
        appacitiveObject.setAttribute("a3", "v3");
        appacitiveObject.setAttribute("a1", "v1");

        appacitiveObject.createInBackground(new Callback<AppacitiveObject>() {
            @Override
            public void success(AppacitiveObject result) {
                assert result.getAllAttributes().containsKey("a1");
                assert result.getAllAttributes().containsKey("a2");
                assert result.getAllAttributes().containsKey("a3");
                assert result.getAttribute("a1").equals("v1");

                result.removeAttribute("a1");
                result.removeAttribute("a2");
                result.setAttribute("a3", "vv3");

                result.updateInBackground(false, new Callback<AppacitiveObject>() {
                    @Override
                    public void success(AppacitiveObject result) {
                        Map<String, String> attributes = result.getAllAttributes();
                        assert attributes.containsKey("a1") == false;
                        assert attributes.containsKey("a2") == false;
                        assert attributes.containsKey("a3");
                        assert attributes.get("a3").equals("vv3");

                        result.removeAttribute("non_existent_attr");
                        result.updateInBackground(false, new Callback<AppacitiveObject>() {
                            @Override
                            public void failure(AppacitiveObject result, Exception e) {
                                assert false;
                            }

                            @Override
                            public void success(AppacitiveObject result) {
                                assert true;
                                somethingHappened.set(true);
                            }
                        });
                    }

                    @Override
                    public void failure(AppacitiveObject result, Exception e) {
                        Assert.fail(e.getMessage());
                    }
                });
            }
        });
        await().untilTrue(somethingHappened);
    }

    @Test
    public void updateTagsTest() throws ValidationException {
        final AtomicBoolean somethingHappened = new AtomicBoolean(false);
        AppacitiveObject appacitiveObject = new AppacitiveObject("object");
        appacitiveObject.addTag("t1");
        appacitiveObject.addTag("t2");
        appacitiveObject.addTag("t3");
        appacitiveObject.createInBackground(new Callback<AppacitiveObject>() {
            @Override
            public void success(AppacitiveObject result) {
                List<String> tags = result.getAllTags();
                assert tags.size() == 3;
                assert tags.contains("t1");
                assert tags.contains("t2");
                assert tags.contains("t3");

                result.addTag("t4");
                result.addTag("t5");

                result.removeTag("t3");
                result.removeTag("t6");
                result.updateInBackground(false, new Callback<AppacitiveObject>() {
                    @Override
                    public void success(AppacitiveObject result) {
                        List<String> tags = result.getAllTags();
                        assert tags.size() == 4;
                        assert result.tagExists("t3") == false;
                        assert result.tagExists("t6") == false;
                        somethingHappened.set(true);
                    }

                    @Override
                    public void failure(AppacitiveObject result, Exception e) {
                        Assert.fail(e.getMessage());
                    }
                });
            }
        });
        await().untilTrue(somethingHappened);
    }

    @Test
    public void multiGetObjectsTest() throws IOException, ValidationException, InterruptedException {
        final AtomicBoolean somethingHappened = new AtomicBoolean(false);
        final AtomicInteger createdObjectsCount = new AtomicInteger(0);
        final ArrayList<Long> ids = new ArrayList<Long>();
        for (int i = 0; i < 3; i++) {
            AppacitiveObject appacitiveObject = new AppacitiveObject("object");
            appacitiveObject.createInBackground(new Callback<AppacitiveObject>() {
                @Override
                public void success(AppacitiveObject result) {
                    ids.add(result.getId());
                    createdObjectsCount.incrementAndGet();
                }
            });
        }
        await().untilAtomic(createdObjectsCount, equalTo(3));

        AppacitiveObject.multiGetInBackground("object", ids, null, new Callback<List<AppacitiveObject>>() {
            @Override
            public void success(List<AppacitiveObject> result) {
                assert result.size() == 3;
                somethingHappened.set(true);
            }

            @Override
            public void failure(List<AppacitiveObject> result, Exception e) {
                Assert.fail(e.getMessage());
            }
        });
        await().untilTrue(somethingHappened);
    }

    @Test
    public void fieldsTest() throws ValidationException {
        final AtomicBoolean somethingHappened = new AtomicBoolean(false);
        AppacitiveObject appacitiveObject = getRandomObject();
        appacitiveObject.setAttribute("aa", "vv");
        appacitiveObject.createInBackground(new Callback<AppacitiveObject>() {
            @Override
            public void success(AppacitiveObject result) {
                List<String> fields = new ArrayList<String>();
                fields.add("intfield");
                fields.add("geofield");
                fields.add(SystemDefinedProperties.attributes);
                fields.add(SystemDefinedProperties.lastModifiedBy);
                fields.add(SystemDefinedProperties.utcDateCreated);
                try {
                    AppacitiveObject.getInBackground("object", result.getId(), fields, new Callback<AppacitiveObject>() {
                        @Override
                        public void success(AppacitiveObject result) {
                            assert result.getPropertyAsString("intfield") != null;
                            assert result.getPropertyAsString("geofield") != null;

                            assert result.getPropertyAsString("stringfield") == null;
                            assert result.getPropertyAsString("textfield") == null;

                            assert result.getAllTags().size() == 0;
                            assert result.getAllAttributes().size() != 0;
                            somethingHappened.set(true);
                        }

                        @Override
                        public void failure(AppacitiveObject result, Exception e) {
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
    public void deleteObjectTest() throws ValidationException {
        final AtomicBoolean somethingHappened = new AtomicBoolean(false);
        AppacitiveObject object = new AppacitiveObject("object");
        object.createInBackground(new Callback<AppacitiveObject>() {
            public void success(AppacitiveObject result) {
                final long id = result.getId();
                result.deleteInBackground(false, new Callback<Void>() {
                    @Override
                    public void success(Void result) {
                        try {
                            AppacitiveObject.getInBackground("object", id, null, new Callback<AppacitiveObject>() {
                                @Override
                                public void success(AppacitiveObject result) {
                                    assert false;
                                }

                                @Override
                                public void failure(AppacitiveObject result, Exception e) {
                                    AppacitiveException ae = (AppacitiveException) e;
                                    assert ae.code.equals(ErrorCodes.NOT_FOUND);
                                    somethingHappened.set(true);
                                }
                            });
                        } catch (ValidationException e) {
                            Assert.fail(e.getMessage());
                        }
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
    public void multiDeleteObjectTest() throws ValidationException, InterruptedException {
        final AtomicInteger count = new AtomicInteger(0);
        final List<Long> ids = new ArrayList<Long>();
        for (int i = 0; i < 3; i++) {
            AppacitiveObject appacitiveObject = new AppacitiveObject("object");
            appacitiveObject.createInBackground(new Callback<AppacitiveObject>() {
                @Override
                public void success(AppacitiveObject result) {
                    ids.add(result.getId());
                    count.incrementAndGet();
                }
            });
        }
        await().untilAtomic(count, equalTo(3));
        AppacitiveObject.bulkDeleteInBackground("object", ids, new Callback<Void>() {
            @Override
            public void success(Void result) {
                for (long id : ids) {
                    try {
                        AppacitiveObject.getInBackground("object", id, null, new Callback<AppacitiveObject>() {
                            @Override
                            public void success(AppacitiveObject result) {
                                assert false;
                            }

                            @Override
                            public void failure(AppacitiveObject result, Exception e) {
                                assert true;
                                count.decrementAndGet();
                            }
                        });
                    } catch (ValidationException e) {
                        Assert.fail(e.getMessage());
                    }
                }
            }

            @Override
            public void failure(Void result, Exception e) {
                Assert.fail(e.getMessage());
            }
        });
        await().untilAtomic(count, equalTo(0));
    }

    @Test
    public void findObjectsTest() throws ValidationException {
        final AtomicBoolean somethingHappened = new AtomicBoolean(false);
        AppacitiveObject appacitiveObject = new AppacitiveObject("object");
        final AppacitiveQuery query = new AppacitiveQuery();
        appacitiveObject.createInBackground(new Callback<AppacitiveObject>() {
            @Override
            public void success(AppacitiveObject result) {
                AppacitiveObject.findInBackground("object", query, null, new Callback<PagedList<AppacitiveObject>>() {
                    @Override
                    public void success(PagedList<AppacitiveObject> result) {
                        assert result.results.size() > 0;
                        somethingHappened.set(true);
                    }

                    @Override
                    public void failure(PagedList<AppacitiveObject> result, Exception e) {
                        Assert.fail(e.getMessage());
                    }
                });
            }
        });
        await().untilTrue(somethingHappened);
    }

    @Test
    public void findObjectsTestWithPagination() throws ValidationException {
        final AtomicBoolean somethingHappened = new AtomicBoolean(false);
        AppacitiveObject appacitiveObject = new AppacitiveObject("object");
        final AppacitiveQuery query = new AppacitiveQuery();
        query.pageNumber = 2;
        query.pageSize = 15;
        appacitiveObject.createInBackground(new Callback<AppacitiveObject>() {
            @Override
            public void success(AppacitiveObject result) {
                AppacitiveObject.findInBackground("object", query, null, new Callback<PagedList<AppacitiveObject>>() {
                    @Override
                    public void success(PagedList<AppacitiveObject> result) {
                        assert result.results.size() == 15;
                        assert result.pagingInfo.pageSize == 15;
                        assert result.pagingInfo.pageNumber == 2;
                        somethingHappened.set(true);
                    }

                    @Override
                    public void failure(PagedList<AppacitiveObject> result, Exception e) {
                        Assert.fail(e.getMessage());
                    }
                });
            }
        });
        await().untilTrue(somethingHappened);
    }

    @Test
    public void findObjectsWithPropertyFilterTest() throws ValidationException {
        final AtomicBoolean somethingHappened = new AtomicBoolean(false);
        AppacitiveObject appacitiveObject = new AppacitiveObject("object");
        appacitiveObject.setStringProperty("stringfield", "hello world123");
        appacitiveObject.setIntProperty("intfield", 2005);
        appacitiveObject.setBoolProperty("boolfield", false);
        appacitiveObject.setStringProperty("geofield", "11.11, 22.22");
        final Date date = new Date();
        TimeZone tz = TimeZone.getTimeZone("UTC");
        final DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        final DateFormat tf = new SimpleDateFormat("HH:mm:ss.SSSSSSS");
        final DateFormat dtf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSSS'Z'");
        final String nowAsISODate = df.format(date);
        final String nowAsISOTime = tf.format(date);
        final String nowAsISODateTime = dtf.format(date);
        appacitiveObject.setStringProperty("datefield", nowAsISODate);
        appacitiveObject.setStringProperty("timefield", nowAsISOTime);
        appacitiveObject.setStringProperty("datetimefield", nowAsISODateTime);

        final AppacitiveQuery query = new AppacitiveQuery();
        final Query q1 = new PropertyFilter("stringfield").isEqualTo("hello world123");
        final Query q2 = new PropertyFilter("intfield").isEqualTo(2005);
        final Query q3 = new PropertyFilter("boolfield").isEqualTo(false);
        final Query q4 = new PropertyFilter("datefield").isEqualTo(nowAsISODate);
        final Query q5 = new PropertyFilter("timefield").isEqualTo(nowAsISOTime);
        final Query q6 = new PropertyFilter("datetimefield").isEqualTo(nowAsISODateTime);
        double[] geo = new double[2];
        geo[0] = 11.11d;
        geo[1] = 22.23d;
        final Query q7 = new GeoFilter("geofield").withinCircle(geo, 10, DistanceMetric.mi);

        query.query = BooleanOperator.and(new ArrayList<Query>() {{
            add(q1);
            add(q2);
            add(q3);
            add(q4);
            add(q5);
            add(q6);
            add(q7);
        }});
        appacitiveObject.createInBackground(new Callback<AppacitiveObject>() {
            @Override
            public void success(AppacitiveObject result) {
                AppacitiveObject.findInBackground("object", query, null, new Callback<PagedList<AppacitiveObject>>() {
                    @Override
                    public void success(PagedList<AppacitiveObject> result) {
                        assert result.results.size() > 0;
                        somethingHappened.set(true);
                    }

                    @Override
                    public void failure(PagedList<AppacitiveObject> result, Exception e) {
                        Assert.fail(e.getMessage());
                    }
                });
            }
        });
        await().untilTrue(somethingHappened);
    }

    @Test
    public void findObjectsWithAttributeFilterTest() throws ValidationException {
        final AtomicBoolean somethingHappened = new AtomicBoolean(false);
        AppacitiveObject appacitiveObject = new AppacitiveObject("object");
        appacitiveObject.setAttribute("a1", "v1");
        appacitiveObject.setAttribute("a2", "v2");
        appacitiveObject.setAttribute("a3", "v3");
        appacitiveObject.setAttribute("a4", "appacitive");
        final AppacitiveQuery query = new AppacitiveQuery();
        final AttributeFilter a1 = new AttributeFilter("a1").isEqualTo("v1");
        final AttributeFilter a2 = new AttributeFilter("a2").endsWith("2");
        final AttributeFilter a3 = new AttributeFilter("a3").startsWith("v");
        final AttributeFilter a4 = new AttributeFilter("a4").like("*acit*");
        query.query = BooleanOperator.and(new ArrayList<Query>() {{
            add(a1);
            add(a2);
            add(a3);
            add(a4);
        }});
        appacitiveObject.createInBackground(new Callback<AppacitiveObject>() {
            @Override
            public void success(AppacitiveObject result) {
                AppacitiveObject.findInBackground("object", query, null, new Callback<PagedList<AppacitiveObject>>() {
                    @Override
                    public void success(PagedList<AppacitiveObject> result) {
                        assert result.results.size() > 0;
                        somethingHappened.set(true);
                    }

                    @Override
                    public void failure(PagedList<AppacitiveObject> result, Exception e) {
                        Assert.fail(e.getMessage());
                    }
                });
            }
        });
        await().untilTrue(somethingHappened);
    }

    @Test
    public void findObjectsWithTagsFilterTest() throws ValidationException {
        final AtomicBoolean somethingHappened = new AtomicBoolean(false);
        AppacitiveObject appacitiveObject = new AppacitiveObject("object");
        List<String> tags = new ArrayList<String>() {{
            add("tag1");
            add("tag2");
            add("tag3");
            add("tag4");
            add("tag5");
        }};
        appacitiveObject.addTags(tags);
        final AppacitiveQuery query = new AppacitiveQuery();
        final TagFilter t1 = new TagFilter().matchAll(new ArrayList<String>() {{
            add("tag1");
            add("tag2");
        }});

        final TagFilter t2 = new TagFilter().matchOneOrMore(new ArrayList<String>() {{
            add("tag4");
            add("tag6");
            add("tag7");
        }});
        query.query = BooleanOperator.and(new ArrayList<Query>() {{
            add(t1);
            add(t2);
        }});

        appacitiveObject.createInBackground(new Callback<AppacitiveObject>() {
            @Override
            public void success(AppacitiveObject result) {
                final AtomicBoolean somethingHappened = new AtomicBoolean(false);
                AppacitiveObject.findInBackground("object", query, null, new Callback<PagedList<AppacitiveObject>>() {
                    @Override
                    public void success(PagedList<AppacitiveObject> result) {
                        assert result.results.size() > 0;
                        for (AppacitiveObject obj : result.results) {
                            assert obj.tagExists("tag1") == true;
                            assert obj.tagExists("tag2") == true;
                            assert obj.tagExists("tag4") == true || obj.tagExists("tag6") == true || obj.tagExists("tag7") == true;
                        }
                        somethingHappened.set(true);
                    }

                    @Override
                    public void failure(PagedList<AppacitiveObject> result, Exception e) {
                        Assert.fail(e.getMessage());
                    }
                });
            }
        });
        await().untilTrue(somethingHappened);
    }

    @Test
    public void getConnectedObjectsTest() throws ValidationException {
        final AtomicBoolean somethingHappened = new AtomicBoolean(false);
        AppacitiveUser user = new AppacitiveUser();
        user.setFirstName(getRandomString());
        user.setUsername(getRandomString());
        user.setPassword(getRandomString());
        user.setEmail(getRandomString().concat("@gmail.com"));

        AppacitiveDevice device = new AppacitiveDevice();
        device.setDeviceType("ios");
        device.setDeviceToken(getRandomString());

        new AppacitiveConnection("my_device").fromNewDevice("device", device).toNewUser("user", user).createInBackground(new Callback<AppacitiveConnection>() {
            @Override
            public void success(AppacitiveConnection result) {
                AppacitiveObject.getConnectedObjectsInBackground("my_device", "user", result.endpointB.objectId, null, null, new Callback<ConnectedObjectsResponse>() {
                    @Override
                    public void success(ConnectedObjectsResponse result) {
                        assert result.results.size() > 0;
                        assert result.results.get(0).object != null;
                        assert result.results.get(0).connection != null;
                        assert result.results.get(0).object.getType().equals("device");
                        somethingHappened.set(true);
                    }

                    @Override
                    public void failure(ConnectedObjectsResponse result, Exception e) {
                        Assert.fail(e.getMessage());
                    }
                });
            }
        });
        await().untilTrue(somethingHappened);

    }

}
