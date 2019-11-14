package com.wizarpos.base.net;

/**
 * Created by wu on 16/3/29.
 */
public class ExRunnable implements Runnable {

    private ExRunnableCallback listener;

    public ExRunnable(ExRunnableCallback listener) {
        this.listener = listener;
    }

    @Override
    public void run() {
        listener.doInBackground();
    }

    public interface ExRunnableCallback {
        void doInBackground();
    }
}
