package com.wizarpos.pay.cardlink.model;

public enum EnumProgressCode
{
	ConfirmAmount(			"请确认金额", 						1),
	ConfirmPAN(				"请确认卡号是否正确", 					2),
	InputAdminPass(			"请输入主管密码", 					3),
	InputAuthCode(			"请输入授权码", 						4),
	InputExpiryDate(		"请输入卡有效期（年年月月）", 			5),
	InputOldBatchNumber(	"请输入原批次号", 					6),
	InputPIN(				"请在密码键盘上输入密码", 				7),
	InputOldRRN(			"请输入原参考号", 					8),
	InputOldTicket(			"请输入原凭证号", 					9),
	InputOldTransDate(		"请输入原交易日期(月月日日)", 			10),
	InputTransAmount(		"请输入交易金额", 					11),
	RequestCard(			"请使用你的银行卡",					12),
	ProcessOnline(			"正在连接服务器", 					13),
	ShowTransTotal(			"交易累计", 							14);

	private String message;
	private int code;

	EnumProgressCode(String var3, int var4) {
		this.message = var3;
		this.code = var4;
	}

	public static String getMessage(int var0) {
		EnumProgressCode[] var4;
		int var3 = (var4 = values()).length;

		for(int var2 = 0; var2 < var3; ++var2) {
			EnumProgressCode var1 = var4[var2];
			if(var1.getCode() == var0) {
				return var1.getMessage();
			}
		}

		return "未知类型";
	}

	public String getMessage() {
		return this.message;
	}

	public int getCode() {
		return this.code;
	}
}