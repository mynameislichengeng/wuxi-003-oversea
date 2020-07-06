package com.motion.libsa920sdk.printer.server;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Process;
import android.util.Log;

import com.motion.libsa920sdk.printer.server.config.SettingINI;
import com.motion.libsa920sdk.printer.server.util.AppThreadPool;
import com.motion.libsa920sdk.printer.server.util.POSLinkCreatorWrapper;
import com.motion.libsa920sdk.printer.server.util.POSLinkThreadPool;
import com.pax.poslink.CommSetting;
import com.pax.poslink.ManageRequest;
import com.pax.poslink.PosLink;
import com.pax.poslink.ProcessTransResult;
import com.pax.poslink.peripheries.POSLinkPrinter;
import com.pax.poslink.util.CountRunTime;


public class PaxPrinterServerManager {

    public static PaxPrinterServerManager getInstance(Context context) {
        if (instance == null) {
            instance = new PaxPrinterServerManager();
            instance.initPOSLink(context);
        }
        return instance;
    }

    private final String TAG = PaxPrinterServerManager.class.getSimpleName();

    private static PaxPrinterServerManager instance;
    private PosLink poslink;
    private ManageRequest request;
    private Context context;


    private void initPOSLink(Context context) {
        this.context = context;
        POSLinkCreatorWrapper.createSync(context, new AppThreadPool.FinishInMainThreadCallback<PosLink>() {
            @Override
            public void onFinish(PosLink result) {
                poslink = result;
            }
        });
    }

    public void printer(String content, final POSLinkPrinter.PrintListener listener) {
        settingRequest(content);
        POSLinkThreadPool.getInstance().runInSingleThread(new Runnable() {
            @Override
            public void run() {
                Process.setThreadPriority(Process.THREAD_PRIORITY_BACKGROUND);
                operatePaxPrinter();
                Handler handler = new Handler(Looper.getMainLooper());
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        listener.onSuccess();
                    }
                });
            }
        });

    }

    private void operatePaxPrinter() {
        Log.i(TAG, "ManageRequest.TransType = " + request.TransType);
        // set the folder where to read the "comsetting.ini" file
        poslink.appDataFolder = SettingINI.getAppDataFolder(context);
        CommSetting commSetting = SettingINI.getCommSettingFromFile(context, SettingINI.getCommonSettingPath(context));
        poslink.SetCommSetting(commSetting);
        poslink.ManageRequest = request;
        CountRunTime.start("Manage");
        //play printer
        ProcessTransResult ptr = poslink.ProcessTrans();
        CountRunTime.countPoint("Manage");
    }


    private void settingRequest(String content) {
        request = new ManageRequest();
        request.TransType = request.ParseTransType("PRINTER");
        request.PrintData = content;
    }
}
