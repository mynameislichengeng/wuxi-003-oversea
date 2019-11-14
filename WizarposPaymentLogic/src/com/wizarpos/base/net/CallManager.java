package com.wizarpos.base.net;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import okhttp3.Call;

/**
 * Created by wu on 16/3/17.
 */
public class CallManager {

//    private volatile static CallManager callManager;

    private List<WeakReference<Call>> calls = new ArrayList<>();

//    private CallManager() {
//    }

//    public static CallManager getInstance() {
//        if (callManager == null) {
//            synchronized (CallManager.class) {
//                if (callManager == null) {
//                    callManager = new CallManager();
//                }
//            }
//        }
//        return callManager;
//    }

    public void add(Call call) {
        WeakReference<Call> callWeakReference = new WeakReference<>(call);
        calls.add(callWeakReference);
    }

    public void remove(Call call) {
        Iterator<WeakReference<Call>> iterator = calls.iterator();
        while (iterator.hasNext()) {
            if (call == iterator.next().get()) {
                iterator.remove();
                break;
            }
        }
    }

    public void cancel(Object tag) {
        Iterator<WeakReference<Call>> iterator = calls.iterator();
        while (iterator.hasNext()) {
            Call call = iterator.next().get();
            if (tag == call.request().tag()) {
                call.cancel();
                iterator.remove();
                break;
            }
        }
    }

    public void cancelAll(Object tag) {
        Iterator<WeakReference<Call>> iterator = calls.iterator();
        while (iterator.hasNext()) {
            Call call = iterator.next().get();
            if (tag == call.request().tag()) {
                call.cancel();
                iterator.remove();
            }
        }
    }
}
