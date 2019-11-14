package com.wizarpos.base.net;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import okhttp3.internal.Util;

/**
 * Created by wu on 16/3/29.
 */
public class ExExecutor {


    private ExecutorService executorService;

    public synchronized ExecutorService executorService() {
        if (executorService == null) {
            executorService = new ThreadPoolExecutor(0, Integer.MAX_VALUE, 60, TimeUnit.SECONDS,
                    new SynchronousQueue<Runnable>(), Util.threadFactory("OkHttp BeforeInBackground", false));
        }
        return executorService;
    }

    synchronized void enqueue(Runnable runnable) {
        executorService().execute(runnable);
    }


}
