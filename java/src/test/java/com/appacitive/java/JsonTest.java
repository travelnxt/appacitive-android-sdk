package com.appacitive.java;

import com.appacitive.core.AppacitiveBatchCall;
import com.appacitive.core.AppacitiveConnection;
import com.appacitive.core.AppacitiveContextBase;
import com.appacitive.core.AppacitiveObject;
import com.appacitive.core.apjson.APJSONException;
import com.appacitive.core.apjson.APJSONObject;
import com.appacitive.core.model.BatchCallRequest;
import com.appacitive.core.model.BatchCallResponse;
import com.appacitive.core.model.Callback;
import com.appacitive.core.model.Environment;
import org.junit.*;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import static com.jayway.awaitility.Awaitility.await;

/**
 * Created by sathley.
 */
public class JsonTest {

    @BeforeClass
    public static void oneTimeSetUp() {
        AppacitiveContextBase.initialize(Keys.masterKey, Environment.sandbox, new JavaPlatform());
    }

    private AtomicBoolean somethingHappened = new AtomicBoolean(false);

    @AfterClass
    public static void oneTimeTearDown() {

        // one-time cleanup code
    }

    @Before
    public void beforeTest() {
        somethingHappened.set(false);
    }

    @After
    public void afterTest() {
    }

    @Test
    public void multiValuedPropertiesTest() {

        AppacitiveObject object = new AppacitiveObject("");
        object.setPropertyAsMultiValued("str_multi", new ArrayList<String>() {{
            add("a");
            add("b");
            add(null);
        }});
        object.setPropertyAsMultiValued("int_multi", new ArrayList<Integer>() {{
            add(1);
            add(2);
            add(null);
        }});
        object.setPropertyAsMultiValued("dec_multi", new ArrayList<Double>() {{
            add(1.1);
            add(2.2);
            add(null);
        }});

        List<String> strs = object.getPropertyAsMultiValuedString("str_multi");
        List<Integer> ints = object.getPropertyAsMultiValuedInt("int_multi");
        List<Double> decs = object.getPropertyAsMultiValuedDouble("dec_multi");

        APJSONObject returnObject = null;
        try {
            APJSONObject apjsonObject = object.getMap();
            String str = apjsonObject.toString();
            returnObject = new APJSONObject(str);
        } catch (Exception e) {


        }
        object.setSelf(returnObject);
        List<String> strs1 = object.getPropertyAsMultiValuedString("str_multi");
        List<Integer> ints1 = object.getPropertyAsMultiValuedInt("int_multi");
        List<Double> decs1 = object.getPropertyAsMultiValuedDouble("dec_multi");


    }

    @Test
    public void multiValuedPropertiesTest2() {

        AppacitiveObject object = new AppacitiveObject("");
        object.setPropertyAsMultiValued("str_multi", new ArrayList<String>() {{
            add("a");
            add("b");
            add(null);
        }});
        object.setPropertyAsMultiValued("int_multi", new ArrayList<String>() {{
            add("1");
            add("2");
            add(null);
        }});
        object.setPropertyAsMultiValued("dec_multi", new ArrayList<String>() {{
            add("1.1");
            add("2.2");
            add(null);
        }});
//
        List<String> strs = object.getPropertyAsMultiValuedString("str_multi");
        List<Integer> ints = object.getPropertyAsMultiValuedInt("int_multi");
        List<Double> decs = object.getPropertyAsMultiValuedDouble("dec_multi");

        APJSONObject returnObject = null;
        try {
            APJSONObject apjsonObject = object.getMap();
            String str = apjsonObject.toString();
            returnObject = new APJSONObject(str);
        } catch (Exception e) {


        }
        object.setSelf(returnObject);
        List<String> strs1 = object.getPropertyAsMultiValuedString("str_multi");
        List<Integer> ints1 = object.getPropertyAsMultiValuedInt("int_multi");
        List<Double> decs1 = object.getPropertyAsMultiValuedDouble("dec_multi");


    }

}
