package com.appacitive.core;

import com.appacitive.core.apjson.APJSONArray;
import com.appacitive.core.apjson.APJSONException;
import com.appacitive.core.apjson.APJSONObject;
import com.appacitive.core.exceptions.AppacitiveException;
import com.appacitive.core.exceptions.UserAuthException;
import com.appacitive.core.exceptions.ValidationException;
import com.appacitive.core.infra.*;
import com.appacitive.core.interfaces.AsyncHttp;
import com.appacitive.core.interfaces.Logger;
import com.appacitive.core.model.*;

import java.io.Serializable;
import java.text.ParseException;
import java.util.*;

/**
 * Created by sathley.
 */
public class AppacitiveUser extends AppacitiveObjectBase implements Serializable, APSerializable {

    public enum SocialProvider {
        FACEBOOK
    }

    public final static Logger LOGGER = APContainer.build(Logger.class);

    public AppacitiveUser() {
        super("user");
    }

    public AppacitiveUser(long userId) {
        super("user", userId);
    }

    public synchronized void setSelf(APJSONObject user) {
        super.setSelf(user);
    }

    @Override
    public synchronized APJSONObject getMap() throws APJSONException {
        return super.getMap();
    }

    public String getPhone() {
        return this.getPropertyAsString("phone");
    }

    public void setPhone(String phone) {
        this.setStringProperty("phone", phone);

    }

    public String getPassword() {
        return this.getPropertyAsString("password");
    }

    public void setPassword(String password) {
        this.setStringProperty("password", password);

    }

    public String getSecretQuestion() {
        return this.getPropertyAsString("secretquestion");
    }

    public void setSecretQuestion(String secretQuestion) {
        this.setStringProperty("secretquestion", secretQuestion);

    }

    public String getSecretAnswer() {
        return this.getPropertyAsString("secretanswer");
    }

    public void setSecretAnswer(String secretAnswer) {
        this.setStringProperty("secretanswer", secretAnswer);

    }

    public String getFirstName() {
        return this.getPropertyAsString("firstname");
    }

    public void setFirstName(String firstName) {
        this.setStringProperty("firstname", firstName);

    }

    public String getLastName() {
        return this.getPropertyAsString("lastname");
    }

    public void setLastName(String lastName) {
        this.setStringProperty("lastname", lastName);

    }

    public String getEmail() {
        return this.getPropertyAsString("email");
    }

    public void setEmail(String email) {
        this.setStringProperty("email", email);

    }

    public String getUsername() {
        return this.getPropertyAsString("username");
    }

    public void setUsername(String username) {
        this.setStringProperty("username", username);

    }

    public Date getBirthDate() throws ParseException {
        return this.getPropertyAsDate("birthdate");
    }

    public void setBirthDate(Date birthDate) {
        this.setDateProperty("birthdate", birthDate);

    }

    public double[] getLocation() {
        return this.getPropertyAsGeo("location");
    }

    public void setLocation(double[] location) {
        this.setGeoProperty("location", location);
    }

    public void signupInBackground(final Callback<AppacitiveUser> callback) {
        LOGGER.info("Signing up new user.");
        List<String> mandatoryFields = new ArrayList<String>() {{
            add("username");
            add("password");
            add("email");
            add("firstname");
        }};
        List<String> missingFields = new ArrayList<String>();
        for (String field : mandatoryFields) {
            if (this.getPropertyAsString(field) == null) {
                missingFields.add(field);
            }
        }

        if (missingFields.size() > 0)
            throw new ValidationException("Following mandatory fields are missing. - " + StringUtils.join(missingFields, ","));

        final String url = Urls.ForUser.createUserUrl().toString();
        final Map<String, String> headers = Headers.assemble();
        final APJSONObject payload;
        try {
            payload = this.getMap();
        } catch (APJSONException e) {
            throw new RuntimeException(e);
        }

        final AppacitiveUser user = this;
        AsyncHttp asyncHttp = APContainer.build(AsyncHttp.class);
        asyncHttp.put(url, headers, payload.toString(), new APCallback() {
            @Override
            public void success(String result) {

                APJSONObject jsonObject;
                try {
                    jsonObject = new APJSONObject(result);
                } catch (APJSONException e) {
                    throw new RuntimeException(e);
                }
                AppacitiveStatus status = new AppacitiveStatus(jsonObject.optJSONObject("status"));
                if (status.isSuccessful()) {
                    user.setSelf(jsonObject.optJSONObject("user"));
                    AppacitiveContextBase.setLoggedInUser(user);
                    if (callback != null) {
                        callback.success(user);
                    }
                } else {
                    if (callback != null)
                        callback.failure(null, new AppacitiveException(status));
                }

            }

            @Override
            public void failure(Exception e) {
                if (callback != null)
                    callback.failure(null, new AppacitiveException(e));
            }
        });
    }

