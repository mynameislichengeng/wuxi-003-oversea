package com.wizarpos.pay.app;

import android.content.Context;
import android.support.multidex.MultiDex;
import android.text.TextUtils;
import android.util.Log;

import com.igexin.sdk.PushManager;
import com.wizarpos.log.util.LogEx;
import com.wizarpos.log.util.LogEx.LogLevelType;
import com.wizarpos.pay.common.Constants;
import com.wizarpos.pay.common.device.DeviceManager;
import com.wizarpos.pay.common.utils.UuidUitl;
import com.wizarpos.pay.db.AppConfigDef;
import com.wizarpos.pay.db.AppConfigHelper;
import com.wizarpos.pay.service.PushService;
import com.wizarpos.pay.service.ReceiveIntentService;


public class Pay2Application extends PaymentApplication {
    private static Pay2Application application;
    private static final String SPEAK_APP_ID = "59ed526f";
    public static String CLIENT_ID = "";


    @Override
    public void onCreate() {
        super.onCreate();
        application = this;
        // refWatcher = LeakCanary.install(this);
        //initGT();//个推
    }


    private void initGT() {
        // com.getui.demo.DemoPushService 为第三方自定义推送服务
        PushManager.getInstance().initialize(this.getApplicationContext(), PushService.class);
        // com.getui.demo.DemoIntentService 为第三方自定义的推送服务事件接收类
        PushManager.getInstance().registerPushIntentService(this.getApplicationContext(), ReceiveIntentService.class);
    }

    protected void setConfig() {
        CustomActivityOnCrash.install(this);
        if (Constants.DEBUGING) {
            CustomActivityOnCrash.setShowErrorDetails(true);// 显示错误详情, 便于调试
        } else {
            CustomActivityOnCrash.setShowErrorDetails(false);
        }

        if (DeviceManager.getInstance().isWizarDevice() || Constants.DEBUGING) {
            LogEx.setLogLevel(LogLevelType.TYPE_LOG_LEVEL_DEBUG);// 显示debug日志, 便于调试
        }

        // AppConfigHelper.setConfig(AppConfigDef.test_load_safe_mode, Constants.TRUE);// 安全模块
        AppConfigHelper.setConfig(AppConfigDef.test_use_printer, Constants.TRUE);// 打印模块
        // AppConfigHelper.setConfig(AppConfigDef.test_load_bank_mode, Constants.TRUE);// 刷卡

        if (Constants.DEBUGING) { // 测试模式下才允许写入sn号
//			 AppConfigHelper.setConfig(AppConfigDef.sn, "WP14521000000449");
            // AppConfigHelper.setConfig(AppConfigDef.sn, "WP13431000000374");
            // AppConfigHelper.setConfig(AppConfigDef.sn, "WP20150723000271");
            AppConfigHelper.setConfig(AppConfigDef.sn, "WP14451000002339");// POS
//			 AppConfigHelper.setConfig(AppConfigDef.sn, "WP14521000000076");// POS
//			 AppConfigHelper.setConfig(AppConfigDef.sn, "WP14521000000053");// POS
//			 AppConfigHelper.setConfig(AppConfigDef.sn, "WP20150723999999");//cashier2 01商户
//			 AppConfigHelper.setConfig(AppConfigDef.sn, "865863026520796");//MX4 pro
//			 AppConfigHelper.setConfig(AppConfigDef.sn, "WP14081000000007");//微盟券测试sn
//			AppConfigHelper.setConfig(AppConfigDef.sn, "WP13431000000218");
        }
    }

    public static Pay2Application getInstance() {
        return application;
    }



    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }


}
