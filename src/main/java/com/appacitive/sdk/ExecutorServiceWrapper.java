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

    public static void init()
    {}

    public static void submit(Runnable runnable)
    {
        executorService.submit(runnable);
    }

    public static <T> Future<T> submit(Callable<T> callable)
    {
        return executorService.submit(callable);
    }

    public static void shutdown()
    {
        executorService.shutdown();
    }

    public static void shutdownNow()
    {
        executorService.shutdownNow();
    }


}