    public static void signupWithFacebookInBackground(final String facebookAccessToken, final Callback<AppacitiveUser> callback) {
        LOGGER.info("Signing up new user with facebook.");
        final String url = Urls.ForUser.authenticateUserUrl().toString();
        final Map<String, String> headers = Headers.assemble();
        Map<String, Object> payloadMap = new HashMap<String, Object>() {{
            put("type", "facebook");
            put("accesstoken", facebookAccessToken);
            put("createnew", true);
        }};
        final APJSONObject payload = new APJSONObject(payloadMap);

        AsyncHttp asyncHttp = APContainer.build(AsyncHttp.class);
        asyncHttp.post(url, headers, payload.toString(), new APCallback() {
            @Override
            public void success(String result) {

                APJSONObject jsonObject;
                try {
                    jsonObject = new APJSONObject(result);
                } catch (APJSONException e) {
                    throw new RuntimeException(e);
                }
                AppacitiveStatus status = new AppacitiveStatus(jsonObject.optJSONObject("status"));
                if (status.isSuccessful()) {
                    String token = jsonObject.optString("token");
                    AppacitiveContextBase.setLoggedInUserToken(token);
                    AppacitiveUser user = new AppacitiveUser();
                    user.setSelf(jsonObject.optJSONObject("user"));
                    AppacitiveContextBase.setLoggedInUser(user);
                    if (callback != null) {
                        callback.success(user);
                    }
                } else {
                    if (callback != null)
                        callback.failure(null, new AppacitiveException(status));
                }

            }

            @Override
            public void failure(Exception e) {
                if (callback != null)
                    callback.failure(null, e);
            }
        });
    }

    public static void signupWithTwitterInBackground(final String oauthToken, final String oauthTokenSecret, String consumerKey, String consumerSecret, final Callback<AppacitiveUser> callback) {
        LOGGER.info("Signing up new user with twitter.");
        final String url = Urls.ForUser.authenticateUserUrl().toString();
        final Map<String, String> headers = Headers.assemble();
        Map<String, Object> payloadMap = new HashMap<String, Object>() {{
            put("type", "twitter");
            put("oauthtoken", oauthToken);
            put("oauthtokensecret", oauthTokenSecret);
            put("createnew", true);
        }};
        if (consumerKey != null)
            payloadMap.put("consumerkey", consumerKey);
        if (consumerSecret != null)
            payloadMap.put("consumersecret", consumerSecret);

        final APJSONObject payload = new APJSONObject(payloadMap);

        AsyncHttp asyncHttp = APContainer.build(AsyncHttp.class);
        asyncHttp.post(url, headers, payload.toString(), new APCallback() {
            @Override
            public void success(String result) {

                APJSONObject jsonObject;
                try {
                    jsonObject = new APJSONObject(result);
                } catch (APJSONException e) {
                    throw new RuntimeException(e);
                }
                AppacitiveStatus status = new AppacitiveStatus(jsonObject.optJSONObject("status"));
                if (status.isSuccessful()) {
                    String token = jsonObject.optString("token");
                    AppacitiveContextBase.setLoggedInUserToken(token);
                    AppacitiveUser user = new AppacitiveUser();
                    user.setSelf(jsonObject.optJSONObject("user"));
                    AppacitiveContextBase.setLoggedInUser(user);
                    if (callback != null) {
                        callback.success(user);
                    }
                } else {
                    if (callback != null)
                        callback.failure(null, new AppacitiveException(status));
                }
            }

            @Override
            public void failure(Exception e) {
                if (callback != null)
                    callback.failure(null, e);
            }
        });
    }

    public static void signupWithTwitterInBackground(final String oauthToken, final String oauthTokenSecret, final Callback<AppacitiveUser> callback)
    {
        signupWithTwitterInBackground(oauthToken, oauthTokenSecret, null, null, callback);
    }

