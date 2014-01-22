package appacitive.exceptions;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sathley.
 */
public class AppacitiveException extends Exception {

    public String code;

    public String message;

    public List<String> additionalMessages;

    public String referenceId;

    public String apiVersion;
}

