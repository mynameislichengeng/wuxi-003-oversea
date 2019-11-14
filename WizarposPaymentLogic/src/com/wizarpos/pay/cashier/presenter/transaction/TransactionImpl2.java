package com.wizarpos.pay.cashier.presenter.transaction;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import com.wizarpos.pay.cashier.model.TransactionInfo2;
import com.wizarpos.pay.cashier.presenter.TransactionTemsController;
import com.wizarpos.pay.cashier.presenter.transaction.inf.Transaction2;
import com.wizarpos.pay.cashier.thrid_app_controller.model.ThirdAppTransactionRequest;
import com.wizarpos.pay.common.Constants;
import com.wizarpos.pay.common.base.BasePresenter.ResultListener;

public class TransactionImpl2 implements Transaction2 {

	protected TransactionInfo2 transactionInfo;

	protected Context context;

	public TransactionImpl2(Context context) {
		this.context = context;
		transactionInfo = new TransactionInfo2();
	}

	public TransactionImpl2(TransactionInfo2 transactionInfo) {
		this.transactionInfo = transactionInfo;
	}

	protected ThirdAppTransactionRequest thirdRequest;

	public void handleIntent(Intent intent) {

		transactionInfo.setTransactionType(intent.getIntExtra(
				Constants.TRANSACTION_TYPE, -1));// 交易类型.如果传入transactionType.则已传入的类型为准,否则子类根据自己的交易类型定义
		String tempStr = intent.getStringExtra(Constants.initAmount);
		if (TextUtils.isEmpty(tempStr)) {
			transactionInfo.setInitAmount(intent.getIntExtra(
					Constants.initAmount, 0));
		} else {
			transactionInfo.setInitAmount(Integer.parseInt(tempStr));
		}
		tempStr = intent.getStringExtra(Constants.shouldAmount);
		if (TextUtils.isEmpty(tempStr)) {
			transactionInfo.setShouldAmount(intent.getIntExtra(
					Constants.shouldAmount, 0));
		} else {
			transactionInfo.setShouldAmount(Integer.parseInt(tempStr));
		}
		if (transactionInfo.getShouldAmount() == 0) {
			transactionInfo.setShouldAmount(transactionInfo.getInitAmount());
		}
		tempStr = intent.getStringExtra(Constants.discountAmount);
		if (TextUtils.isEmpty(tempStr)) {
			transactionInfo.setDiscountAmount(intent.getIntExtra(
					Constants.discountAmount, 0));
		} else {
			transactionInfo.setDiscountAmount(Integer.parseInt(tempStr));
		}
		tempStr = intent.getStringExtra(Constants.reduceAmount);
		if (TextUtils.isEmpty(tempStr)) {
			transactionInfo.setReduceAmount(intent.getIntExtra(
					Constants.reduceAmount, 0));
		} else {
			transactionInfo.setReduceAmount(Integer.parseInt(tempStr));
		}
		tempStr = intent.getStringExtra(Constants.realAmount);
		if (TextUtils.isEmpty(tempStr)) {
			transactionInfo.setRealAmount(intent.getIntExtra(
					Constants.realAmount, 0));
		} else {
			transactionInfo.setRealAmount(Integer.parseInt(tempStr));
		}
		tempStr = intent.getStringExtra(Constants.changeAmount);
		if (TextUtils.isEmpty(tempStr)) {
			transactionInfo.setChangeAmount(intent.getIntExtra(
					Constants.changeAmount, 0));
		} else {
			transactionInfo.setChangeAmount(Integer.parseInt(tempStr));
		}
		tempStr = intent.getStringExtra(Constants.mixInitAmount);
		if (TextUtils.isEmpty(tempStr)) {
			transactionInfo.setMixInitAmount(intent.getIntExtra(
					Constants.mixInitAmount, 0));
		} else {
			transactionInfo.setMixInitAmount(Integer.parseInt(tempStr));
		}
		transactionInfo.setMixTranLogId(intent
				.getStringExtra(Constants.mixTranLogId));
		transactionInfo.setCardNo(intent.getStringExtra("cardNo"));
		transactionInfo.setCardType(intent.getStringExtra("cardType"));

		initExtraParams(intent);
	}

	private void initExtraParams(Intent intent) {
		// 人工/自动 发券设置
		thirdRequest = (ThirdAppTransactionRequest) intent
				.getSerializableExtra(Constants.thirdRequest);
		if (thirdRequest == null) {
			return;
		}
		transactionInfo.setNeedPrint(TextUtils.isEmpty(thirdRequest
				.getNoPrint()));// 有该参数表示第三方应用调用“收款”应用时支付后不打印小票。
		transactionInfo.setNeedTicket(TextUtils.isEmpty(thirdRequest
				.getNoTicket()));// 有该参数表示第三方应用调用“收款”应用时支付后不需要发券
	}

	@Override
	public void getTransDetial(String tranLogId, ResultListener listener) {

	}

	@Override
	public void trans(ResultListener listener) {
	}

	@Override
	public boolean revokeTrans(ResultListener listener) {
		return false;
	}

	@Override
	public int getTransactionType() {
		return transactionInfo.getTransactionType();
	}

	@Override
	public boolean isMixTransaction() {
		return TransactionInfo2.FLAG_MIX.equals(transactionInfo.getMixFlag());
	}

	@Override
	public void onCreate() {

	}

	@Override
	public void onResume() {

	}

	@Override
	public void onPause() {

	}

	@Override
	public void onStop() {

	}

	@Override
	public void onDestory() {

	}

	@Override
	public TransactionInfo2 getTransactionInfo() {
		return transactionInfo;
	}

	public static String getTransactionDes(int transType) {
		// 交易类型编号
		switch (transType) {
		case TransactionTemsController.TRANSACTION_TYPE_BANK_CARD:
			return "银行卡";
		case TransactionTemsController.TRANSACTION_TYPE_MEMBER_CARD:
			return "会员卡";
		case TransactionTemsController.TRANSACTION_TYPE_CASH:
			return "现金";
		case TransactionTemsController.TRANSACTION_TYPE_OTHER:
			return "其他支付";
		case TransactionTemsController.TRANSACTION_TYPE_WEPAY_MEMBER_CARD:
			return "微信会员卡";
		case TransactionTemsController.TRANSACTION_TYPE_ALIPAY:
			return "支付宝支付";
		case TransactionTemsController.TRANSACTION_TYPE_WEPAY_PAY:
			return "微信支付";
		case TransactionTemsController.TRANSACTION_TYPE_TEN_PAY:
			return "手Q支付";
		case TransactionTemsController.TRANSACTION_TYPE_BAIDU_PAY:
			return "百度支付";
		case TransactionTemsController.TRANSACTION_TYPE_MIXPAY:
			return "组合支付	";
		case TransactionTemsController.TRANSACTION_TYPE_CHANGE_DEL:
			return "零头处理";
		case TransactionTemsController.TRANSACTION_TYPE_WEPAY_TICKET_CANCEL:
			return "微信卡券核销";
		case TransactionTemsController.TRANSACTION_TYPE_THIRD_TICKET_CANCEL:
			return "第三方卡券核销";
		case TransactionTemsController.TRANSACTION_TYPE_MEMBER_TICKET_CANCEL:
			return "会员券核销";
		case TransactionTemsController.TRANSACTION_TYPE_NO_MOMBER_TICKET_CANCEL:
			return "非会员券核销";
		case TransactionTemsController.TRANSACTION_TYPE_NO_MOMBER_TICKET_DISCOUNT:
			return "折扣核销";
		default:
			return "";
		}

	}
}
