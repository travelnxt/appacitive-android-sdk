package appacitive.utilities;

/**
 * Created by sathley.
 */
public class Urls {

    private final static String baseURL = "https://apis.appacitive.com/v1.0";
    public static class ForObject
    {
        private final static String endpoint = "object";

        public static String getObjectUrl(String type, long id)
        {
            return String.format("%s/%s/%s/%s", baseURL, endpoint, type, id);
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

        public static String updateObjectUrl(String type, long id)
        {
            return String.format("%s/%s/%s/%s", baseURL, endpoint, type, id);
        }
    }

}
