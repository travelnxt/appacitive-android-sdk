package com.appacitive.sdk;

import com.appacitive.sdk.callbacks.Callback;
import com.appacitive.sdk.exceptions.AppacitiveException;
import com.appacitive.sdk.exceptions.UserAuthException;
import com.appacitive.sdk.exceptions.ValidationException;
import com.appacitive.sdk.infra.APSerializable;
import com.appacitive.sdk.infra.AppacitiveHttp;
import com.appacitive.sdk.infra.Headers;
import com.appacitive.sdk.infra.Urls;

import java.io.Serializable;
import java.text.ParseException;
import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by sathley.
 */
public class AppacitiveUser extends AppacitiveEntity  implements Serializable, APSerializable {

    public final static Logger LOGGER = Logger.getLogger(AppacitiveUser.class.getName());

    public AppacitiveUser(Map<String, Object> user)
    {
        this.setSelf(user);
    }

    public AppacitiveUser()
    {

    }

    public void setSelf(Map<String, Object> user) {

        super.setSelf(user);

        if (user != null) {

            Object object = user.get("__typeid");
            if(object != null)
                this.typeId = Long.parseLong(object.toString());

            object = user.get("__type");
            if(object != null)
                this.type = object.toString();

        }
    }

    public Map<String, Object> getMap() {
        Map<String, Object> nativeMap = super.getMap();
        nativeMap.put("__type", this.type);
        nativeMap.put("__typeid", String.valueOf(this.typeId));

        return nativeMap;
    }

    private String type = null;

    private long typeId = 0;

    public String getType() {
        return type;
    }

    public long getTypeId() {
        return typeId;
    }

    public String getPhone() {
        return this.getProperty("phone");
    }

    public void setPhone(String phone) {
        this.setProperty("phone", phone);

    }

    public String getPassword() {
        return this.getProperty("password");
    }

    public void setPassword(String password) {
        this.setProperty("password", password);

    }

    public String getSecretQuestion() {
        return this.getProperty("secretquestion");
    }

    public void setSecretQuestion(String secretQuestion) {
        this.setProperty("secretquestion", secretQuestion);

    }

    public String getSecretAnswer() {
        return this.getProperty("secretanswer");
    }

    public void setSecretAnswer(String secretAnswer) {
        this.setProperty("secretanswer", secretAnswer);

    }

    public String getFirstName() {
        return this.getProperty("firstname");
    }

    public void setFirstName(String firstName) {
        this.setProperty("firstname", firstName);

    }

    public String getLastName() {
        return this.getProperty("lastname");
    }

    public void setLastName(String lastName) {
        this.setProperty("lastname", lastName);

    }

    public String getEmail() {
        return this.getProperty("email");
    }

    public void setEmail(String email) {
        this.setProperty("email", email);

    }

    public String getUsername() {
        return this.getProperty("username");
    }

    public void setUsername(String username) {
        this.setProperty("username", username);

    }

    public Date getBirthDate() throws ParseException {
        return this.getPropertyAsDate("birthdate");
    }

    public void setBirthDate(Date birthdate) {
        this.setProperty("birthdate", birthdate);

    }

    public double[] getLocation() {
        return this.getPropertyAsGeo("location");
    }

    public void setLocation(String location) {
        this.setProperty("location", location);
    }

