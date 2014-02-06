import com.appacitive.sdk.AppacitiveConnection;
import com.appacitive.sdk.AppacitiveContext;
import com.appacitive.sdk.AppacitiveObject;
import com.appacitive.sdk.callbacks.Callback;
import com.appacitive.sdk.exceptions.AppacitiveException;
import com.appacitive.sdk.exceptions.ValidationError;
import com.appacitive.sdk.infra.Environment;
import com.appacitive.sdk.infra.ErrorCodes;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sathley.
 */

public class ConnectionTest {

    @BeforeClass
    public static void oneTimeSetUp() {
        AppacitiveContext.initialize("up8+oWrzVTVIxl9ZiKtyamVKgBnV5xvmV95u1mEVRrM=", Environment.sandbox);
    }

    @AfterClass
    public static void oneTimeTearDown() {
        // one-time cleanup code
    }

    @Test
    public void createConnectionBetweenNewObjects() throws ValidationError
    {
        final AppacitiveObject parent = new AppacitiveObject("object");
        final AppacitiveObject child = new AppacitiveObject("object");
        parent.createInBackground(new Callback<AppacitiveObject>() {
            @Override
            public void success(final AppacitiveObject p) throws Exception {
                child.createInBackground(new Callback<AppacitiveObject>() {
                    @Override
                    public void success(final AppacitiveObject c) throws Exception {
                        AppacitiveConnection sibling = new AppacitiveConnection("sibling");
                        sibling.endpointA.label = "object";
                        sibling.endpointB.label = "object";
                        sibling.endpointA.objectId = p.getId();
                        sibling.endpointB.objectId = c.getId();
                        sibling.setProperty("field1", "hello");
                        sibling.setProperty("field2", "1520");

                        sibling.addTag("t1");
                        sibling.addTag("t2");

                        sibling.setAttribute("a1", "v1");
                        sibling.setAttribute("a2", "v2");

                        sibling.createInBackground(new Callback<AppacitiveConnection>() {
                            @Override
                            public void success(AppacitiveConnection result) throws Exception {
                                assert result.getId() > 0;
                            }

                            @Override
                            public void failure(AppacitiveConnection result, AppacitiveException e) {
                                assert false;
                            }
                        });
                    }
                });
            }
        });
    }

    @Test
    public void createConnectionBetweenNewObjectsUsingFluentSyntaxTest() throws ValidationError
    {
        final AppacitiveObject parent = new AppacitiveObject("object");
        final AppacitiveObject child = new AppacitiveObject("object");
        parent.createInBackground(new Callback<AppacitiveObject>() {
            @Override
            public void success(final AppacitiveObject p) throws Exception {
                child.createInBackground(new Callback<AppacitiveObject>() {
                    @Override
                    public void success(final AppacitiveObject c) throws Exception {
                        AppacitiveConnection sibling = new AppacitiveConnection("sibling").fromExistingObject("object", p.getId()).toExistingObject("object", c.getId());
                        sibling.setProperty("field1", "hello");
                        sibling.setProperty("field2", "1520");

                        sibling.addTag("t1");
                        sibling.addTag("t2");

                        sibling.setAttribute("a1", "v1");
                        sibling.setAttribute("a2", "v2");

                        sibling.createInBackground(new Callback<AppacitiveConnection>() {
                            @Override
                            public void success(AppacitiveConnection result) throws Exception {
                                assert result.getId() > 0;
                            }

                            @Override
                            public void failure(AppacitiveConnection result, AppacitiveException e) {
                                assert false;
                            }
                        });
                    }
                });
            }
        });
    }

    @Test
    public void createConnectionBetweenNewAndExistingObjectTest() throws ValidationError
    {
        final AppacitiveObject parent = new AppacitiveObject("object");
        final AppacitiveObject child = new AppacitiveObject("object");
        parent.createInBackground(new Callback<AppacitiveObject>() {
            @Override
            public void success(final AppacitiveObject p) throws Exception {
                AppacitiveConnection sibling = new AppacitiveConnection("sibling").fromExistingObject("object", p.getId()).toNewObject("object", child);
                sibling.createInBackground(new Callback<AppacitiveConnection>() {
                    @Override
                    public void success(AppacitiveConnection result) throws Exception {
                        assert result.getId() > 0;
                        assert child.getId() > 0;
                        assert result.endpointB.objectId == child.getId();
                    }

                    @Override
                    public void failure(AppacitiveConnection result, AppacitiveException e) {
                        assert false;
                    }
                });
            }
        });
    }