    public static void getByIdInBackground(long userId, List<String> fields, final Callback<AppacitiveUser> callback) {
        LOGGER.info("Fetch user with id " + userId);
        final String url = Urls.ForUser.getUserUrl(String.valueOf(userId), UserIdType.id, fields).toString();
        final Map<String, String> headers = Headers.assemble();
        AssertUserAuth();

        getInBackgroundHelper(url, headers, callback);
    }

    private static void AssertUserAuth() {
        String token = AppacitiveContextBase.getLoggedInUserToken();
        if (token == null || token.isEmpty() == true)
            throw new UserAuthException();
    }

    public static void getByUsernameInBackground(String username, List<String> fields, Callback<AppacitiveUser> callback) {
        LOGGER.info("Fetch user with username " + username);
        final String url = Urls.ForUser.getUserUrl(username, UserIdType.username, fields).toString();
        final Map<String, String> headers = Headers.assemble();
        AssertUserAuth();
        getInBackgroundHelper(url, headers, callback);
    }

    public static void getLoggedInUserInBackground(List<String> fields, Callback<AppacitiveUser> callback) {
        LOGGER.info("Fetch user with token.");
        final String url = Urls.ForUser.getUserUrl("me", UserIdType.token, fields).toString();
        final Map<String, String> headers = Headers.assemble();
        AssertUserAuth();

        getInBackgroundHelper(url, headers, callback);
    }

    private static void getInBackgroundHelper(String url, Map<String, String> headers, final Callback<AppacitiveUser> callback) {
        AsyncHttp asyncHttp = APContainer.build(AsyncHttp.class);
        asyncHttp.get(url, headers, new APCallback() {
            @Override
            public void success(String result) {

                APJSONObject jsonObject;
                try {
                    jsonObject = new APJSONObject(result);
                } catch (APJSONException e) {
                    throw new RuntimeException(e);
                }
                AppacitiveStatus status = new AppacitiveStatus(jsonObject.optJSONObject("status"));
                if (status.isSuccessful()) {
                    AppacitiveUser user = null;
                    APJSONObject userJson = jsonObject.optJSONObject("user");
                    if (userJson != null) {
                        user = new AppacitiveUser();
                        user.setSelf(userJson);
                    }
                    if (callback != null)
                        callback.success(user);
                } else {
                    if (callback != null)
                        callback.failure(null, new AppacitiveException(status));
                }

            }

            @Override
            public void failure(Exception e) {
                if (callback != null)
                    callback.failure(null, e);
            }
        });
    }

    private static void loginInBackgroundHelper(String url, Map<String, String> headers, APJSONObject payload, final Callback<AppacitiveUser> callback) {
        LOGGER.info("Logging in.");
        AsyncHttp asyncHttp = APContainer.build(AsyncHttp.class);
        asyncHttp.post(url, headers, payload.toString(), new APCallback() {
            @Override
            public void success(String result) {

                APJSONObject jsonObject;
                try {
                    jsonObject = new APJSONObject(result);
                } catch (APJSONException e) {
                    throw new RuntimeException(e);
                }
                AppacitiveStatus status = new AppacitiveStatus(jsonObject.optJSONObject("status"));
                if (status.isSuccessful()) {

                    String token = jsonObject.optString("token");
                    AppacitiveContextBase.setLoggedInUserToken(token);

                    AppacitiveUser user = new AppacitiveUser();
                    user.setSelf(jsonObject.optJSONObject("user"));

                    AppacitiveContextBase.setLoggedInUser(user);
                    if (callback != null)
                        callback.success(user);
                } else {
                    if (callback != null)
                        callback.failure(null, new AppacitiveException(status));
                }

            }

            @Override
            public void failure(Exception e) {
                if (callback != null)
                    callback.failure(null, e);
            }
        });
    }

    public static void loginInBackground(final String username, final String password, long expiry, int attempts, Callback<AppacitiveUser> callback) {
        LOGGER.info("Logging in.");
        final String url = Urls.ForUser.authenticateUserUrl().toString();
        final Map<String, String> headers = Headers.assemble();
        final Map<String, Object> payloadMap = new HashMap<String, Object>() {{
            put("username", username);
            put("password", password);
        }};
        if (expiry > 0)
            payloadMap.put("expiry", expiry);

        if (attempts > 0)
            payloadMap.put("attempts", attempts);
        APJSONObject payload = new APJSONObject(payloadMap);

        loginInBackgroundHelper(url, headers, payload, callback);
    }

