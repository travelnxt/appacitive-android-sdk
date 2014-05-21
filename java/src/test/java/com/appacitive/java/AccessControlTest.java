package com.appacitive.java;

import com.appacitive.core.AppacitiveContextBase;
import com.appacitive.core.AppacitiveObject;
import com.appacitive.core.AppacitiveUser;
import com.appacitive.core.model.Callback;
import com.appacitive.core.model.Environment;
import com.jayway.awaitility.Awaitility;
import com.jayway.awaitility.Duration;
import org.junit.*;

import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;

import static com.jayway.awaitility.Awaitility.await;
import static com.jayway.awaitility.Awaitility.to;

/**
 * Created by sathley on 5/8/2014.
 */
public class AccessControlTest {


    private String clientAPIKey = Keys.clientKey;

    @BeforeClass
    public static void oneTimeSetUp() {
        AppacitiveContextBase.initialize(Keys.masterKey, Environment.sandbox, new JavaPlatform());
    }


    @AfterClass
    public static void oneTimeTearDown() {
        // one-time cleanup code
    }

    @Before
    public void beforeTest() {
        Awaitility.reset();
    }

    private String getRandomString() {
        return UUID.randomUUID().toString();
    }

    @Test
    public void userEntityAclTest()
    {
        final AtomicBoolean somethingHappened = new AtomicBoolean(false);
        final AppacitiveUser adminUser = new AppacitiveUser();
        adminUser.setEmail(getRandomString() + "@gmail.com");
        adminUser.setFirstName(getRandomString());
        String username = getRandomString();
        final String password = getRandomString();
        adminUser.setUsername(username);
        adminUser.setPassword(password);

        adminUser.signupInBackground(new Callback<AppacitiveUser>() {
            @Override
            public void success(final AppacitiveUser user) {
                AppacitiveObject entity = new AppacitiveObject("entity");
                entity.accessControl.denyReadByUser(user.getId());
                entity.createInBackground(new Callback<AppacitiveObject>() {
                    @Override
                    public void success(final AppacitiveObject entity) {
                        user.loginInBackground(password, new Callback<String>() {
                            @Override
                            public void success(String result) {
                                AppacitiveContext.initialize(clientAPIKey, Environment.sandbox, new JavaPlatform());
                                AppacitiveObject.getInBackground("entity", entity.getId(), null, new Callback<AppacitiveObject>() {
                                    @Override
                                    public void success(AppacitiveObject result) {
                                        Assert.fail();
                                    }

                                    @Override
                                    public void failure(AppacitiveObject result, Exception e) {
                                        somethingHappened.set(true);
                                    }
                                });
                            }
                        });
                    }
                });
            }
        });
        await().atMost(Duration.TEN_SECONDS).untilTrue(somethingHappened);
    }
}
