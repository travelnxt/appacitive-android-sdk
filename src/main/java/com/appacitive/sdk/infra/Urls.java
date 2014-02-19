package com.appacitive.sdk.infra;

import com.appacitive.sdk.model.UserIdType;
import com.appacitive.sdk.query.AppacitiveQuery;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by sathley.
 */
public class Urls {

    private final static String baseURL = "https://apis.appacitive.com/v1.0";

    public static class ForObject
    {
        private final static String endpoint = "object";

        public static Url getObjectUrl(String type, long objectId, List<String> fields)
        {
            String suffix = String.format("%s/%s", type, objectId);
            Map<String, String> qsp = new HashMap<String, String>();
            if(fields != null && fields.size() > 0)
            {
                qsp.put("fields", StringUtils.join(fields, ","));
            }
            return new Url(baseURL, endpoint, suffix, qsp);
        }

        public static Url multiGetObjectUrl(String type, List<Long> objectIds, List<String> fields)
        {
            StringBuilder sb = new StringBuilder();
            for (long id : objectIds) {
                if (sb.length() > 0) sb.append(',');
                sb.append(id);
            }
            String suffix = String.format("%s/multiget/%s", type, sb.toString());
            Map<String, String> qsp = new HashMap<String, String>();
            if(fields != null && fields.size() > 0)
            {
                qsp.put("fields", StringUtils.join(fields, ","));
            }
            return new Url(baseURL, endpoint, suffix, qsp);
        }

        public static Url createObjectUrl(String type)
        {
            return new Url(baseURL, endpoint, type, null);
        }

        public static Url deleteObjectUrl(String type, long id, boolean deleteConnections)
        {
            Map<String, String> qsp = new HashMap<String, String>();
            qsp.put("deleteconnections", String.valueOf(deleteConnections));
            return new Url(baseURL, endpoint, String.format("%s/%s", type, id), qsp);
        }

        public static Url bulkDeleteObjectUrl(String type)
        {
            return new Url(baseURL, endpoint, String.format("%s/bulkdelete", type), null);
        }

        public static Url updateObjectUrl(String type, long objectId, boolean withRevision, long revision)
        {
            String suffix = String.format("%s/%s", type, objectId);
            Map<String, String> qsp = new HashMap <String, String>();
            if(withRevision)
            {
                qsp.put("revision", String.valueOf(revision));
            }
            return new Url(baseURL, endpoint, suffix, qsp);
        }

        public static Url findObjectsUrl(String type, final AppacitiveQuery query, List<String> fields)
        {
            Map<String, String> qsp = new HashMap<String, String>();

            if(query != null)
                qsp.putAll(query.asQueryStringParameters());

            if(fields != null && fields.size() > 0)
                qsp.put("fields", StringUtils.join(fields, ","));

            return new Url(baseURL, endpoint, type + "/find/all", qsp);
        }

        public static Url findBetweenTwoObjectsUrl(String type, long objectAId, String relationA, String labelA, long objectBId, String relationB, String labelB, List<String> fields) {

            Map<String, String> qsp = new HashMap<String, String>();
            if(fields != null && fields.size() > 0)
                qsp.put("fields", StringUtils.join(fields, ","));
            return new Url(baseURL, endpoint, String.format("%s/%s/%s/%s/%s/%s/%s", type, String.valueOf(objectAId), relationA, labelA, String.valueOf(objectBId), relationB, labelB), qsp);
        }
    }

    public static class ForConnection
    {
        private final static String endpoint = "connection";

        public static Url createConnectionUrl(String relationType)
        {
            return new Url(baseURL, endpoint, relationType, null);
        }

        public static Url getConnectionUrl(String relationType, long connectionId,  List<String> fields)
        {
            String suffix = String.format("%s/%s", relationType, connectionId);
            Map<String, String> qsp = new HashMap<String, String>();
            if(fields != null && fields.size() > 0)
            {
                qsp.put("fields", StringUtils.join(fields, ","));
            }
            return new Url(baseURL, endpoint, suffix, qsp);
        }

        public static Url multiGetConnectionUrl(String relationType, List<Long> connectionIds, List<String> fields)
        {
            StringBuilder sb = new StringBuilder();
            for (long id : connectionIds) {
                if (sb.length() > 0) sb.append(',');
                sb.append(id);
            }
            String suffix = String.format("%s/multiget/%s", relationType, sb.toString());
            Map<String, String> qsp = new HashMap<String, String>();
            if(fields != null && fields.size() > 0)
            {
                qsp.put("fields", StringUtils.join(fields, ","));
            }
            return new Url(baseURL, endpoint, suffix, qsp);
        }

        public static Url deleteConnectionUrl(String relationType, long connectionId)
        {
            return new Url(baseURL, endpoint, String.format("%s/%s", relationType, connectionId), null);
        }

