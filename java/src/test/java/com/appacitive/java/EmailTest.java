package com.appacitive.java;

import com.appacitive.core.AppacitiveContextBase;
import com.appacitive.core.AppacitiveEmail;
import com.appacitive.core.model.*;
import com.jayway.awaitility.Awaitility;
import org.junit.*;

import java.util.HashMap;
import java.util.concurrent.atomic.AtomicBoolean;

import static com.jayway.awaitility.Awaitility.await;

/**
 * Created by sathley.
 */
public class EmailTest {

    @BeforeClass
    public static void oneTimeSetUp() {
        AppacitiveContextBase.initialize("up8+oWrzVTVIxl9ZiKtyamVKgBnV5xvmV95u1mEVRrM=", Environment.sandbox, new JavaPlatform());
//        Awaitility.setDefaultTimeout(5, TimeUnit.MINUTES);
    }

    @AfterClass
    public static void oneTimeTearDown() {
        // one-time cleanup code
    }

//    private static AtomicBoolean somethingHappened;

    @Before
    public void beforeTest() {
        Awaitility.reset();
    }

    private static final String testEmail = "test@appacitive.com";

    @Test
    public void sendRawEmailTest() throws Exception {
        final AtomicBoolean somethingHappened = new AtomicBoolean(false);
        final AppacitiveEmail email = new AppacitiveEmail("subject").withBody(new RawEmailBody("raw content", false));
        email.to.add(testEmail);
        email.cc.add(testEmail);
        email.bcc.add(testEmail);
        email.fromAddress = testEmail;
        email.replyToAddress = testEmail;
        email.sendInBackground(new Callback<AppacitiveEmail>() {
            @Override
            public void success(AppacitiveEmail result) {
                assert result.getId() > 0;
                somethingHappened.set(true);
            }

            @Override
            public void failure(AppacitiveEmail result, Exception e) {
                Assert.fail(e.getMessage());
                System.out.println(e.getMessage());
            }
        });
        await().untilTrue(somethingHappened);
    }

    @Test
    public void sendTemplatedEmailTest() {
        final AtomicBoolean somethingHappened = new AtomicBoolean(false);
        TemplatedEmailBody body = new TemplatedEmailBody("sample", new HashMap<String, String>() {{
            put("username", "cobra");
        }}, false);
        AppacitiveEmail email = new AppacitiveEmail("subject").withBody(body);
        email.to.add(testEmail);
        email.cc.add(testEmail);
        email.bcc.add(testEmail);
        email.fromAddress = testEmail;
        email.replyToAddress = testEmail;
        email.sendInBackground(new Callback<AppacitiveEmail>() {
            @Override
            public void success(AppacitiveEmail result) {
                assert result.getId() > 0;
                assert result.to.size() == 1;
                assert result.cc.size() == 1;
                assert result.bcc.size() == 1;
                assert result.fromAddress != null;
                assert result.to != null;
                assert result.body != null;
                assert ((TemplatedEmailBody) (result.body)).getData().size() == 1;
                somethingHappened.set(true);
            }

            @Override
            public void failure(AppacitiveEmail result, Exception e) {
                Assert.fail(e.getMessage());
            }
        });
        await().untilTrue(somethingHappened);
    }

    @Test
    public void sendRawEmailWithCustomSmtpTest() {
        final AtomicBoolean somethingHappened = new AtomicBoolean(false);
        SmtpSettings settings = new SmtpSettings("username", "password", "smtp.gmail.com", 465, true);
        AppacitiveEmail email = new AppacitiveEmail("subject").withBody(new RawEmailBody("raw content", false)).withSmtp(settings);
        email.to.add(testEmail);
        email.cc.add(testEmail);
        email.bcc.add(testEmail);
        email.fromAddress = testEmail;
        email.replyToAddress = testEmail;
        email.sendInBackground(new Callback<AppacitiveEmail>() {
            @Override
            public void success(AppacitiveEmail result) {
                assert result.getId() > 0;
                somethingHappened.set(true);
            }

            @Override
            public void failure(AppacitiveEmail result, Exception e) {
                Assert.fail(e.getMessage());
            }
        });
        await().untilTrue(somethingHappened);
    }

    @Test
    public void sendTemplatedEmailWithSmtpTest() {
        final AtomicBoolean somethingHappened = new AtomicBoolean(false);
        SmtpSettings settings = new SmtpSettings("username", "password", "smtp.gmail.com", 465, true);
        TemplatedEmailBody body = new TemplatedEmailBody("sample", new HashMap<String, String>() {{
            put("username", "cobra");
        }}, false);
        AppacitiveEmail email = new AppacitiveEmail("subject").withBody(body).withSmtp(settings);
        email.to.add(testEmail);
        email.cc.add(testEmail);
        email.bcc.add(testEmail);
        email.fromAddress = testEmail;
        email.replyToAddress = testEmail;
        email.sendInBackground(new Callback<AppacitiveEmail>() {
            @Override
            public void success(AppacitiveEmail result) {
                assert result.getId() > 0;
                somethingHappened.set(true);
            }

            @Override
            public void failure(AppacitiveEmail result, Exception e) {
                Assert.fail(e.getMessage());
            }
        });
        await().untilTrue(somethingHappened);
    }

    @Test
    public void sendRawEmailTestSample() throws InterruptedException {
        final AtomicBoolean somethingHappened = new AtomicBoolean(false);
        AppacitiveEmail email = new AppacitiveEmail("subject").withBody(new RawEmailBody("raw content", false));
        email.to.add(testEmail);
        email.cc.add(testEmail);
        email.bcc.add(testEmail);
        email.fromAddress = testEmail;
        email.replyToAddress = testEmail;
        email.sendInBackground(new Callback<AppacitiveEmail>() {
            @Override
            public void success(AppacitiveEmail result) {
                assert result.getId() > 0;
                somethingHappened.set(true);
            }

            @Override
            public void failure(AppacitiveEmail result, Exception e) {
                Assert.fail(e.getMessage());
            }
        });
        await().untilTrue(somethingHappened);
    }

}
