package com.appacitive.core.model;

import com.appacitive.core.AppacitiveEntity;
import com.appacitive.core.apjson.APJSONArray;
import com.appacitive.core.apjson.APJSONException;
import com.appacitive.core.apjson.APJSONObject;
import com.appacitive.core.infra.APSerializable;
import com.appacitive.core.infra.SystemDefinedPropertiesHelper;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by sathley on 8/20/2014.
 */
public class AppacitiveObjectBase extends AppacitiveEntity implements Serializable, APSerializable {

    protected AppacitiveObjectBase()
    {    }

    public AppacitiveObjectBase(String type)
    {
        this.setType(type);
    }

    public AppacitiveObjectBase(String type, long id)
    {
        this(type);
        this.setId(id);
    }

    protected String type = null;

    protected long typeId = 0;

    public Acl accessControl = new Acl();

    public synchronized void setSelf(APJSONObject object) {

        super.setSelf(object);

        if (object != null) {
            if (object.isNull(SystemDefinedPropertiesHelper.typeId) == false)
                this.typeId = object.optLong(SystemDefinedPropertiesHelper.typeId);
            if (object.isNull(SystemDefinedPropertiesHelper.type) == false)
                this.type = object.optString(SystemDefinedPropertiesHelper.type);
        }
    }

    public synchronized APJSONObject getMap() throws APJSONException {
        APJSONObject nativeMap = super.getMap();
        nativeMap.put(SystemDefinedPropertiesHelper.type, this.type);
        nativeMap.put(SystemDefinedPropertiesHelper.typeId, String.valueOf(this.typeId));
        nativeMap.put("__acls", this.accessControl.getMap());
        return nativeMap;
    }

    @Override
    public synchronized APJSONObject getUpdateCommand() throws APJSONException {
        APJSONObject updateCommand = super.getUpdateCommand();
        updateCommand.put(SystemDefinedPropertiesHelper.type, this.type);
        updateCommand.put(SystemDefinedPropertiesHelper.typeId, String.valueOf(this.typeId));
        return updateCommand;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setTypeId(long typeId) {
        this.typeId = typeId;
    }

    public String getType() {
        return type;
    }

    public long getTypeId() {
        return typeId;
    }

    public HashMap<String, String> getAggregate(String aggregateName)
    {
        if(aggregateName.startsWith("$") == false)
            aggregateName = "$".concat(aggregateName);
        if (this.properties.containsKey(aggregateName) == true){
            HashMap<String, String> aggregateValue = new HashMap<String, String>();
            APJSONObject aggregateObject = (APJSONObject)this.properties.get(aggregateName);
            Iterator<String> iterator = aggregateObject.keys();
            while (iterator.hasNext()) {
                String key = iterator.next();
                try {
                    aggregateValue.put(key, aggregateObject.getString(key));
                } catch (APJSONException e) {
                    throw new RuntimeException(e);
                }
            }
            return aggregateValue;
        }
        return null;
    }
}
