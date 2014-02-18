import com.appacitive.sdk.core.AppacitiveContext;
import com.appacitive.sdk.core.model.Environment;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Created by sathley.
 */
public class LinkTest {

    @BeforeClass
    public static void oneTimeSetUp() {
        AppacitiveContext.initialize("up8+oWrzVTVIxl9ZiKtyamVKgBnV5xvmV95u1mEVRrM=", Environment.sandbox);
    }

    @AfterClass
    public static void oneTimeTearDown() {
        // one-time cleanup code
    }

    //@Test


}