    public void signupInBackground(Callback<AppacitiveUser> callback) throws ValidationException {

        List<String> mandatoryFields = new ArrayList<String>() {{
            add("username");
            add("password");
            add("email");
            add("firstname");
        }};
        List<String> missingFields = new ArrayList<String>();
        for (String field : mandatoryFields) {
            if (this.getProperty(field) == null) {
                missingFields.add(field);
            }
        }

        if (missingFields.size() > 0)
            throw new ValidationException("Following mandatory fields are missing. - " + missingFields);

        final String url = Urls.ForUser.createUserUrl().toString();
        final Map<String, String> headers = Headers.assemble();
        final Map<String, Object> payload = this.getMap();
        Future<Map<String, Object>> future = ExecutorServiceWrapper.submit(new Callable<Map<String, Object>>() {
            @Override
            public Map<String, Object> call() throws Exception {
                return AppacitiveHttp.put(url, headers, payload);
            }
        });
        try {
            Map<String, Object> responseMap = future.get();
            AppacitiveStatus status = new AppacitiveStatus((Map<String, Object>) responseMap.get("status"));
            if (status.isSuccessful()) {
                this.setSelf((Map<String, Object>) responseMap.get("user"));
                this.resetUpdateCommands();
                if (callback != null)
                    callback.success(this);
            } else {
                if (callback != null)
                    callback.failure(null, new AppacitiveException(status));
            }

        } catch (Exception e) {
            LOGGER.log(Level.ALL, e.getMessage());
            callback.failure(null, e);
        }
    }

    public static void signupWithFacebookInBackground(final String facebookAccessToken, Callback<AppacitiveUser> callback) throws ValidationException {

        final String url = Urls.ForUser.authenticateUserUrl().toString();
        final Map<String, String> headers = Headers.assemble();
        final Map<String, Object> payload = new HashMap<String, Object>(){{
            put("type", "facebook");
            put("accesstoken", facebookAccessToken);
            put("createnew", "true");
        }};
        Future<Map<String, Object>> future = ExecutorServiceWrapper.submit(new Callable<Map<String, Object>>() {
            @Override
            public Map<String, Object> call() throws Exception {
                return AppacitiveHttp.put(url, headers, payload);
            }
        });
        try {
            Map<String, Object> responseMap = future.get();
            AppacitiveStatus status = new AppacitiveStatus((Map<String, Object>) responseMap.get("status"));
            if (status.isSuccessful()) {
                if (callback != null)
                    callback.success(new AppacitiveUser((Map<String, Object>) responseMap.get("user")));
            } else {
                if (callback != null)
                    callback.failure(null, new AppacitiveException(status));
            }

        } catch (Exception e) {
            LOGGER.log(Level.ALL, e.getMessage());
            callback.failure(null, e);
        }
    }

    public static void getByIdInBackground(long userId, List<String> fields, Callback<AppacitiveUser> callback) throws ValidationException, UserAuthException {

        final String url = Urls.ForUser.getUserUrl(String.valueOf(userId), UserIdType.id, fields).toString();
        final Map<String, String> headers = Headers.assemble();
        AssertUserAuth();
        Future<Map<String, Object>> future = ExecutorServiceWrapper.submit(new Callable<Map<String, Object>>() {
            @Override
            public Map<String, Object> call() throws Exception {
                return AppacitiveHttp.get(url, headers);
            }
        });

        try {
            Map<String, Object> responseMap = future.get();
            AppacitiveStatus status = new AppacitiveStatus((Map<String, Object>) responseMap.get("status"));
            if (status.isSuccessful()) {
                if (callback != null)
                    callback.success(new AppacitiveUser((Map<String, Object>) responseMap.get("user")));
            } else {
                if (callback != null)
                    callback.failure(null, new AppacitiveException(status));
            }

        } catch (Exception e) {
            LOGGER.log(Level.ALL, e.getMessage());
            callback.failure(null, e);
        }
    }

    private static void AssertUserAuth() throws UserAuthException
    {
        String token = AppacitiveContext.getLoggedInUserToken();
        if(token == null || token.isEmpty() == true)
            throw new UserAuthException();
    }

    public static void getByUsernameInBackground(String username, List<String> fields, Callback<AppacitiveUser> callback) throws ValidationException, UserAuthException {

        final String url = Urls.ForUser.getUserUrl(username, UserIdType.username, fields).toString();
        final Map<String, String> headers = Headers.assemble();
        AssertUserAuth();
        Future<Map<String, Object>> future = ExecutorServiceWrapper.submit(new Callable<Map<String, Object>>() {
            @Override
            public Map<String, Object> call() throws Exception {
                return AppacitiveHttp.get(url, headers);
            }
        });

        try {
            Map<String, Object> responseMap = future.get();
            AppacitiveStatus status = new AppacitiveStatus((Map<String, Object>) responseMap.get("status"));
            if (status.isSuccessful()) {
                if (callback != null)
                    callback.success(new AppacitiveUser((Map<String, Object>) responseMap.get("user")));
            } else {
                if (callback != null)
                    callback.failure(null, new AppacitiveException(status));
            }

        } catch (Exception e) {
            LOGGER.log(Level.ALL, e.getMessage());
            callback.failure(null, e);
        }
    }