    public static void loginWithFacebookInBackground(final String facebookAccessToken, Callback<AppacitiveUser> callback) {
        LOGGER.info("Logging in with facebook.");
        final String url = Urls.ForUser.authenticateUserUrl().toString();
        final Map<String, String> headers = Headers.assemble();
        final Map<String, Object> payloadMap = new HashMap<String, Object>() {{
            put("type", "facebook");
            put("accesstoken", facebookAccessToken);
        }};
        APJSONObject payload = new APJSONObject(payloadMap);

        loginInBackgroundHelper(url, headers, payload, callback);

    }

    public static void loginWithTwitterInBackground(final String oauthToken, final String oauthTokenSecret, String consumerKey, String consumerSecret, Callback<AppacitiveUser> callback) {
        LOGGER.info("Logging in with twitter.");
        final String url = Urls.ForUser.authenticateUserUrl().toString();
        final Map<String, String> headers = Headers.assemble();
        final Map<String, Object> payloadMap = new HashMap<String, Object>() {{
            put("type", "twitter");
            put("oauthtoken", oauthToken);
            put("oauthtokensecret", oauthTokenSecret);
        }};
        if (consumerKey != null)
            payloadMap.put("consumerkey", consumerKey);
        if (consumerSecret != null)
            payloadMap.put("consumersecret", consumerSecret);

        APJSONObject payload = new APJSONObject(payloadMap);

        loginInBackgroundHelper(url, headers, payload, callback);
    }

    public static void loginWithTwitterInBackground(final String oauthToken, final String oauthTokenSecret, Callback<AppacitiveUser> callback) {
        loginWithTwitterInBackground(oauthToken, oauthTokenSecret, null, null, callback);
    }

    public void loginInBackground(final String password, Callback<String> callback) {
        this.loginInBackground(password, Integer.MAX_VALUE, callback);
    }

    public void loginInBackground(final String password, int expiry, final Callback<String> callback) {
        LOGGER.info("Logging in.");
        final String url = Urls.ForUser.authenticateUserUrl().toString();
        final Map<String, String> headers = Headers.assemble();
        final Map<String, Object> payloadMap = new HashMap<String, Object>() {{
            put("username", getUsername());
            put("password", password);
        }};
        if (expiry > 0)
            payloadMap.put("expiry", expiry);
        APJSONObject payload = new APJSONObject(payloadMap);
        AsyncHttp asyncHttp = APContainer.build(AsyncHttp.class);
        asyncHttp.post(url, headers, payload.toString(), new APCallback() {
            @Override
            public void success(String result) {

                APJSONObject jsonObject;
                try {
                    jsonObject = new APJSONObject(result);
                } catch (APJSONException e) {
                    throw new RuntimeException(e);
                }
                AppacitiveStatus status = new AppacitiveStatus(jsonObject.optJSONObject("status"));
                if (status.isSuccessful()) {

                    String token = jsonObject.optString("token");
                    AppacitiveContextBase.setLoggedInUserToken(token);

                    if (callback != null)
                        callback.success(token);
                } else {
                    if (callback != null)
                        callback.failure(null, new AppacitiveException(status));
                }

            }

            @Override
            public void failure(Exception e) {
                if (callback != null) {
                    callback.failure(null, e);
                }
            }
        });
    }

