package com.appacitive.java;

import com.appacitive.core.AppacitiveContextBase;
import com.appacitive.core.AppacitiveFile;
import com.appacitive.core.model.Callback;
import com.appacitive.core.model.Environment;
import com.appacitive.core.model.FileUploadUrlResponse;
import com.jayway.awaitility.Awaitility;
import org.junit.*;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import static com.jayway.awaitility.Awaitility.await;

/**
 * Created by sathley.
 */
//@Ignore
public class FileTest {

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

    @Test
    public void getUploadUrlTest() {
        final AtomicBoolean somethingHappened = new AtomicBoolean(false);
        AppacitiveFile.getUploadUrlInBackground("image/png", "abc.png", 10, new Callback<FileUploadUrlResponse>() {
            @Override
            public void success(FileUploadUrlResponse result) {
                assert result.fileId != null && result.fileId.isEmpty() == false;
                assert result.url != null && result.url.isEmpty() == false;
                somethingHappened.set(true);
            }

            @Override
            public void failure(FileUploadUrlResponse result, Exception e) {
                Assert.fail(e.getMessage());
            }
        });
        await().untilTrue(somethingHappened);
    }

    @Test
    public void getDownloadUrlTest() {
        final AtomicBoolean somethingHappened = new AtomicBoolean(false);
        AppacitiveFile.getDownloadUrlInBackground("abc.png", 10, new Callback<String>() {
            @Override
            public void success(String result) {
                assert result != null && result.isEmpty() == false;
                somethingHappened.set(true);
            }

            @Override
            public void failure(String result, Exception e) {
                Assert.fail(e.getMessage());
            }
        });
        await().untilTrue(somethingHappened);
    }

    @Test
    public void deleteFileTest() {
        final AtomicBoolean somethingHappened = new AtomicBoolean(false);
        AppacitiveFile.deleteFileInBackground("abc.png", new Callback<Void>() {
            @Override
            public void success(Void result) {
                assert true;
                somethingHappened.set(true);
            }

            @Override
            public void failure(Void result, Exception e) {
                Assert.fail(e.getMessage());
            }
        });
        await().untilTrue(somethingHappened);
    }
}
