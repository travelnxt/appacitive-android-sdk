package appacitive.infra;

/**
 * Created by sathley.
 */
public class Urls {

    private final static String baseURL = "https://apis.appacitive.com/v1.0";
    public static class ForObject
    {
        private final static String endpoint = "object";

        public static String getObjectUrl(String type, long objectId)
        {
            return String.format("%s/%s/%s/%s", baseURL, endpoint, type, objectId);
        }

        public static String multiGetObjectUrl(String type, long[] objectIds)
        {
            StringBuilder sb = new StringBuilder();
            for (long id : objectIds) {
                if (sb.length() > 0) sb.append(',');
                sb.append(id);
            }
            return String.format("%s/%s/%s/multiget/%s", baseURL, endpoint, type, sb.toString());
        }

        public static String createObjectUrl(String type)
        {
            return String.format("%s/%s/%s", baseURL, endpoint, type);
        }

        public static String deleteObjectUrl(String type, long id, boolean deleteConnections)
        {
            return String.format("%s/%s/%s/%s?deleteconnections=%s", baseURL, endpoint, type, id, deleteConnections);
        }

        public static String bulkDeleteObjectUrl(String type)
        {
            return String.format("%s/%s/%s/bulkdelete", baseURL, endpoint, type);
        }

        public static String updateObjectUrl(String type, long objectId)
        {
            return String.format("%s/%s/%s/%s", baseURL, endpoint, type, objectId);
        }
    }

    public static class ForConnection
    {
        private final static String endpoint = "connection";

        public static String createConnectionUrl(String relationType)
        {
            return String.format("%s/%s/%s", baseURL, endpoint, relationType);
        }

        public static String getConnectionUrl(String relationType, long connectionId)
        {
            return String.format("%s/%s/%s/%s", baseURL, endpoint, relationType, connectionId);
        }

        public static String multiGetConnectionUrl(String relationType, long[] connectionIds)
        {
            StringBuilder sb = new StringBuilder();
            for (long id : connectionIds) {
                if (sb.length() > 0) sb.append(',');
                sb.append(id);
            }
            return String.format("%s/%s/%s/multiget/%s", baseURL, endpoint, relationType, sb.toString());
        }

        public static String deleteConnectionUrl(String relationType, long connectionId)
        {
            return String.format("%s/%s/%s/%s", baseURL, endpoint, relationType, connectionId);
        }

        public static String bulkDeleteConnectionUrl(String relationType)
        {
            return String.format("%s/%s/%s/bulkdelete", baseURL, endpoint, relationType);
        }

        public static String updateConnectionUrl(String relationType, long connectionId)
        {
            return String.format("%s/%s/%s/%s", baseURL, endpoint, relationType, connectionId);
        }
    }

}
