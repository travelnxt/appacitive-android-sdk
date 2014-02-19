package com.appacitive.sdk;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Created by sathley.
 */
class ExecutorServiceWrapper {

    private final static ExecutorService executorService = Executors.newCachedThreadPool();

    public static final void init()
    {}

    public static final void submit(Runnable runnable)
    {
        executorService.submit(runnable);
    }

    public static final <T> Future<T> submit(Callable<T> callable)
    {
        return executorService.submit(callable);
    }

    public static final  void shutdown()
    {
        executorService.shutdown();
    }

    public static final  void shutdownNow()
    {
        executorService.shutdownNow();
    }


}
