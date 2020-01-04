package com.wizarpos.pay.db;

/**
 * 缓存定义表
 *
 * @author wu
 */
public class AppConfigDef {
    // public static final String test_is_wizarpos = "test_is_wizarpos";//
    // 当前是否是wizarpos 测试时使
    public static final String test_use_printer = "test_load_printer";// 当前是否使用打印模块
//																		// 测试时使用
//	public static final String test_load_safe_mode = "test_load_safe_mode";// 当前是否使用安全模块
//																			// 测试时使用
//	public static final String test_use_device_sn = "test_use_device_sn";// 当前是否使用安全模块
//	// 测试时使用
//	public static final String test_load_bank_mode = "test_load_bank_mode";// 当前是否支持刷卡
//																			// 测试时使用
//	public static final String isDebug = "isDebug";// 当前是否为测试模式 测试时使用

    public static final String isInit = "isInit";// 是否初始化标志位

    public static final String ip = "ip";
    public static final String port = "port";
    public static final String order_port = "order_port";// mina端口号
    public static final String app_id = "app_id";
    public static final String app_name = "app_name";
    public static final String secure_password = "secure_password";
    public static final String common_secure_password = "common_secure_password";
    public static final String multi_member_card = "multi_member_card";
    public static final String has_member_set = "has_member_set";
    public static final String has_tranlog_cancel = "has_member_set";
    public static final String sysPosTimeStamp = "sysPosTimeStamp";
    // XXX 慧商户 和 收单应用 的命名过于相似,难以区分,后期修改为醒目的变量名
    public static final String mid = "mid";/* 慧商户号 */
    public static final String checkmid = "checkmid";
    public static final String fid = "fid";/* 慧商户联盟号 */
    public static final String merchantId = "merchantId";/* 收单商户号 */
    public static final String merchantName = "merchantName";/* 收单商户名称 */
    public static final String merchantAddr = "merchantAddr";/* 收单商户地址 */
    public static final String merchantTel = "merchantTel";/* 收单商户电话 */
    public static final String pay_id = "pay_id";/* 收单渠道 */
    public static final String terminalId = "terminalId";/* 终端号 */
    public static final String operatorId = "operatorId";/* 收单操作号 */
    public static final String operatorNo = "operatorNo";/* 慧商户操作员 */
    public static final String operatorTrueName = "operatorTrueName";/* 慧商户操作员真实姓名 */
    public static final String checkLogin = "checkLogin";
    public static final String permission = "permission";
    public static final String alipay_notify_url = "alipay_notify_url";
    public static final String alipay_pattern = "alipay_pattern";
    public static final String alipay_key = "alipay_key";
    public static final String alipay_agent_id = "alipay_agent_id";// 支付宝代理商id
    public static final String alipay_store_id = "alipay_store_id";// 支付宝门店id
    public static final String alipay_store_name = "alipay_store_name";// 支付宝门店名称
    public static final String alipay_payeeTermId = "alipay_payeeTermId";// 支付宝收款方终端号
    public static final String use_alipay = "use_alipay";
    public static final String use_wizarpos_alipay_config = "use_wizarpos_alipay_config";
    public static final String use_pay_route_config = "use_pay_route_config";/* 切换内嵌调用和外部调用支付路由 */
    public static final String weixin_app_id = "weixin_app_id";
    public static final String weixin_app_key = "weixin_app_key";
    public static final String weixin_app_secret = "weixin_app_secret";
    public static final String weixin_partner_id = "weixin_partner_id";
    public static final String weixin_partner_key = "weixin_partner_key";
    public static final String weixin_notify_url = "weixin_notify_url";
    public static final String weixin_mchid_id = "weixin_mchid_id";
    public static final String use_weixin_pay = "use_weixin_pay";
    public static final String use_wizarpos_weixin_pay_config = "use_wizarpos_weixin_pay_config";
    public static final String merchant_has_weixin = "merchant_has_weixin";
    // public static final String offline = "offline";
    public static final String offline_save = "offline_save";
    public static final String pay_set_config = "pay_set_config";
    public static final String member_set_config = "member_set_config";
    public static final String cash_set_config = "cash_set_config";
    public static final String peop_set_config = "peop_set_config";
    public static final String public_key = "public_key";
    public static final String sn = "sn";
    public static final String auto_self_set = "auto_self_set";
    public static final String use_alipay_ticket_checked = "use_alipay_ticket_checked";
    public static final String wepay_token = "wepay_token"; /* 微信token */

