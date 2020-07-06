package com.motion.libsa920sdk.printer.server.util;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

public class POSLinkThreadPool {

    private ExecutorService singleThread = Executors.newSingleThreadExecutor(new ThreadFactory() {
        @Override
        public Thread newThread(Runnable runnable) {
            Thread thread = new Thread(runnable);
            thread.setName("POSLinkSingleThread");
            return thread;
        }
    });
    private static POSLinkThreadPool instance;

    public synchronized static POSLinkThreadPool getInstance() {
        if (instance == null) {
            instance = new POSLinkThreadPool();
        }
        return instance;
    }

    private POSLinkThreadPool() {
    }

    public void runInSingleThread(Runnable r) {
        singleThread.execute(r);
    }
}
