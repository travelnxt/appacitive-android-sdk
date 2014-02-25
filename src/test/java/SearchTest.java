import com.appacitive.sdk.AppacitiveContext;
import com.appacitive.sdk.AppacitiveObject;
import com.appacitive.sdk.infra.JavaPlatform;
import com.appacitive.sdk.infra.SystemDefinedProperties;
import com.appacitive.sdk.model.*;
import com.appacitive.sdk.query.AppacitiveQuery;
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
        AppacitiveContext.initialize("up8+oWrzVTVIxl9ZiKtyamVKgBnV5xvmV95u1mEVRrM=", Environment.sandbox, new JavaPlatform());
    }

    @AfterClass
    public static void oneTimeTearDown() {
        // one-time cleanup code
    }

    @Test
    public void queryDslTest()
    {

    }
}
