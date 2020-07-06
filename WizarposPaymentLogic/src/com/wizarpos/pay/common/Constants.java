package com.wizarpos.pay.common;

import android.util.SparseArray;

import com.wizarpos.pay.app.AppConfigInitUtil;
import com.wizarpos.pay.app.PaymentApplication;
import com.wizarpos.pay.db.AppConfigDef;
import com.wizarpos.pay.db.AppConfigHelper;

import java.util.HashMap;
import java.util.Map;

public class Constants {

    public static final boolean SESSION_FLAG = true;//是否使用session保持;
    public static final boolean UNIFIEDLOGIN_FLAG = true;//商户终端体系改造后为true;
    public static boolean BAT_FLAG = isTrue(AppConfigDef.BAT_FLAG);//是否是bat统一平台支付 默认 true ; 中青旅为 true ; 罗森为 true ;
    public static boolean BAT_V1_4_FLAG = isTrue(AppConfigDef.BAT_V1_4_FLAG);//是否使用V1.4版本的bat统一平台支付 默认 false ; 威富通为 true @yaosong
    public static boolean ROUNDDING_FLAG = isTrue(AppConfigDef.ROUNDDING_FLAG);//是否启用四舍五入 默认 false
    public static boolean OTHER_PAY_REMARK_FLAG = isTrue(AppConfigDef.OTHER_PAY_REMARK_FLAG);//是否启用其他支付备注功能 默认 false ; 易筋经为 true
    public static boolean NEED_BAR_CODE_FLAG = isTrue(AppConfigDef.NEED_BAR_CODE_FLAG);//是否需要打印流水号条码 默认 false ; 罗森版本为 true
    public static boolean DEBUGING = isTrue(AppConfigDef.DEBUGING); //调试模式 默认 false
    public static boolean IS_BLOCK_UI = isTrue(AppConfigDef.IS_BLOCK_UI); //POS打印是否阻塞UI线程 默认为 true
    public static boolean HANDUI_IS_BLOCK_UI = isTrue(AppConfigDef.HANDUI_IS_BLOCK_UI); //手持打印是否阻塞UI线程 默认为 false
    public static boolean SWITCH_KEYBOARD_FLAG = isTrue(AppConfigDef.SWITCH_KEYBOARD_FLAG); //是否允许切换键盘 默认为 false ; 罗森版本为 true
    public static boolean SHOW_BANK_PAY_WHITHOUT_PAYMENTROUTER = isTrue(AppConfigDef.SHOW_BANK_PAY_WHITHOUT_PAYMENTROUTER); //没有收单应用是否显示银行卡支付 wu@[20151110] 默认为 true ;中青旅版本为 false
    public static boolean CONFIG_PAY_MODE = isTrue(AppConfigDef.CONFIG_PAY_MODE); //是否允许自定义支付方式 默认为  false ; 罗森版本为 true
    public static boolean SHOW_DAY_PRITN = isTrue(AppConfigDef.SHOW_DAY_PRITN); //是否显示日结单打印功能 默认为  false ; 罗森版本为 true
    public static boolean DISCOUNTABLE = isTrue(AppConfigDef.DISCOUNTABLE); //是否允许折扣 默认为  false ; 东志信版本为 true wu@[20151115]
    public static boolean THIRD_PAY_TICKET_PUBLISH = isTrue(AppConfigDef.THIRD_PAY_TICKET_PUBLISH);//第三方支付及其他支付允许发券 默认为 false

    public static final int APP_VERSION_NORMAL = 0X01;//正常版
    public static final int APP_VERSION_LAWSON = 0X02;//罗森版
    public static final int APP_VERSION__CHANNEL = 0X03; //渠道版
    public static final int APP_VERSION_PL = 0X04;//普兰
    public static final int APP_VERSION_SIMPLE = 0X05;//普兰
    public static int APP_VERSION_NAME = getIntValue(AppConfigDef.APP_VERSION_NAME);//默认为正常版本
    //    public static final String SUFFIX_URL = "/member-server/service";//服务器版本放到head中
    public static final String SERVER_VERSION = "V1_9";//服务器版本放到head中维护 wu

    //正式地址
//    public static final String DEFAULT_IP = AppConfigInitUtil.IP_INTERNATIONAL;
    //测试地址
    public static final String DEFAULT_IP = AppConfigInitUtil.IP_INTERNATIONAL_FR;


    public static final String DEFAULT_PORT = AppConfigInitUtil.DEFAULT_PORT;

    public static final String SUFFIX_URL = "/member-server/service";//服务器版本放到head中

    public static final String ALIPAYFLAG = "A";
    public static final String BAIDUPAYFLAG = "B";
    public static final String UNION = "UNS";
    public static final String WEPAYFLAG = "W";
    public static final String TENPAYFLAG = "T";
    public static final String YIPAYFLAG = "Y";
    public static final String UNIONFLAG = "U";
    public static final String ALLOW = "T";
    public static final String COLLETON = "ON";
    public static final String COLLETOFF = "OFF";
    public static final String NOTALLOW = "F";
    public static final String UPDATE = "update";

