/**
 * Created by sathley.
 */
package com.appacitive.sdk;

import com.appacitive.sdk.infra.APSerializable;
import com.appacitive.sdk.infra.SystemDefinedProperties;
import com.sun.istack.internal.Nullable;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public abstract class AppacitiveEntity implements Serializable, APSerializable {

    private static final DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    private static final DateFormat timeFormat = new SimpleDateFormat("HH:mm:ss.SSSSSSS");

    private static final DateFormat dateTimeFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSSS'Z'");

//    public AppacitiveEntity(Map<String, Object> entity) {
//        this.setSelf(entity);
//    }

    public void setSelf(Map<String, Object> entity) {
        if (entity != null) {
            //  Wipe out previous data
            this.properties = new HashMap<String, Object>();
            this.attributes = new HashMap<String, String>();
            this.tags = new ArrayList<String>();

            //  Read in new data

            Object object = entity.get(SystemDefinedProperties.id);
            if (object != null)
                this.id = Long.parseLong(object.toString());

            object = entity.get(SystemDefinedProperties.revision);
            if (object != null)
                this.revision = Long.parseLong(object.toString());

            object = entity.get(SystemDefinedProperties.createdBy);
            if (object != null)
                this.createdBy = object.toString();

            object = entity.get(SystemDefinedProperties.lastModifiedBy);
            if (object != null)
                this.lastModifiedBy = object.toString();

            final DateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSSS'Z'");

            try {
                object = entity.get(SystemDefinedProperties.utcDateCreated);
                if (object != null)
                    this.utcDateCreated = format.parse(object.toString());

                object = entity.get(SystemDefinedProperties.utcLastUpdatedDate);
                if (object != null)
                    this.utcLastUpdated = format.parse(object.toString());

            } catch (ParseException e) {
                // Log
            }

            object = entity.get(SystemDefinedProperties.tags);
            if (object != null)
                this.tags = (List<String>) object;

            object = entity.get(SystemDefinedProperties.attributes);
            if (object != null)
                this.attributes = (Map<String, String>) object;

            for (Map.Entry<String, Object> property : entity.entrySet()) {
                if (SystemDefinedProperties.ConnectionSystemProperties.contains(property.getKey()) == false && SystemDefinedProperties.ObjectSystemProperties.contains(property.getKey()) == false) {

                    if (property.getValue().getClass().getCanonicalName().equals(this.tags.getClass().getCanonicalName()))
                        this.properties.put(property.getKey(), property.getValue());
                    else
                        this.properties.put(property.getKey(), property.getValue());
                }
            }
        }
    }

    public Map<String, Object> getMap() {
        Map<String, Object> nativeMap = new HashMap<String, Object>();

        nativeMap.put(SystemDefinedProperties.id, String.valueOf(this.id));
        nativeMap.put(SystemDefinedProperties.revision, String.valueOf(this.revision));
        nativeMap.put(SystemDefinedProperties.createdBy, this.createdBy);
        nativeMap.put(SystemDefinedProperties.lastModifiedBy, this.lastModifiedBy);
        nativeMap.put(SystemDefinedProperties.utcDateCreated, this.utcDateCreated);
        nativeMap.put(SystemDefinedProperties.utcLastUpdatedDate, this.utcLastUpdated);
        if (this.tags != null && tags.size() != 0)
            nativeMap.put(SystemDefinedProperties.tags, this.tags);

        if (this.attributes != null && this.attributes.size() > 0)
            nativeMap.put(SystemDefinedProperties.attributes, this.attributes);

        if (this.properties != null && this.properties.size() > 0)
            for (Map.Entry<String, Object> property : this.properties.entrySet()) {
                if (property.getValue() instanceof ArrayList)
                    nativeMap.put(property.getKey(), property.getValue());
                else if (property.getValue() == null)
                    nativeMap.put(property.getKey(), (property.getValue()));
                else
                    nativeMap.put(property.getKey(), String.valueOf(property.getValue()));
            }

        return nativeMap;
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

    private Map<String, Object> properties = new HashMap<String, Object>();

    private Map<String, String> attributes = new HashMap<String, String>();

    private List<String> tags = new ArrayList<String>();

    private long id = 0;

    private long revision = 0;

    private String createdBy = null;

    private String lastModifiedBy = null;

    private Date utcDateCreated = null;

    private Date utcLastUpdated = null;

    private Map<String, Object> propertiesChanged = new HashMap<String, Object>();

    private Map<String, String> attributesChanged = new HashMap<String, String>();

    private List<String> tagsAdded = new ArrayList<String>();

    private List<String> tagsRemoved = new ArrayList<String>();

    public Map<String, Object> getAllProperties() {
        return this.properties;
    }

    public Map<String, String> getAllAttributes() {
        return this.attributes;
    }

    public List<String> getAllTags() {
        return this.tags;
    }

//    public void setProperty(String propertyName, Object propertyValue) {
//
//        this.properties.put(propertyName, propertyValue);
//        this.propertiesChanged.put(propertyName, propertyValue);
//    }

    public void setStringProperty(String propertyName, @Nullable String propertyValue) {

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
        if (this.properties.containsKey(propertyName) == true)
            return (List<String>) (this.properties.get(propertyName));
        return null;
    }

    public void setPropertyAsMultiValuedInt(String propertyName, List<Integer> propertyValue) {
        this.properties.put(propertyName, propertyValue);
        this.propertiesChanged.put(propertyName, propertyValue);
    }

    public List<Integer> getPropertyAsMultiValuedInt(String propertyName) {
        if (this.properties.containsKey(propertyName) == true)
            return (List<Integer>) (this.properties.get(propertyName));
        return null;
    }

    public void setPropertyAsMultiValuedDouble(String propertyName, List<Double> propertyValue) {
        this.properties.put(propertyName, propertyValue);
        this.propertiesChanged.put(propertyName, propertyValue);
    }

    public List<Double> getPropertyAsMultiValuedDouble(String propertyName) {
        if (this.properties.containsKey(propertyName) == true)
            return (List<Double>) (this.properties.get(propertyName));
        return null;
    }

    public void setAttribute(String attributeName, String attributeValue) {
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

    protected Map<String, Object> getUpdateCommand() {
        Map<String, Object> updateCommand = new HashMap<String, Object>();
        updateCommand.put("__addtags", this.tagsAdded);
        updateCommand.put("__removetags", this.tagsRemoved);
        updateCommand.put("__attributes", this.attributesChanged);
        for (Map.Entry<String, Object> property : this.propertiesChanged.entrySet()) {
            if (property.getValue() instanceof ArrayList)
                updateCommand.put(property.getKey(), property.getValue());
            else if (property.getValue() == null)
                updateCommand.put(property.getKey(), (property.getValue()));
            else
                updateCommand.put(property.getKey(), String.valueOf(property.getValue()));
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
