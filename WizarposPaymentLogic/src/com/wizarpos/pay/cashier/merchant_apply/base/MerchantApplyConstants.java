package com.wizarpos.pay.cashier.merchant_apply.base;

import com.wizarpos.pay.cashier.merchant_apply.entity.MerchantApplyRequest;

/**
 * 
 * @Author: Huangweicai
 * @date 2015-12-1 上午10:25:51
 * @Description: 商户进件常量
 */
public class MerchantApplyConstants {
	/** 该终端已绑定商户，是否确认解绑以绑定新商户*/
	public static final int ERROR_CODE_HAS_BIND = 157;
	/** 商户进件拒绝需进行资料的修改*/
	public static final int STATUS_NOTIFY= 151;
	
	/** 第三方支付范围 对应 {@link MerchantApplyRequest} 里的thirdPayRange参数*/
	public static final String PAY_RANGE_ALI = "A";
	public static final String PAY_RANGE_WEPAY = "W";
	public static final String PAY_RANGE_QQ = "Q";
	public static final String PAY_RANGE_BAIDU = "B";
}