    public static void multiGetInBackground(List<Long> ids, List<String> fields, final Callback<List<AppacitiveUser>> callback) {
        LOGGER.info("Bulk fetching users with ids " + StringUtils.joinLong(ids, " , "));
        final String url = Urls.ForUser.multiGetUserUrl(ids, fields).toString();
        final Map<String, String> headers = Headers.assemble();
//        AssertUserAuth();
        AsyncHttp asyncHttp = APContainer.build(AsyncHttp.class);
        asyncHttp.get(url, headers, new APCallback() {
            @Override
            public void success(String result) {

                APJSONObject jsonObject;
                try {
                    jsonObject = new APJSONObject(result);
                } catch (APJSONException e) {
                    throw new RuntimeException(e);
                }
                AppacitiveStatus status = new AppacitiveStatus(jsonObject.optJSONObject("status"));
                if (status.isSuccessful()) {
                    List<AppacitiveUser> returnUsers = new ArrayList<AppacitiveUser>();
                    APJSONArray objectsArray = jsonObject.optJSONArray("objects");
                    for (int i = 0; i < objectsArray.length(); i++) {
                        APJSONObject userObject = objectsArray.optJSONObject(i);
                        AppacitiveUser user = new AppacitiveUser();
                        user.setSelf(userObject);
                        returnUsers.add(user);
                    }
                    if (callback != null)
                        callback.success(returnUsers);
                } else {
                    if (callback != null)
                        callback.failure(null, new AppacitiveException(status));
                }

            }

            @Override
            public void failure(Exception e) {
                if (callback != null)
                    callback.failure(null, e);
            }
        });
    }

    private static void deleteInBackgroundHelper(String url, Map<String, String> headers, final Callback<Void> callback) {
        AsyncHttp asyncHttp = APContainer.build(AsyncHttp.class);
        asyncHttp.delete(url, headers, new APCallback() {
            @Override
            public void success(String result) {

                APJSONObject jsonObject;
                try {
                    jsonObject = new APJSONObject(result);
                } catch (APJSONException e) {
                    throw new RuntimeException(e);
                }
                AppacitiveStatus status = new AppacitiveStatus(jsonObject.optJSONObject("status"));
                if (status.isSuccessful()) {
                    if (callback != null)
                        callback.success(null);
                } else {
                    if (callback != null)
                        callback.failure(null, new AppacitiveException(status));
                }

            }

            @Override
            public void failure(Exception e) {
                if (callback != null)
                    callback.failure(null, e);
            }
        });
    }

    public static void deleteInBackground(long userId, boolean deleteConnections, Callback<Void> callback) {
        LOGGER.info("Deleting user with id " + userId);
        final String url = Urls.ForUser.deleteObjectUrl(String.valueOf(userId), UserIdType.id, deleteConnections).toString();
        final Map<String, String> headers = Headers.assemble();
        AssertUserAuth();
        deleteInBackgroundHelper(url, headers, callback);
    }

    public static void deleteInBackground(String username, boolean deleteConnections, Callback<Void> callback) {
        LOGGER.info("Deleting user with username " + username);
        final String url = Urls.ForUser.deleteObjectUrl(username, UserIdType.username, deleteConnections).toString();
        final Map<String, String> headers = Headers.assemble();
        AssertUserAuth();
        deleteInBackgroundHelper(url, headers, callback);
    }

    public static void deleteLoggedInUserInBackground(boolean deleteConnections, Callback<Void> callback) {
        LOGGER.info("Deleting logged-in user.");
        final String url = Urls.ForUser.deleteObjectUrl("me", UserIdType.token, deleteConnections).toString();
        final Map<String, String> headers = Headers.assemble();
        AssertUserAuth();
        deleteInBackgroundHelper(url, headers, callback);
    }

    public void deleteInBackground(boolean deleteConnections, Callback<Void> callback) {
        LOGGER.info("Deleting user with username " + this.getUsername());
        final String url = Urls.ForUser.deleteObjectUrl(this.getUsername(), UserIdType.username, deleteConnections).toString();
        final Map<String, String> headers = Headers.assemble();
        AssertUserAuth();
        deleteInBackgroundHelper(url, headers, callback);
    }

    public void updateInBackground(boolean withRevision, final Callback<AppacitiveUser> callback) {
        LOGGER.info("Updating user with id " + this.getId());
        final String url = Urls.ForUser.updateUserUrl(this.getId(), withRevision, this.getRevision()).toString();
        final Map<String, String> headers = Headers.assemble();
        AssertUserAuth();
        final AppacitiveUser user = this;
        APJSONObject payload;
        try {
            payload = super.getUpdateCommand();
            payload.put("__acls", this.accessControl.getMap());
        } catch (APJSONException e) {
            throw new RuntimeException(e);
        }
        AsyncHttp asyncHttp = APContainer.build(AsyncHttp.class);
        asyncHttp.post(url, headers, payload.toString(), new APCallback() {
            @Override
            public void success(String result) {

                APJSONObject jsonObject;
                try {
                    jsonObject = new APJSONObject(result);
                } catch (APJSONException e) {
                    throw new RuntimeException(e);
                }
                AppacitiveStatus status = new AppacitiveStatus(jsonObject.optJSONObject("status"));
                if (status.isSuccessful()) {
                    user.setSelf(jsonObject.optJSONObject("user"));
                    if (callback != null)
                        callback.success(user);
                } else {
                    if (callback != null)
                        callback.failure(null, new AppacitiveException(status));
                }

            }

            @Override
            public void failure(Exception e) {
                if (callback != null)
                    callback.failure(null, e);
            }
        });
    }

