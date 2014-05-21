package com.appacitive.java;

import com.appacitive.core.AppacitiveContextBase;
import com.appacitive.core.AppacitiveUser;
import com.appacitive.core.exceptions.UserAuthException;
import com.appacitive.core.exceptions.ValidationException;
import com.appacitive.core.interfaces.LogLevel;
import com.appacitive.core.model.Callback;
import com.appacitive.core.model.Environment;
import com.appacitive.core.model.Link;
import com.jayway.awaitility.Awaitility;
import org.junit.*;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import static com.jayway.awaitility.Awaitility.await;

/**
 * Created by sathley.
 */
public class LinkTest {

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

    private final static String fbToken = Keys.fbToken;
    private final static String password = "password";
    private final static String twitterOAuthToken = Keys.twitterOAuthToken;
    private final static String twitterOAuthTokenSecret = Keys.twitterOAuthTokenSecret;
    private final static String twitterConsumerKey = Keys.twitterConsumerKey;
    private final static String twitterConsumerSecret = Keys.twitterConsumerSecret;

    private String getRandomString() {
        return UUID.randomUUID().toString();
    }

    private AppacitiveUser getRandomUser() {
        AppacitiveUser user = new AppacitiveUser();
        user.setFirstName(getRandomString());
        user.setUsername(getRandomString());
        user.setEmail(getRandomString().concat("@gmail.com"));
        user.setPassword(password);
        return user;
    }

    @Test
    public void linkTwitterAccountTest() throws ValidationException {
        Awaitility.setDefaultTimeout(20, TimeUnit.SECONDS);
        final AtomicBoolean somethingHappened = new AtomicBoolean(false);
        AppacitiveUser user = getRandomUser();
        user.signupInBackground(new Callback<AppacitiveUser>() {
            @Override
            public void success(final AppacitiveUser result) {
                result.loginInBackground(password, new Callback<String>() {
                    @Override
                    public void success(String result1) {
                        try {
                            result.linkTwitterInBackground(twitterOAuthToken, twitterOAuthTokenSecret, twitterConsumerKey, twitterConsumerSecret, new Callback<Void>() {
                                @Override
                                public void success(Void result2) {
                                    try {
                                        result.getLinkedAccountInBackground("twitter", new Callback<Link>() {
                                            @Override
                                            public void success(Link result3) {
                                                assert result3.getUsername().equals("SushantAthley");
                                                try {
                                                    result.deleteInBackground(true, new Callback<Void>() {
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

                                            @Override
                                            public void failure(Link result, Exception e) {
                                                Assert.fail(e.getMessage());

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
    @Ignore
    public void linkFacebookAccountTest() throws ValidationException {
        Awaitility.setDefaultTimeout(20, TimeUnit.SECONDS);
        final AtomicBoolean somethingHappened = new AtomicBoolean(false);
        AppacitiveUser user = getRandomUser();
        user.signupInBackground(new Callback<AppacitiveUser>() {
            @Override
            public void success(final AppacitiveUser result) {
                result.loginInBackground(password, new Callback<String>() {
                    @Override
                    public void success(String result1) {
                        try {
                            result.linkFacebookInBackground(fbToken, new Callback<Void>() {
                                @Override
                                public void success(Void result2) {
                                    try {
                                        result.getLinkedAccountInBackground("facebook", new Callback<Link>() {
                                            @Override
                                            public void success(Link result3) {
                                                assert result3.getUsername().equals("sushant.athley");
                                                try {
                                                    result.deleteInBackground(true, new Callback<Void>() {
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

                                            @Override
                                            public void failure(Link result, Exception e) {
                                                super.failure(result, e);
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
    @Ignore
    public void getAllLinkedAccountsTest() throws ValidationException {
        Awaitility.setDefaultTimeout(20, TimeUnit.SECONDS);
        final AtomicBoolean somethingHappened = new AtomicBoolean(false);
        AppacitiveUser user = getRandomUser();
        user.signupInBackground(new Callback<AppacitiveUser>() {
            @Override
            public void success(final AppacitiveUser result) {
                result.loginInBackground(password, new Callback<String>() {
                    @Override
                    public void success(String result1) {
                        try {
                            result.linkTwitterInBackground(twitterOAuthToken, twitterOAuthTokenSecret, twitterConsumerKey, twitterConsumerSecret, new Callback<Void>() {
                                @Override
                                public void success(Void result2) {
                                    try {
                                        result.linkFacebookInBackground(fbToken,
                                                new Callback<Void>() {
                                                    @Override
                                                    public void success(Void result3) {
                                                        try {
                                                            result.getAllLinkedAccountsInBackground(new Callback<List<Link>>() {
                                                                @Override
                                                                public void success(List<Link> result4) {
                                                                    assert result4 != null;
                                                                    assert result4.size() == 2;

                                                                    try {
                                                                        result.deleteInBackground(true, new Callback<Void>() {
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

                                                                @Override
                                                                public void failure(List<Link> result, Exception e) {
                                                                    Assert.fail(e.getMessage());
                                                                }
                                                            });
                                                        } catch (UserAuthException e) {
                                                            Assert.fail(e.getMessage());
                                                        }
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
    public void deleteLinkedAccountTest() throws ValidationException {
        Awaitility.setDefaultTimeout(20, TimeUnit.SECONDS);
        final AtomicBoolean somethingHappened = new AtomicBoolean(false);
        AppacitiveUser user = getRandomUser();
        user.signupInBackground(new Callback<AppacitiveUser>() {
            @Override
            public void success(final AppacitiveUser result) {
                result.loginInBackground(password, new Callback<String>() {
                    @Override
                    public void success(String token) {
                        try {
                            result.linkTwitterInBackground(twitterOAuthToken, twitterOAuthTokenSecret, twitterConsumerKey, twitterConsumerSecret, new Callback<Void>() {
                                @Override
                                public void success(Void voidResult) {
                                    try {
                                        result.delinkAccountInBackground("twitter", new Callback<Void>() {
                                            @Override
                                            public void success(Void voidResult) {
                                                try {
                                                    result.getLinkedAccountInBackground("twitter", new Callback<Link>() {
                                                        @Override
                                                        public void success(Link result) {
                                                            assert result == null;
                                                            somethingHappened.set(true);
                                                        }

                                                        @Override
                                                        public void failure(Link result, Exception e) {
                                                            Assert.fail(e.getMessage());
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