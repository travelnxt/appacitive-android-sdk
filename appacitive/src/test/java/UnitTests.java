import appacitive.utilities.AppacitiveHttp;
import org.junit.Test;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by sathley.
 */
public class UnitTests {

    @Test
    public void TestHttpGet() throws IOException
    {
        AppacitiveHttp client = new AppacitiveHttp();
        String url = "https://apis.appacitive.com/v1.0/user/123";
        Map<String, String> headers = new HashMap<String, String>();
        headers.put("Appacitive-Environment", "sandbox");
        headers.put("Appacitive-Apikey","s+JcoR+bFml/rJJKZPHJAllLRcazXmjQF30DLxBvxVQ=");
        Map<String, Object> result = client.Get(url, headers);

    }
}