    public void updatePasswordInBackground(final String oldPassword, final String newPassword, final Callback<Void> callback) {
        LOGGER.info("Updating password.");
        final String url = Urls.ForUser.updatePasswordUrl(this.getId()).toString();
        final Map<String, String> headers = Headers.assemble();
        AssertUserAuth();
        final Map<String, Object> payloadMap = new HashMap<String, Object>() {{
            put("oldpassword", oldPassword);
            put("newpassword", newPassword);
        }};
        APJSONObject payload = new APJSONObject(payloadMap);
        postWithVoidCallbackHelper(url, headers, payload, callback);
    }

    public static void sendResetPasswordEmailInBackground(final String username, final String subjectForEmail, Callback<Void> callback) {
        LOGGER.info("Sending reset password email for " + username + " with subject " + subjectForEmail);
        final String url = Urls.ForUser.sendResetPasswordEmailUrl().toString();
        final Map<String, String> headers = Headers.assemble();
        final Map<String, Object> payloadMap = new HashMap<String, Object>() {{
            put("username", username);
            put("subject", subjectForEmail);
        }};
        APJSONObject payload = new APJSONObject(payloadMap);

        postWithVoidCallbackHelper(url, headers, payload, callback);
    }

    public static void validateCurrentlyLoggedInUserSessionInBackground(Callback<Void> callback) {
        LOGGER.info("Validating currently logged in user.");
        final String url = Urls.ForUser.validateSessionUrl().toString();
        final Map<String, String> headers = Headers.assemble();
        final Map<String, Object> payloadMap = new HashMap<String, Object>();
        try {
            AssertUserAuth();
        } catch (UserAuthException e) {
            if (callback != null)
                callback.failure(null, e);
        }
        APJSONObject payload = new APJSONObject(payloadMap);

        postWithVoidCallbackHelper(url, headers, payload, callback);
    }

    public static void invalidateCurrentlyLoggedInUserSessionInBackground(Callback<Void> callback) {
        LOGGER.info("Invalidating currently logged in user.");
        final String url = Urls.ForUser.invalidateSessionUrl().toString();
        final Map<String, String> headers = Headers.assemble();
        final Map<String, Object> payloadMap = new HashMap<String, Object>();
        try {
            AssertUserAuth();
        } catch (UserAuthException e) {
            if (callback != null)
                callback.success(null);
        }
        APJSONObject payload = new APJSONObject(payloadMap);

        postWithVoidCallbackHelper(url, headers, payload, callback);
    }

    public void checkinInBackground(final double[] coordinates, final Callback<Void> callback) {
        LOGGER.info("Checking in currently logged in user.");
        final String url = Urls.ForUser.checkInUserUrl(this.getId(), coordinates).toString();
        final Map<String, String> headers = Headers.assemble();
        AssertUserAuth();
        final Map<String, Object> payloadMap = new HashMap<String, Object>();
        APJSONObject payload = new APJSONObject(payloadMap);

        AsyncHttp asyncHttp = APContainer.build(AsyncHttp.class);
        asyncHttp.post(url, headers, payload.toString(), new APCallback() {
            @Override
            public void success(String result) {

                APJSONObject jsonObject;
                try {
                    jsonObject = new APJSONObject(result);
                } catch (APJSONException e) {
                    throw new RuntimeException(e);
                }
                AppacitiveStatus status = new AppacitiveStatus(jsonObject.optJSONObject("status"));
                if (status.isSuccessful()) {

                    AppacitiveContextBase.setCurrentLocation(coordinates[0], coordinates[1]);

                    if (callback != null)
                        callback.success(null);
                } else {
                    if (callback != null)
                        callback.failure(null, new AppacitiveException(status));
                }

            }

            @Override
            public void failure(Exception e) {
                if (callback != null)
                    callback.failure(null, e);
            }
        });
    }