    public static void getLoggedInUserInBackground(List<String> fields, Callback<AppacitiveUser> callback) throws ValidationException, UserAuthException {

        final String url = Urls.ForUser.getUserUrl("me", UserIdType.token, fields).toString();
        final Map<String, String> headers = Headers.assemble();
        AssertUserAuth();
        Future<Map<String, Object>> future = ExecutorServiceWrapper.submit(new Callable<Map<String, Object>>() {
            @Override
            public Map<String, Object> call() throws Exception {
                return AppacitiveHttp.get(url, headers);
            }
        });

        try {
            Map<String, Object> responseMap = future.get();
            AppacitiveStatus status = new AppacitiveStatus((Map<String, Object>) responseMap.get("status"));
            if (status.isSuccessful()) {
                if (callback != null)
                    callback.success(new AppacitiveUser((Map<String, Object>) responseMap.get("user")));
            } else {
                if (callback != null)
                    callback.failure(null, new AppacitiveException(status));
            }

        } catch (Exception e) {
            LOGGER.log(Level.ALL, e.getMessage());
            callback.failure(null, e);
        }
    }

    public static void loginInBackground(final String username, final String password, long expiry, int attempts, Callback<AppacitiveUser> callback) {
        final String url = Urls.ForUser.authenticateUserUrl().toString();
        final Map<String, String> headers = Headers.assemble();
        final Map<String, Object> payload = new HashMap<String, Object>() {{
            put("username", username);
            put("password", password);
        }};
        if (expiry > 0)
            payload.put("expiry", expiry);

        if (attempts > 0)
            payload.put("attempts", attempts);

        Future<Map<String, Object>> future = ExecutorServiceWrapper.submit(new Callable<Map<String, Object>>() {
            @Override
            public Map<String, Object> call() throws Exception {
                return AppacitiveHttp.post(url, headers, payload);
            }
        });

        try {
            Map<String, Object> responseMap = future.get();
            AppacitiveStatus status = new AppacitiveStatus((Map<String, Object>) responseMap.get("status"));
            if (status.isSuccessful()) {
                String token = (String)responseMap.get("token");
                AppacitiveContext.setLoggedInUserToken(token);
                if(callback != null)
                {
                    callback.success(new AppacitiveUser((Map<String, Object>) responseMap.get("user")));
                }
                else
                {
                    if(callback != null)
                        callback.failure(null, new AppacitiveException(status));
                }
            }
        } catch (Exception e) {
            LOGGER.log(Level.ALL, e.getMessage());
            callback.failure(null, e);
        }
    }

    public static void loginWithFacebookInBackground(final String facebookAccessToken, Callback<AppacitiveUser> callback) {
        final String url = Urls.ForUser.authenticateUserUrl().toString();
        final Map<String, String> headers = Headers.assemble();
        final Map<String, Object> payload = new HashMap<String, Object>() {{
            put("type", "facebook");
            put("accesstoken", facebookAccessToken);
        }};

        Future<Map<String, Object>> future = ExecutorServiceWrapper.submit(new Callable<Map<String, Object>>() {
            @Override
            public Map<String, Object> call() throws Exception {
                return AppacitiveHttp.post(url, headers, payload);
            }
        });

        try {
            Map<String, Object> responseMap = future.get();
            AppacitiveStatus status = new AppacitiveStatus((Map<String, Object>) responseMap.get("status"));
            if (status.isSuccessful()) {
                String token = (String)responseMap.get("token");
                AppacitiveContext.setLoggedInUserToken(token);
                if(callback != null)
                {
                    callback.success(new AppacitiveUser((Map<String, Object>) responseMap.get("user")));
                }
                else
                {
                    if(callback != null)
                        callback.failure(null, new AppacitiveException(status));
                }
            }
        } catch (Exception e) {
            LOGGER.log(Level.ALL, e.getMessage());
            callback.failure(null, e);
        }
    }

