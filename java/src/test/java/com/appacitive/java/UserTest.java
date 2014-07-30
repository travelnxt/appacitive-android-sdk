package com.appacitive.java;

import com.appacitive.core.AppacitiveContextBase;
import com.appacitive.core.AppacitiveObject;
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
import java.util.concurrent.atomic.AtomicBoolean;

import static com.jayway.awaitility.Awaitility.await;

/**
 * Created by sathley.
 */
public class UserTest {

    @BeforeClass
    public static void oneTimeSetUp() {
        AppacitiveContextBase.initialize(Keys.masterKey, Environment.sandbox, new JavaPlatform());
    }

    @AfterClass
    public static void oneTimeTearDown() {

        // one-time cleanup code
    }

    @Before
    public void beforeTest() {
        Awaitility.reset();
    }

    private String getRandomString() {
        return UUID.randomUUID().toString();
    }

    @Test
    public void signupUserTest() throws ValidationException {
        final AtomicBoolean somethingHappened = new AtomicBoolean(false);
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
        final AtomicBoolean somethingHappened = new AtomicBoolean(false);
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
    public void loginWithUsernamePasswordTest() throws ValidationException {
        final AtomicBoolean somethingHappened = new AtomicBoolean(false);
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
                AppacitiveUser.loginInBackground(username, password, -1, -1, new Callback<AppacitiveUser>() {
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
        final AtomicBoolean somethingHappened = new AtomicBoolean(false);
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
    public void deleteUserTest() {
        final AtomicBoolean somethingHappened = new AtomicBoolean(false);
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
                    }
                });
            }
        });
        await().untilTrue(somethingHappened);
    }

    @Test
    public void changePasswordTest() throws ValidationException {
        final AtomicBoolean somethingHappened = new AtomicBoolean(false);
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
    public void sendResetPasswordTest() {
        final AtomicBoolean somethingHappened = new AtomicBoolean(false);
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
        final AtomicBoolean somethingHappened = new AtomicBoolean(false);
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
                    }
                });
            }
        });
        await().untilTrue(somethingHappened);
    }

    @Test
    public void invalidateSessionTest() throws ValidationException {
        final AtomicBoolean somethingHappened = new AtomicBoolean(false);
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
                        AppacitiveUser.invalidateCurrentlyLoggedInUserSessionInBackground(new Callback<Void>() {
                            @Override
                            public void success(Void result) {
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
                            }

                            @Override
                            public void failure(Void result, Exception e) {
                                Assert.fail(e.getMessage());
                            }
                        });
                    }
                });
            }
        });
        await().untilTrue(somethingHappened);
    }

    @Test
    public void checkinTest() throws ValidationException {
        final AtomicBoolean somethingHappened = new AtomicBoolean(false);
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

                        result.checkinInBackground(geo, new Callback<Void>() {
                            @Override
                            public void success(Void result) {
                                double[] geo = AppacitiveContextBase.getCurrentLocation();
                                assert geo != null;
                                assert geo[0] == (10.11);
                                assert geo[1] == (20.22);
                                somethingHappened.set(true);
                            }

                            @Override
                            public void failure(Void result, Exception e) {
                                Assert.fail(e.getMessage());
                            }
                        });

                    }
                });
            }
        });
        await().untilTrue(somethingHappened);
    }

    @Test
    public void signupUserWithFacebookAccessTokenTest() {
        final AtomicBoolean somethingHappened = new AtomicBoolean(false);
        final String fbToken = "CAAT61GwT5k4BAPa5xqkKIT6CUlZCEPipFM8D8XePHGAsYXgHpO8XiqVz7SvW6iz4ABAgHP7pwlZCjJUnZC4foOMeZAjnOH7ncU8wd2szDzlh9ZBZBnNt18jZCok4TrjDCOm5pmOdMEbvseZBZC9pukw19Ypoeg2IpIgGQqraVZAt6gKZBj5V8tYXLMGdyrRtGwnFpyneifDGPKmVdZAegKaJXD3YYKQqGv1NWGp5C58VSy2EQwZDZD";
        AppacitiveUser.signupWithFacebookInBackground(fbToken, new Callback<AppacitiveUser>() {
            @Override
            public void success(AppacitiveUser result) {
                assert result.getId() > 0;
                AppacitiveUser.loginWithFacebookInBackground(fbToken, new Callback<AppacitiveUser>() {
                    @Override
                    public void success(AppacitiveUser result) {
                        try {
                            result.deleteInBackground(false, new Callback<Void>() {
                                @Override
                                public void success(Void result) {
                                    somethingHappened.set(true);
                                }
                            });
                        } catch (UserAuthException e) {
                            Assert.fail(e.getMessage());
                        }
                    }
                });
            }

            @Override
            public void failure(AppacitiveUser result, Exception e) {
                Assert.fail(e.getMessage());
            }
        });
        await().untilTrue(somethingHappened);
    }

    @Test
    public void sample()
    {
        boolean a = Boolean.parseBoolean("False");

        boolean b = Boolean.parseBoolean("false");
    }

    @Test
    public void getFBFriendsTest()
    {
        final AtomicBoolean somethingHappened = new AtomicBoolean(false);
        AppacitiveUser.signupWithFacebookInBackground("CAACEdEose0cBAGndut68y6mkrmxuWjX4vnR5JQ5AVuobgWgUs9RP8Usy1L0ZABDln4wYnUyehNZANdoGGZA9umThfQMV6ruISnCP3OuZBnl2qfHjD48ZCKO4gnwofiVz4Y81pIlnZABAZCBqVf6kJ6WqtduHLOUYzBVjwIYcYzHYOzlU3W9hm9ZA9hWeVB7AOtLf8isbidL9TskW6JzmI5fY", new Callback<AppacitiveUser>() {
            @Override
            public void success(final AppacitiveUser user1) {
                AppacitiveUser.signupWithFacebookInBackground("CAADMd3sM5bsBAJ1ki1mzNDZCTKpZCD59vUYoYvLg6AeXpsPAX6ZBXczeIQ8oZCigXIVZB4KZB6X8jI1D296vV7ENMzTBbVsuS5G6bNmYXHdO3suEKsD5mvVNFvxyNmaZB4yrmEFgdhIeGZCjn5rZBsZCd2UuqtrgJeRZALPvTXOZButok01dRqhgc7ZBVnCrlTnX6UwHqlGtY77p83pST5ZCQacUKt", new Callback<AppacitiveUser>() {
                    @Override
                    public void success(final AppacitiveUser user2) {
                        user2.getFriends(AppacitiveUser.SocialProvider.FACEBOOK, new Callback<List<AppacitiveUser>>() {
                            @Override
                            public void success(List<AppacitiveUser> result) {
                                assert result.size() > 0;
                                somethingHappened.set(true);
                            }

                            @Override
                            public void failure(List<AppacitiveUser> result, Exception e) {
                                Assert.fail(e.getMessage());
                            }
                        });
                    }

                    @Override
                    public void failure(AppacitiveUser result, Exception e) {
                        Assert.fail(e.getMessage());
                    }
                });
            }

            @Override
            public void failure(AppacitiveUser result, Exception e) {
                Assert.fail(e.getMessage());
            }
        });

        await().untilTrue(somethingHappened);
    }
}
