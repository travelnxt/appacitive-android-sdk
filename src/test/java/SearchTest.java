import com.appacitive.sdk.AppacitiveContext;
import com.appacitive.sdk.AppacitiveObject;
import com.appacitive.sdk.infra.SystemDefinedProperties;
import com.appacitive.sdk.model.Callback;
import com.appacitive.sdk.model.ConnectedObject;
import com.appacitive.sdk.model.ConnectedObjectsResponse;
import com.appacitive.sdk.model.Environment;
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
        AppacitiveContext.initialize("up8+oWrzVTVIxl9ZiKtyamVKgBnV5xvmV95u1mEVRrM=", Environment.sandbox);
    }

    @AfterClass
    public static void oneTimeTearDown() {
        // one-time cleanup code
    }

    @Test
    public void queryDslTest()
    {
        AppacitiveContext.initialize("OucpFOfXszNgDj3/Ct6YLXIg0FvY9b/b0UBxVqtd8do=", Environment.sandbox);
        AppacitiveQuery query = new AppacitiveQuery();
        AppacitiveObject.getConnectedObjectsInBackground("user_lists", "user", 51357983522817298L, query, new ArrayList<String>() {{
                    add("list_name");
                    add(SystemDefinedProperties.utcDateCreated);
                }}, new Callback<ConnectedObjectsResponse>() {
                    @Override
                    public void success(ConnectedObjectsResponse result) {

                    }

                    @Override
                    public void failure(ConnectedObjectsResponse result, Exception e) {

                    }
                }
        );
    }
}
