package com.wizarpos.pay.app;

import android.content.Context;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.exception.DbException;
import com.nexgo.oaf.apiv3.DeviceEngine;
import com.pax.poslink.POSLinkAndroid;
import com.pos.device.SDKManager;
import com.pos.device.SDKManagerCallback;
import com.wizarpos.device.printer.PrinterManager;
import com.wizarpos.device.printer.html.ToHTMLUtil;
import com.wizarpos.hspos.api.HuashiApi;
import com.wizarpos.log.util.LogEx;
import com.wizarpos.pay.cardlink.model.BankCardTransUploadReq;
import com.wizarpos.pay.common.Constants;
import com.wizarpos.pay.common.device.DefaultCardListenerImpl;
import com.wizarpos.pay.common.device.DefaultDisplayerImpl;
import com.wizarpos.pay.common.device.DeviceManager;
import com.wizarpos.pay.common.device.printer.MidFilterPrinterBuilder;
import com.wizarpos.pay.common.utils.UuidUitl;
import com.wizarpos.pay.db.AppConfig;
import com.wizarpos.pay.db.AppConfigDef;
import com.wizarpos.pay.db.AppConfigHelper;
import com.wizarpos.pay.db.AppState;
import com.wizarpos.pay.db.AppStateManager;
import com.wizarpos.pay.db.CashPayRepair;
import com.wizarpos.pay.db.MerchantCardDef;
import com.wizarpos.pay.db.TicketCardDef;
import com.wizarpos.pay.db.UserBean;
import com.wizarpos.pay.model.UserEntity;
import com.wizarpos.recode.print.devicesdk.amp.AMPPrintManager;
import com.wizarpos.recode.print.devicesdk.n3n5.N3N5PrintManager;

public abstract class PaymentApplication extends ImageLoadApp {

    private static PaymentApplication application;

    private DbUtils dbController;

    private HuashiApi posApi;

    public DeviceEngine deviceEngine;//N3或者N5打印原型

//	private List<Activity> activities = new ArrayList<Activity>();

    private final static int CACHE_MAX_SIZE = 100;

    @Override
    public void onCreate() {
        super.onCreate();
        application = this;

        init();
    }

    public static PaymentApplication getInstance() {
        return application;
    }

    private void init() {
        LogEx.initData(this);
        initPosApi();
        initCrashHandler();
        if (!Constants.UNIFIEDLOGIN_FLAG) {
            initDb();// 建库建表
        }
        initConfig();// 初始化配置项
        if (DeviceManager.getInstance().getDeviceType() == DeviceManager.DEVICE_TYPE_N3_OR_N5) {
            deviceEngine = N3N5PrintManager.getInstance().initEngine(this);
        }
        if (DeviceManager.getInstance().getDeviceType() == DeviceManager.DEVICE_TYPE_PAX_A920) {
            POSLinkAndroid.init(getApplicationContext());
        }


    }

    private void initPosApi() {
        posApi = HuashiApi.getInstance();
        String tmpEmvLibDir = this.getApplicationContext().getDir("", 0).getAbsolutePath();
        tmpEmvLibDir = tmpEmvLibDir.substring(0, tmpEmvLibDir.lastIndexOf('/')) + "/lib/libEMVKernal.so";
        posApi.setContext(this.getApplicationContext(), tmpEmvLibDir);
//        posApi.setContext(this.getApplicationContext());
    }

    private void initPushId() {
        if (AppConfigHelper.getConfig(AppConfigDef.appPushId, "").equals("") && Constants.UNIFIEDLOGIN_FLAG) {
            //如果还没有推送id，则生成
            UuidUitl.createUuid();
            AppConfigHelper.setConfig(AppConfigDef.appPushId, UuidUitl.getUuid());
        }
    }

    protected void initCrashHandler() {

    }

