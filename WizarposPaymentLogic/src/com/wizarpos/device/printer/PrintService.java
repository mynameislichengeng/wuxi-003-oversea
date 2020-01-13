package com.wizarpos.device.printer;

import android.app.IntentService;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;
import android.util.Log;

import com.wizarpos.atool.log.Logger;
import com.wizarpos.atool.tool.Tools;

import java.io.File;

/**
 * 打印服务
 *
 * @author wu
 */
public class  PrintService extends IntentService {

    private static final String LOG_TAG = PrintService.class.getSimpleName();

    public static final String SERVICE_NAME = "print";

    public static final String TYPE_STRING = "STRING";
    public static final String TYPE_BITMAP = "BITMAP";
    public static final String TYPE_CUT = "CUT";
    public static final String EXTRA_NEED_WAIT = "EXTRA_NEED_WAIT";
    public static final int TYPE_CUT_VALUE = 1;

    public PrintService() {
        super(SERVICE_NAME);
    }

    private Printer printer;

    /**
     * 打印回调
     */
    public interface PrintListener {
        void onFinish();
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.d("print", "开始打印");
        try {
            printer = PrinterManager.getInstance().getPrinter();
            if (printer == null) {
                Log.d("print", "打印驱动为空");
                return;
            }
            String printStr = intent.getStringExtra(TYPE_STRING);
            String printBitmap = intent.getStringExtra(TYPE_BITMAP);
            int printCut = intent.getIntExtra(TYPE_CUT, -1);

            boolean needWait = intent.getBooleanExtra(EXTRA_NEED_WAIT, false);
            if (needWait) {
                Thread.sleep(5 * 1000);
            }

            if (!TextUtils.isEmpty(printStr)) {
                printer.print(printStr);
            } else if (!TextUtils.isEmpty(printBitmap)) {
                Bitmap bitmap = readPng(printBitmap);
                if (bitmap != null) {
                    printer.print(bitmap);
                    recyleBitmap(bitmap);
                }
                Tools.deleteFile(new File(printBitmap));
            } else if (printCut == TYPE_CUT_VALUE) {
                printer.cutPaper();
            }
        } catch (Exception e) {
            e.printStackTrace();
            Logger.debug("打印出错!!!");
        }
        Log.d("print", "打印结束!!!");
    }

    // 平板打印机切纸方法
    static public byte[] cutPaperByte() {
        return new byte[]{(byte) 0x1B, (byte) 0x69};
    }

    synchronized public void cutPaper() {//暂不使用此方式
//		try {
//			PrinterInterface.PrinterOpen();
//			PrinterInterface.PrinterBegin();
//			PrinterInterface
//					.PrinterWrite(cutPaperByte(), cutPaperByte().length);
//		} catch (Exception e) {
//		} finally {
//			PrinterInterface.PrinterEnd();
//			PrinterInterface.PrinterClose();
//		}
    }

    protected void recyleBitmap(Bitmap bitmap) {
        if (bitmap != null && !bitmap.isRecycled()) {
            bitmap.recycle();
        }
    }

    private Bitmap readPng(String path) {
        File mfile = new File(path);
        if (mfile.exists()) {// 若该文件存在
            Bitmap bm = BitmapFactory.decodeFile(path);
            return bm;
        }
        return null;
    }

}
