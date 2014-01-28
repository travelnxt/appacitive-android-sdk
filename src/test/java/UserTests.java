import com.appacitive.sdk.AppacitiveContext;
import com.appacitive.sdk.AppacitiveUser;
import com.appacitive.sdk.callbacks.Callback;
import com.appacitive.sdk.exceptions.AppacitiveException;
import com.appacitive.sdk.exceptions.ValidationError;
import com.appacitive.sdk.infra.Environment;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.UUID;

/**
 * Created by sathley.
 */
public class UserTests {

    @BeforeClass
    public static void oneTimeSetUp() {
        AppacitiveContext.initialize("up8+oWrzVTVIxl9ZiKtyamVKgBnV5xvmV95u1mEVRrM=", Environment.sandbox);
    }

    @AfterClass
    public static void oneTimeTearDown() {
        // one-time cleanup code
    }

    private String getRandomString()
    {
        return UUID.randomUUID().toString();
    }
    @Test
    public void signupUserTest() throws ValidationError
    {
        AppacitiveUser user = new AppacitiveUser();
        user.setFirstName(getRandomString());
        user.setUsername(getRandomString());
        user.setEmail(getRandomString().concat("@gmail.com"));
        user.setPassword(getRandomString());
        user.signupInBackground(new Callback<AppacitiveUser>() {
            @Override
            public void success(AppacitiveUser result) {
                assert result.getId() > 0;
            }

            @Override
            public void failure(AppacitiveUser result, AppacitiveException e) {
                assert false;
            }
        });
    }

    @Test
    public void signupUserWithFacebookTest()
    {
    }


}
