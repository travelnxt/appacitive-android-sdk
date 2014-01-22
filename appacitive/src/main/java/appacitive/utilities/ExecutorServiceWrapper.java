package appacitive.utilities;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Created by sathley.
 */
// TODO : make accessible only from within the package
public class ExecutorServiceWrapper {
    private static ExecutorService executorService;

    public static void init()
    {
        executorService = Executors.newCachedThreadPool();
    }

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
}
