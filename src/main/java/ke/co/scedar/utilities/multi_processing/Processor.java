package ke.co.scedar.utilities.multi_processing;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * smis-api (ke.co.scedar.utils.multi_processing)
 * Description:  Processor manages a ThreadPoolExecutor and a ScheduledExecutorService for running tasks.
 **/

public class Processor {

    private static ThreadPoolExecutor executor;
    private static ScheduledExecutorService scheduledExecutor;

    // ANSI color codes for logging
    private static final String ANSI_RESET  = "\u001B[0m";
    private static final String ANSI_GREEN  = "\u001B[32m";
    private static final String ANSI_YELLOW = "\u001B[33m";
    private static final String ANSI_RED    = "\u001B[31m";

    // Initialize  Processor
    public static synchronized void init() {
        if ((executor == null || executor.isShutdown()) && (scheduledExecutor == null || scheduledExecutor.isShutdown())) {
            ThreadFactory namedThreadFactory = new ThreadFactory() {
                private final AtomicInteger counter = new AtomicInteger(1);
                @Override
                public Thread newThread(Runnable r) {
                    return new Thread(r, "Processor-" + counter.getAndIncrement());
                }
            };

            int corePoolSize = Runtime.getRuntime().availableProcessors();
            int maxPoolSize = corePoolSize * 2;
            int queueCapacity = 100;
            int scheduledPoolSize = 1;

            executor = new ThreadPoolExecutor(corePoolSize, maxPoolSize, 60L, TimeUnit.SECONDS,
                    new LinkedBlockingQueue<>(queueCapacity), namedThreadFactory,
                    new ThreadPoolExecutor.CallerRunsPolicy() { //Got this idea from chatGPT if the threads are maxed out and the queue is full have the calling thread complete the task
                        @Override
                        public void rejectedExecution(Runnable r, ThreadPoolExecutor e) {
                            System.err.println("[WARN] Task is running in the calling thread due to overload: " + Thread.currentThread().getName());
                            super.rejectedExecution(r, e);
                        }
                    });

            scheduledExecutor = Executors.newScheduledThreadPool(scheduledPoolSize, namedThreadFactory);

            System.out.println(ANSI_GREEN + "[INFO] Processor thread pools initialized:" + ANSI_RESET);
            System.out.println(ANSI_GREEN + "       - Core Pool Size: " + corePoolSize + ANSI_RESET);
            System.out.println(ANSI_GREEN + "       - Max Pool Size: " + maxPoolSize + ANSI_RESET);
            System.out.println(ANSI_GREEN + "       - Queue Capacity: " + queueCapacity + ANSI_RESET);
            System.out.println(ANSI_GREEN + "       - Scheduled Pool Size: " + scheduledPoolSize + ANSI_RESET);
        }
    }

    // Submits a task for immediate execution
    public static void submitTask(Runnable task) {
        if (executor == null) {
            throw new IllegalStateException("Processor not initialized. Call init() at startup.");
        }
        executor.execute(() -> {
            try {
                task.run();
            } catch (OutOfMemoryError e) {
                System.err.println("Out of memory error occurred when running this Runnable: " + e.getMessage());
            } catch (Throwable t) {
                System.err.println("An error occurred when running this Runnable. Error: " + t.getMessage());
                t.printStackTrace();
            }
        });
    }

    // Submits a task to be executed after a specified delay
    public static void submitTask(Runnable task, long delay, TimeUnit unit) {
        if (scheduledExecutor == null) {
            throw new IllegalStateException("Processor not initialized. Call init() at startup.");
        }
        scheduledExecutor.schedule(task, delay, unit);
    }

    public static int getAvailableThreads() {
        return executor.getQueue().remainingCapacity();
    }

    public static int getCorePoolSize() {
        return executor.getCorePoolSize();
    }


    // Shut down the thread pools gracefully
    public static synchronized void shutdown() {
        System.out.println(ANSI_YELLOW + "[WARN] Shutting down Processor thread pools." + ANSI_RESET);

        if (executor != null && !executor.isShutdown()) {
            executor.shutdown();
            try {
                if (!executor.awaitTermination(Integer.MAX_VALUE, TimeUnit.DAYS)) {
                    System.out.println(ANSI_YELLOW + "[WARN] Forcing shutdown of ThreadPoolExecutor." + ANSI_RESET);
                    executor.shutdownNow();
                }
            } catch (InterruptedException ex) {
                System.out.println(ANSI_RED + "[ERROR] Interrupted during shutdown. Forcing shutdown." + ANSI_RESET);
                executor.shutdownNow();
                Thread.currentThread().interrupt();
            }
        }

        if (scheduledExecutor != null && !scheduledExecutor.isShutdown()) {
            scheduledExecutor.shutdown();
            try {
                if (!scheduledExecutor.awaitTermination(Integer.MAX_VALUE, TimeUnit.DAYS)) {
                    System.out.println(ANSI_YELLOW + "[WARN] Forcing shutdown of ScheduledExecutorService." + ANSI_RESET);
                    scheduledExecutor.shutdownNow();
                }
            } catch (InterruptedException ex) {
                System.out.println(ANSI_RED + "[ERROR] Interrupted during shutdown. Forcing shutdown." + ANSI_RESET);
                scheduledExecutor.shutdownNow();
                Thread.currentThread().interrupt();
            }
        }

        System.out.println(ANSI_GREEN + "[INFO] Processor thread pool shutdown complete." + ANSI_RESET);
    }
}
