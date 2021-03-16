package com.wizarpos.pay.cashier.activity;

import java.math.BigDecimal;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.view.View;

import com.alibaba.fastjson.JSONObject;
import com.wizarpos.device.printer.PrintServiceController;
//import com.wizarpos.netpay.server.NetPayProxy;
import com.wizarpos.pay.cashier.presenter.TransactionTemsController;
import com.wizarpos.pay.cashier.thrid_app_controller.ThirdAppFinisher;
import com.wizarpos.pay.cashier.thrid_app_controller.ThirdAppTransactionController;
import com.wizarpos.pay.cashier.thrid_app_controller.model.ThirdAppTransactionEXJsonResponse;
import com.wizarpos.pay.cashier.thrid_app_controller.model.ThirdAppTransactionResponse;
import com.wizarpos.pay.cashier.view.TransactionFlowController;
import com.wizarpos.pay.cashier.view.WaitPrintActivity;
import com.wizarpos.pay.common.Constants;
import com.wizarpos.pay.db.AppConfigDef;
import com.wizarpos.pay.db.AppConfigHelper;
import com.wizarpos.pay.db.AppStateDef;
import com.wizarpos.pay.db.AppStateManager;
import com.wizarpos.pay.thirdapp.ThirdAppBroadcastReceiver;
import com.wizarpos.pay.thirdapp.ThirdAppBroadcastReceiver.ThirdAppListener;
import com.wizarpos.pay.view.fragment.ScalQrFragment;
import com.motionpay.pay2.lite.R;

public abstract class TransactionActivity extends TransactionFlowController implements ThirdAppFinisher {

	private ThirdAppBroadcastReceiver receiver; // 交易监听
	protected ThirdAppTransactionController thirdAppController;
	public static final int PRINT_REQUEST_CDOE = 777;

	/**
	 * 注册交易监听广播
	 */
	protected void registerReceiver(ThirdAppListener listener, String action) {
		receiver = new ThirdAppBroadcastReceiver();
		receiver.setListener(listener);
		IntentFilter filter = new IntentFilter(action);
		LocalBroadcastManager.getInstance(this).registerReceiver(receiver, filter);
	}

	/**
	 * 取消注册交易监听广播
	 */
	protected void unregisterReceiver() {
		try {
			if (receiver != null) {
				LocalBroadcastManager.getInstance(this).unregisterReceiver(receiver);
			}
		} catch (Exception e) {}
	}

	@Override
	public void onBackPressed() {// 禁用返回按钮
		back();
	}

	@Override
	protected void onTitleBackClikced() {
		back();
	}

	protected void back() {
		serviceFaild();
		this.finish();
	}

	protected void serviceFaild(String msg) {
		ThirdAppTransactionResponse response = new ThirdAppTransactionResponse();
		response.setCode(1);
		response.setMessage(msg);
		String responseJson = JSONObject.toJSONString(response);
		if(isInService()){//增加逻辑 判断是否打印结束,如没有,调整至等待打印结束页面 wu
			boolean isPrintting = isPrintting();
			if(isPrintting){
				toPrintingView(responseJson);
			}else{
				AppStateManager.setState(AppStateDef.isInService, Constants.FALSE);
				sendResponse(responseJson);
			}
		}else{
			sendResponse(responseJson);
			this.finish();
		}
	}

	protected void toPrintingView() {
		toPrintingView("");
	}

	protected void toPrintingView(String responseJson) {
		Intent waitPrintIntent = new Intent(this, WaitPrintActivity.class);
		waitPrintIntent.putExtra("response", responseJson);
		startActivityForResult((waitPrintIntent), PRINT_REQUEST_CDOE);
	}

	protected boolean isPrintting() {
		return PrintServiceController.isPrintting(this);
	}

	protected void serviceFaild() {
		serviceFaild("用户取消");
	}