    public static final String print_number = "print_number"; /* 打印张数 */

    public static final String baidupay_key = "baidupay_key";// 百度支付key
    public static final String baidupay_id = "baidupay_id";// 百度支付商户号
    public static final String use_baidupay = "use_baidupay";// 是否启用百度支付
    public static final String user_mix_pay = "use_mix_pad";// 是否启用混合支付
    public static final String use_weixin_ticket_checked = "use_weixin_ticket_checked";// 是否启用混合支付
    public static final String is_need_other_pay = "is_need_other_pay";// 是否启用其他支付

    public static final String xinpay_merchid = "xinpay_merchid";// 鑫蓝商户号
    public static final String xinpay_app_key = "xinpay_app_key";// 鑫蓝key
    public static final String xinpay_agent_pay = "xinpay_agent_pay";// 鑫蓝受理商

    public static final String auth_code = "auth_code";//验证码
    public static final String isAlipay = "isAlipay";//是否有支付宝支付
    public static final String isWepay = "isWepay";//是否有微信支付
    public static final String isTenpay = "isTenpay";//是否有QQ钱包支付
    public static final String isBaidupay = "isBaidupay";//是否有百付宝支付
    public static final String isUnionpay = "isUnionpay";//是否有移动支付被扫

    public static final String jiao_round = "jiao_round";//角是否采用四舍五入
    public static final String fen_round = "fen_round";//分是否采用四舍五入
    public static final String no_round = "no_round";//不采用四舍五入

    public static final String tenpay_bargainor_id = "tenpay_bargainor_id";// qq钱包商户号
    public static final String tenpay_key = "tenpay_key";// qq钱包key
    public static final String tenpay_op_user_id = "tenpay_op_user_id";// qq操作员好
    public static final String tenpay_op_user_passwd = "tenpay_op_user_passwd";// 账号密码，
    // 明文密码做
    // MD5
    // 后的值
    public static final String use_tenpay = "use_tenpay";// 是否启用腾讯支付
    public static final String use_wepay_agent = "use_wepay_agent";// 是否启用微信商户版
    public static final String wepay_agent_mchid = "wepay_agent_mchid";// 微信商户版商户号
    public static final String wepay_agent_key = "wepay_agent_key";// 微信商户版key
    public static final String isUseNetPay = "is_use_net_pay";//是否启用网络收单

    //	public static final String isLogin = "isLogin";//是否登陆
    public static final String rememberPsw = "rememberPsw";    //是否记住密码
    public static final String loginOptName = "loginOptName";    //登陆的操作员
    public static final String loginOptPwd = "loginOptPwd";        //登陆的操作员密码
    public static final String lastPOSLoginTime = "lastPOSLoginTime";    //上次收单路由签到的时间(long类型)

    //商户进件相关的
    public static final String isSavePwd = "isSavePwd";//是否记住密码
    public static final String isAutoLogin = "isAutoLogin";//是否自动登录
    public static final String loginName = "loginName";//登录名
    public static final String loginPwd = "loginPwd";//密码

    public static final String isRemark = "is_remark";//是否有备注
    public static final String isNeedBingBankCard = "is_need_bing_bank_card";//是否需要在银行卡支付完成后
    //打印用于绑定微信卡的二维码 @yaosong[20151110]

    public static final String isInputPassword = "is_input_password";//收款折扣时是否输入安全密码

    public static final String merchantType = "merchantType";//商户类型 如果是微盟商户，会返回weimeng wu@[20150824]

