package com.motion.libsa920sdk.printer.server.util;

import android.content.Context;
import android.hardware.usb.UsbDevice;
import android.util.Log;
import android.widget.Toast;

import com.motion.libsa920sdk.printer.server.config.SettingINI;
import com.pax.poslink.CommSetting;
import com.pax.poslink.PosLink;
import com.pax.poslink.poslink.POSLinkCreator;
import com.pax.poslink.usb.UsbUtil;

/**
 * Created by Leon on 2017/4/24.
 */

public class POSLinkCreatorWrapper {

    private static PosLink create(Context context) {
        //路径：/data/data/com.pax.poslink/files
        String iniFile = context.getFilesDir().getAbsolutePath() + "/" + SettingINI.FILENAME;
        CommSetting commset = SettingINI.getCommSettingFromFile(context, iniFile);
        if (commset.getType().equals(CommSetting.USB) && !UsbUtil.hasPermission(context)) {
            UsbDevice usbDevice = UsbUtil.getDevice(context);
            if (usbDevice == null) {
                Toast.makeText(context, "Please plug in the POS machine with USB.", Toast.LENGTH_SHORT).show();
            }
        }
        return POSLinkCreator.createPoslink(context);
    }

    public static void createSync(final Context context, final AppThreadPool.FinishInMainThreadCallback<PosLink> callback) {
        Log.i("printer", "Start Create POSLink");
        PosLink posLink = POSLinkCreatorWrapper.create(context);
        callback.onFinish(posLink);
        Log.i("printer", "Finish Create POSLink");

    }
}
