import appacitive.AppacitiveContext;
import appacitive.AppacitiveObject;
import appacitive.callbacks.Callback;
import appacitive.exceptions.AppacitiveException;
import appacitive.exceptions.ValidationError;
import appacitive.utilities.Environment;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

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
    public void CreateObjectTest()
    {
        AppacitiveObject newObject = new AppacitiveObject("object");
        newObject.setProperty("intfield", 100);
    }

    @Test
    public void GetObjectAsyncTest() throws IOException, ValidationError, AppacitiveException, InterruptedException
    {
        
    }
}
