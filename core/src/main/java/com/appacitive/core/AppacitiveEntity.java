/**
 * Created by sathley.
 */
package com.appacitive.core;

import com.appacitive.core.apjson.APJSONArray;
import com.appacitive.core.apjson.APJSONException;
import com.appacitive.core.apjson.APJSONObject;
import com.appacitive.core.infra.APSerializable;
import com.appacitive.core.infra.SystemDefinedProperties;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public abstract class AppacitiveEntity implements Serializable, APSerializable {

    private static final DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    private static final DateFormat timeFormat = new SimpleDateFormat("HH:mm:ss.SSSSSSS");

    private static final DateFormat dateTimeFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSSS'Z'");

    protected AppacitiveEntity() {
        properties = new HashMap<String, Object>();
        attributes = new HashMap<String, String>();
        tags = new ArrayList<String>();
        id = 0;
        revision = 0;
        propertiesChanged = new HashMap<String, Object>();
        attributesChanged = new HashMap<String, String>();
        tagsAdded = new ArrayList<String>();
        tagsRemoved = new ArrayList<String>();
    }

    public void setSelf(APJSONObject entity) {
        if (entity != null) {
            //  Wipe out previous data
            this.properties = new HashMap<String, Object>();
            this.attributes = new HashMap<String, String>();
            this.tags = new ArrayList<String>();

            this.resetUpdateCommands();

            //  Read in new data

            this.id = Long.parseLong(entity.optString(SystemDefinedProperties.id, "0"));

            this.revision = Long.parseLong(entity.optString(SystemDefinedProperties.revision, "0"));

            this.createdBy = entity.optString(SystemDefinedProperties.createdBy, null);

            this.lastModifiedBy = entity.optString(SystemDefinedProperties.lastModifiedBy, null);

            final DateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSSS'Z'");

            try {
                this.utcDateCreated = format.parse(entity.optString(SystemDefinedProperties.utcDateCreated, ""));
            } catch (ParseException e) {
                this.utcDateCreated = null;
            }
            try {
                this.utcLastUpdated = format.parse(entity.optString(SystemDefinedProperties.utcLastUpdatedDate, ""));
            } catch (ParseException e) {
                this.utcLastUpdated = null;
            }
            if (entity.isNull(SystemDefinedProperties.tags) == false) {
                APJSONArray tagsArray = entity.optJSONArray(SystemDefinedProperties.tags);
                for (int i = 0; i < tagsArray.length(); i++) {
                    this.tags.add(tagsArray.optString(i));
                }
            }
            if (entity.isNull(SystemDefinedProperties.attributes) == false) {
                APJSONObject attributesObject = entity.optJSONObject(SystemDefinedProperties.attributes);
                Iterator<String> iterator = attributesObject.keys();
                while (iterator.hasNext()) {
                    String key = iterator.next();
                    this.attributes.put(key, attributesObject.optString(key));
                }

            }
            Iterator<String> iterator = entity.keys();
            while (iterator.hasNext()) {
                String key = iterator.next();
                if (SystemDefinedProperties.ConnectionSystemProperties.contains(key) == false && SystemDefinedProperties.ObjectSystemProperties.contains(key) == false) {
                    {
                        Object propertyValue = entity.opt(key);
                        this.properties.put(key, propertyValue);
                    }
                }
            }
        }
    }

    public APJSONObject getMap() throws APJSONException {
        APJSONObject jsonObject = new APJSONObject();

        jsonObject.put(SystemDefinedProperties.id, String.valueOf(this.id));
        jsonObject.put(SystemDefinedProperties.revision, String.valueOf(this.revision));
        if (this.createdBy != null && this.createdBy.isEmpty() == false)
            jsonObject.put(SystemDefinedProperties.createdBy, this.createdBy);
        if (this.lastModifiedBy != null && this.lastModifiedBy.isEmpty() == false)
            jsonObject.put(SystemDefinedProperties.lastModifiedBy, this.lastModifiedBy);
        if (this.utcDateCreated != null)
            jsonObject.put(SystemDefinedProperties.utcDateCreated, this.utcDateCreated);
        if (this.utcLastUpdated != null)
            jsonObject.put(SystemDefinedProperties.utcLastUpdatedDate, this.utcLastUpdated);
        if (this.tags != null && tags.size() != 0)
            jsonObject.put(SystemDefinedProperties.tags, new APJSONArray(this.tags));

        if (this.attributes != null && this.attributes.size() > 0)
            jsonObject.put(SystemDefinedProperties.attributes, new APJSONObject(this.attributes));

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

    public void setId(long id) {
        this.id = id;
    }

    public void setRevision(long revision) {
        this.revision = revision;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public void setLastModifiedBy(String lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    public void setUtcDateCreated(Date utcDateCreated) {
        this.utcDateCreated = utcDateCreated;
    }

    public void setUtcLastUpdated(Date utcLastUpdated) {
        this.utcLastUpdated = utcLastUpdated;
    }

    private Map<String, Object> properties;

    private Map<String, String> attributes;

    private List<String> tags;

    private long id;

    private long revision;

    private String createdBy = null;

    private String lastModifiedBy = null;

    private Date utcDateCreated = null;

    private Date utcLastUpdated = null;

    private Map<String, Object> propertiesChanged;

    private Map<String, String> attributesChanged;

    private List<String> tagsAdded;

    private List<String> tagsRemoved;

    public Map<String, Object> getAllProperties() {
        return this.properties;
    }

    public Map<String, String> getAllAttributes() {
        return this.attributes;
    }

    public List<String> getAllTags() {
        return this.tags;
    }

    public void setStringProperty(String propertyName, String propertyValue) {

        this.properties.put(propertyName, propertyValue);
        this.propertiesChanged.put(propertyName, propertyValue);
    }

    public void setIntProperty(String propertyName, int propertyValue) {

        this.properties.put(propertyName, propertyValue);
        this.propertiesChanged.put(propertyName, propertyValue);
    }

    public void setDoubleProperty(String propertyName, double propertyValue) {

        this.properties.put(propertyName, propertyValue);
        this.propertiesChanged.put(propertyName, propertyValue);
    }

    public void setBoolProperty(String propertyName, boolean propertyValue) {

        this.properties.put(propertyName, propertyValue);
        this.propertiesChanged.put(propertyName, propertyValue);
    }

    public void setDateProperty(String propertyName, Date propertyValue) {

        this.setStringProperty(propertyName, dateFormat.format(propertyValue));
    }

    public void setTimeProperty(String propertyName, Date propertyValue) {

        this.setStringProperty(propertyName, timeFormat.format(propertyValue));
    }

    public void setDateTimeProperty(String propertyName, Date propertyValue) {

        this.setStringProperty(propertyName, dateTimeFormat.format(propertyValue));
    }

    public void setGeoProperty(String propertyName, double[] coordinates) {
        this.setStringProperty(propertyName, String.valueOf(coordinates[0]) + "," + coordinates[1]);
    }

    public String getPropertyAsString(String propertyName) {
        if (this.properties.containsKey(propertyName) == true)
            return String.valueOf(this.properties.get(propertyName));
        return null;
    }

    public int getPropertyAsInt(String propertyName) {
        return Integer.parseInt(this.getPropertyAsString(propertyName));
    }

    public Double getPropertyAsDouble(String propertyName) {
        return Double.parseDouble(this.getPropertyAsString(propertyName));
    }

    public Boolean getPropertyAsBoolean(String propertyName) {
        return Boolean.parseBoolean(this.getPropertyAsString(propertyName));
    }

    public Date getPropertyAsDate(String propertyName) throws ParseException {
        return dateFormat.parse(this.getPropertyAsString(propertyName));
    }

    public Date getPropertyAsTime(String propertyName) throws ParseException {
        return timeFormat.parse(this.getPropertyAsString(propertyName));
    }

    public Date getPropertyAsDateTime(String propertyName) throws ParseException {
        return dateTimeFormat.parse(this.getPropertyAsString(propertyName));
    }

    public double[] getPropertyAsGeo(String propertyName) {
        String[] strCoordinates = this.getPropertyAsString(propertyName).split(",");
        return new double[]{Double.parseDouble(strCoordinates[0]), Double.parseDouble(strCoordinates[1])};
    }

    public void setPropertyAsMultiValuedString(String propertyName, List<String> propertyValue) {
        this.properties.put(propertyName, propertyValue);
        this.propertiesChanged.put(propertyName, propertyValue);
    }

    public List<String> getPropertyAsMultiValuedString(String propertyName) {
        List<String> propertyValue = null;
        if (this.properties.containsKey(propertyName) == true) {
            propertyValue = new ArrayList<String>();
            APJSONArray values = (APJSONArray) properties.get(propertyName);
            for (int i = 0; i < values.length(); i++)
                propertyValue.add(values.optString(i));
        }
        return propertyValue;
    }

    public void setPropertyAsMultiValuedInt(String propertyName, List<Integer> propertyValue) {
        this.properties.put(propertyName, propertyValue);
        this.propertiesChanged.put(propertyName, propertyValue);
    }

    public List<Integer> getPropertyAsMultiValuedInt(String propertyName) {
        List<Integer> propertyValue = null;
        if (this.properties.containsKey(propertyName) == true) {
            propertyValue = new ArrayList<Integer>();
            APJSONArray values = (APJSONArray) properties.get(propertyName);
            for (int i = 0; i < values.length(); i++)
                propertyValue.add(values.optInt(i));
        }
        return propertyValue;
    }

    public void setPropertyAsMultiValuedDouble(String propertyName, List<Double> propertyValue) {
        this.properties.put(propertyName, propertyValue);
        this.propertiesChanged.put(propertyName, propertyValue);
    }

    public List<Double> getPropertyAsMultiValuedDouble(String propertyName) {
        List<Double> propertyValue = null;
        if (this.properties.containsKey(propertyName) == true) {
            propertyValue = new ArrayList<Double>();
            APJSONArray values = (APJSONArray) properties.get(propertyName);
            for (int i = 0; i < values.length(); i++)
                propertyValue.add(values.optDouble(i));
        }
        return propertyValue;
    }

    public <T> List<T> getPropertyAsMultivalued(String propertyName) {
        List<T> propertyValue = null;
        if (this.properties.containsKey(propertyName) == true) {
            propertyValue = new ArrayList<T>();
            APJSONArray values = (APJSONArray) properties.get(propertyName);
            for (int i = 0; i < values.length(); i++)
                propertyValue.add((T) (values.opt(i)));
        }
        return propertyValue;
    }

    public void setAttribute(String attributeName, String attributeValue) {
        if (attributeName == null)
            throw new IllegalArgumentException("Attribute name cannot be null.");
        this.attributes.put(attributeName, attributeValue);
        this.attributesChanged.put(attributeName, attributeValue);
    }

    public String getAttribute(String attributeName) {
        return this.attributes.get(attributeName);
    }

    public void removeAttribute(String attributeName) {
        this.attributes.remove(attributeName);
        this.attributesChanged.put(attributeName, null);
    }

    public void addTag(String tag) {
        if (tag == null)
            throw new IllegalArgumentException("Tag cannot be null.");

        if (this.tags.contains(tag) == false) {
            this.tags.add(tag);
        }
        this.tagsAdded.add(tag);
    }

    public void removeTag(String tag) {
        this.tags.remove(tag);
        this.tagsRemoved.add(tag);
    }

    public void addTags(List<String> tags) {
        for (String tag : tags) {
            if (this.tags.contains(tag) == false) {
                this.tags.add(tag);
                this.tagsAdded.add(tag);
            }
        }
    }

    public void removeTags(List<String> tags) {
        for (String tag : tags) {
            this.tags.remove(tag);
            this.tagsRemoved.add(tag);
        }
    }

    public boolean tagExists(String tag) {
        return this.tags.contains(tag);
    }

    protected APJSONObject getUpdateCommand() throws APJSONException {

        APJSONObject updateCommand = new APJSONObject();
        updateCommand.put("__addtags", new APJSONArray(this.tagsAdded));
        updateCommand.put("__removetags", new APJSONArray(this.tagsRemoved));
        APJSONObject attributesChangedJsonObject = new APJSONObject();
        if (this.attributesChanged.size() > 0) {
            for (Map.Entry<String, String> attribute : this.attributesChanged.entrySet()) {
                if (attribute.getValue() == (null))
                    attributesChangedJsonObject.put(attribute.getKey(), APJSONObject.NULL);
                else
                    attributesChangedJsonObject.put(attribute.getKey(), attribute.getValue());

            }
        }
        updateCommand.put("__attributes", attributesChangedJsonObject);

        for (Map.Entry<String, Object> property : this.propertiesChanged.entrySet()) {
            if (property.getValue().equals(null))
                updateCommand.put(property.getKey(), APJSONObject.NULL);
            else if (property.getValue() instanceof List)
                updateCommand.put(property.getKey(), new APJSONArray((List) property.getValue()));
            else
                updateCommand.put(property.getKey(), property.getValue());
        }
        return updateCommand;
    }

    protected void resetUpdateCommands() {
        this.propertiesChanged = new HashMap<String, Object>();
        this.attributesChanged = new HashMap<String, String>();
        this.tagsAdded = new ArrayList<String>();
        this.tagsRemoved = new ArrayList<String>();
    }

    public long getId() {
        return id;
    }

    public long getRevision() {
        return revision;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public String getLastModifiedBy() {
        return lastModifiedBy;
    }

    public Date getUtcDateCreated() {
        return utcDateCreated;
    }

    public Date getUtcLastUpdated() {
        return utcLastUpdated;
    }
}