    public void loginInBackground(final String password, Callback<String> callback)
    {
        this.loginInBackground(password, Integer.MAX_VALUE, callback);
    }

    public void loginInBackground(final String password, int expiry, Callback<String> callback)
    {
        final String url = Urls.ForUser.authenticateUserUrl().toString();
        final Map<String, String> headers = Headers.assemble();
        final Map<String, Object> payload = new HashMap<String, Object>() {{
            put("username", getUsername());
            put("password", password);
        }};
        if (expiry > 0)
            payload.put("expiry", expiry);

        Future<Map<String, Object>> future = ExecutorServiceWrapper.submit(new Callable<Map<String, Object>>() {
            @Override
            public Map<String, Object> call() throws Exception {
                return AppacitiveHttp.post(url, headers, payload);
            }
        });

        try {
            Map<String, Object> responseMap = future.get();
            AppacitiveStatus status = new AppacitiveStatus((Map<String, Object>) responseMap.get("status"));
            if (status.isSuccessful()) {
                String token = (String)responseMap.get("token");
                AppacitiveContext.setLoggedInUserToken(token);
                if(callback != null)
                {
                    callback.success(token);
                }
                else
                {
                    if(callback != null)
                        callback.failure(null, new AppacitiveException(status));
                }
            }
        } catch (Exception e) {
            LOGGER.log(Level.ALL, e.getMessage());
            callback.failure(null, e);
        }
    }

    public static void multiGetInBackground(List<Long> ids, List<String> fields, Callback<List<AppacitiveUser>> callback) throws ValidationException, UserAuthException {

        final String url = Urls.ForUser.multiGetUserUrl(ids, fields).toString();
        final Map<String, String> headers = Headers.assemble();
//        AssertUserAuth();
        Future<Map<String, Object>> future = ExecutorServiceWrapper.submit(new Callable<Map<String, Object>>() {
            @Override
            public Map<String, Object> call() throws Exception {
                return AppacitiveHttp.get(url, headers);
            }
        });

        try {
            Map<String, Object> responseMap = future.get();
            AppacitiveStatus status = new AppacitiveStatus((Map<String, Object>) responseMap.get("status"));
            if (status.isSuccessful()) {
                ArrayList<Object> objects = (ArrayList<Object>) responseMap.get("objects");
                List<AppacitiveUser> returnUsers = new ArrayList<AppacitiveUser>();
                for (Object user : objects) {
                    returnUsers.add(new AppacitiveUser((Map<String, Object>) user));
                }
                callback.success(returnUsers);
            } else {
                if (callback != null)
                    callback.failure(null, new AppacitiveException(status));
            }
        } catch (Exception e) {
            LOGGER.log(Level.ALL, e.getMessage());
            callback.failure(null, e);
        }
    }

    public static void deleteInBackground(long userId, boolean deleteConnections, Callback<Void> callback) throws UserAuthException{
        final String url = Urls.ForUser.deleteObjectUrl(String.valueOf(userId), UserIdType.id, deleteConnections).toString();
        final Map<String, String> headers = Headers.assemble();
        AssertUserAuth();
        Future<Map<String, Object>> future = ExecutorServiceWrapper.submit(new Callable<Map<String, Object>>() {
            @Override
            public Map<String, Object> call() throws Exception {
                return AppacitiveHttp.delete(url, headers);
            }
        });

        try {
            Map<String, Object> responseMap = future.get();
            AppacitiveStatus status = new AppacitiveStatus((Map<String, Object>) responseMap.get("status"));
            if (status.isSuccessful()) {
                if (callback != null)
                    callback.success(null);
            } else {
                if (callback != null)
                    callback.failure(null, new AppacitiveException(status));
            }
        } catch (Exception e) {
            LOGGER.log(Level.ALL, e.getMessage());
            callback.failure(null, e);
        }
    }

