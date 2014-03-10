package com.appacitive.java;

import com.appacitive.core.AppacitiveContextBase;
import com.appacitive.core.AppacitiveUser;
import com.appacitive.core.exceptions.UserAuthException;
import com.appacitive.core.exceptions.ValidationException;
import com.appacitive.core.model.Callback;
import com.appacitive.core.model.Environment;
import com.jayway.awaitility.Awaitility;
import org.junit.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import static com.jayway.awaitility.Awaitility.await;

/**
 * Created by sathley.
 */
public class UserTest {

    @BeforeClass
    public static void oneTimeSetUp() {
        AppacitiveContextBase.initialize("up8+oWrzVTVIxl9ZiKtyamVKgBnV5xvmV95u1mEVRrM=", Environment.sandbox, new JavaPlatform());
        Awaitility.setDefaultTimeout(5, TimeUnit.MINUTES);
    }

    @AfterClass
    public static void oneTimeTearDown() {
        // one-time cleanup code
    }

    private static AtomicBoolean somethingHappened;

    @Before
    public void beforeTest() {
        somethingHappened = new AtomicBoolean(false);
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
                somethingHappened.set(true);
            }

            @Override
            public void failure(AppacitiveUser result, Exception e) {
                Assert.fail(e.getMessage());
            }
        });
        await().untilTrue(somethingHappened);
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
                        somethingHappened.set(true);
                    }

                    @Override
                    public void failure(String result, Exception e) {
                        Assert.fail(e.getMessage());
                    }
                });
            }
        });
        await().untilTrue(somethingHappened);
    }

    @Test
    public void loginWithUsernamePasswordTest
            () throws ValidationException {
        final String username = getRandomString();
        final String password = getRandomString();
        AppacitiveUser user = new AppacitiveUser();
        user.setEmail(getRandomString().concat("@gmail.com"));
        user.setFirstName(getRandomString());
        user.setUsername(username);
        user.setPassword(password);
        user.signupInBackground(new Callback<AppacitiveUser>() {
            @Override
            public void success(final AppacitiveUser result) {
                AppacitiveUser.loginInBackground(username, password, -1,-1, new Callback<AppacitiveUser>() {
                    @Override
                    public void success(AppacitiveUser result1) {
                        assert result.getId() == result1.getId();
                        somethingHappened.set(true);
                    }

                    @Override
                    public void failure(AppacitiveUser result, Exception e) {
                        Assert.fail(e.getMessage());
                    }
                });
            }
        });
        await().untilTrue(somethingHappened);
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
                                                    somethingHappened.set(true);
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
        await().untilTrue(somethingHappened);
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
                                                somethingHappened.set(true);
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
        await().untilTrue(somethingHappened);
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
                                            somethingHappened.set(true);
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
        await().untilTrue(somethingHappened);
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
                        somethingHappened.set(true);
                    }

                    @Override
                    public void failure(Void result, Exception e) {
                        Assert.fail(e.getMessage());
                    }
                });
            }
        });
        await().untilTrue(somethingHappened);
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
                                    somethingHappened.set(true);
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
        await().untilTrue(somethingHappened);
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
                                                Assert.fail();
                                            }

                                            @Override
                                            public void failure(Void result, Exception e) {
                                                assert true;
                                                somethingHappened.set(true);
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
        await().untilTrue(somethingHappened);
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
                                    Double[] geo = AppacitiveContextBase.getCurrentLocation();
                                    assert geo != null;
                                    assert geo[0].equals(10.11);
                                    assert geo[1].equals(20.22);
                                    somethingHappened.set(true);
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
        await().untilTrue(somethingHappened);
    }
}
