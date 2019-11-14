package com.wizarpos.device.printer;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;

import com.wizarpos.atool.tool.Tools;

import java.io.File;

public class PrintServiceController {

    protected Context context;

    public PrintServiceController(Context context) {
        this.context = context;
    }

    public static boolean isPrintting(Context context) {
        ActivityManager manager = (ActivityManager) context.getApplicationContext().getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (PrintService.class.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    /**
     * 打印文本
     */
    public void print(String str) {
        Intent intent = new Intent(context, PrintService.class);
        intent.putExtra(PrintService.TYPE_STRING, str);
        context.startService(intent);
    }

    /**
     * 打印文本
     */
    public void print(String str, boolean needWait) {
        Intent intent = new Intent(context, PrintService.class);
        intent.putExtra(PrintService.TYPE_STRING, str);
        intent.putExtra(PrintService.EXTRA_NEED_WAIT, needWait);
        context.startService(intent);
    }

    /**
     * 打印bitmap
     */
    public void print(Bitmap bitmap) {
        Intent intent = new Intent(context, PrintService.class);
        String _path = Tools.getSDPath() + File.separator + "common" + File.separator;
        File file = new File(_path);
        if (!file.exists()) {
            file.mkdirs();
        }
        String filePath = _path + System.currentTimeMillis() + ".jpg";
        Tools.writePng(bitmap, new File(filePath));
        try {
            if (!bitmap.isRecycled()) {
                bitmap.recycle();
            }
        } catch (Exception e) {
        }
        intent.putExtra(PrintService.TYPE_BITMAP, filePath);
        context.startService(intent);
    }

    /**
     * 打印bitmap
     */
    public void printBitmap(String filePath) {
        Intent intent = new Intent(context, PrintService.class);
        intent.putExtra(PrintService.TYPE_BITMAP, filePath);
        context.startService(intent);
    }


    public void cutPaper() {
        Intent intent = new Intent(context, PrintService.class);
        intent.putExtra(PrintService.TYPE_CUT, PrintService.TYPE_CUT_VALUE);
        context.startService(intent);
    }

}