    public static void deleteInBackground(String username, boolean deleteConnections, Callback<Void> callback) throws UserAuthException{
        final String url = Urls.ForUser.deleteObjectUrl(username, UserIdType.username, deleteConnections).toString();
        final Map<String, String> headers = Headers.assemble();
        AssertUserAuth();
        Future<Map<String, Object>> future = ExecutorServiceWrapper.submit(new Callable<Map<String, Object>>() {
            @Override
            public Map<String, Object> call() throws Exception {
                return AppacitiveHttp.delete(url, headers);
            }
        });

        try {
            Map<String, Object> responseMap = future.get();
            AppacitiveStatus status = new AppacitiveStatus((Map<String, Object>) responseMap.get("status"));
            if (status.isSuccessful()) {
                if (callback != null)
                    callback.success(null);
            } else {
                if (callback != null)
                    callback.failure(null, new AppacitiveException(status));
            }
        } catch (Exception e) {
            LOGGER.log(Level.ALL, e.getMessage());
            callback.failure(null, e);
        }
    }

    public static void deleteLoggedInUserInBackground(boolean deleteConnections, Callback<Void> callback) throws UserAuthException{
        final String url = Urls.ForUser.deleteObjectUrl("me", UserIdType.token, deleteConnections).toString();
        final Map<String, String> headers = Headers.assemble();
        AssertUserAuth();
        Future<Map<String, Object>> future = ExecutorServiceWrapper.submit(new Callable<Map<String, Object>>() {
            @Override
            public Map<String, Object> call() throws Exception {
                return AppacitiveHttp.delete(url, headers);
            }
        });

        try {
            Map<String, Object> responseMap = future.get();
            AppacitiveStatus status = new AppacitiveStatus((Map<String, Object>) responseMap.get("status"));
            if (status.isSuccessful()) {
                if (callback != null)
                    callback.success(null);
            } else {
                if (callback != null)
                    callback.failure(null, new AppacitiveException(status));
            }
        } catch (Exception e) {
            LOGGER.log(Level.ALL, e.getMessage());
            callback.failure(null, e);
        }
    }

    public void deleteInBackground(boolean deleteConnections, Callback<Void> callback) throws UserAuthException{
        final String url = Urls.ForUser.deleteObjectUrl(this.getUsername(), UserIdType.username, deleteConnections).toString();
        final Map<String, String> headers = Headers.assemble();
        AssertUserAuth();
        Future<Map<String, Object>> future = ExecutorServiceWrapper.submit(new Callable<Map<String, Object>>() {
            @Override
            public Map<String, Object> call() throws Exception {
                return AppacitiveHttp.delete(url, headers);
            }
        });

        try {
            Map<String, Object> responseMap = future.get();
            AppacitiveStatus status = new AppacitiveStatus((Map<String, Object>) responseMap.get("status"));
            if (status.isSuccessful()) {
                if (callback != null)
                    callback.success(null);
            } else {
                if (callback != null)
                    callback.failure(null, new AppacitiveException(status));
            }
        } catch (Exception e) {
            LOGGER.log(Level.ALL, e.getMessage());
            callback.failure(null, e);
        }
    }

    public void updateInBackground(boolean withRevision, Callback<AppacitiveUser> callback) throws UserAuthException{
        final String url = Urls.ForUser.updateUserUrl(this.typeId, withRevision, this.getRevision()).toString();
        final Map<String, String> headers = Headers.assemble();
        AssertUserAuth();
        final Map<String, Object> payload = new HashMap<String, Object>();
        payload.putAll(super.getUpdateCommand());
        Future<Map<String, Object>> future = ExecutorServiceWrapper.submit(new Callable<Map<String, Object>>() {
            @Override
            public Map<String, Object> call() throws Exception {
                return AppacitiveHttp.post(url, headers, payload);
            }
        });

        try {
            Map<String, Object> responseMap = future.get();
            AppacitiveStatus status = new AppacitiveStatus((Map<String, Object>) responseMap.get("status"));
            if (status.isSuccessful()) {
                this.resetUpdateCommands();
                this.setSelf((Map<String, Object>)responseMap.get("user"));
                if (callback != null)
                    callback.success(this);
            } else {
                if (callback != null)
                    callback.failure(null, new AppacitiveException(status));
            }
        } catch (Exception e) {
            LOGGER.log(Level.ALL, e.getMessage());
            callback.failure(null, e);
        }
    }

