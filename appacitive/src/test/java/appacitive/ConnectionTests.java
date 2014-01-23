package appacitive;

import appacitive.infra.Environment;
import org.junit.AfterClass;
import org.junit.BeforeClass;

/**
 * Created by sathley.
 */
public class ConnectionTests {

    @BeforeClass
    public static void oneTimeSetUp() {
        AppacitiveContext.initialize("up8+oWrzVTVIxl9ZiKtyamVKgBnV5xvmV95u1mEVRrM=", Environment.sandbox);
    }

    @AfterClass
    public static void oneTimeTearDown() {
        // one-time cleanup code
    }


}
