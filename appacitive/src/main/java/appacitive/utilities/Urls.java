package appacitive.utilities;

/**
 * Created by sathley.
 */
public class Urls {

    private final static String baseURL = "https://apis.appacitive.com/v1.0";
    public static class ForObject
    {
        private final static String endpoint = "object";

        public static String GetObjectUrl(String type, long id)
        {
            return String.format("%s/%s/%s/%s", baseURL, endpoint, type, id);
        }

        public static String CreateObjectUrl(String type)
        {
            return String.format("%s/%s/%s", baseURL, endpoint, type);
        }
    }

}