    @Test
    public void createConnectionBetweenNewAndNewObjectsTest() throws ValidationError
    {
        final AppacitiveObject parent = new AppacitiveObject("object");
        final AppacitiveObject child = new AppacitiveObject("object");
        new AppacitiveConnection("sibling").fromNewObject("object", parent).toNewObject("object", child).createInBackground(new Callback<AppacitiveConnection>() {
            @Override
            public void success(AppacitiveConnection result) throws Exception {
                assert result.getId() > 0;
                assert parent.getId() > 0;
                assert child.getId() > 0;
                assert result.endpointA.objectId == parent.getId();
                assert result.endpointB.objectId == child.getId();
            }

            @Override
            public void failure(AppacitiveConnection result, AppacitiveException e) {
                assert false;
            }
        });
    }

    @Test
    public void getConnectionTest() throws ValidationError
    {
        final AppacitiveObject parent = new AppacitiveObject("object");
        final AppacitiveObject child = new AppacitiveObject("object");
        new AppacitiveConnection("sibling").fromNewObject("object", parent).toNewObject("object", child).createInBackground(new Callback<AppacitiveConnection>() {
            @Override
            public void success(final AppacitiveConnection result1) throws Exception {
                AppacitiveConnection.getInBackground("sibling", result1.getId(), null, new Callback<AppacitiveConnection>() {
                    @Override
                    public void success(AppacitiveConnection result2) throws Exception {
                        assert result1.getId() == result2.getId();
                    }

                    @Override
                    public void failure(AppacitiveConnection result, AppacitiveException e) {
                        assert false;
                    }
                });
            }
        });
    }

    @Test
    public void multiGetConnectionTest() throws ValidationError, InterruptedException
    {
        final List<Long> ids = new ArrayList<Long>();
        for(int i=0; i< 3;i++)
        {
            final AppacitiveObject parent = new AppacitiveObject("object");
            final AppacitiveObject child = new AppacitiveObject("object");
            new AppacitiveConnection("sibling").fromNewObject("object", parent).toNewObject("object", child).createInBackground(new Callback<AppacitiveConnection>() {
                @Override
                public void success(AppacitiveConnection result) throws Exception {
                    ids.add(result.getId());
                }
            });
        }
        Thread.sleep(5000);
        AppacitiveConnection.multiGetInBackground("sibling", ids, null, new Callback<List<AppacitiveConnection>>() {
            @Override
            public void success(List<AppacitiveConnection> result) throws Exception {
                assert result.size() == 3;
            }

            @Override
            public void failure(List<AppacitiveConnection> result, AppacitiveException e) {
                assert false;
            }
        });
    }

    @Test
    public void deleteConnectionTest() throws ValidationError
    {
        final AppacitiveObject parent = new AppacitiveObject("object");
        final AppacitiveObject child = new AppacitiveObject("object");
        new AppacitiveConnection("sibling").fromNewObject("object", parent).toNewObject("object", child).createInBackground(new Callback<AppacitiveConnection>() {
            @Override
            public void success(AppacitiveConnection result) throws Exception {
                result.deleteInBackground(new Callback<Void>() {
                    @Override
                    public void success(Void result) throws Exception {
                        assert true;
                    }

                    @Override
                    public void failure(Void result, AppacitiveException e) {
                        assert false;
                    }
                });
            }
        });
    }

    @Test
    public void multiDeleteConnectionsTest() throws ValidationError, InterruptedException
    {
        final List<Long> ids = new ArrayList<Long>();
        for(int i = 0; i<3; i++)
        {
            final AppacitiveObject parent = new AppacitiveObject("object");
            final AppacitiveObject child = new AppacitiveObject("object");
            new AppacitiveConnection("sibling").fromNewObject("object", parent).toNewObject("object", child).createInBackground(new Callback<AppacitiveConnection>() {
                @Override
                public void success(AppacitiveConnection result) throws Exception {
                    ids.add(result.getId());
                }
            });
        }
        Thread.sleep(5000);
        AppacitiveConnection.bulkDeleteInBackground("sibling", ids, new Callback<Void>() {
            @Override
            public void success(Void result) throws Exception {
                for(long id:ids)
                {
                    AppacitiveConnection.getInBackground("sibling", id, null, new Callback<AppacitiveConnection>() {
                        @Override
                        public void success(AppacitiveConnection result) throws Exception {
                            assert false;
                        }

                        @Override
                        public void failure(AppacitiveConnection result, AppacitiveException e) {
                            assert e.code.equals(ErrorCodes.NOT_FOUND);
                        }
                    });
                }
            }

            @Override
            public void failure(Void result, AppacitiveException e) {
                assert false;
            }
        });
    }

}
