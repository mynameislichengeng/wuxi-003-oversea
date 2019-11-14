package com.wizarpos.pay.manage.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.wizarpos.hspos.api.EnumCommand;
import com.wizarpos.hspos.api.EnumProgressCode;
import com.wizarpos.hspos.api.HuashiApi;
import com.wizarpos.hspos.api.ParamInfo;
import com.wizarpos.hspos.api.SettleInfo;
import com.wizarpos.hspos.api.TradeListener;
import com.wizarpos.hspos.api.TransInfo;
import com.wizarpos.pay.app.PaymentApplication;
import com.wizarpos.pay.common.base.BaseViewActivity;

public abstract class CardLinkBaseActivity extends BaseViewActivity {

	private static final String LOG_TAG = CardLinkBaseActivity.class.getSimpleName();

	protected final int PROGRESS_NOTIFIER = 1;
	protected final int SUCCESS_NOTIFIER  = 2;
	protected final int FAIL_NOTIFIER     = 3;

	protected HuashiApi posApi;
	protected TransInfo  transInfo;
	protected ParamInfo  paramInfo;
	protected SettleInfo settleInfo;

	protected int errorCode;
	protected String errorMessage;

	protected Handler handler;

	protected EnumCommand curCommand;
	protected int         curProcessCode;
	protected String      curProcessMessage;
    
    @Override  
    protected void onCreate(Bundle savedInstanceState) {  
        super.onCreate(savedInstanceState);

		posApi = PaymentApplication.getInstance().getPosApi();
		posApi.setTradeListener(tradeListener);
		
		handler = new Handler() 
		{ 
			public void handleMessage(Message msg)
			{
				/*这里是处理信息的方法*/
				switch (msg.what)
				{
					case PROGRESS_NOTIFIER:
						Log.d(LOG_TAG , "onProgress[" + curCommand.getCmdCode() + "][" + curProcessCode + "][" + curProcessMessage + "]");
						onProgress();
						switch(curCommand)
						{
							case Balance:
								posApi.balance(transInfo);
								break;
							case Sale:
								posApi.sale(transInfo);
								break;
							case VoidSale:
								posApi.voidSale(transInfo);
								break;
							case Refund:
								posApi.refund(transInfo);
								break;
							case Login:
								posApi.login();
								break;
							case Settle:
								posApi.settle();
								break;
							case InitKey:
								posApi.initKey(transInfo);
								break;
							case QueryAnyTrans:
								posApi.queryAnyTrans(transInfo);
							case DownloadAID:
								posApi.downloadAID();
								break;
							case DownloadCAPK:
								posApi.downloadCAPK();
								break;
						}
						break;
					case FAIL_NOTIFIER:
						Log.d(LOG_TAG, "错误[" + errorCode + "]["+ errorMessage + "]");
						onError();
						break;
					case SUCCESS_NOTIFIER:
						switch(curCommand){
							// 管理类
							// 1.获取参数
							case GetParam:
								Log.d(LOG_TAG, "获取参数 完成");
								break;
							// 2.设置参数
							case SetParam:
								Log.d(LOG_TAG, "设置参数 完成");
								break;
							// 3.下载主密钥
							case InitKey:
								Log.d(LOG_TAG, "下载主密钥 完成");
								break;
							// 4.签到
							case Login:
								Log.d(LOG_TAG, "签到 完成, respCode[" + transInfo.getRespCode() + "]respDesc[" + transInfo.getRespDesc() + "]");
								break;
							// 5.结算
							case Settle:
								Log.d(LOG_TAG, "结算 完成, respCode[" + transInfo.getRespCode() + "]respDesc[" + transInfo.getRespDesc() + "]");
								break;
							// 金融交易类
							// 1.查询余额
							case Balance:
								Log.d(LOG_TAG, "查询余额 完成, respCode[" + transInfo.getRespCode() + "]respDesc[" + transInfo.getRespDesc() + "]");
								break;
							// 2.消费
							case Sale:
								Log.d(LOG_TAG, "消费 完成, respCode[" + transInfo.getRespCode() + "]respDesc[" + transInfo.getRespDesc() + "]");
								break;
							// 3.消费撤销
							case VoidSale:
								Log.d(LOG_TAG, "消费撤销 完成 , respCode[" + transInfo.getRespCode() + "]respDesc[" + transInfo.getRespDesc() + "]");
								break;
							// 4.退货
							case Refund:
								Log.d(LOG_TAG, "退货 完成, respCode[" + transInfo.getRespCode() + "]respDesc[" + transInfo.getRespDesc() + "]");
								break;
							case DownloadAID:
								Log.d(LOG_TAG, "下载AID参数 完成, respCode[" + transInfo.getRespCode() + "]respDesc[" + transInfo.getRespDesc() + "]");
								break;
							case DownloadCAPK:
								Log.d(LOG_TAG, "下载公钥参数 完成, respCode[" + transInfo.getRespCode() + "]respDesc[" + transInfo.getRespDesc() + "]");
								break;
							default:
								break;
						}
						onSuccess();
						break;
				}
			}
		};
    }

