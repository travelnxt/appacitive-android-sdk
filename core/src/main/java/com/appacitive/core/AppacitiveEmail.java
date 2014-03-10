package com.appacitive.core;

import com.appacitive.core.exceptions.AppacitiveException;
import com.appacitive.core.infra.*;
import com.appacitive.core.interfaces.AsyncHttp;
import com.appacitive.core.apjson.APJSONArray;
import com.appacitive.core.apjson.APJSONException;
import com.appacitive.core.apjson.APJSONObject;
import com.appacitive.core.model.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

/**
 * Created by sathley.
 */
public class AppacitiveEmail implements Serializable {

    public AppacitiveEmail(String subject) {
        this.subject = subject;
        this.to = new ArrayList<String>();
        this.cc = new ArrayList<String>();
        this.bcc = new ArrayList<String>();
        id = 0;
    }

    public final static Logger LOGGER = Logger.getLogger(AppacitiveEmail.class.getName());

    public void setId(long id) {
        this.id = id;
    }

    private long id;

    public long getId() {
        return this.id;
    }

    public SmtpSettings smtp = null;

    public List<String> to = null;

    public List<String> cc = null;

    public List<String> bcc = null;

    public String fromAddress = null;

    public String replyToAddress = null;

    private String subject = null;

    public EmailBody body = null;

    private APJSONObject getMap() throws APJSONException {
        APJSONObject nativeMap = new APJSONObject();
//        nativeMap.put(SystemDefinedProperties.id, String.valueOf(this.id));
        if (smtp != null)
            nativeMap.put("smtp", smtp.getMap());
        if (to != null && to.size() > 0)
            nativeMap.put("to", new APJSONArray(to));
        if (cc != null && cc.size() > 0)
            nativeMap.putOpt("cc", new APJSONArray(cc));
        if (bcc != null && bcc.size() > 0)
            nativeMap.putOpt("bcc", new APJSONArray(bcc));
        if (fromAddress != null)
            nativeMap.put("from", fromAddress);
        if (replyToAddress != null)
            nativeMap.put("replyto", replyToAddress);
        if (subject != null)
            nativeMap.put("subject", subject);
        if (body != null)
            nativeMap.putOpt("body", body.getMap());
        return nativeMap;
    }

    private void setSelf(APJSONObject email) {
        if (email != null) {
            this.id = Long.parseLong(email.optString(SystemDefinedProperties.id, "0"));
            this.to = new ArrayList<String>();
            if (email.isNull("to") == false) {
                APJSONArray toArray = email.optJSONArray("to");
                for (int i = 0; i < toArray.length(); i++)
                    this.to.add(toArray.optString(i));

            }
            this.cc = new ArrayList<String>();
            if (email.isNull("cc") == false) {
                APJSONArray ccArray = email.optJSONArray("cc");
                for (int i = 0; i < ccArray.length(); i++)
                    this.cc.add(ccArray.optString(i));

            }
            this.bcc = new ArrayList<String>();
            if (email.isNull("bcc") == false) {
                APJSONArray bccArray = email.optJSONArray("bcc");
                for (int i = 0; i < bccArray.length(); i++)
                    this.bcc.add(bccArray.optString(i));

            }

            this.fromAddress = email.optString("from", null);
            this.replyToAddress = email.optString("replyto", null);
            this.subject = email.optString("subject", null);

            this.smtp = null;
            if (email.isNull("smtp") == false) {
                this.smtp = new SmtpSettings();
                this.smtp.setSelf(email.optJSONObject("smtp"));
            }

            this.body = null;
            if (email.isNull("body") == false) {
                APJSONObject body = email.optJSONObject("body");
                if (body.has("content"))
                    this.body = new RawEmailBody();
                else
                    this.body = new TemplatedEmailBody();

                this.body.setSelf(body);
            }
        }
    }

    public AppacitiveEmail withSmtp(SmtpSettings smtp) {
        this.smtp = smtp;
        return this;
    }

    public AppacitiveEmail withBody(EmailBody body) {
        this.body = body;
        return this;
    }

    public void sendInBackground(final Callback<AppacitiveEmail> callback) {
        final String url = Urls.Misc.sendEmailUrl().toString();
        final Map<String, String> headers = Headers.assemble();
        final APJSONObject payload;
        try {
            payload = this.getMap();
        } catch (APJSONException e) {
            throw new RuntimeException(e);
        }

        final AppacitiveEmail email = this;
        AsyncHttp asyncHttp = APContainer.build(AsyncHttp.class);

        asyncHttp.post(url, headers, payload.toString(), new APCallback() {
            @Override
            public void success(String result) {
                try {
                    APJSONObject jsonObject = new APJSONObject(result);
                    AppacitiveStatus status = new AppacitiveStatus(jsonObject.optJSONObject("status"));
                    if (status.isSuccessful()) {
                        email.setSelf(jsonObject.optJSONObject("email"));
                        if (callback != null)
                            callback.success(email);
                    } else {
                        if (callback != null)
                            callback.failure(null, new AppacitiveException(status));
                    }
                } catch (Exception e) {
                    if (callback != null)
                        callback.failure(null, e);
                }
            }

            @Override
            public void failure(Exception e) {
                if (callback != null)
                    callback.failure(null, e);
            }
        });
    }

//    public Future<AppacitiveEmail> sendInBackground() {
//        final String url = Urls.Misc.sendEmailUrl().toString();
//        final Map<String, String> headers = Headers.assemble();
//        final APJSONObject payload = this.getMap();
//        final AppacitiveEmail email = this;
//        AsyncHttp asyncHttp = APContainer.build(AsyncHttp.class);
//
//        final Future<APJSONObject> future = asyncHttp.post(url, headers, payload);
//
//        return new Future<AppacitiveEmail>() {
//            @Override
//            public boolean cancel(boolean mayInterruptIfRunning) {
//                return future.cancel(mayInterruptIfRunning);
//            }
//
//            @Override
//            public boolean isCancelled() {
//                return future.isCancelled();
//            }
//
//            @Override
//            public boolean isDone() {
//                return future.isDone();
//            }
//
//            @Override
//            public AppacitiveEmail get() throws InterruptedException, ExecutionException {
//                APJSONObject result = future.get();
//
//                AppacitiveStatus status = new AppacitiveStatus(result.optJSONObject("status"));
//                if (status.isSuccessful()) {
//                    email.setSelf(result.optJSONObject("email"));
//                    return email;
//                } else {
//                    throw new ExecutionException(new AppacitiveException(status));
//                }
//            }
//
//            @Override
//            public AppacitiveEmail get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
//                APJSONObject result = future.get(timeout, unit);
//
//                AppacitiveStatus status = new AppacitiveStatus(result.optJSONObject("status"));
//                if (status.isSuccessful()) {
//                    email.setSelf(result.optJSONObject("email"));
//                    return email;
//                } else {
//                    throw new ExecutionException(new AppacitiveException(status));
//                }
//            }
//        };
//    }
}