    public void updatePasswordInBackground(final String oldPassword, final String newPassword, Callback<Void> callback) throws UserAuthException
    {
        final String url = Urls.ForUser.updatePasswordUrl(this.getId()).toString();
        final Map<String, String> headers = Headers.assemble();
        AssertUserAuth();
        final Map<String, Object> payload = new HashMap<String, Object>(){{
            put("oldpassword", oldPassword);
            put("newpassword", newPassword);
        }};

        Future<Map<String, Object>> future = ExecutorServiceWrapper.submit(new Callable<Map<String, Object>>() {
            @Override
            public Map<String, Object> call() throws Exception {
                return AppacitiveHttp.post(url, headers, payload);
            }
        });

        try {
            Map<String, Object> responseMap = future.get();
            AppacitiveStatus status = new AppacitiveStatus((Map<String, Object>) responseMap.get("status"));
            if (status.isSuccessful()) {
                if (callback != null)
                    callback.success(null);
            } else {
                if (callback != null)
                    callback.failure(null, new AppacitiveException(status));
            }
        } catch (Exception e) {
            LOGGER.log(Level.ALL, e.getMessage());
            callback.failure(null, e);
        }
    }

    public static void sendResetPasswordEmailInBackground(final String username, final String subjectForEmail, Callback<Void> callback)
    {
        final String url = Urls.ForUser.sendResetPasswordEmailUrl().toString();
        final Map<String, String> headers = Headers.assemble();
        final Map<String, Object> payload = new HashMap<String, Object>(){{
            put("username", username);
            put("subject", subjectForEmail);
        }};

        Future<Map<String, Object>> future = ExecutorServiceWrapper.submit(new Callable<Map<String, Object>>() {
            @Override
            public Map<String, Object> call() throws Exception {
                return AppacitiveHttp.post(url, headers, payload);
            }
        });

        try {
            Map<String, Object> responseMap = future.get();
            AppacitiveStatus status = new AppacitiveStatus((Map<String, Object>) responseMap.get("status"));
            if (status.isSuccessful()) {
                if (callback != null)
                    callback.success(null);
            } else {
                if (callback != null)
                    callback.failure(null, new AppacitiveException(status));
            }
        } catch (Exception e) {
            LOGGER.log(Level.ALL, e.getMessage());
            callback.failure(null, e);
        }
    }

    public static void validateCurrentlyLoggedInUserSessionInBackground(Callback<Void> callback) throws UserAuthException
    {
        final String url = Urls.ForUser.validateSessionUrl().toString();
        final Map<String, String> headers = Headers.assemble();
        final Map<String, Object> payload = new HashMap<String, Object>();
        AssertUserAuth();
        Future<Map<String, Object>> future = ExecutorServiceWrapper.submit(new Callable<Map<String, Object>>() {
            @Override
            public Map<String, Object> call() throws Exception {
                return AppacitiveHttp.post(url, headers, payload);
            }
        });

        try {
            Map<String, Object> responseMap = future.get();
            AppacitiveStatus status = new AppacitiveStatus((Map<String, Object>) responseMap.get("status"));
            if (status.isSuccessful()) {
                if (callback != null)
                    callback.success(null);
            } else {
                if (callback != null)
                    callback.failure(null, new AppacitiveException(status));
            }
        } catch (Exception e) {
            LOGGER.log(Level.ALL, e.getMessage());
        }
    }

