package com.wizarpos.pay.cashier.presenter.transaction;

import android.content.Context;

import com.wizarpos.pay.cashier.pay_tems.alipay.AlipayMicroTransactionImpl;
import com.wizarpos.pay.cashier.pay_tems.alipay.AlipayNativeTransactionImpl;
import com.wizarpos.pay.cashier.pay_tems.alipay.inf.AlipayMicroTransaction;
import com.wizarpos.pay.cashier.pay_tems.alipay.inf.AlipayNativeTransaction;
import com.wizarpos.pay.cashier.pay_tems.bat.BatCommomTransactionImpl;
import com.wizarpos.pay.cashier.pay_tems.bat.BatMicroTransactionImpl;
import com.wizarpos.pay.cashier.pay_tems.bat.BatNativeTransactionImpl;
import com.wizarpos.pay.cashier.pay_tems.bat.BatTransation;
import com.wizarpos.pay.cashier.pay_tems.bat.inf.BatCommonTransaction;
import com.wizarpos.pay.cashier.pay_tems.bat.inf.MicroTransaction;
import com.wizarpos.pay.cashier.pay_tems.bat.inf.NativeTransaction;
import com.wizarpos.pay.cashier.pay_tems.tenpay.TenpayMicroTransactionImpl;
import com.wizarpos.pay.cashier.pay_tems.tenpay.inf.TenpayMicroTransaction;
import com.wizarpos.pay.cashier.pay_tems.wepay.WepayMicroTransactionImpl;
import com.wizarpos.pay.cashier.pay_tems.wepay.WepayNativeTransactionImpl;
import com.wizarpos.pay.cashier.pay_tems.wepay.inf.WepayMicroTransaction;
import com.wizarpos.pay.cashier.pay_tems.wepay.inf.WepayNativeTransaction;
import com.wizarpos.pay.cashier.presenter.transaction.impl.BaiduPayMicroTransactionImpl;
import com.wizarpos.pay.cashier.presenter.transaction.impl.BaiduPayNativeTransactionImpl;
import com.wizarpos.pay.cashier.presenter.transaction.impl.CardTransactionImpl;
import com.wizarpos.pay.cashier.presenter.transaction.impl.CashTransactionImpl;
import com.wizarpos.pay.cashier.presenter.transaction.impl.MemberTransactionImpl;
import com.wizarpos.pay.cashier.presenter.transaction.impl.MixPayTransaction;
import com.wizarpos.pay.cashier.presenter.transaction.impl.OtherpayTransactionImpl;
import com.wizarpos.pay.cashier.presenter.transaction.inf.BaiduPayMicroTransaction;
import com.wizarpos.pay.cashier.presenter.transaction.inf.BaiduPayNativeTransaction;
import com.wizarpos.pay.cashier.presenter.transaction.inf.CardTransaction;
import com.wizarpos.pay.cashier.presenter.transaction.inf.CashTransaction;
import com.wizarpos.pay.cashier.presenter.transaction.inf.MemberTransaction;

public class TransactionFactory {

	private TransactionFactory() {
	}

	/**
	 * 现金交易
	 */
	public static CashTransaction newCashTransaction(Context context) {
		return new CashTransactionImpl(context);
	}

	/**
	 * 会员卡交易
	 */
	public static MemberTransaction newMemberTransaction(Context context) {
		return new MemberTransactionImpl(context);
	}

	/**
	 * 银行卡交易
	 */
	public static CardTransaction newCardTransaction(Context context) {
		return new CardTransactionImpl(context);
	}

	/**
	 * 微信扫码交易
	 */
	public static WepayNativeTransaction newWepayNativeTransaction(// [@hong 2015-7-17 09:54:16]
			Context context) {
		return new WepayNativeTransactionImpl(context);
	}

	/**
	 * bat被扫交易
	 */
	public static MicroTransaction newBatMicroTransaction(Context context) {//[@hong bat被扫 2015-8-28]
		return new BatMicroTransactionImpl(context);
	}
	/**
	 * bat主扫交易
	 */
	public static NativeTransaction newBatNativeTransaction(Context context) {//[@hong bat主扫 2015-8-28]
		return new BatNativeTransactionImpl(context);
	}
	/**
	 * 微信被扫交易
	 */
	public static WepayMicroTransaction newWepayMicroTransaction(Context context) {
		return new WepayMicroTransactionImpl(context);
	}

	/**
	 * 支付宝扫码交易
	 */
	public static AlipayNativeTransaction newAlipayNativeTransaction(// [@hong 2015-7-17 09:51:44]
			Context context) {
		return new AlipayNativeTransactionImpl(context);
	}

	/**
	 * 支付宝被扫交易
	 */
	public static AlipayMicroTransaction newAlipayMicroTransaction(// [@hong 2015-7-17 09:51:44]
			Context context) {
		return new AlipayMicroTransactionImpl(context);
	}

	/**
	 * 百度被扫交易
	 */
	public static BaiduPayMicroTransaction newBaiduMicroPayTransaction(Context context) {
		return new BaiduPayMicroTransactionImpl(context);
	}

	/**
	 * 百度被扫交易
	 */
	public static BaiduPayNativeTransaction newBaiduNativePayTransaction(Context context) {
		return new BaiduPayNativeTransactionImpl(context);
	}

	/**
	 * 手Q钱包交易
	 */
	public static TenpayMicroTransaction newTenpayMicroTransaction(Context context) {
		return new TenpayMicroTransactionImpl(context);
	}

//	public static TenpayNativeTransaction newTenpayNativeTransaction(Context context) {
//		return new TenpayNativeTransactionImpl(context);
//	}
	/**
	 * 移动支付
	 * @param context
	 * @return
	 */
	public static BatCommonTransaction newCommonTransaction(Context context){
		return new BatCommomTransactionImpl(context);
	}
	/**
	 * 其它支付
	 * 
	 * @param context
	 * @return
	 */
	public static OtherpayTransactionImpl newOtherPayTransaction(Context context) {
		return new OtherpayTransactionImpl(context);
	}
	
	/**
	 * 组合支付
	 * 
	 * @param context
	 * @return
	 */
	public static MixPayTransaction newMixPayTransaction(Context context) {
		return new MixPayTransaction(context);
	}

	/**
	 * bat交易
	 */
	public static BatTransation newBatTransaction(Context context) {//[@yaosong bat 2015-12-30]
		return new BatTransation(context);
	}
}
