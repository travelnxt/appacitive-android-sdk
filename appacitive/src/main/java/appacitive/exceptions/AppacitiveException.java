package appacitive.exceptions;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sathley.
 */
public class AppacitiveException extends Exception {

//    self.code = None
//    self.message = None
//    self.additional_messages = None
//    self.reference_id = None
//    self.version = None

    public AppacitiveException(String message)
    {
        this.message = message;
    }

    public String code;

    public String message;

    public List<String> additionalMessages;

    public String referenceId;

    public String apiVersion;
}