        public static Url bulkDeleteConnectionUrl(String relationType)
        {
            return new Url(baseURL, endpoint, String.format("%s/bulkdelete", relationType), null);
        }

        public static Url updateConnectionUrl(String relationType, long connectionId, boolean withRevision, long revision)
        {
            String suffix = String.format("%s/%s", relationType, connectionId);
            Map<String, String> qsp = new HashMap<String, String>();
            if(withRevision)
            {
                qsp.put("revision", String.valueOf(revision));
            }
            return new Url(baseURL, endpoint, suffix, qsp);
        }

        public static Url findConnectionsUrl(String relationType, final AppacitiveQuery query, final List<String> fields)
        {
            Map<String, String> qsp = new HashMap<String, String>();

            if(query != null)
                qsp.putAll(query.asQueryStringParameters());

            if(fields != null && fields.size() > 0)
                qsp.put("fields", StringUtils.join(fields, ","));

            return new Url(baseURL, endpoint, relationType + "/find/all", qsp);
        }

        public static Url findForObjectsUrl(long objectId1, long objectId2, List<String> fields) {
            Map<String, String> qsp = new HashMap<String, String>();
            if(fields != null && fields.size() > 0)
                qsp.put("fields", StringUtils.join(fields, ","));
            return new Url(baseURL, endpoint, String.format("find/%s/%s", String.valueOf(objectId1), String.valueOf(objectId2)), qsp);

        }

        public static Url findForObjectsAndRelationUrl(String relationType, long objectId1, long objectId2, List<String> fields) {
            Map<String, String> qsp = new HashMap<String, String>();
            if(fields != null && fields.size() > 0)
                qsp.put("fields", StringUtils.join(fields, ","));
            return new Url(baseURL, endpoint, String.format("%s/find/%s/%s", relationType, String.valueOf(objectId1), String.valueOf(objectId2)), qsp);
        }

        public static Url findInterconnectsUrl(List<String> fields) {
            Map<String, String> qsp = new HashMap<String, String>();
            if(fields != null && fields.size() > 0)
                qsp.put("fields", StringUtils.join(fields, ","));

            return new Url(baseURL, endpoint, "interconnects", qsp);
        }

        public static Url findByObjectAndLabelUrl(String relationType, final long objectId, final String label, List<String> fields) {
            Map<String, String> qsp = new HashMap<String, String>();
            if(fields != null && fields.size() > 0)
                qsp.put("fields", StringUtils.join(fields, ","));
            qsp.put("objectid", String.valueOf(objectId));
            qsp.put("label", label);
            return new Url(baseURL, endpoint, "find", qsp);
        }

        public static Url findByObjectAndLabelUrl123(String relationType, final long objectId, final String label, List<String> fields) {
            Map<String, String> qsp = new HashMap<String, String>();
            if(fields != null && fields.size() > 0)
                qsp.put("fields", StringUtils.join(fields, ","));
            qsp.put("objectid", String.valueOf(objectId));
            qsp.put("label", label);
            return new Url(baseURL, endpoint, "find", qsp);
        }

        public static Url getConnectedObjectsUrl(String relationType, String objectType, long objectId, AppacitiveQuery query, List<String> fields) {

            Map<String, String> qsp = new HashMap<String, String>();

            if(query != null)
                qsp.putAll(query.asQueryStringParameters());

            if(fields != null && fields.size() > 0)
                qsp.put("fields", StringUtils.join(fields, ","));

            return new Url(baseURL, endpoint, String.format("%s/%s/%s/find", relationType, objectType, objectId), qsp);
        }
    }

    public static class ForUser
    {
        private final static String endpoint = "user";

        public static Url getUserUrl(String userId, UserIdType type, List<String> fields)
        {
            String suffix = String.valueOf(userId);
            Map<String, String> qsp = new HashMap<String, String>();
            if(fields != null && fields.size() > 0)
            {
                qsp.put("fields", StringUtils.join(fields, ","));
            }

            switch (type)
            {
                case id: suffix = String.valueOf(userId);
                    qsp.put("useridtype", "id");
                    break;
                case username: suffix = String.valueOf(userId);
                    qsp.put("useridtype", "username");
                    break;
                case token: suffix = "me";
                    qsp.put("useridtype", "token");
                    break;
            }
            return new Url(baseURL, endpoint, suffix, qsp);
        }

        public static Url multiGetUserUrl(List<Long> userIds, List<String> fields)
        {
            return Urls.ForObject.multiGetObjectUrl("user", userIds, fields);
        }

        public static Url createUserUrl()
        {
            return new Url(baseURL, endpoint, "create", null);
        }

