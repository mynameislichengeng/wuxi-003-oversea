package com.wizarpos.pay.app;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.wizarpos.atool.tool.Tools;
import com.wizarpos.atool.util.GetSnHelper;
import com.wizarpos.pay.common.Constants;
import com.wizarpos.pay.common.device.DeviceManager;
import com.wizarpos.pay.db.AppConfigDef;
import com.wizarpos.pay.db.AppConfigHelper;
import com.wizarpos.pay.db.UserDao;
import com.wizarpos.pay.db.UserEntityDao;
import com.wizarpos.recode.data.info.SnManager;

import java.util.HashMap;

/**
 * Created by wu on 15/12/15.
 */
public class AppConfigInitUtil {
    //NetBundler http
    public static final String IP_TRAIN = "train.wizarpos.com";         //train
    public static final String IP_CASHIER = "cashier.91huishang.com";     //91
    public static final String IP_C2 = "cashier2.wizarpos.com";      //c2
    public static final String IP_P3 = "portal3.wizarpos.com";       //p3
    public static final String IP_ZQL = "cashier.skypay.cn";          //中青旅
    public static final String IP_DZX = "app.diangedan.net";          //点个单
    public static final String IP_YJJ = "app2.yjinjing.com";          //易筋经
    public static final String IP_INT = "192.168.1.160:8080";          //测试地址
    public static final String IP_INTERNATIONAL_TEST = "haiwai.wizarpos.com";//国际版地址
    //    public static final String PORT_INTERNATIONAL_TEST = "7777";//国际版地址
    public static final String IP_INTERNATIONAL = "motionpaytech.com";//国际版地址
    public static final String IP_INTERNATIONAL_FR = "guigu.wizarpos.com";//法语版测试地址
    public static final String IP_INTERNATIONAL_JPZ = "wa.lyhourpay.com";//柬埔寨地址
    public static final String IP_TEST = "oversea.zc-fund.com";//测试地址
    public static final String IP_INTERNATION_JPZ_TEST = "cambodia.hs1c.cn";//柬埔寨测试地址
    public static final String DEFAULT_PORT = "8080";

    private final static int CACHE_MAX_SIZE = 100;

    public static void init(Context context) {
        HashMap<String, String> appConfig = new HashMap<String, String>(CACHE_MAX_SIZE);
        AppConfigHelper.setCacheMap(appConfig);

        channelInit(context);
        if (null != PaymentApplication.getInstance().getDbController()) {
            defaultInit(context);
        }
    }

