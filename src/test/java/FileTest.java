import com.appacitive.sdk.AppacitiveContext;
import com.appacitive.sdk.AppacitiveFile;
import com.appacitive.sdk.model.Callback;
import com.appacitive.sdk.model.Environment;
import com.appacitive.sdk.model.FileUploadUrlResponse;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

/**
* Created by sathley.
*/
//@Ignore
public class FileTest {

    @BeforeClass
    public static void oneTimeSetUp() {
        AppacitiveContext.initialize("up8+oWrzVTVIxl9ZiKtyamVKgBnV5xvmV95u1mEVRrM=", Environment.sandbox);
    }

    @AfterClass
    public static void oneTimeTearDown() {
        // one-time cleanup code
    }

    @Test
    public void getUploadUrlTest()
    {
        AppacitiveFile.getUploadUrlInBackground("image/png", "abc.png", 10, new Callback<FileUploadUrlResponse>() {
            @Override
            public void success(FileUploadUrlResponse result) {
                assert result.fileId != null && result.fileId.isEmpty() == false;
                assert result.url != null && result.url.isEmpty() == false;
            }

            @Override
            public void failure(FileUploadUrlResponse result, Exception e) {
                Assert.fail(e.getMessage());
            }
        });
    }

    @Test
    public void getDownloadUrlTest()
    {
        AppacitiveFile.getDownloadUrlInBackground("abc.png", 10, new Callback<String>() {
            @Override
            public void success(String result) {
                assert result != null && result.isEmpty() ==false;
            }

            @Override
            public void failure(String result, Exception e) {
                Assert.fail(e.getMessage());
            }
        });
    }

    @Test
    public void deleteFileTest()
    {
        AppacitiveFile.deleteFileInBackground("abc.png", new Callback<Void>() {
            @Override
            public void success(Void result) {
                assert true;
            }

            @Override
            public void failure(Void result, Exception e) {
                Assert.fail(e.getMessage());
            }
        });
    }
}