    private static boolean isTrue(String flag) {
        return Constants.TRUE.equals(AppConfigHelper.getConfig(flag));
    }

    public static int getIntValue(String APP_VERSION_NAME) {
        return Integer.valueOf(AppConfigHelper.getConfig(APP_VERSION_NAME));
    }

    public static void initContantsAfterLogin() {
        BAT_FLAG = isTrue(AppConfigDef.BAT_FLAG);//是否是bat统一平台支付 默认 true ; 中青旅为 true ; 罗森为 true ;
        BAT_V1_4_FLAG = isTrue(AppConfigDef.BAT_V1_4_FLAG);//是否使用V1.4版本的bat统一平台支付 默认 false ; 威富通为 true @yaosong
        ROUNDDING_FLAG = isTrue(AppConfigDef.ROUNDDING_FLAG);//是否启用四舍五入 默认 false
        OTHER_PAY_REMARK_FLAG = isTrue(AppConfigDef.OTHER_PAY_REMARK_FLAG);//是否启用其他支付备注功能 默认 false ; 易筋经为 true
        NEED_BAR_CODE_FLAG = isTrue(AppConfigDef.NEED_BAR_CODE_FLAG);//是否需要打印流水号条码 默认 false ; 罗森版本为 true
//        DEBUGING = isTrue(AppConfigDef.DEBUGING); //调试模式 默认 false
        IS_BLOCK_UI = isTrue(AppConfigDef.IS_BLOCK_UI); //POS打印是否阻塞UI线程 默认为 true
        HANDUI_IS_BLOCK_UI = isTrue(AppConfigDef.HANDUI_IS_BLOCK_UI); //手持打印是否阻塞UI线程 默认为 false
        SWITCH_KEYBOARD_FLAG = isTrue(AppConfigDef.SWITCH_KEYBOARD_FLAG); //是否允许切换键盘 默认为 false ; 罗森版本为 true
        SHOW_BANK_PAY_WHITHOUT_PAYMENTROUTER = isTrue(AppConfigDef.SHOW_BANK_PAY_WHITHOUT_PAYMENTROUTER); //没有收单应用是否显示银行卡支付 wu@[20151110] 默认为 true ;中青旅版本为 false
        CONFIG_PAY_MODE = isTrue(AppConfigDef.CONFIG_PAY_MODE); //是否允许自定义支付方式 默认为  false ; 罗森版本为 true
        SHOW_DAY_PRITN = isTrue(AppConfigDef.SHOW_DAY_PRITN); //是否显示日结单打印功能 默认为  false ; 罗森版本为 true
        DISCOUNTABLE = isTrue(AppConfigDef.DISCOUNTABLE); //是否允许折扣 默认为  false ; 东志信版本为 true wu@[20151115]
        THIRD_PAY_TICKET_PUBLISH = isTrue(AppConfigDef.THIRD_PAY_TICKET_PUBLISH);//第三方支付及其他支付允许发券 默认为 false
        APP_VERSION_NAME = getIntVersion(AppConfigDef.APP_VERSION_NAME); //默认为正常版本
    }

    public static int getIntVersion(String configName) {
        if (PaymentApplication.getInstance().getDbController() != null) {
            return Integer.valueOf(AppConfigHelper.getConfig(configName, APP_VERSION_NORMAL + ""));
        } else {
            return APP_VERSION_NORMAL;
        }
    }

    public static final String ON = "true";
    public static final String OFF = "false";
    public static final String TRUE = "true";
    public static final String FALSE = "false";
    public static final String ISSN = "true";//是否取序列号
    public static final String RECHARGEON = "1";//会员卡充值
    public static final String UNRECHARGEON = "0";//非会员卡充值
    public static final String WEPAY__BAT = "1";//统一平台bat微信支付
    public static final String ALIPAY_BAT = "2";//统一平台bat支付宝支付
    public static final String TENPAY_BAT = "3";//统一平台batQQ钱包支付
    public static final String BAIDUPAY_BAT = "4";//统一平台bat百付宝支付
    public static final String UNION_BAT = "5";//移动支付
    public static final String FLAG_ON = "on";
    public static final String FLAG_OFF = "off";
    public static final String BANKCARDPAY = "T";//是否是银行卡支付
    public static final String BANKCARDPAYQUERY = "1";//银行卡撤销查询

    // 应用启动默认初始化信息
    public static final String DEFAULT_SECURE_PASSWORD = "88888888";
    public static final String COMMON_SECURE_PASSWORD = "43881642";

    public static final Integer DEFAULT_ORDRE_PORT = 10066;
    public static final Integer DEFAULT_PAY_ID = 1; // 1 讯联 2 通联 3 银联cuppos 4
    // 银联unionpay
    public static final String DEFAULT_MULTI_MEMBER_CARD = "false";
    public static final String DEFAULT_APP_ID = "002";
    public static final String DEFAULT_APP_NAME = "收款";
    public static final String DEFAULT_ROUTER = "99"; // 1 融信Gif 2通联Alp 3银联Unp

