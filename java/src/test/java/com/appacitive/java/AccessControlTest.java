package com.appacitive.java;

import com.appacitive.core.AppacitiveContextBase;
import com.appacitive.core.AppacitiveUser;
import com.appacitive.core.model.Callback;
import com.appacitive.core.model.Environment;
import com.jayway.awaitility.Awaitility;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by sathley on 5/8/2014.
 */
public class AccessControlTest {

    @BeforeClass
    public static void oneTimeSetUp() {
        AppacitiveContextBase.initialize("up8+oWrzVTVIxl9ZiKtyamVKgBnV5xvmV95u1mEVRrM=", Environment.sandbox, new JavaPlatform());
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

//    @Test
//    public void userAclTest()
//    {
//        final AtomicBoolean somethingHappened = new AtomicBoolean(false);
//        AppacitiveUser adminUser = new AppacitiveUser();
//        adminUser.setEmail(getRandomString() + "@gmail.com");
//        adminUser.setFirstName(getRandomString());
//        String username = getRandomString();
//        final String password = getRandomString();
//        adminUser.setUsername(username);
//        adminUser.setPassword(password);
//
//        adminUser.signupInBackground(new Callback<AppacitiveUser>() {
//            @Override
//            public void success(AppacitiveUser result) {
//                result.loginInBackground(password, new Callback<String>() {
//                    @Override
//                    public void success(String token) {
//                        AppacitiveUser regularUser = new AppacitiveUser();
//
//                    }
//                });
//            }
//        });
//    }
}
