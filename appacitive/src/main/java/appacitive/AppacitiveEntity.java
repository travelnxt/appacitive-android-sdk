/**
 * Created by sathley.
 */
package appacitive;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public abstract class AppacitiveEntity {
    public final static List<String> ConnectionSystemProperties = Arrays.asList("__relationtype", "__relationid", "__id", "__createdby", "__lastmodifiedby",
            "__utcdatecreated", "__utclastupdateddate", "__tags", "__attributes", "__properties",
            "__revision", "__endpointa", "__endpointb");

    public final static List<String> ObjectSystemProperties = Arrays.asList("__type", "__typeid", "__id", "__createdby", "__lastmodifiedby",
            "__utcdatecreated", "__utclastupdateddate", "__tags", "__attributes", "__properties",
            "__revision");

    public AppacitiveEntity(Map<String, Object> entity)
    {
        if(entity != null)
        {
            this.id = Long.parseLong(entity.get("__id").toString());
            this.revision = Long.parseLong(entity.get("__revision").toString());
            this.createdBy = entity.get("__createdby").toString();
            this.lastModifiedBy = entity.get("__lastmodifiedby").toString();
            DateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSSS'Z'");
            try{
                this.utcDateCreated = format.parse(entity.get("__utcdatecreated").toString());
            }
            catch (ParseException e)
            {

            }
            try{
                this.ucLastUpdated = format.parse(entity.get("__utclastupdateddate").toString());
            }
            catch (ParseException e)
            {

            }
            this.tags = (List<String>)(entity.get("__tags"));
            this.attributes = (Map<String, String>) (entity.get("__attributes"));

            for (Map.Entry<String, Object> property : entity.entrySet())
            {
                if (ConnectionSystemProperties.contains(property.getKey()) == false && ObjectSystemProperties.contains(property.getKey()) == false)
                {

                    if(property.getValue().getClass().getCanonicalName() == this.tags.getClass().getCanonicalName())
                        this.properties.put(property.getKey(), property.getValue());
                    else
                        this.properties.put(property.getKey(), (String)property.getValue());
                }
            }
        }
    }

    public AppacitiveEntity()
    {

    }

    protected Map<String, Object> getMap()
    {
        Map<String, Object> nativeMap = new HashMap<String, Object>();

        nativeMap.put("__id", this.id);
        nativeMap.put("__revision", this.revision);
        nativeMap.put("__createdby", this.createdBy);
        nativeMap.put("__lastmodifiedby", this.lastModifiedBy);
        nativeMap.put("__utcdatecreated", this.utcDateCreated);
        nativeMap.put("__utclastupdateddate", this.ucLastUpdated);
        nativeMap.put("__tags", this.tags);
        nativeMap.put("__attributes", this.attributes);
        for (Map.Entry<String, Object> property : this.properties.entrySet())
        {
            nativeMap.put(property.getKey(), property.getValue());
        }

        return nativeMap;
    }

    private Map<String, Object> properties = new HashMap<String, Object>();

    private Map<String, String> attributes = new HashMap<String, String>();

    private List<String> tags = new ArrayList<String>();

    private long id = 0;

    private long revision = 0;

    private String createdBy = null;

    private String lastModifiedBy = null;

    private Date utcDateCreated = null;

    private Date ucLastUpdated = null;

    private Map<String, String> propertiesChanged = new HashMap<String, String>();

    private Map<String, String> attributesChanged = new HashMap<String, String>();

    private List<String> tagsAdded = new ArrayList<String>();

    private List<String> tagsRemoved = new ArrayList<String>();

    public Map<String, Object> getAllProperties()
    {
        return this.properties;
    }

    public Map<String, String> getAllAttributes()
    {
        return this.attributes;
    }

    public List<String> getAllTags()
    {
        return this.tags;
    }

    public void setProperty(String propertyName, Object propertyValue)
    {
        this.properties.put(propertyName, propertyValue);
        this.propertiesChanged.put(propertyName, propertyValue.toString());
    }

    public Object getProperty(String propertyName)
    {
        return this.properties.get(propertyName);
    }

    public void setAttribute(String attributeName, String attributeValue)
    {
        this.attributes.put(attributeName, attributeValue);
        this.attributesChanged.put(attributeName, attributeValue);
    }

    public String getAttribute(String attributeName)
    {
        return this.attributes.get(attributeName);
    }

    public void removeAttribute(String attributeName)
    {
        this.attributes.remove(attributeName);
        this.attributesChanged.put(attributeName, null);
    }

    public void addTag(String tag)
    {
        if(this.tags.contains(tag) == false)
        {
            this.tags.add(tag);
            this.tagsAdded.add(tag);
        }
    }

    public void removeTag(String tag)
    {
        if(this.tags.contains(tag))
        {
            this.tags.remove(tag);
            this.tagsRemoved.add(tag);
        }
    }

    public void addTags(List<String> tags)
    {
        for (String tag : tags )
        {
            if(this.tags.contains(tag) == false)
            {
                this.tags.add(tag);
                this.tagsAdded.add(tag);
            }
        }
    }

    public void removeTags(List<String> tags)
    {
        for (String tag : tags )
        {
            if(this.tags.contains(tag))
            {
                this.tags.remove(tag);
                this.tagsRemoved.add(tag);
            }
        }
    }

    public boolean tagExists(String tag)
    {
        return this.tags.contains(tag);
    }

    protected Map<String, Object> getUpdateCommand()
    {
        Map<String, Object> updateCommand = new HashMap<String, Object>();
        updateCommand.put("__addtags", this.tagsAdded);
        updateCommand.put("__removetags", this.tagsRemoved);
        updateCommand.put("__attributes", this.attributesChanged);
        for (Map.Entry<String, String> property : this.propertiesChanged.entrySet())
        {
            updateCommand.put(property.getKey(), property.getValue());
        }
        return updateCommand;
    }

    protected void resetUpdateCommands()
    {
        this.propertiesChanged = new HashMap<String, String>();
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

    public Date getUcLastUpdated() {
        return ucLastUpdated;
    }
}