    // 4银联直连Cup 5讯联Cil
    // 99代表不使用内嵌

    /**
     * 初始金额 (传入金额)
     */
    public static final String initAmount = "initAmount";

    /**
     * 应付金额
     */
    public static final String shouldAmount = "shouldAmount";

    /**
     * 实付金额
     */
    public static final String realAmount = "realAmount";
    /**
     * 小费金额
     */
    public static final String tipsAmount = "tipsAmount";
    /**
     * 扣减金额
     */
    public static final String reduceAmount = "reduceAmount";
    /**
     * 找零金额
     */
    public static final String changeAmount = "changeAmount";
    /**
     * 折扣扣减金额
     */
    public static final String discountAmount = "discountAmount";
    /**
     * 混合支付总金额
     */
    public static final String mixInitAmount = "mixInitAmount";

    /**
     * 销售传入的初始金额
     */
    public static final String saleInputAmount = "saleInputAmount";

    /**
     * bat线上支付类型
     */
    public static final String payTypeFlag = "payTypeFlag";

    /**
     * 第三方请求
     */
    public static final String thirdRequest = "thirdRequest";

    public static final String TRANSACTION_TYPE = "TRANSACTION_TYPE";
    public static final String cardNo = "cardNo";
    public static final String cardBalance = "cardBalance";
    public static final String cardType = "cardType";
    public static final String body = "body";
    public static final String mixFlag = "mixFlag";
    public static final String mixTranLogId = "mixTranLogId";
    public static final String isMixTransaction = "1";//是组合支付
    public static final String offline = "offline";
    public static final String tranLogId = "tranLogId";
    public static final String tranId = "tranId";
    public static final String wemengTicketInfo = "wemengTicketInfo";
    public static final String isUsedWemngTicket = "isUsedWemngTicket";
    public static final String commonCashierOrderId = "commonCashierOrderId";
    public static final String merchantType_weimeng = "weimeng";//商户类型：微盟
    public static final String isFirstTrans = "isFirstTrans";//商户类型：微盟

    /**
     * 配置ip
     */
    public static final int CONFIG_IP_REQUEST = 18000;
    public static final int CONFIG_IP_RESPONSE = 18001;

    /**
     * 商户卡激活
     */
    public static final int MERCHANT_CARD_ACTIVE = 19000;
    /**
     * 商户卡注销
     */
    public static final int MERCHANT_CARD_CANCEL = 19001;
    /**
     * 商户卡查询
     */
    public static final int MERCHANT_CARD_QUERY = 19002;
    /**
     * 银行卡支付
     */
    public static final int BANK_CARD_PAY = 19003;
    /**
     * 商户卡支付
     */
    public static final int MERCHANT_CARD_PAY = 19004;
    /**
     * 现金支付
     */
    public static final int CASH_PAY = 19005;
    /**
     * 电子卡充值
     */
    public static final int ELEC_CARD_RECHARGE = 19006;
    /**
     * 电子卡消费
     */
    public static final int ELEC_CARD_PAY = 19007;
    /**
     * 商户卡充值
     */
    public static final int MERCHANT_CARD_RECHARGE = 19008;
    /**
     * 卡券查询
     */
    public static final int TICKET_QUERY = 19009;
    /**
     * 商户卡 action
     */
    public static final int MERCHANT_CARD_ACTION = 19010;
    /**
     * 微信会员卡
     */
    public static final int WEIXIN_MEMBER_CARD_RECHARGE = 19011;
    public static final int WEIXIN_MEMBER_CARD_CONSUME = 19012;
    public static final int WEIXIN_MEMBER_CARD_QUERY = 19013;
    public static final int WEIXIN_MEMBER_CARD_PUBLISH = 19015;
    public static final int WEIXIN_MEMBER_CARD_USE = 19015;
    /**
     * 券使用
     */
    public static final int TICKET_USE_REQUSTCODE = 10001;
    /**
     * 会员卡绑定微信会员卡
     */
    public static final int MERCHANT_CARD_BIND = 19016;
    /**
     * 交易查询
     */
    public static final int QUERY_QUESTCODE = 1001;
    /**
     * 提交商户信息
     */
    public static final String SC_99_NET = "99";
    /**
     * 提交商户信息
     */
    public static final String SC_100_MERCHANT_INFO_SUBMIT = "100";
    /**
     * 首次登录判断
     */
    public static final String SC_119_FIRST_LOGIN = "119";
    /**
     * 商户进件登陆
     */
    public static final String SC_113_MOBILE_LOGIN = "113";
    /**
     * 商户进件注册(申请)
     */
    public static final String SC_111_MERCHANT_APPLY = "111";
    /**
     * 商户进件(绑定其他商户)
     */
    public static final String SC_112_MOBILE_MERCHANT_BLIND = "112";
    /**
     * 商户进件查询
     */
    public static final String SC_114_PRE_MERCHANT_APPLY = "114";
    /**
     * 商户进件(加入已有商户前验证)
     */
    public static final String SC_117_PRE_MERCHANT_BLIND = "117";
    /**
     * >>>>>>> merchant_rebuild
     * 收银业务类型查询
     */
    public static final String SC_101_SERVICE_TYPE_QUERY = "101";
    /**
     * 收银业务类型新增
     */
    public static final String SC_102_SERVICE_TYPE_ADD = "102";
    /**
     * 收银业务类型修改
     */
    public static final String SC_103_SERVICE_TYPE_UPDATE = "103";
    /**
     * 收银业务类型启用
     */
    public static final String SC_104_SERVICE_TYPE_START = "104";
    /**
     * 收银业务类型停用
     */
    public static final String SC_105_SERVICE_TYPE_STOP = "105";
    /**
     * 更改慧商户号信息
     */
    public static final String SC_106_MERCHANT_INFO_MODIFY = "106";
    /**
     * 对验证码进行验证
     */
    public static final String SC_116_MERCHANT_VCODE = "116";
    /**
     * 修改密码和重置密码 （商户进件）
     */
    public static final String SC_115_MERCHANT_PASSWD_MOBILE = "115";