    private static void channelInit(Context context) {
        final String channelName = "international";
        Log.d("channel", channelName);
//        public static final String BAT_FLAG =                                 "BAT_FLAG";                                 //是否是bat统一平台支付 默认 true ; 中青旅为 true ; 罗森为 true ;
//        public static final String BAT_V1_4_FLAG =                            "BAT_V1_4_FLAG";                            //是否使用V1.4版本的bat统一平台支付 默认 true ; 威富通为 true @yaosong
//        public static final String ROUNDDING_FLAG =                           "ROUNDDING_FLAG";                           //是否启用四舍五入 默认 false
//        public static final String OTHER_PAY_REMARK_FLAG =                    "OTHER_PAY_REMARK_FLAG";                    //是否启用其他支付备注功能 默认 false ; 易筋经为 true
//        public static final String NEED_BAR_CODE_FLAG =                       "NEED_BAR_CODE_FLAG";                       //是否需要打印流水号条码 默认 false ;
//        public static final String DEBUGING =                                 "DEBUGING";                                 //调试模式 默认 false
//        public static final String IS_BLOCK_UI =                              "IS_BLOCK_UI";                              //POS打印是否阻塞UI线程 默认为 true
//        public static final String HANDUI_IS_BLOCK_UI =                       "HANDUI_IS_BLOCK_UI";                       //手持打印是否阻塞UI线程 默认为 false
//        public static final String SWITCH_KEYBOARD_FLAG =                     "SWITCH_KEYBOARD_FLAG";                     //是否允许切换键盘 默认为 false ;
//        public static final String CONFIG_PAY_MODE =                          "CONFIG_PAY_MODE";                          //是否允许自定义支付方式 默认为  false ; 罗森版本为 true
//        public static final String SHOW_DAY_PRITN =                           "SHOW_DAY_PRITN";                           //是否显示日结单打印功能 默认为  false ; 罗森版本为 true
//        public static final String DISCOUNTABLE =                             "DISCOUNTABLE";                             //是否允许折扣 默认为  false ; 东志信版本为 true wu@[20151115]
//        public static final String THIRD_PAY_TICKET_PUBLISH =                 "THIRD_PAY_TICKET_PUBLISH";                 //第三方支付及其他支付允许发券 默认为 true
//        public static fianl String APP_VERSION_NAME =                         "APP_VERSION_NAME";                         //默认为正常版本
//        public static final String SHOW_BANK_PAY_WHITHOUT_PAYMENTROUTER =     "SHOW_BANK_PAY_WHITHOUT_PAYMENTROUTER";     //没有收单应用是否显示银行卡支付 默认为 true ;中青旅版本为 false

        if ("train".equals(channelName)) {
            AppConfigHelper.setConfig(AppConfigDef.ip, IP_TRAIN);                                              // 1
            AppConfigHelper.setConfig(AppConfigDef.port, Constants.DEFAULT_PORT);                                // 2
            AppConfigHelper.setConfig(AppConfigDef.BAT_FLAG, Constants.TRUE);                                        // 3
            AppConfigHelper.setConfig(AppConfigDef.BAT_V1_4_FLAG, Constants.TRUE);                                        // 4
            AppConfigHelper.setConfig(AppConfigDef.ROUNDDING_FLAG, Constants.FALSE);                                       // 5
            AppConfigHelper.setConfig(AppConfigDef.OTHER_PAY_REMARK_FLAG, Constants.TRUE);                                        // 6
            AppConfigHelper.setConfig(AppConfigDef.NEED_BAR_CODE_FLAG, Constants.FALSE);                                       // 7
            AppConfigHelper.setConfig(AppConfigDef.IS_BLOCK_UI, Constants.TRUE);                                        // 8
            AppConfigHelper.setConfig(AppConfigDef.HANDUI_IS_BLOCK_UI, Constants.FALSE);                                       // 9
            AppConfigHelper.setConfig(AppConfigDef.SWITCH_KEYBOARD_FLAG, Constants.FALSE);                                       // 10
            AppConfigHelper.setConfig(AppConfigDef.SHOW_BANK_PAY_WHITHOUT_PAYMENTROUTER, Constants.TRUE);                                        // 11
            AppConfigHelper.setConfig(AppConfigDef.CONFIG_PAY_MODE, Constants.TRUE);                                        // 12
            AppConfigHelper.setConfig(AppConfigDef.SHOW_DAY_PRITN, Constants.FALSE);                                       // 13
            AppConfigHelper.setConfig(AppConfigDef.DISCOUNTABLE, Constants.FALSE);                                       // 14
            AppConfigHelper.setConfig(AppConfigDef.THIRD_PAY_TICKET_PUBLISH, Constants.TRUE);                                        // 15
            AppConfigHelper.setConfig(AppConfigDef.APP_VERSION_NAME, Constants.APP_VERSION_NORMAL + "");                     // 16
            AppConfigHelper.setConfig(AppConfigDef.isUseNetPay, Constants.FALSE);                                       // 17
            AppConfigHelper.setConfig(AppConfigDef.DEBUGING, Constants.FALSE);                                       // 18
            AppConfigHelper.setConfig(AppConfigDef.isSupportBaiduPay, Constants.TRUE);                                        // 19
            AppConfigHelper.setConfig(AppConfigDef.isSupportOhterPay, Constants.TRUE);                                        // 20
            AppConfigHelper.setConfig(AppConfigDef.isSupportCash, Constants.TRUE);                                        // 21
            AppConfigHelper.setConfig(AppConfigDef.isSupportMemberCard, Constants.TRUE);                                        // 22
        } else if ("international".equals(channelName)) {
            AppConfigHelper.setConfig(AppConfigDef.ip, IP_INTERNATIONAL);                                                 // 1
            AppConfigHelper.setConfig(AppConfigDef.port, Constants.DEFAULT_PORT);                                // 2
            AppConfigHelper.setConfig(AppConfigDef.BAT_FLAG, Constants.TRUE);                                        // 3
            AppConfigHelper.setConfig(AppConfigDef.BAT_V1_4_FLAG, Constants.TRUE);                                        // 4
            AppConfigHelper.setConfig(AppConfigDef.ROUNDDING_FLAG, Constants.FALSE);                                       // 5
            AppConfigHelper.setConfig(AppConfigDef.OTHER_PAY_REMARK_FLAG, Constants.TRUE);                                        // 6
            AppConfigHelper.setConfig(AppConfigDef.NEED_BAR_CODE_FLAG, Constants.FALSE);                                       // 7
            AppConfigHelper.setConfig(AppConfigDef.IS_BLOCK_UI, Constants.TRUE);                                        // 8
            AppConfigHelper.setConfig(AppConfigDef.HANDUI_IS_BLOCK_UI, Constants.FALSE);                                       // 9
            AppConfigHelper.setConfig(AppConfigDef.SWITCH_KEYBOARD_FLAG, Constants.FALSE);                                       // 10
            AppConfigHelper.setConfig(AppConfigDef.SHOW_BANK_PAY_WHITHOUT_PAYMENTROUTER, Constants.TRUE);                                        // 11
            AppConfigHelper.setConfig(AppConfigDef.CONFIG_PAY_MODE, Constants.TRUE);                                        // 12
            AppConfigHelper.setConfig(AppConfigDef.SHOW_DAY_PRITN, Constants.FALSE);                                       // 13
            AppConfigHelper.setConfig(AppConfigDef.DISCOUNTABLE, Constants.FALSE);                                       // 14
            AppConfigHelper.setConfig(AppConfigDef.THIRD_PAY_TICKET_PUBLISH, Constants.TRUE);                                        // 15
            AppConfigHelper.setConfig(AppConfigDef.APP_VERSION_NAME, Constants.APP_VERSION_NORMAL + "");                     // 16
            AppConfigHelper.setConfig(AppConfigDef.isUseNetPay, Constants.FALSE);                                       // 17
            AppConfigHelper.setConfig(AppConfigDef.DEBUGING, Constants.FALSE);                                       // 18
            AppConfigHelper.setConfig(AppConfigDef.isSupportBaiduPay, Constants.TRUE);                                        // 19
            AppConfigHelper.setConfig(AppConfigDef.isSupportOhterPay, Constants.TRUE);                                        // 20
            AppConfigHelper.setConfig(AppConfigDef.isSupportCash, Constants.TRUE);                                        // 21
            AppConfigHelper.setConfig(AppConfigDef.isSupportMemberCard, Constants.TRUE);                                        // 22
        } else if ("p3".equals(channelName)) {
            AppConfigHelper.setConfig(AppConfigDef.ip, IP_P3);                                                 // 1
            AppConfigHelper.setConfig(AppConfigDef.port, Constants.DEFAULT_PORT);                                // 2
            AppConfigHelper.setConfig(AppConfigDef.BAT_FLAG, Constants.TRUE);                                        // 3
            AppConfigHelper.setConfig(AppConfigDef.BAT_V1_4_FLAG, Constants.TRUE);                                        // 4
            AppConfigHelper.setConfig(AppConfigDef.ROUNDDING_FLAG, Constants.FALSE);                                       // 5
            AppConfigHelper.setConfig(AppConfigDef.OTHER_PAY_REMARK_FLAG, Constants.TRUE);                                        // 6
            AppConfigHelper.setConfig(AppConfigDef.NEED_BAR_CODE_FLAG, Constants.FALSE);                                       // 7
            AppConfigHelper.setConfig(AppConfigDef.IS_BLOCK_UI, Constants.TRUE);                                        // 8
            AppConfigHelper.setConfig(AppConfigDef.HANDUI_IS_BLOCK_UI, Constants.FALSE);                                       // 9
            AppConfigHelper.setConfig(AppConfigDef.SWITCH_KEYBOARD_FLAG, Constants.FALSE);                                       // 10
            AppConfigHelper.setConfig(AppConfigDef.SHOW_BANK_PAY_WHITHOUT_PAYMENTROUTER, Constants.TRUE);                                        // 11
            AppConfigHelper.setConfig(AppConfigDef.CONFIG_PAY_MODE, Constants.TRUE);                                        // 12
            AppConfigHelper.setConfig(AppConfigDef.SHOW_DAY_PRITN, Constants.FALSE);                                       // 13
            AppConfigHelper.setConfig(AppConfigDef.DISCOUNTABLE, Constants.FALSE);                                       // 14
            AppConfigHelper.setConfig(AppConfigDef.THIRD_PAY_TICKET_PUBLISH, Constants.TRUE);                                        // 15
            AppConfigHelper.setConfig(AppConfigDef.APP_VERSION_NAME, Constants.APP_VERSION_NORMAL + "");                     // 16
            AppConfigHelper.setConfig(AppConfigDef.isUseNetPay, Constants.FALSE);                                       // 17
            AppConfigHelper.setConfig(AppConfigDef.DEBUGING, Constants.FALSE);                                       // 18
            AppConfigHelper.setConfig(AppConfigDef.isSupportBaiduPay, Constants.TRUE);                                        // 19
            AppConfigHelper.setConfig(AppConfigDef.isSupportOhterPay, Constants.TRUE);                                        // 20
            AppConfigHelper.setConfig(AppConfigDef.isSupportCash, Constants.TRUE);                                        // 21
            AppConfigHelper.setConfig(AppConfigDef.isSupportMemberCard, Constants.TRUE);                                        // 22
        } else if ("c2".equals(channelName)) {
            AppConfigHelper.setConfig(AppConfigDef.ip, IP_C2);                                                 // 1
            AppConfigHelper.setConfig(AppConfigDef.port, Constants.DEFAULT_PORT);                                // 2
            AppConfigHelper.setConfig(AppConfigDef.BAT_FLAG, Constants.TRUE);                                        // 3
            AppConfigHelper.setConfig(AppConfigDef.BAT_V1_4_FLAG, Constants.TRUE);                                        // 4
            AppConfigHelper.setConfig(AppConfigDef.ROUNDDING_FLAG, Constants.FALSE);                                       // 5
            AppConfigHelper.setConfig(AppConfigDef.OTHER_PAY_REMARK_FLAG, Constants.TRUE);                                        // 6
            AppConfigHelper.setConfig(AppConfigDef.NEED_BAR_CODE_FLAG, Constants.FALSE);                                       // 7
            AppConfigHelper.setConfig(AppConfigDef.IS_BLOCK_UI, Constants.TRUE);                                        // 8
            AppConfigHelper.setConfig(AppConfigDef.HANDUI_IS_BLOCK_UI, Constants.FALSE);                                       // 9
            AppConfigHelper.setConfig(AppConfigDef.SWITCH_KEYBOARD_FLAG, Constants.FALSE);                                       // 10
            AppConfigHelper.setConfig(AppConfigDef.SHOW_BANK_PAY_WHITHOUT_PAYMENTROUTER, Constants.TRUE);                                        // 11
            AppConfigHelper.setConfig(AppConfigDef.CONFIG_PAY_MODE, Constants.TRUE);                                        // 12
            AppConfigHelper.setConfig(AppConfigDef.SHOW_DAY_PRITN, Constants.FALSE);                                       // 13
            AppConfigHelper.setConfig(AppConfigDef.DISCOUNTABLE, Constants.FALSE);                                       // 14
            AppConfigHelper.setConfig(AppConfigDef.THIRD_PAY_TICKET_PUBLISH, Constants.TRUE);                                        // 15
            AppConfigHelper.setConfig(AppConfigDef.APP_VERSION_NAME, Constants.APP_VERSION_NORMAL + "");                     // 16
            AppConfigHelper.setConfig(AppConfigDef.isUseNetPay, Constants.FALSE);                                       // 17
            AppConfigHelper.setConfig(AppConfigDef.DEBUGING, Constants.FALSE);                                       // 18
            AppConfigHelper.setConfig(AppConfigDef.isSupportBaiduPay, Constants.TRUE);                                        // 19
            AppConfigHelper.setConfig(AppConfigDef.isSupportOhterPay, Constants.TRUE);                                        // 20
            AppConfigHelper.setConfig(AppConfigDef.isSupportCash, Constants.TRUE);                                        // 21
            AppConfigHelper.setConfig(AppConfigDef.isSupportMemberCard, Constants.TRUE);                                        // 22
        } else if ("cashier".equals(channelName)) {
            AppConfigHelper.setConfig(AppConfigDef.ip, IP_CASHIER);                                            // 1
            AppConfigHelper.setConfig(AppConfigDef.port, Constants.DEFAULT_PORT);                                // 2
            AppConfigHelper.setConfig(AppConfigDef.BAT_FLAG, Constants.TRUE);                                        // 3
            AppConfigHelper.setConfig(AppConfigDef.BAT_V1_4_FLAG, Constants.TRUE);                                        // 4
            AppConfigHelper.setConfig(AppConfigDef.ROUNDDING_FLAG, Constants.FALSE);                                       // 5
            AppConfigHelper.setConfig(AppConfigDef.OTHER_PAY_REMARK_FLAG, Constants.TRUE);                                        // 6
            AppConfigHelper.setConfig(AppConfigDef.NEED_BAR_CODE_FLAG, Constants.FALSE);                                       // 7
            AppConfigHelper.setConfig(AppConfigDef.IS_BLOCK_UI, Constants.TRUE);                                        // 8
            AppConfigHelper.setConfig(AppConfigDef.HANDUI_IS_BLOCK_UI, Constants.FALSE);                                       // 9
            AppConfigHelper.setConfig(AppConfigDef.SWITCH_KEYBOARD_FLAG, Constants.FALSE);                                       // 10
            AppConfigHelper.setConfig(AppConfigDef.SHOW_BANK_PAY_WHITHOUT_PAYMENTROUTER, Constants.TRUE);                                        // 11
            AppConfigHelper.setConfig(AppConfigDef.CONFIG_PAY_MODE, Constants.TRUE);                                        // 12
            AppConfigHelper.setConfig(AppConfigDef.SHOW_DAY_PRITN, Constants.FALSE);                                       // 13
            AppConfigHelper.setConfig(AppConfigDef.DISCOUNTABLE, Constants.FALSE);                                       // 14
            AppConfigHelper.setConfig(AppConfigDef.THIRD_PAY_TICKET_PUBLISH, Constants.TRUE);                                        // 15
            AppConfigHelper.setConfig(AppConfigDef.APP_VERSION_NAME, Constants.APP_VERSION_NORMAL + "");                     // 16
            AppConfigHelper.setConfig(AppConfigDef.isUseNetPay, Constants.FALSE);                                       // 17
            AppConfigHelper.setConfig(AppConfigDef.DEBUGING, Constants.FALSE);                                       // 18
            AppConfigHelper.setConfig(AppConfigDef.isSupportBaiduPay, Constants.TRUE);                                        // 19
            AppConfigHelper.setConfig(AppConfigDef.isSupportOhterPay, Constants.TRUE);                                        // 20
            AppConfigHelper.setConfig(AppConfigDef.isSupportCash, Constants.TRUE);                                        // 21
            AppConfigHelper.setConfig(AppConfigDef.isSupportMemberCard, Constants.TRUE);                                        // 22
        } else if ("zql".equals(channelName)) {
            AppConfigHelper.setConfig(AppConfigDef.ip, IP_ZQL);                                                // 1
            AppConfigHelper.setConfig(AppConfigDef.port, Constants.DEFAULT_PORT);                                // 2
            AppConfigHelper.setConfig(AppConfigDef.BAT_FLAG, Constants.TRUE);                                        // 3
            AppConfigHelper.setConfig(AppConfigDef.BAT_V1_4_FLAG, Constants.TRUE);                                        // 4
            AppConfigHelper.setConfig(AppConfigDef.ROUNDDING_FLAG, Constants.FALSE);                                       // 5
            AppConfigHelper.setConfig(AppConfigDef.OTHER_PAY_REMARK_FLAG, Constants.TRUE);                                        // 6
            AppConfigHelper.setConfig(AppConfigDef.NEED_BAR_CODE_FLAG, Constants.FALSE);                                       // 7
            AppConfigHelper.setConfig(AppConfigDef.IS_BLOCK_UI, Constants.TRUE);                                        // 8
            AppConfigHelper.setConfig(AppConfigDef.HANDUI_IS_BLOCK_UI, Constants.FALSE);                                       // 9
            AppConfigHelper.setConfig(AppConfigDef.SWITCH_KEYBOARD_FLAG, Constants.FALSE);                                       // 10
            AppConfigHelper.setConfig(AppConfigDef.SHOW_BANK_PAY_WHITHOUT_PAYMENTROUTER, Constants.FALSE);                                       // 11
            AppConfigHelper.setConfig(AppConfigDef.CONFIG_PAY_MODE, Constants.TRUE);                                        // 12
            AppConfigHelper.setConfig(AppConfigDef.SHOW_DAY_PRITN, Constants.FALSE);                                       // 13
            AppConfigHelper.setConfig(AppConfigDef.DISCOUNTABLE, Constants.FALSE);                                       // 14
            AppConfigHelper.setConfig(AppConfigDef.THIRD_PAY_TICKET_PUBLISH, Constants.TRUE);                                        // 15
            AppConfigHelper.setConfig(AppConfigDef.APP_VERSION_NAME, Constants.APP_VERSION_NORMAL + "");                     // 16
            AppConfigHelper.setConfig(AppConfigDef.isUseNetPay, Constants.FALSE);                                       // 17
            AppConfigHelper.setConfig(AppConfigDef.DEBUGING, Constants.FALSE);                                       // 18
            AppConfigHelper.setConfig(AppConfigDef.isSupportBaiduPay, Constants.TRUE);                                        // 19
            AppConfigHelper.setConfig(AppConfigDef.isSupportOhterPay, Constants.TRUE);                                        // 20
            AppConfigHelper.setConfig(AppConfigDef.isSupportCash, Constants.TRUE);                                        // 21
            AppConfigHelper.setConfig(AppConfigDef.isSupportMemberCard, Constants.TRUE);                                        // 22
        } else if ("lawson".equals(channelName)) {
            AppConfigHelper.setConfig(AppConfigDef.ip, IP_C2);                                                 // 1
            AppConfigHelper.setConfig(AppConfigDef.port, Constants.DEFAULT_PORT);                                // 2
            AppConfigHelper.setConfig(AppConfigDef.BAT_FLAG, Constants.TRUE);                                        // 3
            AppConfigHelper.setConfig(AppConfigDef.BAT_V1_4_FLAG, Constants.TRUE);                                        // 4
            AppConfigHelper.setConfig(AppConfigDef.ROUNDDING_FLAG, Constants.FALSE);                                       // 5
            AppConfigHelper.setConfig(AppConfigDef.OTHER_PAY_REMARK_FLAG, Constants.FALSE);                                       // 6
            AppConfigHelper.setConfig(AppConfigDef.NEED_BAR_CODE_FLAG, Constants.FALSE);                                       // 7
            AppConfigHelper.setConfig(AppConfigDef.IS_BLOCK_UI, Constants.TRUE);                                        // 8
            AppConfigHelper.setConfig(AppConfigDef.HANDUI_IS_BLOCK_UI, Constants.FALSE);                                       // 9
            AppConfigHelper.setConfig(AppConfigDef.SWITCH_KEYBOARD_FLAG, Constants.FALSE);                                       // 10
            AppConfigHelper.setConfig(AppConfigDef.SHOW_BANK_PAY_WHITHOUT_PAYMENTROUTER, Constants.TRUE);                                        // 11
            AppConfigHelper.setConfig(AppConfigDef.CONFIG_PAY_MODE, Constants.FALSE);                                       // 12
            AppConfigHelper.setConfig(AppConfigDef.SHOW_DAY_PRITN, Constants.TRUE);                                        // 13
            AppConfigHelper.setConfig(AppConfigDef.DISCOUNTABLE, Constants.FALSE);                                       // 14
            AppConfigHelper.setConfig(AppConfigDef.THIRD_PAY_TICKET_PUBLISH, Constants.TRUE);                                        // 15
            AppConfigHelper.setConfig(AppConfigDef.APP_VERSION_NAME, Constants.APP_VERSION_LAWSON + "");                     // 16
            AppConfigHelper.setConfig(AppConfigDef.isUseNetPay, Constants.FALSE);                                       // 17
            AppConfigHelper.setConfig(AppConfigDef.DEBUGING, Constants.FALSE);                                       // 18
            AppConfigHelper.setConfig(AppConfigDef.isSupportBaiduPay, Constants.FALSE);                                       // 19
            AppConfigHelper.setConfig(AppConfigDef.isSupportOhterPay, Constants.FALSE);                                       // 20
            AppConfigHelper.setConfig(AppConfigDef.isSupportCash, Constants.FALSE);                                       // 21
            AppConfigHelper.setConfig(AppConfigDef.isSupportMemberCard, Constants.FALSE);                                       // 22
        } else if ("dzx".equals(channelName)) {
            AppConfigHelper.setConfig(AppConfigDef.ip, IP_DZX);                                                // 1
            AppConfigHelper.setConfig(AppConfigDef.port, Constants.DEFAULT_PORT);                                // 2
            AppConfigHelper.setConfig(AppConfigDef.BAT_FLAG, Constants.TRUE);                                        // 3
            AppConfigHelper.setConfig(AppConfigDef.BAT_V1_4_FLAG, Constants.TRUE);                                        // 4
            AppConfigHelper.setConfig(AppConfigDef.ROUNDDING_FLAG, Constants.FALSE);                                       // 5
            AppConfigHelper.setConfig(AppConfigDef.OTHER_PAY_REMARK_FLAG, Constants.TRUE);                                        // 6
            AppConfigHelper.setConfig(AppConfigDef.NEED_BAR_CODE_FLAG, Constants.FALSE);                                       // 7
            AppConfigHelper.setConfig(AppConfigDef.IS_BLOCK_UI, Constants.TRUE);                                        // 8
            AppConfigHelper.setConfig(AppConfigDef.HANDUI_IS_BLOCK_UI, Constants.FALSE);                                       // 9
            AppConfigHelper.setConfig(AppConfigDef.SWITCH_KEYBOARD_FLAG, Constants.FALSE);                                       // 10
            AppConfigHelper.setConfig(AppConfigDef.SHOW_BANK_PAY_WHITHOUT_PAYMENTROUTER, Constants.TRUE);                                        // 11
            AppConfigHelper.setConfig(AppConfigDef.CONFIG_PAY_MODE, Constants.TRUE);                                        // 12
            AppConfigHelper.setConfig(AppConfigDef.SHOW_DAY_PRITN, Constants.FALSE);                                       // 13
            AppConfigHelper.setConfig(AppConfigDef.DISCOUNTABLE, Constants.TRUE);                                        // 14
            AppConfigHelper.setConfig(AppConfigDef.THIRD_PAY_TICKET_PUBLISH, Constants.TRUE);                                        // 15
            AppConfigHelper.setConfig(AppConfigDef.APP_VERSION_NAME, Constants.APP_VERSION_NORMAL + "");                     // 16
            AppConfigHelper.setConfig(AppConfigDef.isUseNetPay, Constants.FALSE);                                       // 17
            AppConfigHelper.setConfig(AppConfigDef.DEBUGING, Constants.FALSE);                                       // 18
            AppConfigHelper.setConfig(AppConfigDef.isSupportBaiduPay, Constants.TRUE);                                        // 19
            AppConfigHelper.setConfig(AppConfigDef.isSupportOhterPay, Constants.TRUE);                                        // 20
            AppConfigHelper.setConfig(AppConfigDef.isSupportCash, Constants.TRUE);                                        // 21
            AppConfigHelper.setConfig(AppConfigDef.isSupportMemberCard, Constants.TRUE);                                        // 22
        } else if ("yjj".equals(channelName)) {
            AppConfigHelper.setConfig(AppConfigDef.ip, IP_YJJ);                                                // 1
            AppConfigHelper.setConfig(AppConfigDef.port, Constants.DEFAULT_PORT);                                // 2
            AppConfigHelper.setConfig(AppConfigDef.BAT_FLAG, Constants.TRUE);                                        // 3
            AppConfigHelper.setConfig(AppConfigDef.BAT_V1_4_FLAG, Constants.TRUE);                                        // 4
            AppConfigHelper.setConfig(AppConfigDef.ROUNDDING_FLAG, Constants.FALSE);                                       // 5
            AppConfigHelper.setConfig(AppConfigDef.OTHER_PAY_REMARK_FLAG, Constants.TRUE);                                        // 6
            AppConfigHelper.setConfig(AppConfigDef.NEED_BAR_CODE_FLAG, Constants.FALSE);                                       // 7
            AppConfigHelper.setConfig(AppConfigDef.IS_BLOCK_UI, Constants.TRUE);                                        // 8
            AppConfigHelper.setConfig(AppConfigDef.HANDUI_IS_BLOCK_UI, Constants.FALSE);                                       // 9
            AppConfigHelper.setConfig(AppConfigDef.SWITCH_KEYBOARD_FLAG, Constants.FALSE);                                       // 10
            AppConfigHelper.setConfig(AppConfigDef.SHOW_BANK_PAY_WHITHOUT_PAYMENTROUTER, Constants.TRUE);                                        // 11
            AppConfigHelper.setConfig(AppConfigDef.CONFIG_PAY_MODE, Constants.TRUE);                                        // 12
            AppConfigHelper.setConfig(AppConfigDef.SHOW_DAY_PRITN, Constants.FALSE);                                       // 13
            AppConfigHelper.setConfig(AppConfigDef.DISCOUNTABLE, Constants.TRUE);                                        // 14
            AppConfigHelper.setConfig(AppConfigDef.THIRD_PAY_TICKET_PUBLISH, Constants.TRUE);                                        // 15
            AppConfigHelper.setConfig(AppConfigDef.APP_VERSION_NAME, Constants.APP_VERSION_NORMAL + "");                     // 16
            AppConfigHelper.setConfig(AppConfigDef.isUseNetPay, Constants.FALSE);                                       // 17
            AppConfigHelper.setConfig(AppConfigDef.DEBUGING, Constants.FALSE);                                       // 18
            AppConfigHelper.setConfig(AppConfigDef.isSupportBaiduPay, Constants.TRUE);                                        // 19
            AppConfigHelper.setConfig(AppConfigDef.isSupportOhterPay, Constants.TRUE);                                        // 20
            AppConfigHelper.setConfig(AppConfigDef.isSupportCash, Constants.TRUE);                                        // 21
            AppConfigHelper.setConfig(AppConfigDef.isSupportMemberCard, Constants.TRUE);                                        // 22
        } else {
            AppConfigHelper.setConfig(AppConfigDef.ip, Constants.DEFAULT_IP);                                  // 1
            AppConfigHelper.setConfig(AppConfigDef.port, Constants.DEFAULT_PORT);                                // 2
            AppConfigHelper.setConfig(AppConfigDef.BAT_FLAG, Constants.TRUE);                                        // 3
            AppConfigHelper.setConfig(AppConfigDef.BAT_V1_4_FLAG, Constants.TRUE);                                        // 4
            AppConfigHelper.setConfig(AppConfigDef.ROUNDDING_FLAG, Constants.FALSE);                                       // 5
            AppConfigHelper.setConfig(AppConfigDef.OTHER_PAY_REMARK_FLAG, Constants.TRUE);                                        // 6
            AppConfigHelper.setConfig(AppConfigDef.NEED_BAR_CODE_FLAG, Constants.FALSE);                                       // 7
            AppConfigHelper.setConfig(AppConfigDef.IS_BLOCK_UI, Constants.TRUE);                                        // 8
            AppConfigHelper.setConfig(AppConfigDef.HANDUI_IS_BLOCK_UI, Constants.FALSE);                                       // 9
            AppConfigHelper.setConfig(AppConfigDef.SWITCH_KEYBOARD_FLAG, Constants.FALSE);                                       // 10
            AppConfigHelper.setConfig(AppConfigDef.SHOW_BANK_PAY_WHITHOUT_PAYMENTROUTER, Constants.TRUE);                                        // 11
            AppConfigHelper.setConfig(AppConfigDef.CONFIG_PAY_MODE, Constants.TRUE);                                        // 12
            AppConfigHelper.setConfig(AppConfigDef.SHOW_DAY_PRITN, Constants.FALSE);                                       // 13
            AppConfigHelper.setConfig(AppConfigDef.DISCOUNTABLE, Constants.FALSE);                                       // 14
            AppConfigHelper.setConfig(AppConfigDef.THIRD_PAY_TICKET_PUBLISH, Constants.TRUE);                                        // 15
            AppConfigHelper.setConfig(AppConfigDef.APP_VERSION_NAME, Constants.APP_VERSION_SIMPLE + "");                     // 16
            AppConfigHelper.setConfig(AppConfigDef.isUseNetPay, Constants.FALSE);                                       // 17
            AppConfigHelper.setConfig(AppConfigDef.DEBUGING, Constants.FALSE);                                       // 18
            AppConfigHelper.setConfig(AppConfigDef.isSupportBaiduPay, Constants.TRUE);                                        // 19
            AppConfigHelper.setConfig(AppConfigDef.isSupportOhterPay, Constants.TRUE);                                        // 20
            AppConfigHelper.setConfig(AppConfigDef.isSupportCash, Constants.TRUE);                                        // 21
            AppConfigHelper.setConfig(AppConfigDef.isSupportMemberCard, Constants.TRUE);                                        // 22
        }
    }


