import com.appacitive.sdk.AppacitiveContext;
import com.appacitive.sdk.AppacitiveUser;
import com.appacitive.sdk.exceptions.UserAuthException;
import com.appacitive.sdk.exceptions.ValidationException;
import com.appacitive.sdk.infra.JavaPlatform;
import com.appacitive.sdk.model.Callback;
import com.appacitive.sdk.model.Environment;
import com.appacitive.sdk.model.Link;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.List;
import java.util.UUID;

/**
 * Created by sathley.
 */
public class LinkTest {

    @BeforeClass
    public static void oneTimeSetUp() {
        AppacitiveContext.initialize("up8+oWrzVTVIxl9ZiKtyamVKgBnV5xvmV95u1mEVRrM=", Environment.sandbox, new JavaPlatform());
    }

    @AfterClass
    public static void oneTimeTearDown() {
        // one-time cleanup code
    }

    private final static String fbToken = "CAACEdEose0cBACxiLXShRg8OicLZAgzFMZCbqh3YulBwBYZBZC2MPX3K8slSXZBFamDqaQ0wfZBa8Bf9wgfVJ0elb8bA3G6WW0EYZCmAKKo6rta7neBRGNxlgRBY6i8vFPCWN2dDedUZBVzSiF3tDtOiUNJ1QLlodgp6USfmZAaZBKZCyBst1ZCVWecZA86lGABpB2kRf0CQ1MuS94QZDZD";
    private final static String password = "password";
    private final static String twitterOAuthToken = "431607023-yb8pICZ1WKdu3qFqCDo5gWbRHwHs9Rg7FoV1PZt9";
    private final static String twitterOAuthTokenSecret = "PIEx8WA5iQ4xicHzttMuq83ZZqOoEBUdQR4g1e4JAA";
    private final static String twitterConsumerKey = "MRlvKD2KZaSnJzecYNK2RA";
    private final static String twitterConsumerSecret = "Pn0tKisq7EfIOkzUF568yGmHdc9bMTM9OPoT6a5wmRc";

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
                                                assert result3.username.equals("SushantAthley");
                                                try {
                                                    result.deleteInBackground(true, new Callback<Void>() {
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
    }

    @Test
    public void linkFacebookAccountTest() throws ValidationException {
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
                                                assert result3.username.equals("sushant.athley");
                                                try {
                                                    result.deleteInBackground(true, new Callback<Void>() {
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
    }

    @Test
    public void getAllLinkedAccountsTest() throws ValidationException {
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
    }
}