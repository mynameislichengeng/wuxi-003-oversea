package com.wizarpos.device.printer.html;

import com.wizarpos.device.printer.html.model.BitmapPrintModel;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * 调用打印机打印Bitmap的维护队列
 * Created by Song on 2017/10/31.
 */

public class BitmapPrinterHelper {
    private static BitmapPrinterHelper instance;
    protected static boolean isBitmapPrinting = false;
    private BlockingQueue<BitmapPrintModel> bq = new LinkedBlockingQueue<>();
    private Thread myTaskThread;

    private BitmapPrinterHelper() {
        myTaskThread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    BitmapPrintModel myTask = null;
                    try {
                        while (isBitmapPrinting) {
                            Thread.sleep(300L);
                        }
                        // 这个地方会主动阻塞
                        isBitmapPrinting = true;
                        myTask = bq.take();
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

    private void handlerPrint(final BitmapPrintModel myTask) {
        PrinterBitmapUtil.printBitmap(myTask.getBm(), 0, 0);
        if (myTask.getImageFile().exists()) {
            myTask.getImageFile().delete();
        }
        isBitmapPrinting = false;
    }

    public synchronized static BitmapPrinterHelper getInstance() {
        if (null == instance) {
            instance = new BitmapPrinterHelper();
        }
        return instance;
    }

    public synchronized void print(final BitmapPrintModel bitmap) {
        bq.add(bitmap);
    }

}
