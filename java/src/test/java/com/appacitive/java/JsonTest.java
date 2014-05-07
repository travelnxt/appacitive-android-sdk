package com.appacitive.java;

import com.appacitive.core.AppacitiveObject;
import com.appacitive.core.apjson.APJSONException;
import com.appacitive.core.apjson.APJSONObject;
import com.appacitive.core.model.Access;
import com.appacitive.core.model.Acl;
import com.appacitive.core.model.Permission;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sathley.
 */
public class JsonTest {

    @Test
    public void multiValuedPropertiesTest() {

        AppacitiveObject object = new AppacitiveObject();
        object.setPropertyAsMultiValued("str_multi", new ArrayList<String>() {{
            add("a");
            add("b");
            add(null);
        }});
        object.setPropertyAsMultiValued("int_multi", new ArrayList<Integer>() {{
            add(1);
            add(2);
            add(null);
        }});
        object.setPropertyAsMultiValued("dec_multi", new ArrayList<Double>() {{
            add(1.1);
            add(2.2);
            add(null);
        }});

        List<String> strs = object.getPropertyAsMultiValuedString("str_multi");
        List<Integer> ints = object.getPropertyAsMultiValuedInt("int_multi");
        List<Double> decs = object.getPropertyAsMultiValuedDouble("dec_multi");

        APJSONObject returnObject = null;
        try {
            APJSONObject apjsonObject = object.getMap();
            String str = apjsonObject.toString();
            returnObject = new APJSONObject(str);
        } catch (Exception e) {


        }
        object.setSelf(returnObject);
        List<String> strs1 = object.getPropertyAsMultiValuedString("str_multi");
        List<Integer> ints1 = object.getPropertyAsMultiValuedInt("int_multi");
        List<Double> decs1 = object.getPropertyAsMultiValuedDouble("dec_multi");


    }

    @Test
    public void multiValuedPropertiesTest2() {

        AppacitiveObject object = new AppacitiveObject();
        object.setPropertyAsMultiValued("str_multi", new ArrayList<String>() {{
            add("a");
            add("b");
            add(null);
        }});
        object.setPropertyAsMultiValued("int_multi", new ArrayList<String>() {{
            add("1");
            add("2");
            add(null);
        }});
        object.setPropertyAsMultiValued("dec_multi", new ArrayList<String>() {{
            add("1.1");
            add("2.2");
            add(null);
        }});
//
        List<String> strs = object.getPropertyAsMultiValuedString("str_multi");
        List<Integer> ints = object.getPropertyAsMultiValuedInt("int_multi");
        List<Double> decs = object.getPropertyAsMultiValuedDouble("dec_multi");

        APJSONObject returnObject = null;
        try {
            APJSONObject apjsonObject = object.getMap();
            String str = apjsonObject.toString();
            returnObject = new APJSONObject(str);
        } catch (Exception e) {


        }
        object.setSelf(returnObject);
        List<String> strs1 = object.getPropertyAsMultiValuedString("str_multi");
        List<Integer> ints1 = object.getPropertyAsMultiValuedInt("int_multi");
        List<Double> decs1 = object.getPropertyAsMultiValuedDouble("dec_multi");


    }


}
