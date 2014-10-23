/**
 * Created by sathley.
 */
package com.appacitive.core;

import com.appacitive.core.apjson.APJSONArray;
import com.appacitive.core.apjson.APJSONException;
import com.appacitive.core.apjson.APJSONObject;
import com.appacitive.core.infra.APSerializable;
import com.appacitive.core.infra.SystemDefinedPropertiesHelper;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public abstract class AppacitiveEntity implements Serializable, APSerializable {

    public AppacitiveEntity() {
        properties = new ConcurrentHashMap<String, Object>();
        attributes = new ConcurrentHashMap<String, String>();
        tags = new ArrayList<String>();
        id = 0;
        revision = 0;
        propertiesChanged = new ConcurrentHashMap<String, Object>();

        integerPropertyIncrements = new ArrayList<IntegerPropertyIncrement>();
        integerPropertyDecrements = new ArrayList<IntegerPropertyDecrement>();
        decimalPropertyIncrements = new ArrayList<DecimalPropertyIncrement>();
        decimalPropertyDecrements = new ArrayList<DecimalPropertyDecrement>();

        addedItemses = new ArrayList<ItemsCollection>();
        uniquelyAddedItemses = new ArrayList<ItemsCollection>();
        removedItemses = new ArrayList<ItemsCollection>();

        attributesChanged = new ConcurrentHashMap<String, String>();
        tagsAdded = new ArrayList<String>();
        tagsRemoved = new ArrayList<String>();
    }

    public synchronized void setSelf(APJSONObject entity) {
        if (entity != null) {
            //  Wipe out previous data
            this.properties = new ConcurrentHashMap<String, Object>();
            this.attributes = new ConcurrentHashMap<String, String>();
            this.tags = new ArrayList<String>();

            this.resetUpdateCommands();

            //  Read in new data

            this.id = Long.parseLong(entity.optString(SystemDefinedPropertiesHelper.id, "0"));

            this.revision = Long.parseLong(entity.optString(SystemDefinedPropertiesHelper.revision, "0"));

            this.createdBy = entity.optString(SystemDefinedPropertiesHelper.createdBy, null);

            this.lastModifiedBy = entity.optString(SystemDefinedPropertiesHelper.lastModifiedBy, null);
            DateFormat dateTimeFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSSS'Z'");
            try {
                this.utcDateCreated = dateTimeFormat.parse(entity.optString(SystemDefinedPropertiesHelper.utcDateCreated, ""));
            } catch (ParseException e) {
                this.utcDateCreated = null;
            }
            try {
                this.utcLastUpdated = dateTimeFormat.parse(entity.optString(SystemDefinedPropertiesHelper.utcLastUpdatedDate, ""));
            } catch (ParseException e) {
                this.utcLastUpdated = null;
            }
            if (entity.isNull(SystemDefinedPropertiesHelper.tags) == false) {
                APJSONArray tagsArray = entity.optJSONArray(SystemDefinedPropertiesHelper.tags);
                for (int i = 0; i < tagsArray.length(); i++) {
                    this.tags.add(tagsArray.optString(i));
                }
            }
            if (entity.isNull(SystemDefinedPropertiesHelper.attributes) == false) {
                APJSONObject attributesObject = entity.optJSONObject(SystemDefinedPropertiesHelper.attributes);
                Iterator<String> iterator = attributesObject.keys();
                while (iterator.hasNext()) {
                    String key = iterator.next();
                    this.attributes.put(key, attributesObject.optString(key));
                }

            }
            Iterator<String> iterator = entity.keys();
            while (iterator.hasNext()) {
                String key = iterator.next();
                if (SystemDefinedPropertiesHelper.ConnectionSystemProperties.contains(key) == false && SystemDefinedPropertiesHelper.ObjectSystemProperties.contains(key) == false) {
                    {
                        Object propertyValue = entity.opt(key);
                        if (propertyValue.equals(null))
                            this.properties.put(key, null);
                        else if (propertyValue instanceof APJSONArray)
                            this.properties.put(key, ((APJSONArray) propertyValue).values);
                        else this.properties.put(key, propertyValue);
                    }
                }
            }
        }
    }

    public synchronized APJSONObject getMap() throws APJSONException {
        APJSONObject jsonObject = new APJSONObject();

        if (this.id > 0)
            jsonObject.put(SystemDefinedPropertiesHelper.id, String.valueOf(this.id));
        if (this.revision > 0)
            jsonObject.put(SystemDefinedPropertiesHelper.revision, String.valueOf(this.revision));
        if (this.createdBy != null && this.createdBy.isEmpty() == false)
            jsonObject.put(SystemDefinedPropertiesHelper.createdBy, this.createdBy);
        if (this.lastModifiedBy != null && this.lastModifiedBy.isEmpty() == false)
            jsonObject.put(SystemDefinedPropertiesHelper.lastModifiedBy, this.lastModifiedBy);
        if (this.utcDateCreated != null)
            jsonObject.put(SystemDefinedPropertiesHelper.utcDateCreated, this.utcDateCreated);
        if (this.utcLastUpdated != null)
            jsonObject.put(SystemDefinedPropertiesHelper.utcLastUpdatedDate, this.utcLastUpdated);
        if (this.tags != null && tags.size() != 0)
            jsonObject.put(SystemDefinedPropertiesHelper.tags, new APJSONArray(this.tags));

        if (this.attributes != null && this.attributes.size() > 0)
            jsonObject.put(SystemDefinedPropertiesHelper.attributes, new APJSONObject(this.attributes));

        if (this.properties != null && this.properties.size() > 0)
            for (Map.Entry<String, Object> property : this.properties.entrySet()) {
                if (property.getValue() == null)
                    jsonObject.put(property.getKey(), APJSONObject.NULL);
                else if (property.getValue() instanceof List)
                    jsonObject.put(property.getKey(), new APJSONArray((List) property.getValue()));
                else
                    jsonObject.put(property.getKey(), property.getValue());

            }

        return jsonObject;
    }

    public synchronized void setId(long id) {
        this.id = id;
    }

    public synchronized void setRevision(long revision) {
        this.revision = revision;
    }

    public synchronized void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public synchronized void setLastModifiedBy(String lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    public synchronized void setUtcDateCreated(Date utcDateCreated) {
        this.utcDateCreated = utcDateCreated;
    }

    public synchronized void setUtcLastUpdated(Date utcLastUpdated) {
        this.utcLastUpdated = utcLastUpdated;
    }

    protected Map<String, Object> properties;

    protected Map<String, String> attributes;

    protected List<String> tags;

    protected long id;

    protected long revision;

    protected String createdBy = null;

    protected String lastModifiedBy = null;

    protected Date utcDateCreated = null;

    protected Date utcLastUpdated = null;

    private Map<String, Object> propertiesChanged;

    private List<IntegerPropertyIncrement> integerPropertyIncrements;

    private List<IntegerPropertyDecrement> integerPropertyDecrements;

    private List<DecimalPropertyIncrement> decimalPropertyIncrements;

    private List<DecimalPropertyDecrement> decimalPropertyDecrements;

    private List<ItemsCollection> addedItemses;

    private List<ItemsCollection> uniquelyAddedItemses;

    private List<ItemsCollection> removedItemses;

    private Map<String, String> attributesChanged;

    private List<String> tagsAdded;

    private List<String> tagsRemoved;

    public synchronized Map<String, Object> getAllProperties() {
        return this.properties;
    }

    public synchronized Map<String, String> getAllAttributes() {
        return this.attributes;
    }

    public synchronized List<String> getAllTags() {
        return this.tags;
    }

    public synchronized void setStringProperty(String propertyName, String propertyValue) {

        this.properties.put(propertyName, propertyValue);
        this.propertiesChanged.put(propertyName, propertyValue);
    }

    public synchronized void setIntProperty(String propertyName, int propertyValue) {

        this.properties.put(propertyName, propertyValue);
        this.propertiesChanged.put(propertyName, propertyValue);
    }

    public synchronized void setDoubleProperty(String propertyName, double propertyValue) {
        if (Double.isInfinite(propertyValue) || Double.isNaN(propertyValue)) {
            throw new RuntimeException("Invalid double value : " + propertyValue);
        }
        this.properties.put(propertyName, propertyValue);
        this.propertiesChanged.put(propertyName, propertyValue);
    }

    public synchronized void setBoolProperty(String propertyName, boolean propertyValue) {

        this.properties.put(propertyName, propertyValue);
        this.propertiesChanged.put(propertyName, propertyValue);
    }

    public synchronized void setDateProperty(String propertyName, Date propertyValue) {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        this.setStringProperty(propertyName, dateFormat.format(propertyValue));
    }

//    public synchronized void setTimeProperty(String propertyName, Date propertyValue) {
//        DateFormat timeFormat = new SimpleDateFormat("HH:mm:ss.SSSSSSS");
//        this.setStringProperty(propertyName, timeFormat.format(propertyValue));
//    }

    public synchronized void setDateTimeProperty(String propertyName, Date propertyValue) {
        DateFormat dateTimeFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSSS'Z'");
        this.setStringProperty(propertyName, dateTimeFormat.format(propertyValue));
    }

    public synchronized void setGeoProperty(String propertyName, double[] coordinates) {
        this.setStringProperty(propertyName, String.valueOf(coordinates[0]) + "," + coordinates[1]);
    }

    public synchronized String getPropertyAsString(String propertyName) {
        if (this.properties.containsKey(propertyName) == true)
            return String.valueOf(this.properties.get(propertyName));
        return null;
    }

    public synchronized Integer getPropertyAsInt(String propertyName) {
        String intValue = this.getPropertyAsString(propertyName);
        if (intValue == null)
            return null;
        return Integer.parseInt(intValue);
    }

    public synchronized Double getPropertyAsDouble(String propertyName) {
        String doubleValue = this.getPropertyAsString(propertyName);
        if (doubleValue == null)
            return null;
        return Double.parseDouble(doubleValue);
    }

    public synchronized Boolean getPropertyAsBoolean(String propertyName) {
        String boolValue = this.getPropertyAsString(propertyName);
        if (boolValue == null)
            return null;
        return Boolean.parseBoolean(boolValue);
    }

    public synchronized Date getPropertyAsDate(String propertyName) {
        String dateValue = this.getPropertyAsString(propertyName);
        if (dateValue == null)
            return null;

        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        try {
            return dateFormat.parse(dateValue);
        } catch (ParseException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

//    public synchronized Date getPropertyAsTime(String propertyName) {
//        String timeValue = this.getPropertyAsString(propertyName);
//        if (timeValue == null)
//            return null;
//
//        DateFormat timeFormat = new SimpleDateFormat("HH:mm:ss.SSSSSSS");
//        try {
//            return timeFormat.parse(timeValue);
//        } catch (ParseException e) {
//            throw new RuntimeException(e.getMessage());
//        }
//    }

    public synchronized Date getPropertyAsDateTime(String propertyName) {
        String datetimeValue = this.getPropertyAsString(propertyName);
        if (datetimeValue == null)
            return null;

        DateFormat dateTimeFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSSS'Z'");
        try {
            return dateTimeFormat.parse(datetimeValue);
        } catch (ParseException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public synchronized double[] getPropertyAsGeo(String propertyName) {
        String geoValue = this.getPropertyAsString(propertyName);
        if (geoValue == null)
            return null;
        String[] strCoordinates = geoValue.split(",");
        return new double[]{Double.parseDouble(strCoordinates[0]), Double.parseDouble(strCoordinates[1])};
    }

    //  multi valued properties

    public synchronized List<String> getPropertyAsMultiValuedString(String propertyName) {
        List<String> propertyValue = null;
        if (this.properties.containsKey(propertyName) == true) {
            propertyValue = new ArrayList<String>();
            List values = (List) properties.get(propertyName);
            for (Object value : values)
                if (value == null || value.equals(null))
                    propertyValue.add(null);
                else
                    propertyValue.add(value.toString());
        }
        return propertyValue;
    }

    public synchronized List<Integer> getPropertyAsMultiValuedInt(String propertyName) {
        List<Integer> propertyValue = null;
        if (this.properties.containsKey(propertyName) == true) {
            propertyValue = new ArrayList<Integer>();
            List values = (List) properties.get(propertyName);
            for (Object value : values)
                if (value == null || value.equals(null))
                    propertyValue.add(null);
                else
                    propertyValue.add(Integer.valueOf(value.toString()));
        }
        return propertyValue;
    }

    public synchronized List<Double> getPropertyAsMultiValuedDouble(String propertyName) {
        List<Double> propertyValue = null;
        if (this.properties.containsKey(propertyName) == true) {
            propertyValue = new ArrayList<Double>();
            List values = (List) properties.get(propertyName);
            for (Object value : values)
                if (value == null || value.equals(null))
                    propertyValue.add(null);
                else
                    propertyValue.add(Double.valueOf(value.toString()));
        }
        return propertyValue;
    }

    public synchronized void setPropertyAsMultiValued(String propertyName, List<?> items) {
        this.properties.put(propertyName, new ArrayList<Object>(items));
        this.propertiesChanged.put(propertyName, new ArrayList<Object>(items));
    }

    public synchronized <T> void addItemsToMultiValuedProperty(String propertyName, List<?> items) {
//        if (this.properties.containsKey(propertyName)) {
//
//            ((Collection) (this.properties.get(propertyName))).addAll(items);
//        }
        this.addedItemses.add(new ItemsCollection(propertyName, items));
    }

    public synchronized <T> void uniquelyAddItemsToMultiValuedProperty(String propertyName, List<T> items) {
//        if (this.properties.containsKey(propertyName)) {
//
//            ((Collection) (this.properties.get(propertyName))).removeAll(items);
//            ((Collection) (this.properties.get(propertyName))).addAll(items);
//        }
        this.uniquelyAddedItemses.add(new ItemsCollection(propertyName, items));
    }

    public synchronized <T> void removeItemsFromMultiValuedProperty(String propertyName, List<T> items) {
//        if (this.properties.containsKey(propertyName)) {
//
//            ((Collection) (this.properties.get(propertyName))).removeAll(items);
//        }
        this.removedItemses.add(new ItemsCollection(propertyName, items));
    }

    //  counters

    public synchronized void incrementIntegerProperty(String propertyName, int incrementBy) {
//        Integer value = this.getPropertyAsInt(propertyName);
//        if (value != null)
//            this.setIntProperty(propertyName, value + incrementBy);
        this.integerPropertyIncrements.add(new IntegerPropertyIncrement(propertyName, incrementBy));
    }

    public synchronized void decrementIntegerProperty(String propertyName, int decrementBy) {
//        Integer value = this.getPropertyAsInt(propertyName);
//        if (value != null)
//            this.setIntProperty(propertyName, value - decrementBy);
        this.integerPropertyDecrements.add(new IntegerPropertyDecrement(propertyName, decrementBy));
    }

    public synchronized void incrementDecimalProperty(String propertyName, double incrementBy) {
//        Double value = this.getPropertyAsDouble(propertyName);
//        if (value != null)
//            this.setDoubleProperty(propertyName, value + incrementBy);
        this.decimalPropertyIncrements.add(new DecimalPropertyIncrement(propertyName, incrementBy));
    }

    public synchronized void decrementDecimalProperty(String propertyName, double decrementBy) {
//        Double value = this.getPropertyAsDouble(propertyName);
//        if (value != null)
//            this.setDoubleProperty(propertyName, value - decrementBy);
        this.decimalPropertyDecrements.add(new DecimalPropertyDecrement(propertyName, decrementBy));
    }

    public synchronized void removeProperty(String propertyName) {
        this.properties.put(propertyName, null);
        this.propertiesChanged.put(propertyName, null);
    }


    public synchronized void setAttribute(String attributeName, String attributeValue) {
        if (attributeName == null)
            throw new IllegalArgumentException("Attribute name cannot be null.");
        this.attributes.put(attributeName, attributeValue);
        this.attributesChanged.put(attributeName, attributeValue);
    }

    public synchronized String getAttribute(String attributeName) {
        return this.attributes.get(attributeName);
    }

    public synchronized void removeAttribute(String attributeName) {
        this.attributes.remove(attributeName);
        this.attributesChanged.put(attributeName, null);
    }

    public synchronized void addTag(String tag) {
        if (tag == null)
            throw new IllegalArgumentException("Tag cannot be null.");

        if (this.tags.contains(tag) == false) {
            this.tags.add(tag);
        }
        this.tagsAdded.add(tag);
    }

    public synchronized void removeTag(String tag) {
        this.tags.remove(tag);
        this.tagsRemoved.add(tag);
    }

    public synchronized void addTags(List<String> tags) {
        for (String tag : tags) {
            if (this.tags.contains(tag) == false) {
                this.tags.add(tag);
                this.tagsAdded.add(tag);
            }
        }
    }

    public synchronized void removeTags(List<String> tags) {
        for (String tag : tags) {
            this.tags.remove(tag);
            this.tagsRemoved.add(tag);
        }
    }

    public synchronized boolean tagExists(String tag) {
        return this.tags.contains(tag);
    }

    public synchronized APJSONObject getUpdateCommand() throws APJSONException {

        APJSONObject updateCommand = new APJSONObject();
        if(this.id > 0)
            updateCommand.put(SystemDefinedPropertiesHelper.id, String.valueOf(this.id));
        if (this.tagsAdded != null && this.tagsAdded.size() > 0)
            updateCommand.put("__addtags", new APJSONArray(this.tagsAdded));

        if (this.tagsRemoved != null && this.tagsRemoved.size() > 0)
            updateCommand.put("__removetags", new APJSONArray(this.tagsRemoved));

        if (this.attributesChanged.size() > 0) {
            APJSONObject attributesChangedJsonObject = new APJSONObject();
            for (Map.Entry<String, String> attribute : this.attributesChanged.entrySet()) {
                if (attribute.getValue() == (null))
                    attributesChangedJsonObject.put(attribute.getKey(), APJSONObject.NULL);
                else
                    attributesChangedJsonObject.put(attribute.getKey(), attribute.getValue());

            }
            updateCommand.put(SystemDefinedPropertiesHelper.attributes, attributesChangedJsonObject);
        }


        for (Map.Entry<String, Object> property : this.propertiesChanged.entrySet()) {
            if (property.getValue() == null || property.getValue().equals(null))
                updateCommand.put(property.getKey(), APJSONObject.NULL);
            else if (property.getValue() instanceof List)
                updateCommand.put(property.getKey(), new APJSONArray((List) property.getValue()));
            else
                updateCommand.put(property.getKey(), property.getValue());
        }

        //   counters
        for (final IntegerPropertyIncrement incr : this.integerPropertyIncrements) {
            updateCommand.put(incr.propertyName, new APJSONObject(new HashMap() {{
                put("incrementby", incr.increment);
            }}));
        }
        for (final IntegerPropertyDecrement decr : this.integerPropertyDecrements) {
            updateCommand.put(decr.propertyName, new APJSONObject(new HashMap() {{
                put("decrementby", decr.decrement);
            }}));
        }

        for (final DecimalPropertyIncrement incr : this.decimalPropertyIncrements) {
            updateCommand.put(incr.propertyName, new APJSONObject(new HashMap() {{
                put("incrementby", incr.increment);
            }}));
        }
        for (final DecimalPropertyDecrement decr : this.decimalPropertyDecrements) {
            updateCommand.put(decr.propertyName, new APJSONObject(new HashMap() {{
                put("decrementby", decr.decrement);
            }}));
        }

        //  multivalued updates
        for (final ItemsCollection itemsCollection : this.addedItemses) {
            updateCommand.put(itemsCollection.propertyName, new APJSONObject(new HashMap<String, Object>() {{
                put("additems", new APJSONArray(itemsCollection.addedItems));
            }}));
        }
        for (final ItemsCollection itemsCollection : this.uniquelyAddedItemses) {
            updateCommand.put(itemsCollection.propertyName, new APJSONObject(new HashMap<String, Object>() {{
                put("adduniqueitems", new APJSONArray(itemsCollection.addedItems));
            }}));
        }
        for (final ItemsCollection itemsCollection : this.removedItemses) {
            updateCommand.put(itemsCollection.propertyName, new APJSONObject(new HashMap<String, Object>() {{
                put("removeitems", new APJSONArray(itemsCollection.addedItems));
            }}));
        }

        return updateCommand;
    }

    protected synchronized void resetUpdateCommands() {
        this.propertiesChanged = new HashMap<String, Object>();
        this.attributesChanged = new HashMap<String, String>();
        this.tagsAdded = new ArrayList<String>();
        this.tagsRemoved = new ArrayList<String>();

        this.integerPropertyIncrements = new ArrayList<IntegerPropertyIncrement>();
        this.integerPropertyDecrements = new ArrayList<IntegerPropertyDecrement>();
        this.decimalPropertyIncrements = new ArrayList<DecimalPropertyIncrement>();
        this.decimalPropertyDecrements = new ArrayList<DecimalPropertyDecrement>();

        this.addedItemses = new ArrayList<ItemsCollection>();
        this.uniquelyAddedItemses = new ArrayList<ItemsCollection>();
        this.removedItemses = new ArrayList<ItemsCollection>();
    }

    public synchronized long getId() {
        return id;
    }

    public synchronized long getRevision() {
        return revision;
    }

    public synchronized String getCreatedBy() {
        return createdBy;
    }

    public synchronized String getLastModifiedBy() {
        return lastModifiedBy;
    }

    public synchronized Date getUtcDateCreated() {
        return utcDateCreated;
    }

    public synchronized Date getUtcLastUpdated() {
        return utcLastUpdated;
    }

    private class IntegerPropertyIncrement {
        public IntegerPropertyIncrement(String propertyName, int incrementBy) {
            this.increment = incrementBy;
            this.propertyName = propertyName;
        }

        public String propertyName;

        public int increment;
    }

    private class IntegerPropertyDecrement {
        public IntegerPropertyDecrement(String propertyName, int decrementBy) {
            this.decrement = decrementBy;
            this.propertyName = propertyName;
        }

        public String propertyName;

        public int decrement;
    }

    private class DecimalPropertyIncrement {
        public DecimalPropertyIncrement(String propertyName, double incrementBy) {
            this.increment = incrementBy;
            this.propertyName = propertyName;
        }

        public String propertyName;

        public double increment;
    }

    private class DecimalPropertyDecrement {
        public DecimalPropertyDecrement(String propertyName, double decrementBy) {
            this.decrement = decrementBy;
            this.propertyName = propertyName;
        }

        public String propertyName;

        public double decrement;
    }

    private class ItemsCollection<T> {
        public ItemsCollection(String propertyName, List<T> addedItems) {
            this.propertyName = propertyName;
            this.addedItems = addedItems;
        }

        public String propertyName;
        public List<T> addedItems;
    }
}