	private void doOnProgressAction() {
		if(curProcessCode == EnumProgressCode.InputTransAmount.getCode())
		{
			transInfo.setTransAmount(1);
		}
		else if(curProcessCode == EnumProgressCode.InputAuthCode.getCode())
		{
			transInfo.setAuthCode("123456");
		}
		else if(curProcessCode == EnumProgressCode.InputOldRRN.getCode())
		{
			transInfo.setOldRRN("123456789012");
		}
		else if(curProcessCode == EnumProgressCode.InputOldTicket.getCode())
		{
			transInfo.setOldTrace(123);
		}
		else if(curProcessCode == EnumProgressCode.InputOldTransDate.getCode())
		{
			transInfo.setOldTransDate("0306"); // MMDD
		}
		else if(curProcessCode == EnumProgressCode.ShowTransTotal.getCode())
		{
			Log.d(LOG_TAG, "交易累计：内卡借记 " + settleInfo.getCupDebitCount() + "/" + settleInfo.getCupDebitAmount()
					+ "\n外卡借记 " + settleInfo.getAbrDebitCount() + "/" + settleInfo.getAbrDebitAmount());
		}
	}

	protected abstract void onProgress();

	protected abstract void onSuccess();

	protected abstract void onError();
    
	private TradeListener tradeListener =new TradeListener(){
		@Override
		public void onProgress(EnumCommand cmd, int progressCode,String message, Object param) {
	    	if(cmd == EnumCommand.Settle)
	    	{
	    		settleInfo = (SettleInfo)param;
	    	}
	    	else{
	    		transInfo = (TransInfo)param;
	    	}
			
			Message msg = new Message();
	    	msg.what = PROGRESS_NOTIFIER;
	    	curCommand = cmd;
	    	curProcessCode = progressCode;
	    	curProcessMessage = message;
	    	handler.sendMessage(msg);
		}

		@Override
		public void onTransSucceed(EnumCommand cmd, Object params) {
		 	curCommand = cmd;
			if(cmd == EnumCommand.GetParam)
			{
				paramInfo = (ParamInfo) params;
			}
			else if(cmd == EnumCommand.SetParam)
			{
				
			}
			else{
				transInfo = (TransInfo) params;
			}
	    	Message msg = new Message();
	    	msg.what = SUCCESS_NOTIFIER;
	    	handler.sendMessage(msg);
		}

		@Override
		public void onTransFailed(EnumCommand cmd, final int error, final String message) {
			curCommand = cmd;
			errorCode = error;
			errorMessage = message;
	    	Message msg = new Message();
	    	msg.what = FAIL_NOTIFIER;
	    	handler.sendMessage(msg);
		}					
		
	};
}
