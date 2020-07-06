package com.motion.libsa920sdk.printer.local;

import android.content.Context;
import android.graphics.Bitmap;

import com.motion.libsa920sdk.printer.server.config.SettingINI;
import com.pax.poslink.CommSetting;
import com.pax.poslink.peripheries.POSLinkPrinter;
import com.pax.poslink.peripheries.ProcessResult;

public class PaxPrinterLocalManager {

    private Context context;


    public static PaxPrinterLocalManager getInstance(Context context) {
        if (instance == null) {
            instance = new PaxPrinterLocalManager();
            instance.init(context);
        }
        return instance;
    }

    private static PaxPrinterLocalManager instance;

    private void init(Context context) {
        this.context = context;
    }


    public void printerText(String content, final POSLinkPrinter.PrintListener listener) {

        POSLinkPrinter.getInstance(context).print(content, POSLinkPrinter.CutMode.DO_NOT_CUT, new POSLinkPrinter.PrintListener() {
            @Override
            public void onSuccess() {
                listener.onSuccess();
            }

            @Override
            public void onError(ProcessResult processResult) {
                listener.onError(processResult);
            }
        });
    }

    public void printerBitmap(Bitmap bitmap, final POSLinkPrinter.PrintListener listener) {

        POSLinkPrinter.getInstance(context).print(bitmap, POSLinkPrinter.CutMode.DO_NOT_CUT, new POSLinkPrinter.PrintListener() {
            @Override
            public void onSuccess() {
                listener.onSuccess();
            }

            @Override
            public void onError(ProcessResult processResult) {
                listener.onError(processResult);
            }
        });
    }

    public void settingPrintWidth(int width){
        POSLinkPrinter.getInstance(context).setPrintWidth(width);
    }

}
