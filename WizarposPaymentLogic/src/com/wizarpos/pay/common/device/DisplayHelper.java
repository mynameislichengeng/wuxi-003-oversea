package com.wizarpos.pay.common.device;

import com.wizarpos.atool.tool.Tools;
import com.wizarpos.pay.db.AppConfigDef;
import com.wizarpos.pay.db.AppConfigHelper;

import java.util.HashMap;

public class DisplayHelper {
	private Displayer displayer;

	private DisplayHelper() {
		displayer = DeviceManager.getInstance().getDisplayer();
	}

	private static DisplayHelper helper;

	public static DisplayHelper getInstance() {
		if (helper == null || helper.displayer == null) {// 单例嵌套
			helper = new DisplayHelper();
		}
		return helper;
	}

	public void startDisplayer() {

	}

	private String bottomstring = AppConfigHelper
			.getConfig(AppConfigDef.merchantName);

	public void setBottomstring(String bottomstring) {
		this.bottomstring = bottomstring;
	}

	/**
	 * 现金支付成功
	 */
	public void cashPaySuccess(String receivable, String paid, String givechange) {
		// 初始化客显，欢迎页面
		HashMap<String, String> dismap = new HashMap<String, String>();
		dismap.put("templetName", "cashPaySuccess.html");
		dismap.put("receivable", receivable);
		dismap.put("paid", paid);
		dismap.put("givechange", givechange);
		dismap.put("bottomstring", bottomstring);
		displayer.display(dismap);
	}

	/**
	 * 券使用
	 */
	public void giveTicket(String kxamount, String[] kxitem1, String[] kxitem2,
			String[] kxitem3, String[] kxitem4) {
		// 初始化客显，欢迎页面
		HashMap<String, String> dismap = new HashMap<String, String>();
		dismap.put("templetName", "giveTicket.html");
		dismap.put("item1", kxitem1[0]);
		dismap.put("itemAmount1", kxitem1[1]);
		dismap.put("itemNum1", kxitem1[2]);

		dismap.put("item2", kxitem2[0]);
		dismap.put("itemAmount2", kxitem2[1]);
		dismap.put("itemNum2", kxitem2[2]);

		dismap.put("item3", kxitem3[0]);
		dismap.put("itemAmount3", kxitem3[1]);
		dismap.put("itemNum3", kxitem3[2]);

		dismap.put("item4", kxitem4[0]);
		dismap.put("itemAmount4", kxitem4[1]);
		dismap.put("itemNum4", kxitem4[2]);

		dismap.put("itemsamount", Tools.formatFen(Long.parseLong(kxamount)));
		displayer.display(dismap);
	}

	/**
	 * 会员卡支付成功
	 * 
	 * @param cardNo
	 * @param membername
	 * @param consume
	 * @param balance
	 */
	public void startKxMemberCard(String cardNo, String membername,
			String consume, String balance) {
		// 初始化客显，欢迎页面
		HashMap<String, String> dismap = new HashMap<String, String>();
		dismap.put("templetName", "memberPaySuccess.html");
		dismap.put("cardNo", cardNo);
		dismap.put("membername", membername);
		dismap.put("consume", consume);
		dismap.put("balance", balance);
		dismap.put("bottomstring", bottomstring);
		displayer.display(dismap);
	}

	/**
	 * 输入密码
	 */
	public void startKxInputPassword() {
		// 初始化客显，欢迎页面
		HashMap<String, String> dismap = new HashMap<String, String>();
		dismap.put("templetName", "inputPassWord.html");
		dismap.put("bottomstring", bottomstring); // 底部广告语
		displayer.display(dismap);
	}

	/**
	 * 客显调用
	 */
	public void startKxBankCard(String bankcardno, String consume) {
		// 初始化客显，欢迎页面
		HashMap<String, String> dismap = new HashMap<String, String>();
		dismap.put("templetName", "bankCardPaySuccess.html");
		dismap.put("bankcardno", bankcardno);
		dismap.put("consume", consume);
		dismap.put("bottomstring", bottomstring);
		displayer.display(dismap);
	}

}
