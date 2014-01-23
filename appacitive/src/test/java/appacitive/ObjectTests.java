package appacitive;

import appacitive.callbacks.Callback;
import appacitive.exceptions.AppacitiveException;
import appacitive.exceptions.ValidationError;
import appacitive.infra.AppacitiveHttp;
import appacitive.infra.Environment;
import appacitive.infra.Headers;
import appacitive.infra.Urls;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

/**
 * Created by sathley.
 */
public class ObjectTests {

    @BeforeClass
    public static void oneTimeSetUp() {
        AppacitiveContext.initialize("up8+oWrzVTVIxl9ZiKtyamVKgBnV5xvmV95u1mEVRrM=", Environment.sandbox);
    }

    @AfterClass
    public static void oneTimeTearDown() {
        // one-time cleanup code
    }

    @Test
    public void CreateObjectTest() throws ValidationError
    {
        final AppacitiveObject newObject = new AppacitiveObject("object");
        newObject.setProperty("intfield", "100");
        newObject.setProperty("decimalfield", "20.251100");
        newObject.setProperty("boolfield", "true");
        newObject.setProperty("stringfield", "hello world");
        newObject.setProperty("textfield", "Objects represent your data stored inside the Appacitive platform. Every object is mapped to the type that you create via the designer in your management console. If we were to use conventional databases as a metaphor, then a type would correspond to a table and an object would correspond to one row inside that table. object api allows you to store, retrieve and manage all the data that you store inside Appacitive. You can retrieve individual records or lists of records based on a specific filter criteria.");
//        newObject.setProperty("datefield", (new Date()));
//        newObject.setProperty("timefield", Calendar.getInstance().getTime());
//        newObject.setProperty("datetimefield", new Date());
        newObject.setProperty("geofield", "10.11, 20.22");
        newObject.setProperty("multifield", new ArrayList<String>(){{
            add("val1");
            add("500");
            add("false");
        }});

        newObject.createInBackground(new Callback<Void>() {
            public void success(Void result) {
                assert newObject.getId() > 0;
            }

            public void failure(Void result, AppacitiveException e) {
                assert false;
            }
        });

    }

    @Test
    public void MultiGetObjectsTest() throws IOException, ValidationError, AppacitiveException, InterruptedException
    {
        final String url = Urls.ForObject.multiGetObjectUrl("object", new long[]{47597872003285638L,47597872052568716L});
        final Map<String, String> headers = Headers.assemble();
        Map<String, Object> objects = AppacitiveHttp.get(url, headers);
    }
}