    /**
     * 商户号修改调用
     */
    public static final String SC_150_MERCHANT_MODIFY = "150";

    /**
     * 商户号会员联盟号审核
     */
    public static final String SC_151_MERCHANT_CHECK = "151";

    /**
     * 商户号会员联盟申请进度查询
     */
    public static final String SC_152_MERCHANT_CHECK = "152";

    /**
     * 商户号会员联盟号审核清单列表
     */
    public static final String SC_153_MERCHANT_CHECK = "153";

    /**
     * 商户卡定义新增
     */
    public static final String SC_201_MERCHANT_CARD_DEF_ADD = "201";
    /**
     * 商户卡定义停用
     */
    public static final String SC_202_MERCHANT_CARD_DEF_STOP = "202";
    /**
     * 商户卡定义启用
     */
    public static final String SC_203_MERCHANT_CARD_DEF_START = "203";
    /**
     * 商户卡定义修改
     */
    public static final String SC_204_MERCHANT_CARD_DEF_UPDATE = "204";

    /**
     * 商户卡激活
     */
    public static final String SC_300_MERCHANT_CARD_ACTIVE = "300";
    /**
     * 商户卡查询
     */
    public static final String SC_301_MERCHANT_CARD_QUERY = "301";
    /**
     * 商户卡充值
     */
    public static final String SC_302_MERCHANT_CARD_RECHARGE = "302";
    /**
     * 商户卡支付
     */
    public static final String SC_304_MERCHANT_CARD_PAY = "304";

    /**
     * 商户卡注销
     */
    public static final String SC_306_MERCHANT_CARD_CANCEL = "306";
    /**
     * 商户卡重新激活
     */
    public static final String SC_307_MERCHANT_CARD_REACTIVE = "307";
    /**
     * 商户卡列表
     */
    public static final String SC_308_MERCHANT_CARD_LIST = "308";
    /**
     * 修改卡主信息
     */
    public static final String SC_311_MERCHANT_CARD_UPDATE = "311";
    /**
     * 会员卡信息查询(组合模糊查询)
     */
    public static final String SC_341_MERCHANT_CARD_QUERY_UNION = "341";
    /**
     * 营销规则
     */
    public static final String SC_912_MARKETING_PAY = "912";
    /**
     * 交易撤销
     */
    public static final String SC_310_TRAN_CANCEL = "310";
    /**
     * 支付方式统计饼图
     */
    public static final String SC_318_PAY_PEI3D = "318";
    /**
     * 现金支付
     */
    public static final String SC_400_CASH_PAY = "400";
    /**
     * 其他支付
     */
    public static final String SC_401_OTHER_PAY = "401";
    /**
     * 现金离线支付
     */
    public static final String SC_402_CASH_OFFLINE_PAY = "402";
    /**
     * 现金撤销
     */
    public static final String SC_405_CASH_CANCEL = "405";

    /**
     * 卡包卡券定义列表
     */
    public static final String SC_500_TICKET_DEF_LIST = "500";
    /**
     * 卡包卡券定义新增
     */
    public static final String SC_511_TICKET_DEF_ADD = "511";
    /**
     * 卡包卡券定义查询
     */
    public static final String SC_512_TICKET_DEF_QUERY = "512";
    /**
     * 卡包卡券定义修改
     */
    public static final String SC_513_TICKET_DEF_UPDATE = "513";
    /**
     * 卡包卡券定义启用
     */
    public static final String SC_514_TICKET_DEF_START = "514";
    /**
     * 更新小费信息
     */
    public static final String SC_956_TIPS_UPDATE = "956";
    /**
     * 获取费率(957)
     */
    public static final String SC_957_RATE_UPDATE = "957";
    /**
     * 卡包卡券定义停用
     */
    public static final String SC_515_TICKET_DEF_STOP = "515";
    /** 卡包卡券关联商户卡定义查询，根据商户卡信息查卡券信息 */
    // public static final String SC_TICKET_RELATE_QUERY = "516";
    /**
     * 根据卡券定义查关联的商户卡
     */
    public static final String SC_517_CARD_TYPE_RELATE_QUERY = "517";
    /**
     * 可发行的卡券定义列表
     */
    public static final String SC_518_TICKET_DEF_PUBLISHABLE = "518";
    /**
     * 可发行的卡券定义列表(有卡包)
     */
    public static final String SC_520_TICKET_DEF_PUBLISHABLE = "520";

