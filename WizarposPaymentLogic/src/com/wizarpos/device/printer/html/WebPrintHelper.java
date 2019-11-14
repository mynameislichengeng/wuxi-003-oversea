package com.wizarpos.device.printer.html;

import android.os.Handler;

import com.wizarpos.device.printer.html.model.HTMLPrintModel;
import com.wizarpos.pay.app.PaymentApplication;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * 优化WebPrint的调用以及支持批量调用
 * Created by Song on 2017/10/31.
 */

public class WebPrintHelper {
    private static WebPrintHelper instance;
    protected static boolean isWebPrinting = false;
    private BlockingQueue<Object> bq = new LinkedBlockingQueue<Object>();
    private Handler handler = new Handler();
    private Thread myTaskThread;

    private WebPrintHelper() {
        myTaskThread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {

                    String myTask = null;
                    try {
                        while (isWebPrinting) {
                            Thread.sleep(300L);
                        }
                        // 这个地方会主动阻塞
                        isWebPrinting = true;
                        myTask = (String) bq.take();
                    } catch (InterruptedException e1) {
                        e1.printStackTrace();
                    }
                    if (myTask != null) {
                        handlerPrint(myTask);
                    }
                }
            }
        });
        myTaskThread.start();
    }

    private void handlerPrint(final String myTask) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                try {
                    PaymentApplication.getInstance().startActivity(WebPrintActivity.getStartIntent(PaymentApplication.getInstance(), myTask));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public synchronized static WebPrintHelper getInstance() {
        if (null == instance) {
            instance = new WebPrintHelper();
        }
        return instance;
    }

    public synchronized void print(final String str) {
        bq.add(str);
    }

    public synchronized void print(HTMLPrintModel model) {
        print(ToHTMLUtil.toHtml(model));
    }

}
