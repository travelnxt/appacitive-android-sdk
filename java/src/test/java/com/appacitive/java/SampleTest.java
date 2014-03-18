package com.appacitive.java;

import com.appacitive.core.AppacitiveContextBase;
import com.appacitive.core.AppacitiveEmail;
import com.appacitive.core.exceptions.ValidationException;
import com.appacitive.core.model.Callback;
import com.appacitive.core.model.Environment;
import com.appacitive.core.model.RawEmailBody;
import org.junit.*;

import java.io.IOException;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import static com.jayway.awaitility.Awaitility.await;

/**
 * Created by sathley.
 */
//@Ignore


/**
 * Created by sathley.
 */
@Ignore
public class SampleTest {

    @BeforeClass
    public static void oneTimeSetUp() {
        AppacitiveContextBase.initialize("up8+oWrzVTVIxl9ZiKtyamVKgBnV5xvmV95u1mEVRrM=", Environment.sandbox, new JavaPlatform());
    }

    private volatile AtomicBoolean somethingHappened = new AtomicBoolean(false);

    @AfterClass
    public static void oneTimeTearDown() {
        // one-time cleanup code
    }


    @Before
    public void beforeTest() {
        somethingHappened.set(false);
    }

    @After
    public void afterTest() throws IOException {
        somethingHappened.set(false);
    }

    private String getRandomString() {
        return UUID.randomUUID().toString();
    }

    private static final String testEmail = "test@appacitive.com";

    @Test
    public void updateObjectTest() throws ValidationException, InterruptedException {
        AppacitiveContextBase.initialize("up8+oWrzVTVIxl9ZiKtyamVKgBnV5xvmV95u1mEVRrM=", Environment.sandbox, new JavaPlatform());
        AppacitiveEmail email = new AppacitiveEmail("subject").withBody(new RawEmailBody("raw content", false));
        email.to.add(testEmail);
        email.cc.add(testEmail);
        email.bcc.add(testEmail);
        email.fromAddress = testEmail;
        email.replyToAddress = testEmail;
        email.sendInBackground(new Callback<AppacitiveEmail>() {
            @Override
            public void success(AppacitiveEmail result) {
                assert false;
                somethingHappened.set(true);

            }

            @Override
            public void failure(AppacitiveEmail result, Exception e) {
                Assert.fail(e.getMessage());
                System.out.println(e.getMessage());
            }
        });
        await().atMost(5, TimeUnit.SECONDS).untilTrue(somethingHappened);
    }

    @Test
    public void updateObject2Test() throws ValidationException, InterruptedException {
        AppacitiveContextBase.initialize("up8+oWrzVTVIxl9ZiKtyamVKgBnV5xvmV95u1mEVRrM=", Environment.sandbox, new JavaPlatform());
        AppacitiveEmail email = new AppacitiveEmail("subject").withBody(new RawEmailBody("raw content", false));
        email.to.add(testEmail);
        email.cc.add(testEmail);
        email.bcc.add(testEmail);
        email.fromAddress = testEmail;
        email.replyToAddress = testEmail;
        email.sendInBackground(new Callback<AppacitiveEmail>() {
            @Override
            public void success(AppacitiveEmail result) {
                assert true;
                somethingHappened.set(true);

            }

            @Override
            public void failure(AppacitiveEmail result, Exception e) {
                Assert.fail(e.getMessage());
                System.out.println(e.getMessage());
            }
        });
        await().atMost(5, TimeUnit.SECONDS).untilTrue(somethingHappened);
    }


}
