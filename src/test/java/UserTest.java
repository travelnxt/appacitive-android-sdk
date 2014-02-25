import com.appacitive.sdk.AppacitiveContext;
import com.appacitive.sdk.AppacitiveUser;
import com.appacitive.sdk.exceptions.UserAuthException;
import com.appacitive.sdk.exceptions.ValidationException;
import com.appacitive.sdk.infra.JavaPlatform;
import com.appacitive.sdk.model.Callback;
import com.appacitive.sdk.model.Environment;
import com.appacitive.sdk.model.PlatformType;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
* Created by sathley.
*/
public class UserTest {

    @BeforeClass
    public static void oneTimeSetUp() {
        AppacitiveContext.initialize("up8+oWrzVTVIxl9ZiKtyamVKgBnV5xvmV95u1mEVRrM=", Environment.sandbox, new JavaPlatform());
    }

    @AfterClass
    public static void oneTimeTearDown() {
        // one-time cleanup code
    }

    private String getRandomString() {
        return UUID.randomUUID().toString();
    }

    @Test
    public void signupUserTest() throws ValidationException {
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
            public void failure(AppacitiveUser result, Exception e) {
                Assert.fail(e.getMessage());
            }
        });
    }

    @Test
    public void loginUserTest() throws ValidationException {
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
                    public void success(String result) {
                        assert result != null && result.isEmpty() == false;
                    }

                    @Override
                    public void failure(String result, Exception e) {
                        Assert.fail(e.getMessage());
                    }
                });
            }
        });
    }

    @Test
    public void multiGetUsersTest() throws ValidationException {
        final List<Long> ids = new ArrayList<Long>();
        AppacitiveUser user = new AppacitiveUser();
        user.setFirstName(getRandomString());
        user.setUsername(getRandomString());
        user.setEmail(getRandomString().concat("@gmail.com"));
        user.setPassword(getRandomString());
        user.signupInBackground(new Callback<AppacitiveUser>() {
            @Override
            public void success(AppacitiveUser result) {
                ids.add(result.getId());
                AppacitiveUser user = new AppacitiveUser();
                user.setFirstName(getRandomString());
                user.setUsername(getRandomString());
                user.setEmail(getRandomString().concat("@gmail.com"));
                user.setPassword(getRandomString());
                try {
                    user.signupInBackground(new Callback<AppacitiveUser>() {
                        @Override
                        public void success(AppacitiveUser result) {
                            ids.add(result.getId());
                            AppacitiveUser user = new AppacitiveUser();
                            user.setFirstName(getRandomString());
                            user.setUsername(getRandomString());
                            user.setEmail(getRandomString().concat("@gmail.com"));
                            user.setPassword(getRandomString());
                            try {
                                user.signupInBackground(new Callback<AppacitiveUser>() {
                                    @Override
                                    public void success(AppacitiveUser result) {
                                        ids.add(result.getId());
                                        try {
                                            AppacitiveUser.multiGetInBackground(ids, null, new Callback<List<AppacitiveUser>>() {
                                                @Override
                                                public void success(List<AppacitiveUser> result) {
                                                    assert result != null;
                                                    assert result.size() == 3;
                                                }

                                                @Override
                                                public void failure(List<AppacitiveUser> result, Exception e) {
                                                    Assert.fail(e.getMessage());
                                                }
                                            });
                                        } catch (Exception e) {
                                            Assert.fail(e.getMessage());
                                        }
                                    }
                                });
                            } catch (ValidationException e) {
                                Assert.fail(e.getMessage());
                            }
                        }
                    });
                } catch (ValidationException e) {
                    Assert.fail(e.getMessage());
                }
            }
        });
    }

    @Test
    public void deleteUserTest() throws ValidationException {
        AppacitiveUser user = new AppacitiveUser();
        user.setFirstName(getRandomString());
        user.setUsername(getRandomString());
        user.setEmail(getRandomString().concat("@gmail.com"));
        final String pwd = getRandomString();
        user.setPassword(pwd);
        user.signupInBackground(new Callback<AppacitiveUser>() {
            @Override
            public void success(final AppacitiveUser result) {
                result.loginInBackground(pwd, new Callback<String>() {
                    @Override
                    public void success(String result1) {
                        try {
                            result.deleteInBackground(false, new Callback<Void>() {
                                @Override
                                public void success(Void result1) {
                                    try {
                                        AppacitiveUser.getByIdInBackground(result.getId(), null, new Callback<AppacitiveUser>() {
                                            @Override
                                            public void success(AppacitiveUser result) {
                                                assert false;
                                            }

                                            @Override
                                            public void failure(AppacitiveUser result, Exception e) {
                                                assert true;
                                            }
                                        });
                                    } catch (Exception e) {
                                        Assert.fail(e.getMessage());
                                    }
                                }

                                @Override
                                public void failure(Void result, Exception e) {
                                    Assert.fail(e.getMessage());
                                }
                            });
                        } catch (Exception e) {
                            Assert.fail(e.getMessage());
                        }
                    }
                });

            }
        });
    }

    @Test
    public void changePasswordTest() throws ValidationException {
        AppacitiveUser user = new AppacitiveUser();
        user.setFirstName(getRandomString());
        user.setUsername(getRandomString());
        user.setEmail(getRandomString().concat("@gmail.com"));
        final String oldPwd = getRandomString();
        final String newPwd = getRandomString();
        user.setPassword(oldPwd);

        user.signupInBackground(new Callback<AppacitiveUser>() {
            @Override
            public void success(final AppacitiveUser result) {
                result.loginInBackground(oldPwd, new Callback<String>() {
                    @Override
                    public void success(String result1) {
                        try {
                            result.updatePasswordInBackground(oldPwd, newPwd, new Callback<Void>() {
                                @Override
                                public void success(Void result2) {
                                    result.loginInBackground(newPwd, new Callback<String>() {
                                        @Override
                                        public void success(String result) {
                                            assert result != null && result.isEmpty() == false;
                                        }

                                        @Override
                                        public void failure(String result, Exception e) {
                                            Assert.fail(e.getMessage());
                                        }
                                    });
                                }

                                @Override
                                public void failure(Void result, Exception e) {
                                    Assert.fail(e.getMessage());
                                }
                            });
                        } catch (UserAuthException e) {
                            Assert.fail(e.getMessage());
                        }
                    }
                });
            }
        });
    }

    @Test
    public void sendResetPasswordTest() throws ValidationException {
        AppacitiveUser user = new AppacitiveUser();
        user.setFirstName(getRandomString());
        user.setUsername(getRandomString());
        user.setEmail(getRandomString().concat("@gmail.com"));
        final String pwd = getRandomString();
        user.setPassword(pwd);

        user.signupInBackground(new Callback<AppacitiveUser>() {
            @Override
            public void success(AppacitiveUser result) {
                AppacitiveUser.sendResetPasswordEmailInBackground(result.getUsername(), "Reset password Email Subject", new Callback<Void>() {
                    @Override
                    public void success(Void result) {
                        assert true;
                    }

                    @Override
                    public void failure(Void result, Exception e) {
                        Assert.fail(e.getMessage());
                    }
                });
            }
        });
    }

    @Test
    public void validateSessionTest() throws ValidationException {
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
                    public void success(String result) {
                        try {
                            AppacitiveUser.validateCurrentlyLoggedInUserSessionInBackground(new Callback<Void>() {
                                @Override
                                public void success(Void result) {
                                    assert true;
                                }

                                @Override
                                public void failure(Void result, Exception e) {
                                    Assert.fail(e.getMessage());
                                }
                            });
                        } catch (UserAuthException e) {
                            Assert.fail(e.getMessage());
                        }
                    }
                });
            }
        });
    }

    @Test
    public void invalidateSessionTest() throws ValidationException {
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
                    public void success(String result) {
                        assert result != null && result.isEmpty() == false;
                        try {
                            AppacitiveUser.invalidateCurrentlyLoggedInUserSessionInBackground(new Callback<Void>() {
                                @Override
                                public void success(Void result) {
                                    try {
                                        AppacitiveUser.validateCurrentlyLoggedInUserSessionInBackground(new Callback<Void>() {
                                            @Override
                                            public void success(Void result) {
                                                assert false;
                                            }

                                            @Override
                                            public void failure(Void result, Exception e) {
                                                assert true;
                                            }
                                        });
                                    } catch (UserAuthException e) {
                                        Assert.fail(e.getMessage());
                                    }

                                }

                                @Override
                                public void failure(Void result, Exception e) {
                                    Assert.fail(e.getMessage());
                                }
                            });
                        } catch (UserAuthException e) {
                            Assert.fail(e.getMessage());
                        }
                    }
                });
            }
        });
    }

    @Test
    public void checkinTest() throws ValidationException {
        AppacitiveUser user = new AppacitiveUser();
        user.setFirstName(getRandomString());
        user.setUsername(getRandomString());
        user.setEmail(getRandomString().concat("@gmail.com"));
        final String pwd = getRandomString();
        user.setPassword(pwd);

        user.signupInBackground(new Callback<AppacitiveUser>() {
            @Override
            public void success(final AppacitiveUser result) {
                result.loginInBackground(pwd, new Callback<String>() {
                    @Override
                    public void success(String result1) {
                        double[] geo = new double[2];
                        geo[0] = 10.11d;
                        geo[1] = 20.22d;
                        try {
                            result.checkinInBackground(geo, new Callback<Void>() {
                                @Override
                                public void success(Void result) {
                                    assert true;
                                    Double[] geo = AppacitiveContext.getCurrentLocation();
                                    assert geo != null;
                                    assert geo[0].equals(10.11);
                                    assert geo[1].equals(20.22);
                                }

                                @Override
                                public void failure(Void result, Exception e) {
                                    Assert.fail(e.getMessage());
                                }
                            });
                        } catch (UserAuthException e) {
                            Assert.fail(e.getMessage());
                        }
                    }
                });
            }
        });
    }
}
