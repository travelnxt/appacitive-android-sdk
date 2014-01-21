package appacitive.callbacks;

import appacitive.exceptions.AppacitiveException;

/**
 * Created by sathley.
 */
public abstract class GetCallBack<T>
{
    public abstract void Done(T entity, AppacitiveException e);
}
