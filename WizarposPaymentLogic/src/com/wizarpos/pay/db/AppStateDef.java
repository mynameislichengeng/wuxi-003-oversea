package com.wizarpos.pay.db;

/**
 * app运行时的状态量定义表 <br>
 * 同步维护数据库
 * 
 * @author wu
 */
public class AppStateDef {
	public static final String isInit = "isInit";// 是否已经初始化

	public static final String isLogin = "isLogin";// 是否已经登陆
	public static final String isInService = "isInService";// 是否是地方应用调用
	public static final String isOffline = "isOffline";// 是否是地方应用调用
	public static final String PUB_CERT_AILAS = "PUB_CERT_AILAS";// 证书
	public static final String LAST_PRINT = "LAST_PRINT";// 重打最后一笔打印
	public static final String LAST_PAY_MODE = "LAST_PAY_MODE";// 最后一次选中的支付方式
	public static final String isRegisterTerminal = "isRegisterTerminal";//是否调用服务端接口进行推送注册
}
