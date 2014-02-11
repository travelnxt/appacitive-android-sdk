import com.appacitive.sdk.AppacitiveContext;
import com.appacitive.sdk.AppacitiveObject;
import com.appacitive.sdk.PagedList;
import com.appacitive.sdk.callbacks.Callback;
import com.appacitive.sdk.exceptions.AppacitiveException;
import com.appacitive.sdk.exceptions.ValidationException;
import com.appacitive.sdk.infra.*;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import static org.junit.Assert.*;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by sathley.
 */
@Ignore
public class ObjectTest {

    @BeforeClass
    public static void oneTimeSetUp() {
        AppacitiveContext.initialize("up8+oWrzVTVIxl9ZiKtyamVKgBnV5xvmV95u1mEVRrM=", Environment.sandbox);
    }

    @AfterClass
    public static void oneTimeTearDown() {
        // one-time cleanup code
    }

    @Test
    public void createFullObjectTest() throws ValidationException, ParseException {

        AppacitiveObject newObject = new AppacitiveObject("object");
        newObject.setProperty("intfield", 100);
        newObject.setProperty("decimalfield", 20.251100);
        newObject.setProperty("boolfield", true);
        newObject.setProperty("stringfield", "hello world");
        newObject.setProperty("textfield", "Objects represent your data stored inside the Appacitive platform. Every object is mapped to the type that you create via the designer in your management console. If we were to use conventional databases as a metaphor, then a type would correspond to a table and an object would correspond to one row inside that table. object api allows you to store, retrieve and manage all the data that you store inside Appacitive. You can retrieve individual records or lists of records based on a specific filter criteria.");
        final Date date = new Date();
        TimeZone tz = TimeZone.getTimeZone("UTC");
        final DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        final DateFormat tf = new SimpleDateFormat("HH:mm:ss.SSSSSSS");
        final DateFormat dtf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSSS'Z'");
        final String nowAsISODate = df.format(date);
        final String nowAsISOTime = tf.format(date);
        final String nowAsISODateTime = dtf.format(date);
        newObject.setProperty("datefield", nowAsISODate);
        newObject.setProperty("timefield", nowAsISOTime);
        newObject.setProperty("datetimefield", nowAsISODateTime);
        newObject.setProperty("geofield", "10.11, 20.22");
        newObject.setProperty("multifield", new ArrayList<Object>() {{
            add("val1");
            add(500);
            add(false);
        }});

        newObject.addTag("t1");
        List<String> tags = new ArrayList<String>();
        tags.add("t2");
        tags.add("t3");

        newObject.addTags(tags);

        newObject.setAttribute("a1", "v1");
        newObject.setAttribute("a2", "v2");
        newObject.createInBackground(new Callback<AppacitiveObject>() {

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
                    assert false;
                }
                assertTrue(result.getPropertyAsGeo("geofield")[0] == 10.11d);
                assertTrue(result.getPropertyAsGeo("geofield")[1] == 20.22d);
                assertTrue(result.getPropertyAsMultiValued("multifield").size() == 3);
                assertTrue((result.getPropertyAsMultiValued("multifield").get(0).equals("val1")));
                assertTrue((result.getPropertyAsMultiValued("multifield").get(1).equals("500")));
                assertTrue((result.getPropertyAsMultiValued("multifield").get(2).equals("False")));
            }


            public void failure(AppacitiveObject result, AppacitiveException e) {
                assert false;
            }
        });

    }

    @Test
    public void multiLingualObjectCreateTest() throws ValidationException {
        AppacitiveObject newObject = new AppacitiveObject("object");
        final String randomString1 = " 以下便是有关此问题的所有信息";
        final String randomString2 = " ä»¥ä¸ä¾¿æ¯æå³æ­¤é®é¢çææä¿¡æ¯";
        newObject.setProperty("stringfield", randomString1);
        newObject.setProperty("textfield", randomString2);
        newObject.createInBackground(new Callback<AppacitiveObject>() {
            public void success(AppacitiveObject result) {
                assertTrue(result.getId() > 0);
                assertTrue(result.getProperty("stringfield").toString().equals(randomString1));
                assertTrue(result.getProperty("textfield").toString().equals(randomString2));
            }

            public void failure(AppacitiveObject result, AppacitiveException e) {
                assert false;
            }
        });
    }

    private static AppacitiveObject getRandomObject() {
        AppacitiveObject newObject = new AppacitiveObject("object");
        newObject.setProperty("intfield", 100);
        newObject.setProperty("decimalfield", 20.251100);
        newObject.setProperty("boolfield", true);
        newObject.setProperty("stringfield", "hello world");
        newObject.setProperty("textfield", "Objects represent your data stored inside the Appacitive platform. Every object is mapped to the type that you create via the designer in your management console. If we were to use conventional databases as a metaphor, then a type would correspond to a table and an object would correspond to one row inside that table. object api allows you to store, retrieve and manage all the data that you store inside Appacitive. You can retrieve individual records or lists of records based on a specific filter criteria.");
        final Date date = new Date();
        TimeZone tz = TimeZone.getTimeZone("UTC");
        final DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        final DateFormat tf = new SimpleDateFormat("HH:mm:ss.SSSSSSS");
        final DateFormat dtf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSSS'Z'");
        final String nowAsISODate = df.format(date);
        final String nowAsISOTime = tf.format(date);
        final String nowAsISODateTime = dtf.format(date);
        newObject.setProperty("datefield", nowAsISODate);
        newObject.setProperty("timefield", nowAsISOTime);
        newObject.setProperty("datetimefield", nowAsISODateTime);
        newObject.setProperty("geofield", "10.11, 20.22");
        newObject.setProperty("multifield", new ArrayList<Object>() {{
            add("val1");
            add(500);
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
        AppacitiveObject object = getRandomObject();
        getRandomObject().createInBackground(new Callback<AppacitiveObject>() {
            public void success(AppacitiveObject result) {
                result.setProperty("intfield", 200);
                result.setProperty("decimalfield", 40.50200);
                result.setProperty("boolfield", false);
                result.setProperty("stringfield", "hello world again !!");
                final Date date = new Date();
                TimeZone tz = TimeZone.getTimeZone("UTC");
                final DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                final DateFormat tf = new SimpleDateFormat("HH:mm:ss.SSSSSSS");
                final DateFormat dtf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSSS'Z'");
                final String nowAsISODate = df.format(date);
                final String nowAsISOTime = tf.format(date);
                final String nowAsISODateTime = dtf.format(date);
                result.setProperty("datefield", nowAsISODate);
                result.setProperty("timefield", nowAsISOTime);
                result.setProperty("datetimefield", nowAsISODateTime);
                result.setProperty("geofield", "15.55, 33.88");
                result.setProperty("multifield", new ArrayList<Object>() {{
                    add("val2");
                    add(800);
                    add(true);
                }});
                result.updateInBackground(false, new Callback<AppacitiveObject>() {
                    @Override
                    public void success(AppacitiveObject result) {
                        assertTrue(result.getRevision() == 2);
                        assertTrue(result.getPropertyAsInt("intfield") == 200);
                        assertTrue(result.getPropertyAsDouble("decimalfield") == 40.502d);
                        assertTrue(!result.getPropertyAsBoolean("boolfield"));
                        try {
                            assertTrue(df.format(result.getPropertyAsDate("datefield")).equals(nowAsISODate));
                            assertTrue(tf.format(result.getPropertyAsTime("timefield")).equals(nowAsISOTime));
                            assertTrue(dtf.format(result.getPropertyAsDateTime("datetimefield")).equals(nowAsISODateTime));
                        } catch (ParseException pe) {
                            assert false;
                        }
                        assertTrue(result.getPropertyAsGeo("geofield")[0] == 15.55d);
                        assertTrue(result.getPropertyAsGeo("geofield")[1] == 33.88d);
                        assertTrue(result.getPropertyAsMultiValued("multifield").size() == 3);
                        assertTrue((result.getPropertyAsMultiValued("multifield").get(0).equals("val2")));
                        assertTrue((result.getPropertyAsMultiValued("multifield").get(1).equals("800")));
                        assertTrue((result.getPropertyAsMultiValued("multifield").get(2).equals("True")));
                    }

                    @Override
                    public void failure(AppacitiveObject result, AppacitiveException e) {
                        assert false;
                    }
                });
            }
        });
        getRandomObject().createInBackground(new Callback<AppacitiveObject>() {
            public void success(AppacitiveObject result) {

            }

            public void failure(AppacitiveObject result, AppacitiveException e) {

            }
        });

    }

    @Test
    public void updateObjectPropertyAsNull() throws Exception {
        AppacitiveObject appacitiveObject = getRandomObject();
        appacitiveObject.createInBackground(new Callback<AppacitiveObject>() {
            @Override
            public void success(AppacitiveObject result) {
                result.setProperty("intfield", null);
                result.setProperty("decimalfield", null);
                result.setProperty("boolfield", null);
                result.setProperty("stringfield", null);
                result.setProperty("textfield", null);
                result.setProperty("datefield", null);
                result.setProperty("timefield", null);
                result.setProperty("datetimefield", null);
                result.setProperty("geofield", null);
                result.setProperty("multifield", null);
                result.updateInBackground(false, new Callback<AppacitiveObject>() {
                    @Override
                    public void success(AppacitiveObject result) {
                        assert result.getRevision() == 2;
                    }

                    @Override
                    public void failure(AppacitiveObject result, AppacitiveException e) {
                        assert false;
                    }
                });
            }
        });
    }

    @Test
    public void updateEmptyObjectTest() throws ValidationException {
        AppacitiveObject appacitiveObject = new AppacitiveObject("object");
        appacitiveObject.createInBackground(new Callback<AppacitiveObject>() {
            @Override
            public void success(AppacitiveObject result) {
                result.setProperty("intfield", 200);
                result.setProperty("decimalfield", 40.50200);
                result.setProperty("boolfield", false);
                result.setProperty("stringfield", "hello world again !!");
                final Date date = new Date();
                TimeZone tz = TimeZone.getTimeZone("UTC");
                final DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                final DateFormat tf = new SimpleDateFormat("HH:mm:ss.SSSSSSS");
                final DateFormat dtf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSSS'Z'");
                final String nowAsISODate = df.format(date);
                final String nowAsISOTime = tf.format(date);
                final String nowAsISODateTime = dtf.format(date);
                result.setProperty("datefield", nowAsISODate);
                result.setProperty("timefield", nowAsISOTime);
                result.setProperty("datetimefield", nowAsISODateTime);
                result.setProperty("geofield", "15.55, 33.88");
                result.setProperty("multifield", new ArrayList<Object>() {{
                    add("val2");
                    add(800);
                    add(true);
                }});

                result.updateInBackground(false, new Callback<AppacitiveObject>() {
                    @Override
                    public void success(AppacitiveObject result) {
                        assertTrue(result.getRevision() == 2);
                        assertTrue(result.getPropertyAsInt("intfield") == 200);
                        assertTrue(result.getPropertyAsDouble("decimalfield") == 40.502d);
                        assertTrue(!result.getPropertyAsBoolean("boolfield"));
                        try {
                            assertTrue(df.format(result.getPropertyAsDate("datefield")).equals(nowAsISODate));
                            assertTrue(tf.format(result.getPropertyAsTime("timefield")).equals(nowAsISOTime));
                            assertTrue(dtf.format(result.getPropertyAsDateTime("datetimefield")).equals(nowAsISODateTime));
                        } catch (ParseException pe) {
                            assert false;
                        }
                        assertTrue(result.getPropertyAsGeo("geofield")[0] == 15.55d);
                        assertTrue(result.getPropertyAsGeo("geofield")[1] == 33.88d);
                        assertTrue(result.getPropertyAsMultiValued("multifield").size() == 3);
                        assertTrue((result.getPropertyAsMultiValued("multifield").get(0).equals("val2")));
                        assertTrue((result.getPropertyAsMultiValued("multifield").get(1).equals("800")));
                        assertTrue((result.getPropertyAsMultiValued("multifield").get(2).equals("True")));
                    }

                    @Override
                    public void failure(AppacitiveObject result, AppacitiveException e) {
                        assert false;
                    }
                });
            }
        });
    }

    @Test
    public void updateWithValidRevisionTest() throws ValidationException {
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
                    }

                    @Override
                    public void failure(AppacitiveObject result, AppacitiveException e) {
                        assert false;
                    }
                });
            }
        });
    }

    @Test
    public void updateWithInvalidRevisionTest() throws Exception {
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
                                        public void failure(AppacitiveObject result, AppacitiveException e) {
                                            assert e.code.equals(ErrorCodes.INCORRECT_REVISION);
                                        }
                                    });
                                }
                            });
                        }
                    });
                } catch (ValidationException e) {
                    assert false;
                }
            }
        });


    }

    @Test
    public void updateExistingNullValuesToNullTest() throws ValidationException {
        AppacitiveObject appacitiveObject = new AppacitiveObject("object");
        appacitiveObject.setProperty("intfield", null);
        appacitiveObject.setProperty("decimalfield", null);
        appacitiveObject.setProperty("boolfield", null);
        appacitiveObject.setProperty("stringfield", null);
        appacitiveObject.setProperty("textfield", null);
        appacitiveObject.setProperty("datefield", null);
        appacitiveObject.setProperty("timefield", null);
        appacitiveObject.setProperty("datetimefield", null);
        appacitiveObject.setProperty("geofield", null);
        appacitiveObject.setProperty("multifield", null);
        appacitiveObject.createInBackground(new Callback<AppacitiveObject>() {
            @Override
            public void success(AppacitiveObject result) {
                result.setProperty("intfield", null);
                result.setProperty("decimalfield", null);
                result.setProperty("boolfield", null);
                result.setProperty("stringfield", null);
                result.setProperty("textfield", null);
                result.setProperty("datefield", null);
                result.setProperty("timefield", null);
                result.setProperty("datetimefield", null);
                result.setProperty("geofield", null);
                result.setProperty("multifield", null);
                result.updateInBackground(false, new Callback<AppacitiveObject>() {
                    @Override
                    public void success(AppacitiveObject result) {
                        assert result.getRevision() == 2;
                    }

                    @Override
                    public void failure(AppacitiveObject result, AppacitiveException e) {
                        assert false;
                    }
                });
            }
        });
    }

    @Test
    public void updateAttributesTest() throws ValidationException {
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

                result.setAttribute("a1", null);
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
                            public void failure(AppacitiveObject result, AppacitiveException e) {
                                assert true;
                            }

                            @Override
                            public void success(AppacitiveObject result) {
                                assert false;
                            }
                        });
                    }

                    @Override
                    public void failure(AppacitiveObject result, AppacitiveException e) {
                        assert false;
                    }
                });
            }
        });
    }

    @Test
    public void updateTagsTest() throws ValidationException {
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
                    }

                    @Override
                    public void failure(AppacitiveObject result, AppacitiveException e) {
                        assert false;
                    }
                });
            }
        });
    }

    @Test
    public void multiGetObjectsTest() throws IOException, ValidationException, InterruptedException {
        final ArrayList<Long> ids = new ArrayList<Long>();
        for (int i = 0; i < 3; i++) {
            AppacitiveObject appacitiveObject = new AppacitiveObject("object");
            appacitiveObject.createInBackground(new Callback<AppacitiveObject>() {
                @Override
                public void success(AppacitiveObject result) {
                    ids.add(result.getId());
                }
            });
        }
        Thread.sleep(5000);
        AppacitiveObject.multiGetInBackground("object", ids, null, new Callback<List<AppacitiveObject>>() {
            @Override
            public void success(List<AppacitiveObject> result) {
                assert result.size() == 3;
            }

            @Override
            public void failure(List<AppacitiveObject> result, AppacitiveException e) {
                assert false;
            }
        });
    }

    @Test
    public void fieldsTest() throws ValidationException {
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
                            assert result.getProperty("intfield") != null;
                            assert result.getProperty("geofield") != null;

                            assert result.getProperty("stringfield") == null;
                            assert result.getProperty("textfield") == null;

                            assert result.getAllTags().size() == 0;
                            assert result.getAllAttributes().size() != 0;
                        }

                        @Override
                        public void failure(AppacitiveObject result, AppacitiveException e) {
                            assert false;
                        }
                    });
                } catch (ValidationException e) {
                    assert false;
                }
            }
        });
    }

    @Test
    public void deleteObjectTest() throws ValidationException {
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
                                public void failure(AppacitiveObject result, AppacitiveException e) {
                                    assert e.code.equals(ErrorCodes.NOT_FOUND);
                                }
                            });
                        } catch (ValidationException e) {
                            assert false;
                        }
                    }

                    @Override
                    public void failure(Void result, AppacitiveException e) {
                        assert false;
                    }
                });
            }
        });
    }

    @Test
    public void multiDeleteObjectTest() throws ValidationException, InterruptedException {
        final List<Long> ids = new ArrayList<Long>();
        for (int i = 0; i < 3; i++) {
            AppacitiveObject appacitiveObject = new AppacitiveObject("object");
            appacitiveObject.createInBackground(new Callback<AppacitiveObject>() {
                @Override
                public void success(AppacitiveObject result) {
                    ids.add(result.getId());
                }
            });
        }
        Thread.sleep(10000);
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
                            public void failure(AppacitiveObject result, AppacitiveException e) {
                                assert true;
                            }
                        });
                    } catch (ValidationException e) {
                        assert false;
                    }
                }
            }

            @Override
            public void failure(Void result, AppacitiveException e) {
                assert false;
            }
        });

    }

    @Test
    public void findObjectsTest() throws ValidationException {
        AppacitiveObject appacitiveObject = new AppacitiveObject("object");
        appacitiveObject.createInBackground(new Callback<AppacitiveObject>() {
            @Override
            public void success(AppacitiveObject result) {
                AppacitiveObject.findInBackground("object", null, null, new Callback<PagedList<AppacitiveObject>>() {
                    @Override
                    public void success(PagedList<AppacitiveObject> result) {
                        assert result.results.size() > 0;
                    }

                    @Override
                    public void failure(PagedList<AppacitiveObject> result, AppacitiveException e) {
                        assert false;
                    }
                });
            }
        });
    }
}
