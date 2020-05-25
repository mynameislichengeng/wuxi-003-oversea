package com.wizarpos.device.printer;

import android.graphics.Bitmap;

/**
 * 打印驱动默认配置
 *
 * @author wu
 */
public class DefaultPrinterImpl implements Printer {

    @Override
    public void print(String str) {
        PrinterHelper.print(str);
    }

    @Override
    public void print(Bitmap bitmap) {
//        PrinterHelper.printBitmap(bitmap);
    }

    @Override
    public void cutPaper() {

    }

}