	protected void serviceSuccess(Intent intent) {
		String responseJson = bundleResponseJson(intent);
		if(isInService()){//增加逻辑 判断是否打印结束,如没有,调整至等待打印结束页面 wu
			boolean isPrintting = isPrintting();
			if(isPrintting){
				toPrintingView(responseJson);
			}else{
				AppStateManager.setState(AppStateDef.isInService, Constants.FALSE);
				sendResponse(responseJson);
				this.finish();
			}
		}else{
			sendResponse(responseJson);
			this.finish();
		}
	}

	protected boolean isInService() {
		return Constants.TRUE.equals(AppStateManager.getState(AppStateDef.isInService));
	}


	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if(fromPrintingViewSuccess(requestCode, resultCode)){//从打印结束页面返回
			printFinish(data);
		}
	}

	protected boolean fromPrintingViewSuccess(int requestCode, int resultCode) {
		return requestCode == PRINT_REQUEST_CDOE && resultCode == RESULT_OK;
	}

	protected void printFinish(Intent data) {
		AppStateManager.setState(AppStateDef.isInService, Constants.FALSE);
		String response = data.getStringExtra("response");
		sendResponse(response);
		this.finish();
	}

	@NonNull
	private String bundleResponseJson(Intent intent) {
		ThirdAppTransactionResponse response = praseResponseIntent(intent);
		if (response != null) {
			response.setCode(0);
			response.setMessage("success");
		} else {
			response = new ThirdAppTransactionResponse();
			response.setCode(1);
			response.setMessage("交易结果解析失败");
		}
		return JSONObject.toJSONString(response);
	}

	private void sendResponse(String responseJson) {
		Intent intent = new Intent();
		intent.putExtra("response", responseJson);
		intent.setAction(ThirdAppBroadcastReceiver.FILTER_TRANSACITON);
		LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
	}

	protected void bundleThridTransactionResponse(Intent intent) {
		thirdAppController.bundleResponse(intent);
	}

	public ThirdAppTransactionResponse praseResponseIntent(Intent intent) {
		try {
			ThirdAppTransactionEXJsonResponse exJsonBean = new ThirdAppTransactionEXJsonResponse();
			exJsonBean.setExtraAmount(intent.getStringExtra(Constants.discountAmount));
			exJsonBean.setInputAmount(intent.getStringExtra(Constants.initAmount));
			exJsonBean.setPayAmount(intent.getStringExtra(Constants.realAmount));
			exJsonBean.setReduceAmount(intent.getStringExtra(Constants.reduceAmount));
			exJsonBean.setReturnAmount(intent.getStringExtra(Constants.changeAmount));
			exJsonBean.setTradeNo(intent.getStringExtra(Constants.tranId));
			exJsonBean.setCardBalance(intent.getStringExtra(Constants.cardBalance));
			setPayMode(intent.getIntExtra("TRANSACTION_TYPE", 0), exJsonBean);
			exJsonBean.setCardNo(intent.getStringExtra(Constants.cardNo));
			exJsonBean.setTicketReduceAmount(intent.getStringExtra("ticketReduceAmount"));
			String mixFlag = intent.getStringExtra(Constants.mixFlag);
			if (Constants.TRUE.equals(intent.getStringExtra("offline"))) {
				exJsonBean.setOffline(Constants.TRUE);
				exJsonBean.setOfflineTranLogId(intent.getStringExtra("tranLogId"));
			} else if (Constants.isMixTransaction.equals(mixFlag)) {
				exJsonBean.setMasterTranLogId(intent.getStringExtra(Constants.mixTranLogId));
			} else {
				exJsonBean.setMasterTranLogId(intent.getStringExtra("tranLogId"));
			}
			ThirdAppTransactionResponse appTransactionResponse = new ThirdAppTransactionResponse();
			appTransactionResponse.setExJson(exJsonBean);
			return appTransactionResponse;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	private void setPayMode(int transactionType, ThirdAppTransactionEXJsonResponse exJsonBean) {
		exJsonBean.setPayMode(transactionType + "");
		switch (transactionType) {
		case TransactionTemsController.TRANSACTION_TYPE_BANK_CARD:
			exJsonBean.setPayModeDesc("银行卡");
			break;
		case TransactionTemsController.TRANSACTION_TYPE_MEMBER_CARD:
			exJsonBean.setPayModeDesc("会员卡");
			break;
		case TransactionTemsController.TRANSACTION_TYPE_CASH:
			exJsonBean.setPayModeDesc("现金");
			break;
		case TransactionTemsController.TRANSACTION_TYPE_OTHER:
			exJsonBean.setPayModeDesc("其他支付");
			break;
		case TransactionTemsController.TRANSACTION_TYPE_WEPAY_MEMBER_CARD:
			exJsonBean.setPayModeDesc("微信会员卡");
			break;
		case TransactionTemsController.TRANSACTION_TYPE_ALIPAY:
			exJsonBean.setPayModeDesc("支付宝");
			break;
		case TransactionTemsController.TRANSACTION_TYPE_WEPAY_PAY:
			exJsonBean.setPayModeDesc("微信");
			break;
		case TransactionTemsController.TRANSACTION_TYPE_TEN_PAY:
			exJsonBean.setPayModeDesc("QQ钱包");
			break;
		case TransactionTemsController.TRANSACTION_TYPE_BAIDU_PAY:
			exJsonBean.setPayModeDesc("百度支付");
			break;
		case TransactionTemsController.TRANSACTION_TYPE_MIXPAY:
			exJsonBean.setPayModeDesc("组合支付");//@hong 添加组合支付 20151214
			break;
		case TransactionTemsController.TRANSACTION_TYPE_UNION_PAY:
			exJsonBean.setPayModeDesc("移动支付");//@hong 添加移动支付20151229
			break;
		default:
			break;
		}

	}

	/**
	 * 
	 * @Author: Huangweicai
	 * @date 2015-9-1 上午10:57:37 
	 * @Description: 伸缩二维码
	 */
	public void scalImg(String ivUrl) {
		if(TextUtils.isEmpty(ivUrl)){
			return;
		}
		findViewById(R.id.toolbar).setVisibility(View.GONE);
		Fragment qrFragement = new ScalQrFragment();
		Bundle bundle = new Bundle();
		bundle.putString("url", ivUrl);
		qrFragement.setArguments(bundle);
		getSupportFragmentManager().beginTransaction().addToBackStack(null).replace(R.id.rlMain, qrFragement).commit();
	}	
	
	@Override
	public void finishTransacton(int resultCode, Intent intent) {
		if(thirdAppController.getRequestBean() == null)
		{
			String response = intent.getStringExtra("response");
			System.out.println("response is :" + response);
			if(AppConfigHelper.getConfig(AppConfigDef.isUseNetPay, false)) {
//				NetPayProxy.getInstance().paySuccess(this, response);
			}
			this.finish();
			return;
		}
		if(thirdAppController.getRequestBean().getTransType().equals(ThirdAppTransactionController.TRANS_TYPE_NET_TRANSACT))
		{//若是网络收单支付
			String response = intent.getStringExtra("response");
			System.out.println("response is :" + response);
			if(AppConfigHelper.getConfig(AppConfigDef.isUseNetPay, false)) {
//				NetPayProxy.getInstance().paySuccess(this, response);
			}
			this.finish();
			return;
		}
		setResult(resultCode, intent);
		this.finish();
	}
	
	/**
	 * 求差
	 */
	public static double sub(double v1, double v2) {
		BigDecimal b1 = new BigDecimal(Double.toString(v1));
		BigDecimal b2 = new BigDecimal(Double.toString(v2));
		return (b1.subtract(b2)).doubleValue();
	}
	
	public static double add(double v1, double v2) {
		BigDecimal b1 = new BigDecimal(Double.toString(v1));
		BigDecimal b2 = new BigDecimal(Double.toString(v2));
		return (b1.add(b2)).doubleValue();
	}
}
