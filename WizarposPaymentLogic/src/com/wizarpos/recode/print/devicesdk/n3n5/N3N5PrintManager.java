package com.wizarpos.recode.print.devicesdk.n3n5;

import android.content.Context;

import com.nexgo.oaf.apiv3.APIProxy;
import com.nexgo.oaf.apiv3.DeviceEngine;

public class N3N5PrintManager {

    private static N3N5PrintManager instance = new N3N5PrintManager();
    private DeviceEngine deviceEngine;

    public static N3N5PrintManager getInstance() {
        if (instance.deviceEngine == null) {

        }
        return instance;
    }

    public DeviceEngine initEngine(Context context) {
        deviceEngine = APIProxy.getDeviceEngine(context);
        return deviceEngine;
    }

    public DeviceEngine getDeviceEngine() {
        return deviceEngine;
    }
}
