package com.wizarpos.pay.cashier.presenter;

import android.app.Activity;
import android.content.Intent;

/**
 * 交易流程中相关界面控制
 * 
 * @author wu
 * 
 */
public interface ITransactionFlowController {
	/**
	 * 输入界面
	 */
	public void toInputTicketView(Activity activity, String title, Intent intent, int requestCode);
	public void toInputTicketView(Activity activity, String title, boolean isUseSwipe, Intent intent, int requestCode);
	public void toInputView(Activity activity, String title, Intent intent, int requestCode);
	public void toInputView(Activity activity, String title, boolean isUseSwipe, Intent intent, int requestCode);
	public void toInputView(Activity activity, String title, boolean isUseSwipe, boolean isUseText, boolean isUseCamera, Intent intent, int requestCode);
	public void toInputTicketView(Activity activity, String title, boolean isUseSwipe, boolean isUseText, boolean isUseCamera, Intent intent, int requestCode);
	public void toMemberInputView(Activity activity, String title, Intent intent, int requestCode);

	/**
	 * 现金消费
	 * 
	 * @param activity
	 * @param intent
	 */
	public void toCashTransactionView(Activity activity, Intent intent);

	public void toCashMixTransactionView(Activity activity, Intent intent, int requestCode);

	/**
	 * 其他支付
	 * 
	 * @param activity
	 * @param intent
	 */
	public void toOtherTransactionView(Activity activity, Intent intent);

	public void toOtherMixTransactionView(Activity activity, Intent intent, int requestCode);

	/**
	 * 银行卡消费
	 * 
	 * @param activity
	 * @param intent
	 */
	public void toCardTransactionView(Activity activity, Intent intent);

	public void toCardMixTransactionView(Activity activity, Intent intent, int requestCode);

	/**
	 * 会员卡消费
	 * 
	 * @param activity
	 * @param intent
	 */
	
//	public void toMemberTransactionVew(Activity activity, Intent intent);

	public void toMemberMixTransactionVew(Activity activity, Intent intent, int requestCode);

	/**
	 * 支付宝被扫支付
	 * 
	 * @param activity
	 * @param intent
	 */
	public void toAlipayMicroTransaction(Activity activity, Intent intent);

	public void toAlipayMixMicroTransaction(Activity activity, Intent intent, int requestCode);

	/**
	 * 支付宝扫码支付
	 * 
	 * @param activity
	 * @param intent
	 */
	public void toAlipayNativeTransaction(Activity activity, Intent intent);

	public void toAlipayMixNativeTransaction(Activity activity, Intent intent, int requestCode);

	/**
	 * 微信被扫支付
	 * 
	 * @param activity
	 * @param intent
	 */
	public void toWepayMicroTransaction(Activity activity, Intent intent);

	public void toWepayMixMicroTransaction(Activity activity, Intent intent, int requestCode);

	/**
	 * 微信扫码支付
	 * 
	 * @param activity
	 * @param intent
	 */
	public void toWepayNativeTransaction(Activity activity, Intent intent);

	public void toWepayMixNativeTransaction(Activity activity, Intent intent, int requestCode);

	/**
	 * 手q被扫支付
	 * 
	 * @param activity
	 * @param intent
	 */
	public void toTenpayMicroTransaction(Activity activity, Intent intent);

	public void toTenpayMixMicroTransaction(Activity activity, Intent intent, int requestCode);

	/**
	 * 手q扫码支付
	 * 
	 * @param activity
	 * @param intent
	 */
	public void toTenpayNativeTransaction(Activity activity, Intent intent);

	public void toTenpayMixNativeTransaction(Activity activity, Intent intent, int requestCode);

	/**
	 * 百度被扫支付
	 * 
	 * @param activity
	 * @param intent
	 */
	public void toBaiduMicroTransaction(Activity activity, Intent intent);

	public void toBaiduMixMicroTransaction(Activity activity, Intent intent, int requestCode);
	
	/**
	 * 移动支付被扫 (木有组合支付)
	 * @param activity
	 * @param intent
	 */
	public void toUnionMicroTransaction(Activity activity,Intent intent);

	/**
	 * 百度扫码支付
	 * 
	 * @param activity
	 * @param intent
	 */
	public void toBaiduNativeTransaction(Activity activity, Intent intent);

	public void toBaiduMixNativeTransaction(Activity activity, Intent intent, int requestCode);

	/**
	 * 支付成功
	 * 
	 * @param activity
	 * @param intent
	 */
	public void toTransactionSuccess(Activity activity, Intent intent);

	/**
	 * 组合支付
	 * 
	 * @param activity
	 * @param intent
	 */
	public void toMixTransaction(Activity activity, Intent intent);

	/**
	 * 支付失败
	 */
	public void toTransactionFaild();

	/**
	 * 券发行界面
	 * 
	 * @param activity
	 * @param intent
	 */
	public void toTicketPublishActivity(Activity activity, Intent intent);

	/**
	 * 组合支付微信卡券核销
	 * @param activity
	 * @param intent
	 * @param requestCode
	 */
	public void toWepayTicketCancelMix(Activity activity, Intent intent, int requestCode);
	
	/**
	 * 组合支付第三方卡券核销
	 * @param activity
	 * @param intent
	 * @param requestCode
	 */
	public void toThridTicketCancelMix(Activity activity, Intent intent, int requestCode);
	
	/**
	 * 组合支付会员券核销
	 * @param activity
	 * @param intent
	 * @param requestCode
	 */
	public void toMixMemberTicketPass(Activity activity,Intent intent,int requestCode);
	
	/**
	 * 组合支付普通券核销
	 * @param activity
	 * @param intent
	 */
	public void toMixNormalTicketPass(Activity activity,Intent intent,int requestCode);
}
