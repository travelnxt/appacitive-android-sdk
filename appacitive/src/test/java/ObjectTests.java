import appacitive.AppacitiveContext;
import appacitive.AppacitiveObject;
import appacitive.callbacks.GetCallBack;
import appacitive.exceptions.AppacitiveException;
import appacitive.exceptions.ValidationError;
import appacitive.utilities.Environment;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.Console;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by sathley.
 */
public class ObjectTests {

    @BeforeClass
    public static void oneTimeSetUp() {
        AppacitiveContext.initialize("up8+oWrzVTVIxl9ZiKtyamVKgBnV5xvmV95u1mEVRrM=", Environment.live);
    }

    @AfterClass
    public static void oneTimeTearDown() {
        // one-time cleanup code
    }
    @Test
    public void GetObjectTest() throws IOException, ValidationError, AppacitiveException
    {
        AppacitiveObject obj = AppacitiveObject.Get("object", 46071177921298749L, new ArrayList<String>());
        PrintWriter out = new PrintWriter(new FileWriter("C:\\sourcecode\\outputfile.txt"));
        out.print(obj.getId());
        out.close();
    }

    @Test
    public void GetObjectAsyncTest() throws IOException, ValidationError, AppacitiveException, InterruptedException
    {
        AppacitiveObject.GetAsync("object", 46071177921298749L,new ArrayList<String>(), new GetCallBack<AppacitiveObject>() {
            @Override
            public void Done(AppacitiveObject entity, AppacitiveException e) {
                if(entity != null)
                {
                    try
                    {
                        PrintWriter out = new PrintWriter(new FileWriter("C:\\sourcecode\\outputfile.txt"));
                        out.print(entity.getId());
                        out.close();
                    }
                    catch (Exception e1){}

                }
                else assert false;
            }
        });
        Thread.sleep(10000);
    }
}
