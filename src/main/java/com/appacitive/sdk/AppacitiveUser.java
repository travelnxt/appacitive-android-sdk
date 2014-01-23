package com.appacitive.sdk;

import java.text.ParseException;
import java.util.Date;
import java.util.logging.Logger;

/**
 * Created by sathley.
 */
public class AppacitiveUser extends AppacitiveEntity {

    private final static Logger LOGGER = Logger.getLogger(AppacitiveUser.class.getName());

//    @property
//    def location(self):
//            return self.get_property('location')
//
//    @location.setter
//    def location(self, value):
//            self.set_property('location', value)
//
//    @property
//    def birthdate(self):
//            return self.get_property('birthdate')
//
//    @birthdate.setter
//    def birthdate(self, value):
//            self.set_property('birthdate', value)
//
//    @property
//    def isemailverified(self):
//            return self.get_property('isemailverified')
//
//    @isemailverified.setter
//    def isemailverified(self, value):
//            self.set_property('isemailverified', value)
//
//    @property
//    def isenabled(self):
//            return self.get_property('isenabled')
//
//    @isenabled.setter
//    def isenabled(self, value):
//            self.set_property('isenabled', value)
//


    public String getPhone()
    {
        return this.getProperty("phone");
    }

    public void setPhone(String phone)
    {
        this.setProperty("phone", phone);

    }

    public String getPassword()
    {
        return this.getProperty("password");
    }

    public void setPassword(String password)
    {
        this.setProperty("password", password);

    }

    public String getSecretQuestion()
    {
        return this.getProperty("secretquestion");
    }

    public void setSecretQuestion(String secretQuestion)
    {
        this.setProperty("secretquestion", secretQuestion);

    }

    public String getSecretAnswer()
    {
        return this.getProperty("secretanswer");
    }

    public void setSecretAnswer(String secretAnswer)
    {
        this.setProperty("secretanswer", secretAnswer);

    }

    public String getFirstname()
    {
        return this.getProperty("firstname");
    }

    public void setFirstname(String firstname)
    {
        this.setProperty("firstname", firstname);

    }

    public String getLastname()
    {
        return this.getProperty("lastname");
    }

    public void setLastname(String lastname)
    {
        this.setProperty("lastname", lastname);

    }

    public String getEmail()
    {
        return this.getProperty("email");
    }

    public void setEmail(String email)
    {
        this.setProperty("email", email);

    }

    public String getUsername()
    {
        return this.getProperty("username");
    }

    public void setUsername(String username)
    {
        this.setProperty("username", username);

    }

    public Date getBirthDate() throws ParseException
    {
        return this.getPropertyAsDate("birthdate");
    }

    public void setBirthDate(Date birthdate)
    {
        this.setProperty("birthdate", birthdate);

    }

    public double[] getLocation()
    {
        return this.getPropertyAsGeo("location");
    }

    public void setLocation(String location)
    {
        this.setProperty("location", location);
    }
}
