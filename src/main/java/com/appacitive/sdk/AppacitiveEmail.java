package com.appacitive.sdk;

import com.appacitive.sdk.exceptions.AppacitiveException;
import com.appacitive.sdk.infra.*;
import com.appacitive.sdk.interfaces.Http;
import com.appacitive.sdk.model.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.logging.Level;
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

    private Map<String, Object> getMap() {
        Map<String, Object> nativeMap = new HashMap<String, Object>();
//        nativeMap.put(SystemDefinedProperties.id, String.valueOf(this.id));
        if (smtp != null)
            nativeMap.put("smtp", smtp.getMap());
        if (to != null && to.size() > 0)
            nativeMap.put("to", to);
        if (cc != null && cc.size() > 0)
            nativeMap.put("cc", cc);
        if (bcc != null && bcc.size() > 0)
            nativeMap.put("bcc", bcc);
        if (fromAddress != null)
            nativeMap.put("from", fromAddress);
        if (replyToAddress != null)
            nativeMap.put("replyto", replyToAddress);
        if (subject != null)
            nativeMap.put("subject", subject);
        if (body != null)
            nativeMap.put("body", body.getMap());
        return nativeMap;
    }

    private void setSelf(Map<String, Object> entity) {
        Object object = entity.get(SystemDefinedProperties.id);
        if (object != null)
            this.id = Long.parseLong(object.toString());

        object = entity.get("to");
        if (object != null)
            this.to = (List<String>) object;

        object = entity.get("cc");
        if (object != null)
            this.cc = (List<String>) object;

        object = entity.get("bcc");
        if (object != null)
            this.bcc = (List<String>) object;

        object = entity.get("from");
        if (object != null)
            this.fromAddress = object.toString();

        object = entity.get("replyto");
        if (object != null)
            this.replyToAddress = object.toString();
        object = entity.get("subject");
        if (object != null)
            this.subject = object.toString();

        object = entity.get("smtp");
        if (object != null){
            this.smtp = new SmtpSettings();
            this.smtp.setSelf((Map<String, Object>) object);
        }

        object = entity.get("body");
        if (object != null) {
            if (((Map<String, Object>) object).containsKey("content"))
                this.body = new RawEmailBody((Map<String, Object>) object);
            else
                this.body = new TemplatedEmailBody((Map<String, Object>) object);
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

    public void sendInBackground(Callback<AppacitiveEmail> callback) {
        final String url = Urls.Misc.sendEmailUrl().toString();
        final Map<String, String> headers = Headers.assemble();
        final Map<String, Object> payload = this.getMap();

        Future<Map<String, Object>> future = ExecutorServiceWrapper.submit(new Callable<Map<String, Object>>() {
            @Override
            public Map<String, Object> call() throws Exception {
                return APContainer.build(Http.class).post(url, headers, payload);
            }
        });
        AppacitiveStatus status;
        boolean isSuccessful;

        try {
            Map<String, Object> responseMap = future.get();
            status = new AppacitiveStatus((Map<String, Object>) responseMap.get("status"));
            isSuccessful = status.isSuccessful();
            if (isSuccessful) {
                this.setSelf((Map<String, Object>) responseMap.get("email"));
            }
        } catch (Exception e) {
            LOGGER.log(Level.ALL, e.getMessage());
            if (callback != null) callback.failure(null, e);
            return;
        }
        if (isSuccessful) {
            if (callback != null)
                callback.success(this);

        } else {
            if (callback != null)
                callback.failure(null, new AppacitiveException(status));
        }
    }
}
