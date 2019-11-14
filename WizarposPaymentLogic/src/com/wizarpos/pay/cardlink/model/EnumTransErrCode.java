package com.wizarpos.pay.cardlink.model;

public enum EnumTransErrCode
{
    NoError(                "",                 0),
    
	AmountOverLimit(		"金额超限", 			1),
    AmountZero(				"金额为0不可脱机,脱机拒绝", 2),
    AppBlocked(				"应用锁定,交易中止", 		3),
    
    BlackCard(				"此卡是黑名单卡", 		10),
	Busy(    		    	"系统正忙", 			11),
	
    CardBlocked(			"卡片锁定,交易中止", 		20),
    ChecksumError(			"校验码错误", 			21),
    CommReadTimeout(		"接收数据超时", 			22),
    ConnectFailed(			"建立连接失败", 			23),
    CupNoTip(				"内卡不支持小费", 		24),
    
    DataMissing(			"数据缺失,交易中止", 		30),
    DecryptKeyError(		"密钥解密失败", 			31),
    
    ExpiryCard(				"卡片已过期,交易拒绝", 	40),
    GenAcError(				"GAC错,交易中止", 		41),
    
    IcAuthError(			"IC卡数据认证失败,交易中止",50),
    IcError(				"IC卡出错,交易中止", 	51),
    InitIcError(			"初始化IC卡设备失败", 	52),
	InsufficientBalance(	"余额不足,交易拒绝", 		53),
    KeyCheckError(			"密钥校验错", 			54),
    
    LoginFail(				"签到失败", 			60),
    LoginFirst(				"请先做签到", 			61),
    LoginIdError(			"操作员不存在", 			62),
    
    MacCheckError(			"MAC错", 			70),
    MacSetError(			"MAC设置失败", 		71),
    MoreCard(				"有多张卡，请使用单一卡片", 	72),
    
    NoApp(					"无此AID", 			80),
    NoTransInCard(			"卡内没有交易明细", 		81),
    NoTransInTerminal(		"终端内没有交易流水", 		82),
    NotAccepted(			"NOT ACCEPTED,交易中止", 83),
    NotSupport(				"终端不支持该交易", 		84),
    CannotChangeMID(		"终端内有交易，不能修改商户号",85),
    CannotChangeTID(		"终端内有交易，不能修改终端号",86),
    
    OldPasswordError(		"原密码不正确", 			90),
    OpenIcError(			"打开IC卡设备失败", 		91),
    OpenMsrError(			"打开磁条阅读器设备失败", 	92),
    
    ParaError(				"参数错误",			100),
    PasswordError(			"密码错", 				101),
    Pack8583Error(			"数据打包错", 			102),
    PinTimeout(				"输入密码超时,交易中止", 	103),
    PinpadError(			"密码键盘错误", 			104),
    PrinterError(			"打印机错误", 			105),
    PrinterNoPaper(			"打印机缺纸，请先装纸", 	106),
    
    ReadIcError(			"读取IC卡错误", 		110),
    RefundOverLimit(		"超退货额度", 			111),
    ReversalError(			"冲正失败", 			112),
    SettleFirst(			"请先结算", 			113),
    
    TipNotSupport(			"小费不支持", 			120),
    TipOverLimit(			"超小费额度", 			121),
    TransEmpty(				"无交易流水", 			122),
    TransAdjusted(			"该交易已调整", 			123),
    TransFull(				"存储满，请先结算", 		124),
    TransNotFound(			"原交易不存在", 			125),
    TransDeclined(			"交易拒绝", 			126),
    TryOtherCard(			"请尝试其他交易方式", 		127),
    
    Unpack8583Error(		"数据解包错", 			130),
    UnpackMsgtypeError(		"消息类型错", 			131),
	UnpackAmountError(		"金额不一致", 			132),
	UnpackSettleFlagError(	"对账应答码错", 			133),
	UnpackTidError(			"终端号不一致", 			134),
	UnpackMidError(			"商户号不一致", 			135),
	UnpackProcessCodeError(	"交易处理码不一致", 		136),
	UnpackPanError(			"卡号不一致", 			137),
	UnpackTraceError(		"受卡方系统跟踪号不一致", 	138),
    UserCancelled(			"用户取消", 			139),
    UnknownError(			"未知错误", 			140),
	
    VerifyIcError(			"IC卡验证失败", 		150);	
	
	


	
	private String message;
	private int code;
  
	private EnumTransErrCode(String message, int code)
	{
		this.message = message;
		this.code = code;
	}
  
	public static String getMessage(int code)
	{
		for (EnumTransErrCode e : EnumTransErrCode.values()) {
			if (e.getCode() == code) {
				return e.getMessage();
			}
		}
		return "未知类型";
	}
  
	public String getMessage()
	{
		return this.message;
	}
  
	public int getCode()
  	{
		return this.code;
  	}
}