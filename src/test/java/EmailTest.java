import com.appacitive.sdk.AppacitiveContext;
import com.appacitive.sdk.AppacitiveEmail;
import com.appacitive.sdk.infra.JavaPlatform;
import com.appacitive.sdk.model.*;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.HashMap;

/**
 * Created by sathley.
 */
public class EmailTest {

    @BeforeClass
    public static void oneTimeSetUp() {
        AppacitiveContext.initialize("up8+oWrzVTVIxl9ZiKtyamVKgBnV5xvmV95u1mEVRrM=", Environment.sandbox, new JavaPlatform());
    }

    @AfterClass
    public static void oneTimeTearDown() {
        // one-time cleanup code
    }

    private static final String testEmail = "test@appacitive.com";

    @Test
    public void sendRawEmailTest()
    {
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
            }

            @Override
            public void failure(AppacitiveEmail result, Exception e) {
                Assert.fail(e.getMessage());
            }
        });
    }

    @Test
    public void sendTemplatedEmailTest()
    {
        TemplatedEmailBody body = new TemplatedEmailBody("sample", new HashMap<String, String>(){{put("username", "cobra");}}, false);
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
            }

            @Override
            public void failure(AppacitiveEmail result, Exception e) {
                Assert.fail(e.getMessage());
            }
        });
    }

    @Test
    public void sendRawEmailWithCustomSmtpTest()
    {
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
            }

            @Override
            public void failure(AppacitiveEmail result, Exception e) {
                Assert.fail(e.getMessage());
            }
        });
    }

    @Test
    public void sendTemplatedEmailWithSmtpTest()
    {
        SmtpSettings settings = new SmtpSettings("username", "password", "smtp.gmail.com", 465, true);
        TemplatedEmailBody body = new TemplatedEmailBody("sample", new HashMap<String, String>(){{put("username", "cobra");}}, false);
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
            }

            @Override
            public void failure(AppacitiveEmail result, Exception e) {
                Assert.fail(e.getMessage());
            }
        });
    }

    @Test
    public void sendRawEmailTestSample()
    {
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
            }

            @Override
            public void failure(AppacitiveEmail result, Exception e) {
                Assert.fail(e.getMessage());
            }
        });
    }

}
