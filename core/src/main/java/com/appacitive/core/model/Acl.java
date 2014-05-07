package com.appacitive.core.model;

import com.appacitive.core.apjson.APJSONArray;
import com.appacitive.core.apjson.APJSONException;
import com.appacitive.core.apjson.APJSONObject;
import com.appacitive.core.infra.StringUtils;

import java.util.*;

/**
 * Created by sathley on 5/7/2014.
 */
public class Acl {


    Map<String, Map<Permission, EnumSet<Access>>> userPermissions = new HashMap<String, Map<Permission, EnumSet<Access>>>();

    Map<String, Map<Permission, EnumSet<Access>>> usergroupPermissions = new HashMap<String, Map<Permission, EnumSet<Access>>>();

    public synchronized APJSONArray getMap() throws APJSONException
    {
        APJSONArray acls = new APJSONArray();
        for (Map.Entry<String, Map<Permission, EnumSet<Access>>> entry : userPermissions.entrySet())
        {
            APJSONObject entryMap = new APJSONObject();
            entryMap.put("sid", entry.getKey());
            entryMap.put("type", "user");
            Map<Permission, EnumSet<Access>> permissionSet = entry.getValue();
            for(Map.Entry<Permission, EnumSet<Access>> permission : permissionSet.entrySet())
            {
                EnumSet<Access> accesses = permission.getValue();
                List<String> accessesAsString = new ArrayList<String>();
                for(Access access:accesses)
                {
                    accessesAsString.add(access.name());
                }
                entryMap.put(permission.getKey().name(), new APJSONArray(accessesAsString));
            }
            acls.put(entryMap);
        }

        for (Map.Entry<String, Map<Permission, EnumSet<Access>>> entry : usergroupPermissions.entrySet())
        {
            APJSONObject entryMap = new APJSONObject();
            entryMap.put("sid", entry.getKey());
            entryMap.put("type", "usergroup");
            Map<Permission, EnumSet<Access>> permissionSet = entry.getValue();
            for(Map.Entry<Permission, EnumSet<Access>> permission : permissionSet.entrySet())
            {
                EnumSet<Access> accesses = permission.getValue();
                List<String> accessesAsString = new ArrayList<String>();
                for(Access access:accesses)
                {
                    accessesAsString.add(access.name());
                }
                entryMap.put(permission.getKey().name(), new APJSONArray(accessesAsString));
            }
            acls.put(entryMap);
        }
        return acls;
    }


    synchronized void HandleUserPermissionChanges(String userKey, Permission permission, Access access)
    {
        Map<Permission, EnumSet<Access>> permissionSet = userPermissions.get(userKey);
        if(permissionSet == null)
        {
            permissionSet = new HashMap<Permission, EnumSet<Access>>();
            permissionSet.put(permission, EnumSet.of(access));
            userPermissions.put(userKey, permissionSet);
        }
        else {
            EnumSet<Access> accessSet = permissionSet.get(permission);
            if(accessSet == null)
            {
                accessSet = EnumSet.of(access);
                permissionSet.put(permission, accessSet);
            }
            else {
                accessSet.add(access);
            }
        }
    }

    synchronized void HandleUsergroupPermissionChanges(String userKey, Permission permission, Access access)
    {
        Map<Permission, EnumSet<Access>> permissionSet = usergroupPermissions.get(userKey);
        if(permissionSet == null)
        {
            permissionSet = new HashMap<Permission, EnumSet<Access>>();
            permissionSet.put(permission, EnumSet.of(access));
            usergroupPermissions.put(userKey, permissionSet);
        }
        else {
            EnumSet<Access> accessSet = permissionSet.get(permission);
            if(accessSet == null)
            {
                accessSet = EnumSet.of(access);
                permissionSet.put(permission, accessSet);
            }
            else {
                accessSet.add(access);
            }
        }
    }

