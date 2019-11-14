package com.wizarpos.pay.thirdapp;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import com.wizarpos.base.net.ResponseListener;
import com.wizarpos.base.net.Response;
//import com.wizarpos.netpay.server.NetPayProxy;
import com.wizarpos.pay.cashier.thrid_app_controller.model.ThirdAppTransactionRequest;
import com.wizarpos.pay.cashier.thrid_app_controller.model.ThirdAppTransactionResponse;
import com.wizarpos.pay.cashier.view.GatheringActivity;
import com.wizarpos.pay.common.utils.Calculater;
import com.wizarpos.pay.common.utils.Logger2;
import com.wizarpos.pay.db.AppConfigDef;
import com.wizarpos.pay.db.AppConfigHelper;

/**
 * 
 * @Author: Huangweicai
 * @date 2015-12-2 上午9:54:59
 * @Description:网络收单第三方 播放语音
 */
public class PayNetactivityProxy extends GatheringActivity{
	
	private ThirdAppTransactionRequest requestBean;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestBean = thirdAppController.getRequestBean();
		if(!checkMid())
		{
			this.finish();
			return;
		}
		speak();
		
	}
	
	/**
	 * 
	 * @Author: Huangweicai
	 * @date 2016-1-6 上午10:42:34  
	 * @Description:检查mid与本机是否相同 不同则返回
	 */
	private boolean checkMid()
	{
		String mid = AppConfigHelper.getConfig(AppConfigDef.mid, "");
		if(TextUtils.isEmpty(mid))
		{
			Log.w("PayNetactivityProxy", "mid is null.mid为空");
			return false;
		}
		if(requestBean == null)
		{
			return false;
		}
		if(!mid.equals(requestBean.getMid()))
		{
			errorRequest("商户号不同,不能进行交易.当前商户号为:" + mid + ",请求商户号为:" + requestBean.getMid() );
			thirdAppController.reset();
			return false;
		}
		return true;
	}

	private void errorRequest(String errorInfo) {
		Logger2.debug(errorInfo);
		ThirdAppTransactionResponse response = new ThirdAppTransactionResponse();
		response.setCode(1);
		response.setMessage(errorInfo);
//		NetPayProxy.getInstance().paySuccess(this, JSONObject.toJSONString(response));
	}
	
	private void speak()
	{
		boolean isNeedVoice = AppConfigHelper.getConfig(AppConfigDef.isNeedVoice, true);
		if(isNeedVoice)
		{//支持语音
			SpeakMgr.getInstants().initSpeak(this,new ResponseListener() {
				
				@Override
				public void onSuccess(Response response) {
					if(requestBean!=null)
					{
						SpeakMgr.getInstants().speak("收款" + Calculater.formotFen(requestBean.getAmount()) + "元");
					}
				}
				
				@Override
				public void onFaild(Response arg0) {
					
				}
			});
		} else
		{//不支持语音
			SpeakMgr.getInstants().onDestroy();
		}
	}
	
}