    public static final String isSupportBankCard = "isSupportBankCard";
    public static final String isSupportMemberCard = "isSupportMemberCard";
    public static final String isSupportCash = "isSupportCash";
    public static final String isSupportWepay = "isSupportWepay";
    public static final String isSupportAlipay = "isSupportAlipay";
    public static final String isSupportTenpay = "isSupportTenpay";
    public static final String isSupportBaiduPay = "isSupportBaiduPay";
    public static final String isSupportOhterPay = "isSupportOhterPay";
    public static final String isSupportMixPay = "isSupportMixPay";
    public static final String isSupportTicketCancel = "isSupportTicketCancel";
    public static final String isSupportUnionPay = "isSupportUnionPay";//是否支持移动支付
//    public static final String isCollectTip = "isCollectTip";//是否开通小票功能
//    public static final String isSetPercentageAmount = "isSetPercentageAmount";//是否设置百分比
//    public static final String isAllowCustomAmount = "isAllowCustomAmount";//是否允许顾客输入小费


    public static final String saleDeductType = "saleDeductType";//整单提成金额
    public static final String isOpenMemberDeduct = "isOpenMemberDeduct";//会员充值是否开启员工提成 "0"为关闭 "1"为开启


    public static final String BAT_FLAG = "BAT_FLAG";//是否是bat统一平台支付 默认 true ; 中青旅为 true ; 罗森为 true ;
    public static final String BAT_V1_4_FLAG = "BAT_V1_4_FLAG";//是否使用V1.4版本的bat统一平台支付 默认 false ; 威富通为 true @yaosong
    public static final String ROUNDDING_FLAG = "ROUNDDING_FLAG";//是否启用四舍五入 默认 false
    public static final String OTHER_PAY_REMARK_FLAG = "OTHER_PAY_REMARK_FLAG";//是否启用其他支付备注功能 默认 false ; 易筋经为 true
    public static final String NEED_BAR_CODE_FLAG = "NEED_BAR_CODE_FLAG";//是否需要打印流水号条码 默认 false ; 罗森版本为 true
    public static final String DEBUGING = "DEBUGING"; //调试模式 默认 false
    public static final String IS_BLOCK_UI = "IS_BLOCK_UI"; //POS打印是否阻塞UI线程 默认为 true
    public static final String HANDUI_IS_BLOCK_UI = "HANDUI_IS_BLOCK_UI"; //手持打印是否阻塞UI线程 默认为 false
    public static final String SWITCH_KEYBOARD_FLAG = "SWITCH_KEYBOARD_FLAG"; //是否允许切换键盘 默认为 false ; 罗森版本为 true
    public static final String SHOW_BANK_PAY_WHITHOUT_PAYMENTROUTER = "SHOW_BANK_PAY_WHITHOUT_PAYMENTROUTER"; //没有收单应用是否显示银行卡支付 wu@[20151110] 默认为 true ;中青旅版本为 false
    public static final String CONFIG_PAY_MODE = "CONFIG_PAY_MODE"; //是否允许自定义支付方式 默认为  false ; 罗森版本为 true
    public static final String SHOW_DAY_PRITN = "SHOW_DAY_PRITN"; //是否显示日结单打印功能 默认为  false ; 罗森版本为 true
    public static final String DISCOUNTABLE = "DISCOUNTABLE"; //是否允许折扣 默认为  false ; 东志信版本为 true wu@[20151115]
    public static final String THIRD_PAY_TICKET_PUBLISH = "THIRD_PAY_TICKET_PUBLISH";//第三方支付及其他支付允许发券 默认为 false
    public static final String SWITCH_LANGUAGE = "SWITCH_LANGUAGE";//设置显示语言

    public static String APP_VERSION_NAME = "APP_VERSION_NAME";//默认为正常版本

    public static final String SUFFIX_URL = "SUFFIX_URL";//服务器版本放到head中
    public static final String SERVER_VERSION = "SERVER_VERSION";//服务器版本放到head中维护 wu

