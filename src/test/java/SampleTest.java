import com.appacitive.sdk.AppacitiveContext;
import com.appacitive.sdk.model.Environment;
import org.json.JSONObject;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by sathley.
 */
@Ignore
public class SampleTest {
    @BeforeClass
    public static void oneTimeSetUp() {
        AppacitiveContext.initialize("up8+oWrzVTVIxl9ZiKtyamVKgBnV5xvmV95u1mEVRrM=", Environment.sandbox);
    }

    @AfterClass
    public static void oneTimeTearDown() {
        // one-time cleanup code
    }

    @Test
    public void serializationTest()
    {
        Map<String, Object> map = new HashMap<String, Object>(){{
            put("a1", null);
            put("a2", "v2");
        }};
        JSONObject object = new JSONObject(map);
        Object x = null;

        System.out.println(object);
    }

}