    public static final String SC_519_WECHAT_TTICKET_DEF_QUERY_NO_MEMBER = "519";

    /**
     * 卡券列表
     */
    public static final String SC_303_TICKET_LIST = "303";
    /**
     * 卡券发行
     */
    public static final String SC_501_TICKET_PUBLISH = "501";
    /**
     * 卡券作废
     */
    public static final String SC_504_TICKET_CANCEL = "504";
    /**
     * 卡券发行 解决多次发送红包券 返回成功问题 解决方法:将501接口换成505接口 返回对象加了个message字段
     */
    public static final String SC_505_TICKET_PUBLISH = "505";
    /**
     * 微盟券发放查询(用于第三方支付)
     */
    public static final String SC_506_TICKET_FF_QUERY = "506";

    /**
     * 券使用查询验证
     */
    public static final String SC_507_QUERY_TICKET = "507";
    /**
     * 卡券使用
     */
    public static final String SC_502_TICKET_USE = "502";
    /**
     * 非会员券查询
     */
    public static final String SC_503_TICKET_DEF_QUERY_NO_MEMBER = "503";

    /**
     * 交易明细查询
     */
    public static final String SC_601_TRAN_LOG_QUERY = "601";
    /**
     * 交易明细查询
     */
    public static final String SC_602_TRAN_LOG_SUM = "602";
    /**
     * 商户卡交易查询 -- 列表
     */
    public static final String SC_605_MERCHANT_CARD_TRAN_LOG = "605";
    /**
     * 交易详细
     */
    public static final String SC_607_TRAN_LOG_DETAIL = "607";

    /**
     * 支付易交易明细查询
     */
    public static final String SC_608_TRAN_LOG_QUERY = "608";

    /**
     * 支付易交易明细查询(根据SN号来查询信息 目前用于罗森版本)
     */
    public static final String SC_615_TRAN_LOG_QUERY = "615";

    /**
     * 民生支付宝支付交易明细查询(输入trade_no)
     */
    public static final String SC_620_TRAN_LOG_QUERY_MS = "620";

    /**
     * 支付易交易汇总查询
     */
    public static final String SC_609_TRAN_LOG_SUM = "609";

    /**
     * 支付易交易汇总查询(根据SN号来查询信息 目前用于罗森版本)
     */
    public static final String SC_616_TRAN_LOG_SUM = "616";
    /**
     * 交易记录查询（根据时间范围查询  ）
     */
    public static final String SC_924_TRAN_DETAIL = "924";


    /**
     * 交易记录查询（根据时间范围查询  ）--与924的功能一致
     * 含有分页查询，这里按记录分页
     */
    public static final String SC_964_TRAN_DETAIL_PAGE = "964";

    /**
     * 交易记录查询（根据时间范围查询  ）--与924，964的功能一致
     * 含有分页查询，这里是按天分页,
     */
    public static final String SC_965_TRAN_DETAIL_PAGE = "965";

    /**
     * 查找当前关联的商户列表
     */
    public static final String SC_966_SELECT_FUNDS_LIST = "966";

    /**
     * 券的详情
     */
    public static final String SC_925_TICKET_DETAIL = "925";
    /**
     * 日结单
     */
    public static final String SC_926_DAILY_SUMMARY = "926";
    /**
     * 日结单新版
     */
    public static final String SC_961_DAILY_SUMMARY_PLUS = "961";

    /**
     * 用来替换 961接口的
     */
    public static final String SC_963_DAILY_SUMMARY_PLUS = "963";
    /**
     * 修改操作员登录密码
     */
    public static final String SC_943_OPERATOR_RESET_PASSWORD = "943";
    /**
     * 修改罗森日结单时间戳
     */
    public static final String SC_618_UPDATE_DAYTIME = "618";


    /**
     * 支付易交易详细
     */
    public static final String SC_610_TRAN_LOG_DETAIL = "610";

    /**
     * 银行卡消费
     */
    public static final String SC_700_BANK_CARD_PAY = "700";

    /**
     * 银行卡撤销
     */
    public static final String SC_702_BANK_CARD_CANCEL = "702";

    /**
     * 获取短信验证码
     */
    public static final String SC_704_CL_SEND_MSG_CODE = "704";

    /**
     * 从服务器获取自定的微信openid
     */
    public static final String SC_801_REQUEST_WEIXIN_CODE = "801";

    /**
     * 组合支付会员卡消费撤销
     */
    public static final String SC_805_CANCEL_MIX_TRANLOG = "805";


