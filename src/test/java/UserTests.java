import com.appacitive.sdk.AppacitiveContext;
import com.appacitive.sdk.AppacitiveObject;
import com.appacitive.sdk.AppacitiveUser;
import com.appacitive.sdk.callbacks.Callback;
import com.appacitive.sdk.exceptions.AppacitiveException;
import com.appacitive.sdk.exceptions.ValidationError;
import com.appacitive.sdk.infra.Environment;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by sathley.
 */
public class UserTests {

    @BeforeClass
    public static void oneTimeSetUp() {
        AppacitiveContext.initialize("up8+oWrzVTVIxl9ZiKtyamVKgBnV5xvmV95u1mEVRrM=", Environment.sandbox);
    }

    @AfterClass
    public static void oneTimeTearDown() {
        // one-time cleanup code
    }

    private String getRandomString()
    {
        return UUID.randomUUID().toString();
    }
    @Test
    public void signupUserTest() throws ValidationError
    {
        AppacitiveUser user = new AppacitiveUser();
        user.setFirstName(getRandomString());
        user.setUsername(getRandomString());
        user.setEmail(getRandomString().concat("@gmail.com"));
        user.setPassword(getRandomString());
        user.signupInBackground(new Callback<AppacitiveUser>() {
            @Override
            public void success(AppacitiveUser result) {
                assert result.getId() > 0;
            }

            @Override
            public void failure(AppacitiveUser result, AppacitiveException e) {
                assert false;
            }
        });
    }

   @Test
    public void loginUserTest() throws ValidationError
   {
       AppacitiveUser user = new AppacitiveUser();
       user.setFirstName(getRandomString());
       user.setUsername(getRandomString());
       user.setEmail(getRandomString().concat("@gmail.com"));
       final String pwd = getRandomString();
       user.setPassword(pwd);
       user.signupInBackground(new Callback<AppacitiveUser>() {
           @Override
           public void success(AppacitiveUser result) {
               result.loginInBackground(pwd, new Callback<String>() {
                   @Override
                   public void success(String result) throws Exception {
                       assert result != null && result.isEmpty() == false;
                   }

                   @Override
                   public void failure(String result, AppacitiveException e) {
                       assert false;
                   }
               });
           }
       });
   }

    @Test
    public void multiGetUsersTest() throws ValidationError
    {
        final List<Long> ids = new ArrayList<Long>();
        AppacitiveUser user = new AppacitiveUser();
        user.setFirstName(getRandomString());
        user.setUsername(getRandomString());
        user.setEmail(getRandomString().concat("@gmail.com"));
        user.setPassword(getRandomString());
        user.signupInBackground(new Callback<AppacitiveUser>() {
            @Override
            public void success(AppacitiveUser result) throws Exception {
                ids.add(result.getId());
                AppacitiveUser user = new AppacitiveUser();
                user.setFirstName(getRandomString());
                user.setUsername(getRandomString());
                user.setEmail(getRandomString().concat("@gmail.com"));
                user.setPassword(getRandomString());
                user.signupInBackground(new Callback<AppacitiveUser>() {
                    @Override
                    public void success(AppacitiveUser result) throws Exception {
                        ids.add(result.getId());
                        AppacitiveUser user = new AppacitiveUser();
                        user.setFirstName(getRandomString());
                        user.setUsername(getRandomString());
                        user.setEmail(getRandomString().concat("@gmail.com"));
                        user.setPassword(getRandomString());
                        user.signupInBackground(new Callback<AppacitiveUser>() {
                            @Override
                            public void success(AppacitiveUser result) throws Exception {
                                ids.add(result.getId());
                                AppacitiveUser.multiGetInBackground(ids, null, new Callback<List<AppacitiveUser>>() {
                                    @Override
                                    public void success(List<AppacitiveUser> result) throws Exception {
                                        assert result != null;
                                        assert result.size() == 3;
                                    }

                                    @Override
                                    public void failure(List<AppacitiveUser> result, AppacitiveException e) {
                                        assert false;
                                    }
                                });
                            }
                        });
                    }
                });
            }
        });
    }

    @Test
    public void deleteUserTest() throws ValidationError
    {
        AppacitiveUser user = new AppacitiveUser();
        user.setFirstName(getRandomString());
        user.setUsername(getRandomString());
        user.setEmail(getRandomString().concat("@gmail.com"));
        final String pwd = getRandomString();
        user.setPassword(pwd);
        user.signupInBackground(new Callback<AppacitiveUser>() {
            @Override
            public void success(final AppacitiveUser result) throws Exception {
                result.loginInBackground(pwd, new Callback<String>() {
                    @Override
                    public void success(String result1) throws Exception {
                        result.deleteInBackground(false, new Callback<Void>() {
                            @Override
                            public void success(Void result1) throws Exception {
                                AppacitiveUser.getByIdInBackground(result.getId(), null, new Callback<AppacitiveUser>() {
                                    @Override
                                    public void success(AppacitiveUser result) throws Exception {
                                        assert false;
                                    }

                                    @Override
                                    public void failure(AppacitiveUser result, AppacitiveException e) {
                                        assert true;
                                    }
                                });
                            }

                            @Override
                            public void failure(Void result, AppacitiveException e) {
                                assert false;
                            }
                        });
                    }
                });

            }
        });
    }

