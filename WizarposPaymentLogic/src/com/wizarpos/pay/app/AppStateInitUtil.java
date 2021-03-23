package com.wizarpos.pay.app;

import android.content.Context;

import com.wizarpos.pay.common.Constants;
import com.wizarpos.pay.db.AppState;
import com.wizarpos.pay.db.AppStateDef;
import com.wizarpos.pay.db.AppStateManager;

import java.util.HashMap;

/**
 * Created by wu on 15/12/15.
 */
public class AppStateInitUtil {

    private final static int CACHE_MAX_SIZE = 30;

    public static void init() {
        HashMap<String, String> appStateMap = new HashMap<String, String>(CACHE_MAX_SIZE);
        AppStateManager.setCacheMap(appStateMap);// 运行时状态
        resetAppState();
    }

    public static void resetAppState() {
        try {
            AppStateManager.setState(AppStateDef.isInService, Constants.FALSE);
            AppStateManager.setState(AppStateDef.isOffline, Constants.FALSE);
//            AppStateManager.setState(AppStateDef.isLogin, Constants.FALSE);
            AppStateManager.setState(AppStateDef.isRegisterTerminal, Constants.FALSE);
            AppStateManager.setState(AppStateDef.PUB_CERT_AILAS, "");
            AppStateManager.setState(AppStateDef.LAST_PRINT, "");
//            AppStateManager.setState(AppStateDef.LAST_PAY_MODE, "");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
