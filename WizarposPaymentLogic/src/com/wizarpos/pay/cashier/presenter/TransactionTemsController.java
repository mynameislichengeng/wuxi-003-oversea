package com.wizarpos.pay.cashier.presenter;

import com.wizarpos.pay.cashier.thrid_app_controller.model.ThirdAppTransactionRequest;

/**
 * 可用交易类型控制接口
 * 
 * @author wu
 * 
 */
public interface TransactionTemsController {
	// 交易类型编号
	public static final int TRANSACTION_TYPE_BANK_CARD = 1;// 1 银行卡
	public static final int TRANSACTION_TYPE_MEMBER_CARD = 2;// 2会员卡
	public static final int TRANSACTION_TYPE_CASH = 3;// 3 现金
	public static final int TRANSACTION_TYPE_OTHER = 4;// 4 其他
	public static final int TRANSACTION_TYPE_WEPAY_MEMBER_CARD = 6;// 6 微信会员卡
	public static final int TRANSACTION_TYPE_ALIPAY = 7;// 7支付宝支付
	public static final int TRANSACTION_TYPE_WEPAY_PAY = 8;// 8微信支付
	public static final int TRANSACTION_TYPE_TEN_PAY = 9;// 9手Q支付
	public static final int TRANSACTION_TYPE_BAIDU_PAY = 10;// 10百度支付
	public static final int TRANSACTION_TYPE_MIXPAY = 11;// 11组合支付
	public static final int TRANSACTION_TYPE_CHANGE_DEL = 12;// 11零头处理
	public static final int TRANSACTION_TYPE_WEPAY_TICKET_CANCEL = 13;// 13微信卡券核销
	public static final int TRANSACTION_TYPE_THIRD_TICKET_CANCEL = 14;// 14第三方卡券核销
	public static final int TRANSACTION_TYPE_MEMBER_TICKET_CANCEL = 15;// 15会员券核销
	public static final int TRANSACTION_TYPE_NO_MOMBER_TICKET_CANCEL = 16;// 16非会员券核销
	public static final int TRANSACTION_TYPE_NO_MOMBER_TICKET_DISCOUNT = 17;// 17折扣核销
	public static final int TRANSACTION_TYPE_UNION_PAY = 18;//18移动支付被扫
	
	
	/**
	 * 禁用银行卡交易
	 */
	void hideCardTransaction();

	/**
	 * 禁用会员卡交易
	 */
	void hideMemberTransaction();

	/**
	 * 禁用现金交易
	 */
	void hideCashTransaction();

	/**
	 * 禁用混合支付
	 */
	void hideMixTransaction();

	/**
	 * 禁用支付宝支付
	 */
	void hideAlipayTransaction();

	/**
	 * 禁用支付宝被扫支付
	 */
	void hideAlipayMicroTransaction();

	/**
	 * 禁用支付宝主扫支付
	 */
	void hideAlipayNativeTransaction();

	/**
	 * 禁用微信支付
	 */
	void hideWxpayTransacton();

	/**
	 * 禁用微信被扫支付
	 */
	void hideWxpayMicroTransaction();

	/**
	 * 禁用微信主扫支付
	 */
	void hideWxpayNativeTransaction();

	/**
	 * 禁用百度支付
	 */
	void hideBaiduTransaction();

	/**
	 * 禁用百度扫码支付
	 */
	void hideBaiduNativeTransaction();

	/**
	 * 禁用百度被扫支付
	 */
	void hideBaiduMicroTransaction();

	/**
	 * 禁用手Q支付
	 */
	void hideTenpayTransaction();

	/**
	 * 禁用手Q扫码支付
	 */
	void hideTenpayNativeTransaction();

	/**
	 * 禁用手Q被扫支付
	 */
	void hideTenpayMicroTransaction();

	/**
	 * 禁用卡券核销
	 */
	void hideTicketPassTransaction();

	/**
	 * 禁用其他支付
	 */
	void hideOtherTransaction();
	
	/**
	 * 直接跳转到交易界面
	 */
	void skipToTransaction(ThirdAppTransactionRequest requestBean);

}