    public static void invalidateCurrentlyLoggedInUserSessionInBackground(Callback<Void> callback) throws UserAuthException
    {
        final String url = Urls.ForUser.invalidateSessionUrl().toString();
        final Map<String, String> headers = Headers.assemble();
        final Map<String, Object> payload = new HashMap<String, Object>();
        AssertUserAuth();
        Future<Map<String, Object>> future = ExecutorServiceWrapper.submit(new Callable<Map<String, Object>>() {
            @Override
            public Map<String, Object> call() throws Exception {
                return AppacitiveHttp.post(url, headers, payload);
            }
        });

        try {
            Map<String, Object> responseMap = future.get();
            AppacitiveStatus status = new AppacitiveStatus((Map<String, Object>) responseMap.get("status"));
            if (status.isSuccessful()) {
                if (callback != null)
                    callback.success(null);
            } else {
                if (callback != null)
                    callback.failure(null, new AppacitiveException(status));
            }
        } catch (Exception e) {
            LOGGER.log(Level.ALL, e.getMessage());
        }
    }

    public void checkinInBackground(double[] coordinates, Callback<Void> callback) throws UserAuthException
    {
        final String url = Urls.ForUser.checkInUserUrl(this.getId(), coordinates).toString();
        final Map<String, String> headers = Headers.assemble();
        AssertUserAuth();
        final Map<String, Object> payload = new HashMap<String, Object>();

        Future<Map<String, Object>> future = ExecutorServiceWrapper.submit(new Callable<Map<String, Object>>() {
            @Override
            public Map<String, Object> call() throws Exception {
                return AppacitiveHttp.post(url, headers, payload);
            }
        });

        try {
            Map<String, Object> responseMap = future.get();
            AppacitiveStatus status = new AppacitiveStatus((Map<String, Object>) responseMap.get("status"));
            if (status.isSuccessful()) {
                AppacitiveContext.setCurrentLocation(coordinates[0], coordinates[1]);
                if (callback != null)
                    callback.success(null);
            } else {
                if (callback != null)
                    callback.failure(null, new AppacitiveException(status));
            }
        } catch (Exception e) {
            LOGGER.log(Level.ALL, e.getMessage());
        }
    }

    public void linkFacebookInBackground(String facebookAccessToken, Callback<Void> callback) throws UserAuthException
    {
        final String url = Urls.ForUser.linkAccountUrl(this.getId()).toString();
        final Map<String, String> headers = Headers.assemble();
        final Map<String, Object> payload = new HashMap<String, Object>();
        AssertUserAuth();
        payload.put("authtype", "facebook");
        payload.put("accesstoken", facebookAccessToken);
        Future<Map<String, Object>> future = ExecutorServiceWrapper.submit(new Callable<Map<String, Object>>() {
            @Override
            public Map<String, Object> call() throws Exception {
                return AppacitiveHttp.post(url, headers, payload);
            }
        });

        try {
            Map<String, Object> responseMap = future.get();
            AppacitiveStatus status = new AppacitiveStatus((Map<String, Object>) responseMap.get("status"));
            if (status.isSuccessful()) {
                if (callback != null)
                    callback.success(null);
            } else {
                if (callback != null)
                    callback.failure(null, new AppacitiveException(status));
            }
        } catch (Exception e) {
            LOGGER.log(Level.ALL, e.getMessage());
        }
    }

    public void linkTwitterInBackground(String oauthToken, String oauthTokenSecret, String consumerKey, String consumerSecret, Callback<Void> callback) throws UserAuthException
    {
        final String url = Urls.ForUser.linkAccountUrl(this.getId()).toString();
        final Map<String, String> headers = Headers.assemble();
        final Map<String, Object> payload = new HashMap<String, Object>();
        AssertUserAuth();
        payload.put("authtype", "twitter");
        payload.put("oauthtoken", oauthToken);
        payload.put("oauthtokensecret", oauthTokenSecret);

        if(consumerKey != null && consumerKey.isEmpty() == false)
            payload.put("consumerkey", consumerKey);

        if(consumerSecret != null && consumerSecret.isEmpty() == false)
            payload.put("consumersecret", consumerSecret);

        Future<Map<String, Object>> future = ExecutorServiceWrapper.submit(new Callable<Map<String, Object>>() {
            @Override
            public Map<String, Object> call() throws Exception {
                return AppacitiveHttp.post(url, headers, payload);
            }
        });

        try {
            Map<String, Object> responseMap = future.get();
            AppacitiveStatus status = new AppacitiveStatus((Map<String, Object>) responseMap.get("status"));
            if (status.isSuccessful()) {
                if (callback != null)
                    callback.success(null);
            } else {
                if (callback != null)
                    callback.failure(null, new AppacitiveException(status));
            }
        } catch (Exception e) {
            LOGGER.log(Level.ALL, e.getMessage());
        }
    }