    public static void defaultInit(Context context) {
        // 初始化必备参数
        if (Constants.UNIFIEDLOGIN_FLAG) {
            UserEntityDao.getInstance().init();// 用户列表
        } else {
            UserDao.getInstance().init();// 用户列表
        }

        String sn = SnManager.getSn(context);

        AppConfigHelper.setConfig(AppConfigDef.sn, sn);// sn
        if (TextUtils.isEmpty(AppConfigHelper.getConfig(AppConfigDef.ip))) {
            AppConfigHelper.setConfig(AppConfigDef.ip, Constants.DEFAULT_IP);
        }
        if (TextUtils.isEmpty(AppConfigHelper.getConfig(AppConfigDef.port))) {
            AppConfigHelper.setConfig(AppConfigDef.port, Constants.DEFAULT_PORT);
        }
        if (TextUtils.isEmpty(AppConfigHelper.getConfig(AppConfigDef.secure_password))) {// 安全密码
            AppConfigHelper.setConfig(AppConfigDef.secure_password, Tools.md5(Constants.DEFAULT_SECURE_PASSWORD));
        }
        if (TextUtils.isEmpty(AppConfigHelper.getConfig(AppConfigDef.common_secure_password))) {//通用安全密码
            AppConfigHelper.setConfig(AppConfigDef.common_secure_password, Tools.md5(Constants.COMMON_SECURE_PASSWORD));
        }
        if (TextUtils.isEmpty(AppConfigHelper.getConfig(AppConfigDef.use_alipay))) {
            AppConfigHelper.setConfig(AppConfigDef.use_alipay, Constants.OFF);
        }
        if (TextUtils.isEmpty(AppConfigHelper.getConfig(AppConfigDef.use_weixin_pay))) {
            AppConfigHelper.setConfig(AppConfigDef.use_weixin_pay, Constants.OFF);
        }
        if (TextUtils.isEmpty(AppConfigHelper.getConfig(AppConfigDef.use_weixin_pay))) {
            AppConfigHelper.setConfig(AppConfigDef.xinpay_agent_pay, Constants.OFF);
        }
        if (TextUtils.isEmpty(AppConfigHelper.getConfig(AppConfigDef.use_baidupay))) {
            AppConfigHelper.setConfig(AppConfigDef.use_baidupay, Constants.OFF);
        }
        if (TextUtils.isEmpty(AppConfigHelper.getConfig(AppConfigDef.use_tenpay))) {
            AppConfigHelper.setConfig(AppConfigDef.use_tenpay, Constants.OFF);
        }
        if (TextUtils.isEmpty(AppConfigHelper.getConfig(AppConfigDef.app_id))) {
            AppConfigHelper.setConfig(AppConfigDef.app_id, Constants.DEFAULT_APP_ID);
        }
        if (TextUtils.isEmpty(AppConfigHelper.getConfig(AppConfigDef.app_name))) {
            AppConfigHelper.setConfig(AppConfigDef.app_name, Constants.DEFAULT_APP_NAME);
        }
        if (TextUtils.isEmpty(AppConfigHelper.getConfig(AppConfigDef.order_port))) {
            AppConfigHelper.setConfig(AppConfigDef.order_port, Constants.DEFAULT_ORDRE_PORT + "");
        }
        if (TextUtils.isEmpty(AppConfigHelper.getConfig(AppConfigDef.isSupportCash))) {
            AppConfigHelper.setConfig(AppConfigDef.isSupportCash, Constants.TRUE);
        }
        if (TextUtils.isEmpty(AppConfigHelper.getConfig(AppConfigDef.isSupportBankCard))) {
            AppConfigHelper.setConfig(AppConfigDef.isSupportBankCard, Constants.TRUE);
        }
        if (TextUtils.isEmpty(AppConfigHelper.getConfig(AppConfigDef.isSupportMemberCard))) {
            AppConfigHelper.setConfig(AppConfigDef.isSupportMemberCard, Constants.TRUE);
        }
        if (TextUtils.isEmpty(AppConfigHelper.getConfig(AppConfigDef.isSupportTenpay))) {
            AppConfigHelper.setConfig(AppConfigDef.isSupportTenpay, Constants.TRUE);
        }
        if (TextUtils.isEmpty(AppConfigHelper.getConfig(AppConfigDef.isSupportWepay))) {
            AppConfigHelper.setConfig(AppConfigDef.isSupportWepay, Constants.TRUE);
        }
        if (TextUtils.isEmpty(AppConfigHelper.getConfig(AppConfigDef.isSupportAlipay))) {
            AppConfigHelper.setConfig(AppConfigDef.isSupportAlipay, Constants.TRUE);
        }
        if (TextUtils.isEmpty(AppConfigHelper.getConfig(AppConfigDef.isSupportBaiduPay))) {
            AppConfigHelper.setConfig(AppConfigDef.isSupportBaiduPay, Constants.TRUE);
        }
        if (TextUtils.isEmpty(AppConfigHelper.getConfig(AppConfigDef.isSupportMixPay))) {
            AppConfigHelper.setConfig(AppConfigDef.isSupportMixPay, Constants.TRUE);
        }
        if (TextUtils.isEmpty(AppConfigHelper.getConfig(AppConfigDef.isSupportTicketCancel))) {
            AppConfigHelper.setConfig(AppConfigDef.isSupportTicketCancel, Constants.TRUE);
        }
        if (TextUtils.isEmpty(AppConfigHelper.getConfig(AppConfigDef.isSupportOhterPay))) {
            AppConfigHelper.setConfig(AppConfigDef.isSupportOhterPay, Constants.TRUE);
        }
        if (TextUtils.isEmpty(AppConfigHelper.getConfig(AppConfigDef.isUseNetPay))) {//网络收单
            AppConfigHelper.setConfig(AppConfigDef.isUseNetPay, Constants.FALSE);
        }

    }

}