    public void allowReadByUser(long userId){
        String userKey = String.valueOf(userId);
        HandleUserPermissionChanges(userKey, Permission.allow, Access.read);
    }

    public void allowUpdateByUser(long userId){
        String userKey = String.valueOf(userId);
        HandleUserPermissionChanges(userKey, Permission.allow, Access.update);
    }

    public void allowDeleteByUser(long userId){
        String userKey = String.valueOf(userId);
        HandleUserPermissionChanges(userKey, Permission.allow, Access.delete);
    }

    public void allowManagingAccessByUser(long userId){
        String userKey = String.valueOf(userId);
        HandleUserPermissionChanges(userKey, Permission.allow, Access.manageaccess);
    }

    public void allowReadByUser(String username){
        HandleUserPermissionChanges(username, Permission.allow, Access.read);
    }

    public void allowUpdateByUser(String username){
        HandleUserPermissionChanges(username, Permission.allow, Access.update);
    }

    public void allowDeleteByUser(String username){
        HandleUserPermissionChanges(username, Permission.allow, Access.delete);
    }

    public void allowManagingAccessByUser(String username){
        HandleUserPermissionChanges(username, Permission.allow, Access.manageaccess);
    }

    public void allowReadByUserGroup(long groupId){
        String userKey = String.valueOf(groupId);
        HandleUsergroupPermissionChanges(userKey, Permission.allow, Access.read);
    }

    public void allowUpdateByUserGroup(long groupId){
        String userKey = String.valueOf(groupId);
        HandleUsergroupPermissionChanges(userKey, Permission.allow, Access.update);
    }

    public void allowDeleteByUserGroup(long groupId){
        String userKey = String.valueOf(groupId);
        HandleUsergroupPermissionChanges(userKey, Permission.allow, Access.delete);
    }

    public void allowManagingAccessByUserGroup(long groupId){
        String userKey = String.valueOf(groupId);
        HandleUsergroupPermissionChanges(userKey, Permission.allow, Access.manageaccess);
    }

    public void allowReadByUserGroup(String groupName){
        HandleUsergroupPermissionChanges(groupName, Permission.allow, Access.read);
    }

    public void allowUpdateByUserGroup(String groupName){
        HandleUsergroupPermissionChanges(groupName, Permission.allow, Access.update);
    }

    public void allowDeleteByUserGroup(String groupName){
        HandleUsergroupPermissionChanges(groupName, Permission.allow, Access.delete);
    }

    public void allowManagingAccessByUserGroup(String groupName){
        HandleUsergroupPermissionChanges(groupName, Permission.allow, Access.manageaccess);
    }



    public void denyReadByUser(long userId){
        String userKey = String.valueOf(userId);
        HandleUserPermissionChanges(userKey, Permission.deny, Access.read);
    }

    public void denyUpdateByUser(long userId){
        String userKey = String.valueOf(userId);
        HandleUserPermissionChanges(userKey, Permission.deny, Access.update);
    }

    public void denyDeleteByUser(long userId){
        String userKey = String.valueOf(userId);
        HandleUserPermissionChanges(userKey, Permission.deny, Access.delete);
    }

    public void denyManagingAccessByUser(long userId){
        String userKey = String.valueOf(userId);
        HandleUserPermissionChanges(userKey, Permission.deny, Access.manageaccess);
    }

    public void denyReadByUser(String username){
        HandleUserPermissionChanges(username, Permission.deny, Access.read);
    }

    public void denyUpdateByUser(String username){
        HandleUserPermissionChanges(username, Permission.deny, Access.update);
    }

    public void denyDeleteByUser(String username){
        HandleUserPermissionChanges(username, Permission.deny, Access.delete);
    }

    public void denyManagingAccessByUser(String username){
        HandleUserPermissionChanges(username, Permission.deny, Access.manageaccess);
    }

    public void denyReadByUserGroup(long groupId){
        String userKey = String.valueOf(groupId);
        HandleUsergroupPermissionChanges(userKey, Permission.deny, Access.read);
    }