    /**
     * 初始化数据库
     */
    private void initDb() {
        try {
            dbController = DbUtils.create(this, "common.db");
            // 建表
            dbController.createTableIfNotExist(AppConfig.class);// 配置表
            dbController.createTableIfNotExist(AppState.class);// 状态表
            dbController.createTableIfNotExist(CashPayRepair.class);// 离线交易(现金)
            dbController.createTableIfNotExist(MerchantCardDef.class);// 会员卡定义表*
            // 这张表支付易中未使用到
            dbController.createTableIfNotExist(TicketCardDef.class);// 券定义表*
            // 这张表支付易中未使用到
            dbController.createTableIfNotExist(UserBean.class);// 券定义表*
            // 这张表支付易中未使用到
            dbController.createTableIfNotExist(BankCardTransUploadReq.class);
        } catch (DbException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
    }

    /**
     * 初始化数据库 根据mid来创建表
     */
    public void initDb(String mid) {
        try {
            if (TextUtils.isEmpty(mid)) {//需要根据mid来创建表
                throw new RuntimeException("mid为空");
            }
            dbController = DbUtils.create(this, "common_" + mid + ".db");
            // 建表
            dbController.createTableIfNotExist(AppConfig.class);// 配置表
            dbController.createTableIfNotExist(AppState.class);// 状态表
            dbController.createTableIfNotExist(CashPayRepair.class);// 离线交易(现金)
            dbController.createTableIfNotExist(MerchantCardDef.class);// 会员卡定义表*
            // 这张表支付易中未使用到
            dbController.createTableIfNotExist(TicketCardDef.class);// 券定义表*
//            dbController.createTableIfNotExist(UserBean.class);// 券定义表*
            dbController.createTableIfNotExist(UserEntity.class);// 券定义表*
            restoreDb();
            AppConfigInitUtil.defaultInit(this);
//            initConfig();// 初始化配置项
//            initState();// 初始化运行时状态
            Constants.initContantsAfterLogin();
            initState();// 初始化运行时状态
            setConfig();// 子类实现 初始化DB参数
            initDevice();// 初始化驱动
            initPushId();
        } catch (DbException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
    }

    /**
     * @Author: Huangweicai
     * @date 2016-2-16 下午5:36:41
     * @Description:登录完成拿到mid后恢复之前setConfig的数据 将这些数据写入到数据库
     */
    private void restoreDb() {
        AppConfigHelper.restoreMap();
        AppStateManager.restoreMap();
    }

    /**
     * 初始化配置表
     */
    private void initConfig() {
        AppConfigInitUtil.init(this);
    }

    private void initState() {
        AppStateInitUtil.init();
    }

    public static void resetAppState() {
        AppStateInitUtil.resetAppState();
    }

    protected abstract void setConfig();

    /**
     * 驱动
     */
    private void initDevice() {
        DeviceManager deviceManager = DeviceManager.getInstance();
        deviceManager.setDisplayer(new DefaultDisplayerImpl(this));

        deviceManager.setCardListener(new DefaultCardListenerImpl());// wizarpos刷卡
        PrinterManager.getInstance().setPrinter(new MidFilterPrinterBuilder());
        if (DeviceManager.getInstance().getDeviceType() == DeviceManager.DEVICE_TYPE_N3_OR_N5) {

            deviceEngine = N3N5PrintManager.getInstance().initEngine(this);
        } else if (DeviceManager.getInstance().getDeviceType() != DeviceManager.DEVICE_TYPE_WIZARHAND_Q1) {
            ToHTMLUtil.fontSize = ToHTMLUtil.FONT_SIZE_Q2;
        }

    }


    public DbUtils getDbController() {
        return dbController;
    }

    public boolean isWemengMerchant() {
        return Constants.merchantType_weimeng.equals(AppConfigHelper.getConfig(AppConfigDef.merchantType));
    }


    //获取IMEI地址
    public String getImei() {
        String sn = "";
        TelephonyManager mTelephonyMgr = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        sn = mTelephonyMgr.getDeviceId();
        return sn;
    }

    public HuashiApi getPosApi() {
        return posApi;
    }
}