    public static final String merchantHandLogo = "merchantHandLogo";//商户 logo wu
    public static final String merchantPOSLogo = "merchantPOSLogo";//商户 logo wu
    public static final String agentHandLogo = "agentHandLogo";//代理商 logo wu
    public static final String agentPosLogo = "agentPosLogo";//代理商 logo wu


    public static final String refundOperId = "refund_oper_id";//
    public static final String refundOperName = "refund_oper_name";//

    /**
     * 网络收单
     */
    public static final String isNeedVoice = "";//是否需要开启语音
    //商户终端体系改造 yaosong
    public static final String sessionId = "sessionId";//sessionId
    public static final String lastOpreatorUpateTime = "lastOpreatorUpateTime";//上次更新操作员的时间
    public static final String appPushId = "wizarposAppPushId";//推送Id
    public static final String authFlag = "authFlag";//当前操作员权限类别
    public static final String exchangeRate = "exchangeRate";//汇率
    public static final String collectTips = "collectTips";//是否启用小费
    public static final String tipsPercentageAllow = "tipsPercentageAllow";//是否允许手动设置百分比，T允许，F不允许
    public static final String percentP1 = "percentP1";//1档小费百分比
    public static final String percentP2 = "percentP2";//2档小费百分比
    public static final String percentP3 = "percentP3";//3档小费百分比
    public static final String tipsCustomAllow = "tipsCustomAllow";//是否允许顾客输入小费金额, T允许  F不允许
    public static final String tipsTerminalAllow = "tipsTerminalAllow";//是否允许客户端修改参数,T允许  F不允许
    public static final String mandatoryFlag = "mandatoryFlag";//是否需要invoice
    public static final String invoicenum = "invoicenum";
//	public static final String loginMid = "loginMid";//登陆的商户号
//	public static final String loginMerchantName = "loginMerchantName";//登陆的商户名


    //SP中使用的常量
    public static final String SP_loginedMerchant = "SP_loginedMerchant";//商户号+商户名，存储于SP中的字段名
    //用于存储登陆过信息的SP文件名
    public static final String SP_loginedInfo = "SP_loginedInfo";
    public static final String SP_lastLoginId = "SP_lastLoginId";
    public static final String SP_lastPasswd = "SP_lastPasswd";
    public static final String SP_lastLoginMid = "SP_lastLoginMid";
    public static final String SP_isRemember = "SP_isRemember";


    public static final String CARDLINK_LOGIN = "CARDLINK_LOGIN";
    public static final String CARDLINK_INIT_KEY = "CARDLINK_INIT_KEY";
    public static final String CARDLINK_MECHANTID = "CARDLINK_MECHANTID";
    public static final String CARDLINK_TERMINALID = "CARDLINK_TERMINALID";
    public static final String CARDLINK_TPDU = "CARDLINK_TPDU";
    public static final String CARDLINK_SERVERIP = "CARDLINK_SERVERIP";
    public static final String CARDLINK_SERVERPORT = "CARDLINK_SERVERPORT";
    public static final String CARDLINK_AUTHCODE = "CARDLINK_AUTHCODE";


    public static final String TERMCAP = "TERMCAP";//终端能力

    public static final String PRINT_CONTEXT = "PRINT_CONTEXT";//打印商户小票内容
    public static final String PRINT_CUSTOMER_CONTEXT = "PRINT_CUSTOMER_CONTEXT";//打印顾客小票内容
    public static final String PRINT_SALE_REFUND_CONTEXT = "PRINT_SALE_REFUND_CONTEXT";//打印商户撤销小票内容
    public static final String PRINT_CUSTOMER_REFUND_CONTEXT = "PRINT_CUSTOMER_REFUND_CONTEXT";//打印顾客撤销小票内容

    public static final String CNY_AMOUNT = "CNY_AMOUNT";//由于支付时有轮询情况，不在同一个类及接口中返回数据，用于临时存储

    public static final String CLIENT_ID = "CLIENT_ID";//个推连接成功后的clientid

}
