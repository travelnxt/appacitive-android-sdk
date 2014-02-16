import com.appacitive.sdk.AppacitiveContext;
import com.appacitive.sdk.Environment;
import com.appacitive.sdk.query.BooleanOperator;
import com.appacitive.sdk.query.PropertyFilter;
import com.appacitive.sdk.query.Query;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.ArrayList;

/**
 * Created by sathley.
 */
public class SearchTest {

    @BeforeClass
    public static void oneTimeSetUp() {
        AppacitiveContext.initialize("up8+oWrzVTVIxl9ZiKtyamVKgBnV5xvmV95u1mEVRrM=", Environment.sandbox);
    }

    @AfterClass
    public static void oneTimeTearDown() {
        // one-time cleanup code
    }

    @Test
    public void queryDslTest()
    {
        final PropertyFilter f1 = new PropertyFilter("p1").isEqualTo(false);
        final PropertyFilter f2 = new PropertyFilter("p2").between(100, 200);
        String v1 = BooleanOperator.and(new ArrayList<Query>(){{
            add(f1);
            add(f2);
        }}).asString();
    }
}