    public void linkFacebookInBackground(String facebookAccessToken, Callback<Void> callback) {
        LOGGER.info("Linking facebook account.");
        final String url = Urls.ForUser.linkAccountUrl(this.getId()).toString();
        final Map<String, String> headers = Headers.assemble();
        final Map<String, Object> payloadMap = new HashMap<String, Object>();
        AssertUserAuth();
        payloadMap.put("authtype", "facebook");
        payloadMap.put("accesstoken", facebookAccessToken);
        APJSONObject payload = new APJSONObject(payloadMap);

        postWithVoidCallbackHelper(url, headers, payload, callback);
    }

    public void linkTwitterInBackground(String oauthToken, String oauthTokenSecret, String consumerKey, String consumerSecret, Callback<Void> callback) {
        LOGGER.info("Linking twitter account.");
        final String url = Urls.ForUser.linkAccountUrl(this.getId()).toString();
        final Map<String, String> headers = Headers.assemble();
        final Map<String, Object> payloadMap = new HashMap<String, Object>();
        AssertUserAuth();
        payloadMap.put("authtype", "twitter");
        payloadMap.put("oauthtoken", oauthToken);
        payloadMap.put("oauthtokensecret", oauthTokenSecret);

        if (consumerKey != null && consumerKey.isEmpty() == false)
            payloadMap.put("consumerkey", consumerKey);

        if (consumerSecret != null && consumerSecret.isEmpty() == false)
            payloadMap.put("consumersecret", consumerSecret);

        APJSONObject payload = new APJSONObject(payloadMap);

        postWithVoidCallbackHelper(url, headers, payload, callback);
    }

    public void delinkAccountInBackground(String linkName, Callback<Void> callback) {
        LOGGER.info("Delinking account " + linkName);
        final String url = Urls.ForUser.delinkAccountUrl(this.getId(), linkName).toString();
        final Map<String, String> headers = Headers.assemble();
        final Map<String, Object> payloadMap = new HashMap<String, Object>();
        AssertUserAuth();
        APJSONObject payload = new APJSONObject(payloadMap);

        postWithVoidCallbackHelper(url, headers, payload, callback);
    }

    private static void postWithVoidCallbackHelper(String url, Map<String, String> headers, APJSONObject payload, final Callback<Void> callback) {
        AsyncHttp asyncHttp = APContainer.build(AsyncHttp.class);
        asyncHttp.post(url, headers, payload.toString(), new APCallback() {
            @Override
            public void success(String result) {

                APJSONObject jsonObject;
                try {
                    jsonObject = new APJSONObject(result);
                } catch (APJSONException e) {
                    throw new RuntimeException(e);
                }
                AppacitiveStatus status = new AppacitiveStatus(jsonObject.optJSONObject("status"));
                if (status.isSuccessful()) {
                    if (callback != null)
                        callback.success(null);
                } else {
                    if (callback != null)
                        callback.failure(null, new AppacitiveException(status));
                }

            }

            @Override
            public void failure(Exception e) {
                if (callback != null)
                    callback.failure(null, e);
            }
        });
    }

    public void getLinkedAccountInBackground(String linkName, final Callback<Link> callback) {
        LOGGER.info("Fetching linked account " + linkName);
        final String url = Urls.ForUser.getLinkAccountUrl(this.getId(), linkName).toString();
        final Map<String, String> headers = Headers.assemble();
        AssertUserAuth();
        AsyncHttp asyncHttp = APContainer.build(AsyncHttp.class);
        asyncHttp.get(url, headers, new APCallback() {
            @Override
            public void success(String result) {

                APJSONObject jsonObject;
                try {
                    jsonObject = new APJSONObject(result);
                } catch (APJSONException e) {
                    throw new RuntimeException(e);
                }
                AppacitiveStatus status = new AppacitiveStatus(jsonObject.optJSONObject("status"));
                Link link = null;
                APJSONObject identityObject = jsonObject.optJSONObject("identity");
                if (status.isSuccessful()) {
                    if (identityObject != null) {
                        link = new Link();
                        link.setSelf(identityObject);
                    }
                    if (callback != null)
                        callback.success(link);
                } else {
                    if (callback != null)
                        callback.failure(null, new AppacitiveException(status));
                }

            }

            @Override
            public void failure(Exception e) {
                if (callback != null)
                    callback.failure(null, e);
            }
        });
    }

