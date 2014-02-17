/**
 * Created by sathley.
 */
package com.appacitive.sdk.core;

import com.appacitive.sdk.core.infra.APSerializable;
import com.appacitive.sdk.core.infra.SystemDefinedProperties;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public abstract class AppacitiveEntity  implements Serializable, APSerializable {

    public AppacitiveEntity(Map<String, Object> entity) {
        this.setSelf(entity);
    }

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

    public AppacitiveEntity() {

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

    private Map<String, Object> properties = new HashMap<String, Object>();

    private Map<String, String> attributes = new HashMap<String, String>();

    private List<String> tags = new ArrayList<String>();

    public long id = 0;

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

    public void setProperty(String propertyName, Object propertyValue) {

        this.properties.put(propertyName, propertyValue);
        this.propertiesChanged.put(propertyName, propertyValue);
    }

    public String getProperty(String propertyName) {
        if(this.properties.containsKey(propertyName) && this.properties.get(propertyName) != null)
            return String.valueOf(this.properties.get(propertyName));
        return null;
    }

    public int getPropertyAsInt(String propertyName) {
        return Integer.parseInt(this.getProperty(propertyName));
    }

    public double getPropertyAsDouble(String propertyName) {
        return Double.parseDouble(this.getProperty(propertyName));
    }

    public boolean getPropertyAsBoolean(String propertyName) {
        return Boolean.parseBoolean(this.getProperty(propertyName));
    }

    public Date getPropertyAsDate(String propertyName) throws ParseException {
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        return format.parse(this.getProperty(propertyName));
    }

    public Date getPropertyAsTime(String propertyName) throws ParseException {
        DateFormat format = new SimpleDateFormat("HH:mm:ss.SSSSSSS");
        return format.parse(this.getProperty(propertyName));
    }

    public Date getPropertyAsDateTime(String propertyName) throws ParseException {
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSSS'Z'");
        return format.parse(this.getProperty(propertyName));
    }

    public double[] getPropertyAsGeo(String propertyName) {
        String[] strCoordinates = this.getProperty(propertyName).split(",");
        return new double[]{Double.parseDouble(strCoordinates[0]), Double.parseDouble(strCoordinates[1])};
    }

    public void setPropertyAsGeo(String propertyName, double[] coordinates)
    {
        StringBuilder sb = new StringBuilder();
        sb.append(coordinates[0]).append(",").append(coordinates[1]);
        this.properties.put(propertyName, sb.toString());
    }

    public List<String> getPropertyAsMultiValued(String propertyName) {
        if(this.properties.containsKey(propertyName) && this.properties.get(propertyName) != null)
            return (ArrayList<String>) (this.properties.get(propertyName));
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
