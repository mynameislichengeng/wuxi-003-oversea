package com.wizarpos.pay.common;

/**
 * Created by wu on 15/11/13.
 */
public class Config_Common {

    public static final boolean BAT_FLAG = true;//是否是bat统一平台支付 默认 false ; 中青旅为 true ; 罗森为 true ;
    public static final boolean BAT_V1_4_FLAG = false;//是否使用V1.4版本的bat统一平台支付 默认 false ; 威富通为 true @yaosong
    public static final boolean ROUNDDING_FLAG = false;//是否启用四舍五入 默认 false
    public static final boolean OTHER_PAY_REMARK_FLAG = false;//是否启用其他支付备注功能 默认 false ; 易筋经为 true
    public static final boolean NEED_BAR_CODE_FLAG = false;//是否需要打印流水号条码 默认 false ; 罗森版本为 true
    public static final boolean DEBUGING = false; //调试模式 默认 false
    public static final boolean IS_BLOCK_UI = true; //POS打印是否阻塞UI线程 默认为 true
    public static final boolean HANDUI_IS_BLOCK_UI = false; //手持打印是否阻塞UI线程 默认为 true
    public static final boolean SWITCH_KEYBOARD_FLAG = false; //是否允许切换键盘 默认为 false ; 罗森版本为 true
    public static final boolean SHOW_BANK_PAY_WHITHOUT_PAYMENTROUTER = true; //没有收单应用是否显示银行卡支付 wu@[20151110] 默认为 true ;中青旅版本为 false
    public static final boolean CONFIG_PAY_MODE = false; //是否允许自定义支付方式 默认为  false ; 罗森版本为 true
    public static final boolean SHOW_DAY_PRITN = false; //是否显示日结单打印功能 默认为  false ; 罗森版本为 true
    public static final boolean DISCOUNTABLE = false; //是否允许折扣 默认为  false ; 东志信版本为 true wu@[20151115]
    public static final boolean THIRD_PAY_TICKET_PUBLISH = false;//第三方支付及其他支付允许发券 默认为 false

    public static final int APP_VERSION_NORMAL = 0X01;//正常版
    public static final int APP_VERSION_LAWSON = 0X02;//罗森版
    public static final int APP_VERSION__CHANNEL  = 0X03; //渠道版
    public static final int APP_VERSION_PL = 0X04;//普兰
    public static int APP_VERSION_NAME = APP_VERSION_NORMAL;//默认为正常版本

    public static final String SUFFIX_URL = "/member-server/service";//服务器版本放到head中
    public static final String SERVER_VERSION = "V1_5";//服务器版本放到head中维护 wu

    //*************************************************************************
    //
    // 罗森cashier2
    //
    // *************************************************************************
    //public static final String DEFAULT_IP = "train.wizarpos.com";
   //	public static final String DEFAULT_IP = "cashier.tjw2015.cn";
//    	public static final String DEFAULT_IP = "cashier.91huishang.com";
    public static final String DEFAULT_IP = "cashier2.wizarpos.com";
//    public static final String DEFAULT_IP = "portal3.wizarpos.com";
    public static final String DEFAULT_PORT = "80";

}