    /**
     * 微信卡查询
     */
    public static final String SC_810_WEIXIN_CARD_QUERY = "810";
    /**
     * 同步服务器时间
     */
    public static final String SC_811_SYNC_TIME = "811";
    /**
     * 国际版移动支付
     */
    public static final String SC_953_BAT_COMMON_PAY = "953";
    /**
     * 会员卡微信会员卡绑定
     */
    public static final String SC_812_BIND_MEMBER_CARD = "812";
    /**
     * 支付宝消费
     */
    public static final String SC_813_ALIPAY = "813";
    /**
     * 微信消费
     */
    public static final String SC_814_TENPAY = "814";

    /**
     * 获取支付宝支付订单号
     */
    public static final String SC_815_ALIPAY_ORDER_NO = "815";
    /**
     * 获取微信支付订单号
     */
    public static final String SC_816_WEIXIN_ORDER_NO = "816";
    /**
     * 检查订单
     */
    public static final String SC_817_CHECK_ORDER_NO = "817";
    /**
     * 商户定义查询
     */
    public static final String SC_818_MERCHANT_DEF_QUERY = "818";
    /**
     * 订单查询
     */
    public static final String SC_819_ORDER_DEF_QUERY = "819";
    /**
     * 订单明细
     */
    public static final String SC_820_ORDER_DEF_DETAIL = "820";
    /**
     * 国际版订单明细
     */
    public static final String SC_954_ORDER_DEF_DETAIL = "954";
    /**
     * 支付宝撤销
     */
    public static final String SC_809_CANCEL_ALIPAY = "809";
    /**
     * 商户管家关注者查询
     */
    public static final String SC_821_SUBSCRIBER_QUERY = "821";
    /**
     * 商户管家取消关注
     */
    public static final String SC_822_SUBSCRIBER_UNBIND = "822";
    /**
     * 获取微信ticket
     */
    public static final String SC_823_WEIXIN_TICKET = "823";
    /**
     * 获取支付宝订单二维码
     */
    public static final String SC_824_GET_ALIPAY_ORDER_QR_CODE = "824";
    /**
     * 获取微信订单二维码
     */
    public static final String SC_825_GET_WEIXIN_ORDER_QR_CODE = "825";
    /**
     * 商户信息更新
     */
    public static final String SC_826_MERCHANT_INFO_UPDATE = "826";

    /**
     * 银行卡注册
     */
    public static final String SC_828_BANK_CARD_REGISTER = "828";
    /**
     * 银行卡绑定微信，生成二维码的ticket
     */
    public static final String SC_829_BANK_CARD_BINDING_TICKET = "829";
    /**
     * 会员卡换卡
     */
    public static final String SC_830_MERCHANT_CARD_CHANGE = "830";
    /**
     * 冻结/解冻
     */
    public static final String SC_831_MERCHANT_CARD_FREEZE = "831";
    /**
     * 商户配置信息
     */
    public static final String SC_832_MERCHANT_CONFIG_INFO = "832";
    /**
     * 银行卡支付结果，成功或失败
     */
    public static final String SC_833_BANK_CARD_PAY_RESULT = "833";

    /**
     * 支付易订单查询
     */
    public static final String SC_834_ORDER_DEF_QUERY = "834";
    /**
     * 微信撤销
     */
    public static final String SC_836_WEIXIN_PAY_REVERSE = "836";
    /**
     * 同步服务员
     */
    public static final String SC_340_FU_WU_YUAN_ASYC = "340";

    /**
     * 微信撤销(提交慧银后台)
     */
    public static final String SC_837_WEIXIN_PAY_REVERSE_PLATFORM = "837";

    /**
     * 获取QQ钱包支付订单号
     */
    public static final String SC_840_TENPAY_ORDER_NO = "840";

    /**
     * 支付宝被扫支付
     */
    public static final String SC_853_ALIPAY_MICRO_PAY = "853";
    /**
     * Bat生成订单
     */
    public static final String SC_870_ONLINE_CREAT_ORDER = "870";
    /**
     * Bat生成订单_V1.4
     */
    public static final String SC_873_ONLINE_CREAT_ORDER = "873";
    /**
     * 营销二期
     */
    public static final String SC_872_THIRD_GIVE_TICKETS = "872";
    /**
     * Bat支付
     */
    public static final String SC_871_ONLINE_CREAT_PAY = "871";
    /**
     * Bat支付_V1.4
     */
    public static final String SC_874_ONLINE_CREAT_PAY = "874";
    /**
     * bat支付被扫移动支付
     */
    public static final String SC_875_BAT_UNION_PAY = "875";

    /**
     * QQ钱包
     */
    public static final String SC_841_TENPAY_QQ = "841";

    /**
     * 生成百度钱包订单号
     */
    public static final String SC_843_BAIDU_PAY_ORDER_NO = "843";

    /**
     * 百度钱包
     */
    public static final String SC_850_BAIDU = "850";

    public static final String SC_851_BAIDU_REVOKE = "851";

