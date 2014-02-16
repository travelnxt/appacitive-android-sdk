import com.appacitive.sdk.*;
import com.appacitive.sdk.callbacks.Callback;
import com.appacitive.sdk.exceptions.ValidationException;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

/**
 * Created by sathley.
 */
public class GraphTest {

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
    public void filterQueryTest() throws ValidationException
    {
        final AppacitiveObject parent = new AppacitiveObject("object");
        final String unique = getRandomString();

        AppacitiveObject child = new AppacitiveObject("object");
        child.setProperty("stringfield", unique);

        new AppacitiveConnection("link").fromNewObject("parent", parent).toNewObject("child", child).createInBackground(new Callback<AppacitiveConnection>() {
            @Override
            public void success(AppacitiveConnection result) {
                AppacitiveGraphSearch.filterQueryInBackground("sample_filter", new HashMap<String, String>(){{put("search_value", unique);}}, new Callback<List<Long>>() {
                            @Override
                            public void success(List<Long> result) {
                                assert result.size() == 1;
                                assert result.get(0) == parent.id;
                            }

                            @Override
                            public void failure(List<Long> result, Exception e) {
                                Assert.fail(e.getMessage());
                            }
                        });
            }
        });
    }

    @Test
    public void projectQueryTest() throws ValidationException
    {
        final String val1 = getRandomString();
        final String val2 = getRandomString();
        final AppacitiveObject root = new AppacitiveObject("object");
        final AppacitiveObject level1child = new AppacitiveObject("object");
        level1child.setProperty("stringfield", val1);

        final AppacitiveConnection level1edge = new AppacitiveConnection("link");
        level1edge.fromNewObject("parent", root).toNewObject("child", level1child);
        level1edge.createInBackground(new Callback<AppacitiveConnection>() {
            @Override
            public void success(AppacitiveConnection result) {
                final AppacitiveObject level2child = new AppacitiveObject("object");
                level2child.setProperty("stringfield", val2);
                final AppacitiveConnection level2edge = new AppacitiveConnection("link");
                level2edge.fromExistingObject("parent", level1child.id).toNewObject("child", level2child);
                try{
                level2edge.createInBackground(new Callback<AppacitiveConnection>() {
                    @Override
                    public void success(AppacitiveConnection result) {
                        List<Long> ids = new ArrayList<Long>();
                        ids.add(root.getId());
                        AppacitiveGraphSearch.projectQueryInBackground("sample_project", ids, new HashMap<String, String>(){{put("level1_filter", val1);put("level2_filter", val2);}}, new Callback<List<AppacitiveGraphNode>>() {
                                    @Override
                                    public void success(List<AppacitiveGraphNode> result) {
                                        assert result.size() == 1;
                                        assert result.get(0).object != null;
                                        assert result.get(0).object.id == root.id;
                                        List<AppacitiveGraphNode> level1children = result.get(0).getChildren("level1_children");
                                        assert level1children.size() == 1;
                                        assert level1children.get(0).object != null;
                                        assert level1children.get(0).object.id == level1child.id;
                                        assert level1children.get(0).connection != null;
                                        assert level1children.get(0).connection.id == level1edge.id;
                                        assert level1children.get(0).connection.endpointA.objectId == root.id;
                                        assert level1children.get(0).connection.endpointB.objectId == level1child.id;

                                        List<AppacitiveGraphNode> level2children = level1children.get(0).getChildren("level2_children");
                                        assert level2children.size() == 1;
                                        assert level2children.get(0).object != null;
                                        assert level2children.get(0).object.id == level2child.id;
                                        assert level2children.get(0).connection != null;
                                        assert level2children.get(0).connection.id == level2edge.id;
                                        assert level2children.get(0).connection.endpointA.objectId == level1child.id;
                                        assert level2children.get(0).connection.endpointB.objectId == level2child.id;
                                    }

                                    @Override
                                    public void failure(List<AppacitiveGraphNode> result, Exception e) {
                                        Assert.fail(e.getMessage());
                                    }
                                });
                    }
                });
                }
                catch (ValidationException e )
                {
                    Assert.fail(e.getMessage());
                }
            }
        });

    }
}