    public void denyUpdateByUserGroup(long groupId){
        String userKey = String.valueOf(groupId);
        HandleUsergroupPermissionChanges(userKey, Permission.deny, Access.update);
    }

    public void denyDeleteByUserGroup(long groupId){
        String userKey = String.valueOf(groupId);
        HandleUsergroupPermissionChanges(userKey, Permission.deny, Access.delete);
    }

    public void denyManagingAccessByUserGroup(long groupId){
        String userKey = String.valueOf(groupId);
        HandleUsergroupPermissionChanges(userKey, Permission.deny, Access.manageaccess);
    }

    public void denyReadByUserGroup(String groupName){
        HandleUsergroupPermissionChanges(groupName, Permission.deny, Access.read);
    }

    public void denyUpdateByUserGroup(String groupName){
        HandleUsergroupPermissionChanges(groupName, Permission.deny, Access.update);
    }

    public void denyDeleteByUserGroup(String groupName){
        HandleUsergroupPermissionChanges(groupName, Permission.deny, Access.delete);
    }

    public void denyManagingAccessByUserGroup(String groupName){
        HandleUsergroupPermissionChanges(groupName, Permission.deny, Access.manageaccess);
    }



    public void resetReadByUser(long userId){
        String userKey = String.valueOf(userId);
        HandleUserPermissionChanges(userKey, Permission.dontcare, Access.read);
    }

    public void resetUpdateByUser(long userId){
        String userKey = String.valueOf(userId);
        HandleUserPermissionChanges(userKey, Permission.dontcare, Access.update);
    }

    public void resetDeleteByUser(long userId){
        String userKey = String.valueOf(userId);
        HandleUserPermissionChanges(userKey, Permission.dontcare, Access.delete);
    }

    public void resetManagingAccessByUser(long userId){
        String userKey = String.valueOf(userId);
        HandleUserPermissionChanges(userKey, Permission.dontcare, Access.manageaccess);
    }

    public void resetReadByUser(String username){
        HandleUserPermissionChanges(username, Permission.dontcare, Access.read);
    }

    public void resetUpdateByUser(String username){
        HandleUserPermissionChanges(username, Permission.dontcare, Access.update);
    }

    public void resetDeleteByUser(String username){
        HandleUserPermissionChanges(username, Permission.dontcare, Access.delete);
    }

    public void resetManagingAccessByUser(String username){
        HandleUserPermissionChanges(username, Permission.dontcare, Access.manageaccess);
    }

    public void resetReadByUserGroup(long groupId){
        String userKey = String.valueOf(groupId);
        HandleUsergroupPermissionChanges(userKey, Permission.dontcare, Access.read);
    }

    public void resetUpdateByUserGroup(long groupId){
        String userKey = String.valueOf(groupId);
        HandleUsergroupPermissionChanges(userKey, Permission.dontcare, Access.update);
    }

    public void resetDeleteByUserGroup(long groupId){
        String userKey = String.valueOf(groupId);
        HandleUsergroupPermissionChanges(userKey, Permission.dontcare, Access.delete);
    }

    public void resetManagingAccessByUserGroup(long groupId){
        String userKey = String.valueOf(groupId);
        HandleUsergroupPermissionChanges(userKey, Permission.dontcare, Access.manageaccess);
    }

    public void resetReadByUserGroup(String groupName){
        HandleUsergroupPermissionChanges(groupName, Permission.dontcare, Access.read);
    }

    public void resetUpdateByUserGroup(String groupName){
        HandleUsergroupPermissionChanges(groupName, Permission.dontcare, Access.update);
    }

    public void resetDeleteByUserGroup(String groupName){
        HandleUsergroupPermissionChanges(groupName, Permission.dontcare, Access.delete);
    }

    public void resetManagingAccessByUserGroup(String groupName){
        HandleUsergroupPermissionChanges(groupName, Permission.dontcare, Access.manageaccess);
    }
}