    @Test
    public void changePasswordTest() throws ValidationError
    {
        AppacitiveUser user = new AppacitiveUser();
        user.setFirstName(getRandomString());
        user.setUsername(getRandomString());
        user.setEmail(getRandomString().concat("@gmail.com"));
        final String oldPwd = getRandomString();
        final String newPwd = getRandomString();
        user.setPassword(oldPwd);

        user.signupInBackground(new Callback<AppacitiveUser>() {
            @Override
            public void success(final AppacitiveUser result) throws Exception {
                result.loginInBackground(oldPwd, new Callback<String>() {
                    @Override
                    public void success(String result1) throws Exception {
                        result.updatePasswordInBackground(oldPwd, newPwd, new Callback<Void>() {
                            @Override
                            public void success(Void result2) throws Exception {
                                result.loginInBackground(newPwd, new Callback<String>() {
                                    @Override
                                    public void success(String result) throws Exception {
                                        assert result != null && result.isEmpty() == false;
                                    }

                                    @Override
                                    public void failure(String result, AppacitiveException e) {
                                        assert false;
                                    }
                                });
                            }

                            @Override
                            public void failure(Void result, AppacitiveException e) {
                                assert false;
                            }
                        });
                    }
                });
            }
        });
    }

    @Test
    public void sendResetPasswordTest() throws ValidationError
    {
        AppacitiveUser user = new AppacitiveUser();
        user.setFirstName(getRandomString());
        user.setUsername(getRandomString());
        user.setEmail(getRandomString().concat("@gmail.com"));
        final String pwd = getRandomString();
        user.setPassword(pwd);

        user.signupInBackground(new Callback<AppacitiveUser>() {
            @Override
            public void success(AppacitiveUser result) throws Exception {
                AppacitiveUser.sendResetPasswordEmailInBackground(result.getUsername(), "Reset password Email Subject", new Callback<Void>() {
                    @Override
                    public void success(Void result) throws Exception {
                        assert true;
                    }

                    @Override
                    public void failure(Void result, AppacitiveException e) {
                        assert false;
                    }
                });
            }
        });
    }

    @Test
    public void validateSessionTest() throws ValidationError
    {
        AppacitiveUser user = new AppacitiveUser();
        user.setFirstName(getRandomString());
        user.setUsername(getRandomString());
        user.setEmail(getRandomString().concat("@gmail.com"));
        final String pwd = getRandomString();
        user.setPassword(pwd);
        user.signupInBackground(new Callback<AppacitiveUser>() {
            @Override
            public void success(AppacitiveUser result) throws Exception {
                result.loginInBackground(pwd, new Callback<String>() {
                    @Override
                    public void success(String result) throws Exception {
                        AppacitiveUser.validateCurrentlyLoggedInUserSessionInBackground(new Callback<Void>() {
                            @Override
                            public void success(Void result) throws Exception {
                                assert true;
                            }

                            @Override
                            public void failure(Void result, AppacitiveException e) {
                                assert false;
                            }
                        });
                    }
                });
            }
        });
    }

    @Test
    public void invalidateSessionTest() throws ValidationError
    {
        AppacitiveUser user = new AppacitiveUser();
        user.setFirstName(getRandomString());
        user.setUsername(getRandomString());
        user.setEmail(getRandomString().concat("@gmail.com"));
        final String pwd = getRandomString();
        user.setPassword(pwd);
        user.signupInBackground(new Callback<AppacitiveUser>() {
            @Override
            public void success(AppacitiveUser result) throws Exception {
                result.loginInBackground(pwd, new Callback<String>() {
                    @Override
                    public void success(String result) throws Exception {
                        assert result != null && result.isEmpty() == false;
                        AppacitiveUser.invalidateCurrentlyLoggedInUserSessionInBackground(new Callback<Void>() {
                            @Override
                            public void success(Void result) throws Exception {
                                AppacitiveUser.validateCurrentlyLoggedInUserSessionInBackground(new Callback<Void>() {
                                    @Override
                                    public void success(Void result) throws Exception {
                                        assert false;
                                    }

                                    @Override
                                    public void failure(Void result, AppacitiveException e) {
                                        assert true;
                                    }
                                });

                            }

                            @Override
                            public void failure(Void result, AppacitiveException e) {
                                assert false;
                            }
                        });
                    }
                });
            }
        });
    }

    @Test
    public void checkinTest() throws ValidationError
    {
        AppacitiveUser user = new AppacitiveUser();
        user.setFirstName(getRandomString());
        user.setUsername(getRandomString());
        user.setEmail(getRandomString().concat("@gmail.com"));
        final String pwd = getRandomString();
        user.setPassword(pwd);

        user.signupInBackground(new Callback<AppacitiveUser>() {
            @Override
            public void success(final AppacitiveUser result) throws Exception {
                result.loginInBackground(pwd, new Callback<String>() {
                    @Override
                    public void success(String result1) throws Exception {
                        double[] geo = new double[2];
                        geo[0] = 10.11d;
                        geo[1] = 20.22d;
                        result.checkinInBackground(geo, new Callback<Void>() {
                            @Override
                            public void success(Void result) throws Exception {
                                assert true;
                                Double[] geo = AppacitiveContext.getCurrentLocation();
                                assert geo != null;
                                assert geo[0].equals(10.11);
                                assert geo[1].equals(20.22);
                            }

                            @Override
                            public void failure(Void result, AppacitiveException e) {
                                assert false;
                            }
                        });
                    }
                });
            }
        });
    }
}