    public void delinkAccountInBackground(String linkName, Callback<Void> callback) throws UserAuthException
    {
        final String url = Urls.ForUser.delinkAccountUrl(this.getId(), linkName).toString();
        final Map<String, String> headers = Headers.assemble();
        final Map<String, Object> payload = new HashMap<String, Object>();
        AssertUserAuth();
        Future<Map<String, Object>> future = ExecutorServiceWrapper.submit(new Callable<Map<String, Object>>() {
            @Override
            public Map<String, Object> call() throws Exception {
                return AppacitiveHttp.post(url, headers, payload);
            }
        });

        try {
            Map<String, Object> responseMap = future.get();
            AppacitiveStatus status = new AppacitiveStatus((Map<String, Object>) responseMap.get("status"));
            if (status.isSuccessful()) {
                if (callback != null)
                    callback.success(null);
            } else {
                if (callback != null)
                    callback.failure(null, new AppacitiveException(status));
            }
        } catch (Exception e) {
            LOGGER.log(Level.ALL, e.getMessage());
        }
    }

    public void getLinkedAccountInBackground(String linkName, Callback<Link> callback) throws UserAuthException
    {
        final String url = Urls.ForUser.getLinkAccountUrl(this.getId(), linkName).toString();
        final Map<String, String> headers = Headers.assemble();
        AssertUserAuth();
        Future<Map<String, Object>> future = ExecutorServiceWrapper.submit(new Callable<Map<String, Object>>() {
            @Override
            public Map<String, Object> call() throws Exception {
                return AppacitiveHttp.get(url, headers);
            }
        });

        try {
            Map<String, Object> responseMap = future.get();
            AppacitiveStatus status = new AppacitiveStatus((Map<String, Object>) responseMap.get("status"));
            if (status.isSuccessful()) {
                if (callback != null){
                    Map<String, Object> link = (Map<String, Object>) responseMap.get("identity");
                    callback.success(new Link(link));
                }


            } else {
                if (callback != null)
                    callback.failure(null, new AppacitiveException(status));
            }
        } catch (Exception e) {
            LOGGER.log(Level.ALL, e.getMessage());
        }
    }

    public void getAllLinkedAccountsInBackground(Callback<List<Link>> callback) throws UserAuthException
    {
        final String url = Urls.ForUser.getAllLinkAccountUrl(this.getId()).toString();
        final Map<String, String> headers = Headers.assemble();
        AssertUserAuth();
        Future<Map<String, Object>> future = ExecutorServiceWrapper.submit(new Callable<Map<String, Object>>() {
            @Override
            public Map<String, Object> call() throws Exception {
                return AppacitiveHttp.get(url, headers);
            }
        });

        try {
            Map<String, Object> responseMap = future.get();
            AppacitiveStatus status = new AppacitiveStatus((Map<String, Object>) responseMap.get("status"));
            if (status.isSuccessful()) {
                if (callback != null){
                    ArrayList<Object> links = (ArrayList<Object>) responseMap.get("identities");
                    List<Link> returnLinks = new ArrayList<Link>();
                    for(Object link : links)
                    {
                        returnLinks.add(new Link((Map<String, Object>) link));
                    }
                    callback.success(returnLinks);
                }
            } else {
                if (callback != null)
                    callback.failure(null, new AppacitiveException(status));
            }
        } catch (Exception e) {
            LOGGER.log(Level.ALL, e.getMessage());
        }
    }

}
