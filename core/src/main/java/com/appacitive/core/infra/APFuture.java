package com.appacitive.core.infra;

import java.io.Serializable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * Created by sathley.
 */
public abstract class APFuture<T> implements Future<T>, Serializable {
    @Override
    public abstract boolean cancel(boolean mayInterruptIfRunning);

    @Override
    public abstract boolean isCancelled();

    @Override
    public abstract boolean isDone();

    @Override
    public abstract T get() throws InterruptedException, ExecutionException;

    @Override
    public abstract T get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException;
}
