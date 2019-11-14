package com.wizarpos.pay.common.print;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.wizarpos.atool.tool.Tools;
import com.wizarpos.device.printer.PrintServiceController;
import com.wizarpos.device.printer.PrinterManager;
import com.wizarpos.log.util.LogEx;
import com.wizarpos.pay.common.Constants;
import com.wizarpos.pay.common.device.DeviceManager;
import com.wizarpos.pay.common.device.printer.MidFilterPrinterBuilder;

import java.io.File;

/**
 * 打印控制代理
 * Created by wu on 15/11/23.
 */
public class PrintServiceControllerProxy {

    private PrintServiceController printer;
    private MidFilterPrinterBuilder printerSync;

    public PrintServiceControllerProxy(Context context) {
        if (Constants.HANDUI_IS_BLOCK_UI) { //是否阻塞UI
            printerSync = new MidFilterPrinterBuilder();
        } else {
            printer = new PrintServiceController(context);
        }
    }


    /**
     * 打印文本
     */
    public void print(String str) {
        if (!DeviceManager.getInstance().isSupprotPrint()) {
            LogEx.d("print", "不支持的设备类型");
            return;
        }
        setPrinter();
        LogEx.d("print", "开始打印文本: BLOCK_UI " + Constants.HANDUI_IS_BLOCK_UI);
        LogEx.d("print", "打印内容: " + str);
        if (Constants.HANDUI_IS_BLOCK_UI) {
            printerSync.print(str);
        } else {
            printer.print(str);
        }
    }

    /**
     * 打印文本
     */
    public void print(String str, boolean needWait) {
        if (!DeviceManager.getInstance().isSupprotPrint()) {
            LogEx.d("print", "不支持的设备类型");
            return;
        }
        setPrinter();
        LogEx.d("print", "开始打印文本: BLOCK_UI " + Constants.HANDUI_IS_BLOCK_UI);
        LogEx.d("print", "打印内容: " + str);
        if (Constants.HANDUI_IS_BLOCK_UI) {
            printerSync.print(str);
        } else {
            printer.print(str, needWait);
        }
    }

    /**
     * 打印bitmap
     */
    public void print(Bitmap bitmap) {
        if (!DeviceManager.getInstance().isSupprotPrint()) {
            LogEx.d("print", "不支持的设备类型");
            if (bitmap != null && !bitmap.isRecycled()) {
                bitmap.recycle();
            }
            return;
        }
        setPrinter();
        LogEx.d("print", "开始打印图片: BLOCK_UI " + Constants.HANDUI_IS_BLOCK_UI);
        if (Constants.HANDUI_IS_BLOCK_UI) {
            printerSync.print(bitmap);
        } else {
            printer.print(bitmap);
        }
    }

    /**
     * 打印bitmap
     */
    public void printBitmap(String filePath) {
        if (Constants.HANDUI_IS_BLOCK_UI) {
            Bitmap bitmap = readPng(filePath);
            if (bitmap != null) {
                if (printer != null) {
                    printer.print(bitmap);
                }
                recyleBitmap(bitmap);
            }
            Tools.deleteFile(new File(filePath));
        } else {
            printer.printBitmap(filePath);
        }
    }


    public void cutPaper() { //暂不使用此方法
        if (!DeviceManager.getInstance().isSupprotPrint()) {
            LogEx.d("print", "不支持的设备类型");
            return;
        }
//        if (Constants.HANDUI_IS_BLOCK_UI) {
//            printerSync.cutPaper();
//        } else {
//            printer.cutPaper();
//        }
    }


    private Bitmap readPng(String path) {
        File mfile = new File(path);
        if (mfile.exists()) {// 若该文件存在
            Bitmap bm = BitmapFactory.decodeFile(path);
            return bm;
        }
        return null;
    }

    protected void recyleBitmap(Bitmap bitmap) {
        if (bitmap != null && !bitmap.isRecycled()) {
            bitmap.recycle();
        }
//        System.gc();
    }

    private void setPrinter() {
        if (PrinterManager.getInstance().getPrinter() == null) {
            PrinterManager.getInstance().setPrinter(new MidFilterPrinterBuilder());
        }
    }
}