        public static Url deleteObjectUrl(String userId, UserIdType type, boolean deleteConnections)
        {
            String suffix = null;
            Map<String, String> qsp = new HashMap<String, String>();
            qsp.put("deleteconnections", String.valueOf(deleteConnections));
            switch (type)
            {
                case id: suffix = userId;
                    qsp.put("useridtype", "id");
                    break;
                case username: suffix = userId;
                    qsp.put("useridtype", "username");
                    break;
                case token: suffix = "me";
                    qsp.put("useridtype", "token");
                    break;
            }
            return new Url(baseURL, endpoint, suffix, qsp);
        }

        public static Url updateUserUrl(long userId, boolean withRevision, long revision)
        {
            String suffix = String.valueOf(userId);
            Map<String, String> qsp = new HashMap<String, String>();
            if(withRevision)
            {
                qsp.put("revision", String.valueOf(revision));
            }
            return new Url(baseURL, endpoint, suffix, qsp);
        }

        public static Url authenticateUserUrl()
        {
            String suffix = "authenticate";
            return new Url(baseURL, endpoint, suffix, null);
        }

        public static Url updatePasswordUrl(Long userId)
        {
            String suffix = String.format("%s/%s", String.valueOf(userId), "changepassword");
            Map<String, String> qsp = new HashMap<String, String>();
            qsp.put("useridtype", "id");
            return new Url(baseURL, endpoint, suffix, qsp);
        }

        public static Url sendResetPasswordEmailUrl()
        {
            return new Url(baseURL, endpoint, "sendresetpasswordemail", null);
        }

        public static Url validateSessionUrl()
        {
            return new Url(baseURL, endpoint, "validate", null);
        }

        public static Url invalidateSessionUrl()
        {
            return new Url(baseURL, endpoint, "invalidate", null);
        }

        public static Url checkInUserUrl(long userId, double[] coordinates)
        {
            String suffix = String.format("%s/%s", String.valueOf(userId), "checkin");
            Map<String, String> qsp = new HashMap<String, String>();
            qsp.put("lat", String.valueOf(coordinates[0]));
            qsp.put("long", String.valueOf(coordinates[1]));
            return new Url(baseURL, endpoint, suffix, qsp);

        }

        public static Url linkAccountUrl(long userId)
        {
            String suffix = String.format("%s/%s", String.valueOf(userId), "link");
            return new Url(baseURL, endpoint, suffix, null);
        }

        public static Url delinkAccountUrl(long userId, String linkName)
        {
            String suffix = String.format("%s/%s/%s", String.valueOf(userId), linkName, "delink");
            return new Url(baseURL, endpoint, suffix, null);
        }

        public static Url getLinkAccountUrl(long userId, String linkName)
        {
            String suffix = String.format("%s/%s/%s", String.valueOf(userId),"linkedaccounts",  linkName);
            return new Url(baseURL, endpoint, suffix, null);
        }

        public static Url getAllLinkAccountUrl(long userId)
        {
            String suffix = String.format("%s/%s", String.valueOf(userId),"linkedaccounts");
            return new Url(baseURL, endpoint, suffix, null);
        }
    }

    public static class Misc
    {
        public static Url sendPushUrl()
        {
            return new Url(baseURL, "push", "", null);
        }

        public static Url sendEmailUrl()
        {
            return new Url(baseURL, "email", "send", null);
        }

        public static Url filterQueryUrl(String queryName)
        {
            return new Url(baseURL, "search", queryName+"/filter", null);
        }

        public static Url projectQueryUrl(String queryName)
        {
            return new Url(baseURL, "search", queryName+"/project", null);
        }
    }

    public static class ForFile
    {
        private final static String endpoint = "file";

        public static Url getUploadUrl(final String contentType)
        {
            return new Url(baseURL, endpoint, "uploadurl", new HashMap<String, String>(){{
                put("contenttype", contentType);
            }});

        }

        public static Url getDownloadUrl(final String fileId)
        {
            return new Url(baseURL, endpoint, "download/".concat(fileId), null);
        }

        public static Url getDeleteUrl(final String fileId)
        {
            return new Url(baseURL, endpoint, "delete/".concat(fileId), null);
        }
    }

    public static class ForDevice
    {
        private final static String endpoint = "device";

        public static Url getRegisterUrl()
        {
            return new Url(baseURL, endpoint, "register", null);
        }

        public static Url getDeviceUrl(String deviceId)
        {
            return new Url(baseURL, endpoint, deviceId, null);
        }

        public static Url updateDeviceUrl(long deviceId, boolean withRevision, long revision)
        {
            Map<String, String> qsp = new HashMap<String, String>();
            if(withRevision == true)
                qsp.put("revision", String.valueOf(revision));

            return new Url(baseURL, endpoint, String.valueOf(deviceId), qsp);
        }

        public static Url deleteDeviceUrl(long deviceId, boolean deleteConnections)
        {
            Map<String, String> qsp = new HashMap<String, String>();
            qsp.put("deleteconnections", String.valueOf(deleteConnections));
            return new Url(baseURL, endpoint, String.valueOf(deviceId), qsp);
        }
    }

}