    public void getAllLinkedAccountsInBackground(final Callback<List<Link>> callback) {
        LOGGER.info("Fetching all linked accounts");
        final String url = Urls.ForUser.getAllLinkAccountUrl(this.getId()).toString();
        final Map<String, String> headers = Headers.assemble();
        AssertUserAuth();
        AsyncHttp asyncHttp = APContainer.build(AsyncHttp.class);
        asyncHttp.get(url, headers, new APCallback() {
            @Override
            public void success(String result) {

                APJSONObject jsonObject;
                try {
                    jsonObject = new APJSONObject(result);
                } catch (APJSONException e) {
                    throw new RuntimeException(e);
                }
                AppacitiveStatus status = new AppacitiveStatus(jsonObject.optJSONObject("status"));
                List<Link> returnLinks = new ArrayList<Link>();
                if (status.isSuccessful()) {
                    APJSONArray links = jsonObject.optJSONArray("identities");
                    for (int i = 0; i < links.length(); i++) {
                        APJSONObject linkObject = links.optJSONObject(i);
                        Link link = new Link();
                        link.setSelf(linkObject);
                        returnLinks.add(link);
                    }
                    if (callback != null)
                        callback.success(returnLinks);
                } else {
                    if (callback != null)
                        callback.failure(null, new AppacitiveException(status));
                }

            }

            @Override
            public void failure(Exception e) {
                if (callback != null)
                    callback.failure(null, e);
            }
        });

    }

    public void fetchLatestInBackground(final Callback<Void> callback) {
        LOGGER.info("Fetching latest user with id " + this.getId());
        final String url = Urls.ForUser.getUserUrl(String.valueOf(this.getId()), UserIdType.id, null).toString();
        final Map<String, String> headers = Headers.assemble();
        final AppacitiveUser user = this;
        AsyncHttp asyncHttp = APContainer.build(AsyncHttp.class);
        asyncHttp.get(url, headers, new APCallback() {
            @Override
            public void success(String result) {

                APJSONObject jsonObject;
                try {
                    jsonObject = new APJSONObject(result);
                } catch (APJSONException e) {
                    throw new RuntimeException(e);
                }
                AppacitiveStatus status = new AppacitiveStatus(jsonObject.optJSONObject("status"));
                if (status.isSuccessful()) {
                    user.setSelf(jsonObject.optJSONObject("user"));
                    if (callback != null) {
                        callback.success(null);
                    }
                } else {
                    if (callback != null)
                        callback.failure(null, new AppacitiveException(status));
                }

            }

            @Override
            public void failure(Exception e) {
                if (callback != null)
                    callback.failure(null, e);
            }
        });
    }

    public void getFriends(SocialProvider provider, final Callback<List<AppacitiveUser>> callback)
    {
        LOGGER.info("Fetching friends for user with id " + this.getId());
        final String url = Urls.ForUser.getFriendsUrl(this.getId(), provider.toString().toLowerCase()).toString();
        final Map<String, String> headers = Headers.assemble();
        AsyncHttp asyncHttp = APContainer.build(AsyncHttp.class);
        final List<AppacitiveUser> returnUsers = new ArrayList<AppacitiveUser>();
        asyncHttp.get(url, headers, new APCallback() {
            @Override
            public void success(String result) {
                APJSONObject jsonObject;
                try {
                    jsonObject = new APJSONObject(result);
                } catch (APJSONException e) {
                    throw new RuntimeException(e);
                }
                AppacitiveStatus status = new AppacitiveStatus(jsonObject.optJSONObject("status"));
                if (status.isSuccessful()) {
                    APJSONArray usersArray = jsonObject.optJSONArray("friends");
                    for (int i = 0; i < usersArray.length(); i++) {
                        AppacitiveUser user = new AppacitiveUser();
                        user.setSelf(usersArray.optJSONObject(i));
                        returnUsers.add(user);
                    }
                    if (callback != null)
                        callback.success(returnUsers);
                } else if (callback != null)
                    callback.failure(null, new AppacitiveException(status));
            }

            @Override
            public void failure(Exception e) {
                if (callback != null)
                    callback.failure(null, e);
            }
        });
    }

}
