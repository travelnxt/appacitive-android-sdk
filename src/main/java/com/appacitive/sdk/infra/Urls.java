package com.appacitive.sdk.infra;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by sathley.
 */
public class Urls {

    private final static String baseURL = "https://apis.appacitive.com/v1.0";

    private static String join(List<String> lst, String prefix)
    {
        final StringBuilder sb = new StringBuilder();
        String separator = "";
        for(Object f : lst)
        {
            sb.append(separator);
            separator = prefix;
            sb.append(f);
        }
        return sb.toString();
    }

    public static class ForObject
    {
        private final static String endpoint = "object";

        public static Url getObjectUrl(String type, long objectId, List<String> fields)
        {
            String suffix = String.format("%s/%s", type, objectId);
            Map<String, String> qsp = new HashMap<String, String>();
            if(fields != null && fields.size() > 0)
            {
                qsp.put("fields", join(fields, ","));
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
                qsp.put("fields", join(fields, ","));
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
            Map<String, String> qsp = new HashMap<String, String>();
            if(withRevision)
            {
                qsp.put("revision", String.valueOf(revision));
            }
            return new Url(baseURL, endpoint, suffix, qsp);
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
                qsp.put("fields", join(fields, ","));
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
                qsp.put("fields", join(fields, ","));
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
                qsp.put("fields", join(fields, ","));
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
    }

}
