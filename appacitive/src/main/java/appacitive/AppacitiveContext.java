package appacitive;

import appacitive.utilities.Environment;

/**
 * Created by sathley.
 */
public class AppacitiveContext {

    private static String loggedInUserToken;

    public static String apiKey;

    public static String environment;

    public static String getLoggedInUserToken() {
        return loggedInUserToken;
    }

    public static void setLoggedInUserToken(String userToken) {
        AppacitiveContext.loggedInUserToken = userToken;
    }

    public static void initialize(String apiKey, Environment environment)
    {
        AppacitiveContext.apiKey = apiKey;
        AppacitiveContext.environment = environment.name();
    }

    public static boolean isInitialized()
    {
        return AppacitiveContext.apiKey.isEmpty() == false && AppacitiveContext.environment.isEmpty() == false;
    }
}
