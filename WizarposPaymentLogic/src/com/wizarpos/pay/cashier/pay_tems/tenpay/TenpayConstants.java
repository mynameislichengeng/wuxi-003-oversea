package com.wizarpos.pay.cashier.pay_tems.tenpay;

import java.util.HashMap;
import java.util.Map;

import android.text.TextUtils;

/**
 * 
 * @Author:Huangweicai
 * @Date:2015-5-28
 * @Reason:财付通参数
 */
public class TenpayConstants {

	/** 商户号 由手Q支付系统统一分配 */
	public static final String BARGAINOR_ID = "1900000109";
	/** 手Q支付与商户之间约定的32位字符串，算签名sign时使用 */
	public static final String MD5_SECRET_KEY = "8934e7d15453e97507ef794cf7b0519d";
	/** 通知地址 **/
	public static final String MICRO_NOTIFY_URL = "http://wuxi2.wizarpos.com/member-server/QQPayNotify";
	/** 通知地址 **/
	public static final String NATIVE_NOTIFY_URL = "http://wuxi2.wizarpos.com/member-server/QQActivePayNotify";
	/** 设备号 **/
	public static final String DEVICE_ID = "WP10000100001";
	/** 提交付款码支付接口 */
	public static final String SCAN_PAY_URL = "https://myun.tenpay.com/cgi-bin/scan/code_pay.cgi";
	/** 查询 */
	public static final String ORDER_QUERY_URL = "https://myun.tenpay.com/cgi-bin/clientv1.0/qpay_order_query.cgi";
	/** 撤销 **/
	public static final String ORDER_REVOKE_URL = "https://myun.tenpay.com/cgi-bin/scan/code_cancel.cgi";

	/** 错误码 */
	public static final Map<Integer, String> ERROR_CODE = new HashMap<Integer, String>();
	/** 错误码起始 */
	private final static int ERROR_CODE_HALF = 66227000;

	private final static String nativePayUrl = "https://www.tenpay.com/app/wsm/wsm_pay_req_code.cgi";

	static {
		ERROR_CODE.put(ERROR_CODE_HALF + 1, "商户签名校验失败");
		ERROR_CODE.put(ERROR_CODE_HALF + 2, "商户没有手Q支付权限");
		ERROR_CODE.put(ERROR_CODE_HALF + 3, "不在扫码支付的允许列表");
		ERROR_CODE.put(ERROR_CODE_HALF + 4, "字符集转失败");
		ERROR_CODE.put(ERROR_CODE_HALF + 5, "需要用户输入支付密码");
		ERROR_CODE.put(ERROR_CODE_HALF + 6, "用户授权码无效");
		ERROR_CODE.put(ERROR_CODE_HALF + 7, "用户证书非法");
		ERROR_CODE.put(ERROR_CODE_HALF + 8, "余额不足");
		ERROR_CODE.put(ERROR_CODE_HALF + 9, "撤单失败，订单已撤单");
		ERROR_CODE.put(ERROR_CODE_HALF + 10, "撤单失败，订单已支付成功");
		ERROR_CODE.put(ERROR_CODE_HALF + 11, "撤单失败，订单已退款");
	}

	public static String praseError(String des, int errorCode) {
		if (TextUtils.isEmpty(des)) {
			String result = TenpayConstants.ERROR_CODE.get(errorCode);
			if (TextUtils.isEmpty(result)) {
				result = "未知异常";
			}
			return result + "[" + errorCode + "]";
		} else {
			return des + "[" + errorCode + "]";
		}
	}
}