    /**
     * 终端流水号查询
     */
    public static final String SC_900_TERMINAL_TRANLOG_ID = "900";
    /**
     * 国际版终端流水号查询
     */
    public static final String SC_958_TERMINAL_TRANLOG_ID = "958";

    /**
     * 撤销交易
     */
    public static final String SC_901_TRANLOG_CANCEL = "901";
    /**
     * 国际版撤销交易
     */
    public static final String SC_955_TRANLOG_CANCEL = "955";

    /**
     * 混合支付撤销交易
     */
    public static final String SC_906_GET_MIX_PAY_REVERSE = "906";

    /**
     * 取Token
     */
    public static final String SC_904_ACCESS_TOKEN = "904";

    /**
     * 拉取第三方券信息
     */
    public static final String SC_910_THIRD_TICKET_INFO = "910";

    /**
     * 核销第三方券
     */
    public static final String SC_911_THIRD_TICKET_INFO = "911";

    /**
     * 核销weixin券
     */
    public static final String SC_701_WEPAY_TICKET_PASS = "701";
    /**
     * 微信卡券查询
     */
    public static final String SC_703_WEIXIN_TICKET_DETAIL = "703";

    /**
     * 混合支付微信卡券核销
     */
    public static final String SC_791_MIX_PAY_UNMEMTICKET = "791";
    /**
     * 混合支付-会员券
     */
    public static final String SC_792_MIX_PAY_MEMTICKET = "792";
    /**
     * 混合支付-微信卡券核销(单独使用)
     */
    public static final String SC_793_MIX_PAY_WXSINGLE_TICKET = "793";

    /**
     * 统一初始化
     */
    public static final String SC_999_UNIT_SET = "999";

    /**
     * 民生支付宝
     */
    public static final String MS_ALIPAY = "107";

    /**
     * 微信支付宝撤销
     */
    public static final String CANCEL_WX_ALIPAY_TRANS = "";

    /**
     * 微信支付宝撤销
     */
    public static final String SC_838_QQ_PAY_REVERSE = "838";

    /**
     * 手Q支付撤销
     */
    public static final String SC_842_TENPAY_QQ_VOID = "842";

    /**
     * 手Q支付撤销
     */
    public static final String SC_403_TENPAY_OTHERPAY_REVERSE = "403";

    /**
     * 组合支付-抹零
     */
    public static final String SC_795_MIX_PAY_WIPE_ZERO = "795";

    /**
     * 组合支付-折扣
     */
    public static final String SC_796_MIX_PAY_DISCOUNT = "796";
    /**
     * 组合支付
     */
    public static final String SC_800_MIXPAY = "800";
    /**
     * 组合支付撤销
     */
    public static final String SC_801_MIXPAY_CANCEL = "801";
    /**
     * 卡券单独核销
     */
    public static final String SC_406_TICKET = "406";
    /**
     * 手持汇总查询
     */
    public static final String SC_614_HANDER_STATIC_TOTAL = "614";
    /**
     * 手持汇总查询 根据sn号查询
     */
    public static final String SC_617_HANDER_STATIC_TOTAL = "617";
    /**
     * 渠道版收款
     */
    public static final String SC_880_HANDER_CHANEL_TOTAL = "880";
    /**
     * 券交易金额明细
     */
    public static final String SC_881_TICKET_DETIAL = "881";
    /**
     * 新增会员数明细
     */
    public static final String SC_880_NEW_MEMBER = "882";

    public static final String SC_1000_CARD_LINK = "1000";
    /**
     * 退款权限验证
     */
    public static final String SC_960_REFUND = "960";


    public static final String TEST_CARD_NO = "105100000100002045";

    /**
     * 交易类型
     */
    public static final Map<String, String> TRAN_TYPE = new HashMap<String, String>();


    public static String[] kinds = {"会员卡消费", "银行卡消费", "现金消费", "离线现金消费", "现金撤销",
            "会员卡交易撤销", "其它消费", "用券", "券作废", "支付宝消费",
            "支付宝撤销", "微信消费撤销", "百度钱包支付撤销", "微信消费", "微信/支付宝/QQ钱包消费撤销", "生成支付宝订单",
            "生成微信订单", "QQ钱包支付", "QQ钱包支付撤销", "百度钱包支付", "其他消费撤销", "组合支付", "组合支付撤销"};//kinds只包含交易的TRANS_TYPE

