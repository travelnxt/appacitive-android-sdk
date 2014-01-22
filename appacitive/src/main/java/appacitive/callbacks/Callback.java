package appacitive.callbacks;

import appacitive.exceptions.AppacitiveException;

/**
 * Created by sathley.
 */
public abstract class Callback<T>
{
    public abstract void success(T result);

    public abstract void failure(T result, AppacitiveException e);
}