    static {
        TRAN_TYPE.put(SC_700_BANK_CARD_PAY, "银行卡消费");
        TRAN_TYPE.put(SC_702_BANK_CARD_CANCEL, "银行卡撤销");
        TRAN_TYPE.put(SC_304_MERCHANT_CARD_PAY, "会员卡消费");
        TRAN_TYPE.put(SC_400_CASH_PAY, "现金消费");
        TRAN_TYPE.put(SC_300_MERCHANT_CARD_ACTIVE, "会员卡激活");
        TRAN_TYPE.put(SC_302_MERCHANT_CARD_RECHARGE, "会员卡充值");
        TRAN_TYPE.put(SC_307_MERCHANT_CARD_REACTIVE, "会员卡激活");
        TRAN_TYPE.put(SC_306_MERCHANT_CARD_CANCEL, "会员卡注销");
        TRAN_TYPE.put(SC_301_MERCHANT_CARD_QUERY, "注销");
        TRAN_TYPE.put(SC_310_TRAN_CANCEL, "会员卡交易撤销");
        TRAN_TYPE.put(SC_318_PAY_PEI3D, "支付方式统计饼图 ");
        TRAN_TYPE.put(SC_401_OTHER_PAY, "其它消费");
        TRAN_TYPE.put(SC_402_CASH_OFFLINE_PAY, "离线现金消费");
        TRAN_TYPE.put(SC_405_CASH_CANCEL, "现金撤销");
        TRAN_TYPE.put(SC_502_TICKET_USE, "用券");
        TRAN_TYPE.put(SC_504_TICKET_CANCEL, "券作废");
        TRAN_TYPE.put(SC_830_MERCHANT_CARD_CHANGE, "会员卡换卡");
        TRAN_TYPE.put(SC_813_ALIPAY, "Alipay");
        TRAN_TYPE.put(SC_814_TENPAY, "Wechat Pay");
//        TRAN_TYPE.put(SC_814_TENPAY, "Wechat" + PaymentApplication.getInstance().getString(R.string.pay_tag));
//        TRAN_TYPE.put(SC_820_ORDER_DEF_DETAIL, "Wechat" + " " + PaymentApplication.getInstance().getString(R.string.pay_tag) + " " + PaymentApplication.getInstance().getString(R.string.refund_tag));
//        TRAN_TYPE.put(SC_809_CANCEL_ALIPAY, "Alipay" + " " + PaymentApplication.getInstance().getString(R.string.refund_tag));
        TRAN_TYPE.put(SC_820_ORDER_DEF_DETAIL, "Wechat Pay");
        TRAN_TYPE.put(SC_809_CANCEL_ALIPAY, "Alipay");
        TRAN_TYPE.put(SC_815_ALIPAY_ORDER_NO, "生成支付宝订单");
        TRAN_TYPE.put(SC_816_WEIXIN_ORDER_NO, "生成微信订单");
        TRAN_TYPE.put(SC_831_MERCHANT_CARD_FREEZE, "会员卡冻结/解冻");
        TRAN_TYPE.put(SC_841_TENPAY_QQ, "QQ钱包支付");
        TRAN_TYPE.put(SC_842_TENPAY_QQ_VOID, "QQ钱包支付撤销");
        TRAN_TYPE.put(SC_850_BAIDU, "百度钱包支付");
        TRAN_TYPE.put(SC_851_BAIDU_REVOKE, "百度钱包支付撤销");
        TRAN_TYPE.put(SC_403_TENPAY_OTHERPAY_REVERSE, "其他消费撤销");
        TRAN_TYPE.put(SC_800_MIXPAY, "组合支付");
        TRAN_TYPE.put(SC_801_MIXPAY_CANCEL, "组合支付撤销");
        TRAN_TYPE.put(SC_406_TICKET, "卡券核销");

        TRAN_TYPE.put("825", "鑫蓝支付宝支付");
        TRAN_TYPE.put("826", "鑫蓝支付宝支付撤销");
        TRAN_TYPE.put("827", "鑫蓝微信支付");
        TRAN_TYPE.put("828", "鑫蓝微信支付撤销");

        TRAN_TYPE.put("873", "Union Pay QC");
        TRAN_TYPE.put("874", "Union Pay QC");//银联支付撤销
    }


    public static String getStateDesc(int state) {
        String desc = "等待支付";
        if (state == 2) {
            desc = "已支付";
        } else if (state == 3) {
            desc = "请求取消";
        } else if (state == 4) {
            desc = "已取消";
        } else if (state == 5) {
            desc = "已完成";
        } else if (state == 6) {
            desc = "正在支付";
        }
        return desc;
    }

    /**
     * 营销规则中传递的营销规则
     * 1、现金支付  2、会员卡支付  3、微信支付  4、支付宝支付  5、银行卡支付
     */
    public static final String CASH = "1";
    public static final String MEMBER_CARD = "2";
    public static final String WEPAY = "3";
    public static final String ALIPAY = "4";
    public static final String BANKCARD = "5";
    public static final int QRCODE_LENGTH = 300;

    public static String getPayModeDesc(int payMode) {
        SparseArray<String> m = new SparseArray<String>();
        m.put(1, "银行卡");
        m.put(2, "会员卡");
        m.put(3, "现金");
        m.put(4, "其它");
        m.put(5, "电子卡");
        m.put(6, "微信会员卡");
        m.put(7, "支付宝");
        m.put(8, "微信");
        return m.get(payMode);
    }

    public static final String ASSETS_DIR = "file:///android_asset/";
    public static final String CACHE_DIR = "/common";
    public static final String DAILY_TRANS_TAG = "1001";
    public static final String TRANLOG_DETAIL_TAG = "1002";
}
