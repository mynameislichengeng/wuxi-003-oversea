package com.wizarpos.pay.cashier.view;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.text.TextUtils;

import com.wizarpos.pay.cashier.activity.AlipayMicroActivity;
import com.wizarpos.pay.cashier.activity.AlipayNativeActivity;
import com.wizarpos.pay.cashier.activity.BaiduMicroPayActivity;
import com.wizarpos.pay.cashier.activity.BaiduNativePayActivity;
import com.wizarpos.pay.cashier.activity.BankCardPayActivity;
import com.wizarpos.pay.cashier.activity.MixPayActivity;
import com.wizarpos.pay.cashier.activity.QwalletMicroActivity;
import com.wizarpos.pay.cashier.activity.QwalletNativeActivity;
import com.wizarpos.pay.cashier.activity.ThirdTicketCancleActivity;
import com.wizarpos.pay.cashier.activity.UnionPayMicroActivity;
import com.wizarpos.pay.cashier.activity.WepayMicroActivity;
import com.wizarpos.pay.cashier.activity.WepayNativeActivity;
import com.wizarpos.pay.cashier.activity.WepayTicketCancleMixActivity;
import com.wizarpos.pay.cashier.presenter.ITransactionFlowController;
import com.wizarpos.pay.cashier.view.input.InputInfoActivity;
import com.wizarpos.pay.cashier.view.input.TicketScanUseActivity;
import com.wizarpos.pay.common.Constants;
import com.wizarpos.pay.common.base.BaseViewActivity;
import com.wizarpos.pay.common.utils.UIHelper;
import com.wizarpos.pay.db.AppConfigDef;
import com.wizarpos.pay.db.AppConfigHelper;
import com.wizarpos.pay.ui.newui.NewMainActivity;
import com.wizarpos.pay.ui.newui.NewPaySuccessActivity;
import com.motionpay.pay2.lite.R;

public class TransactionFlowController extends BaseViewActivity implements ITransactionFlowController {
	/** 在跳转到会员应用的时候保存intent*/
	protected Intent toMemberIntent;
	

	@Override
	public void toCashTransactionView(Activity activity, Intent intent) {
		intent.setClass(activity, CashPayActivity.class);
		startAcitvityAndResetTimer(activity, intent);
	}

	@Override
	public void toCardTransactionView(Activity activity, Intent intent) {
		intent.setClass(activity, BankCardPayActivity.class);
		startAcitvityAndResetTimer(activity, intent);
	}

	// @Override
	// public void toMemberTransactionVew(Activity activity, Intent intent) {
	// intent.setClass(activity, InputInfoActivity.class);
	// intent.putExtra(InputInfoActivity.TITLE, "会员卡");
	// startAcitvityAndResetTimer(activity, intent);
	// }

	@Override
	public void toAlipayMicroTransaction(Activity activity, Intent intent) {
		if (Constants.BAT_FLAG) {
			// bat替换@hong
			intent.setClass(this, AlipayMicroActivity.class);
			startAcitvityAndResetTimer(activity, intent);
		} else {
			String appId = AppConfigHelper.getConfig(AppConfigDef.alipay_pattern), key = AppConfigHelper.getConfig(AppConfigDef.alipay_key);
			if ((!Constants.TRUE.equals(AppConfigHelper.getConfig(AppConfigDef.use_alipay))) || (TextUtils.isEmpty(appId) || TextUtils.isEmpty(key))) {
				UIHelper.ToastMessage(activity, getResources().getString(R.string.alipay_toast));
			} else {
				intent.setClass(this, AlipayMicroActivity.class);
				startAcitvityAndResetTimer(activity, intent);
			}
		}
	}

	@Override
	public void toAlipayNativeTransaction(Activity activity, Intent intent) {
		// bat替换@hong
		if (Constants.BAT_FLAG) {
			intent.setClass(this, AlipayNativeActivity.class);
			startAcitvityAndResetTimer(activity, intent);
		} else {
			String appId = AppConfigHelper.getConfig(AppConfigDef.alipay_pattern), key = AppConfigHelper.getConfig(AppConfigDef.alipay_key);
			if ((!Constants.TRUE.equals(AppConfigHelper.getConfig(AppConfigDef.use_alipay))) || (TextUtils.isEmpty(appId) || TextUtils.isEmpty(key))) {
				UIHelper.ToastMessage(activity, getResources().getString(R.string.alipay_toast));
			} else {
				intent.setClass(this, AlipayNativeActivity.class);
				startAcitvityAndResetTimer(activity, intent);
			}

		}
	}

	@Override
	public void toWepayMicroTransaction(Activity activity, Intent intent) {
		if (Constants.BAT_FLAG) {
			// bat替换@hong
			intent.setClass(this, WepayMicroActivity.class);
			startAcitvityAndResetTimer(activity, intent);
		} else {
			String appId = AppConfigHelper.getConfig(AppConfigDef.weixin_app_id), appKey = AppConfigHelper.getConfig(AppConfigDef.weixin_app_key),
					mchid = AppConfigHelper.getConfig(AppConfigDef.weixin_mchid_id);
			if ((!Constants.TRUE.equals(AppConfigHelper.getConfig(AppConfigDef.use_weixin_pay)))
					|| (TextUtils.isEmpty(appId) || TextUtils.isEmpty(appKey) || TextUtils.isEmpty(mchid))) {
				UIHelper.ToastMessage(activity, getResources().getString(R.string.wechatpay_toast));
			} else {
				if (AppConfigHelper.getConfig(AppConfigDef.xinpay_agent_pay).equals("true")) {
					intent.setClass(this, WepayMicorAgentActivity.class);
				} else {
					intent.setClass(this, WepayMicroActivity.class);
				}
				startAcitvityAndResetTimer(activity, intent);
			}

		}
	}
	
	@Override
	public void toUnionMicroTransaction(Activity activity, Intent intent) {
		if (Constants.BAT_FLAG) {
			//BAT模式才有移动支付支付
			intent.setClass(this, UnionPayMicroActivity.class);
			startAcitvityAndResetTimer(activity, intent);
		}
	}

	@Override
	public void toWepayNativeTransaction(Activity activity, Intent intent) {
		if (Constants.BAT_FLAG) {
			// bat替换@hong
			intent.setClass(this, WepayNativeActivity.class);
			startAcitvityAndResetTimer(activity, intent);
		} else {
			String appId = AppConfigHelper.getConfig(AppConfigDef.weixin_app_id), appKey = AppConfigHelper.getConfig(AppConfigDef.weixin_app_key),
					mchid = AppConfigHelper.getConfig(AppConfigDef.weixin_mchid_id);
			if ((!Constants.TRUE.equals(AppConfigHelper.getConfig(AppConfigDef.use_weixin_pay)))
					|| (TextUtils.isEmpty(appId) || TextUtils.isEmpty(appKey) || TextUtils.isEmpty(mchid))) {
				UIHelper.ToastMessage(activity, getResources().getString(R.string.wechatpay_toast));
			} else {
				if (AppConfigHelper.getConfig(AppConfigDef.xinpay_agent_pay).equals("true")) {
					intent.setClass(this, WepayNativeAgentActivity.class);
				} else {
					intent.setClass(this, WepayNativeActivity.class);
				}
				startAcitvityAndResetTimer(activity, intent);
			}
		}
	}

	public void toTransactionSuccess(Activity activity, Intent intent) {
//		intent.setClass(activity, PaySuccessActivity.class);
		intent.setClass(activity, NewPaySuccessActivity.class);
		startAcitvityAndResetTimer(activity, intent);
	}

	public void toTransactionFaild() {

	}

	@Override
	public void toTicketPublishActivity(Activity activity, Intent intent) {
		intent.setClass(activity, TicketPublishActivity.class);
		startAcitvityAndResetTimer(activity, intent);
	}

	private void startAcitvityAndResetTimer(Activity activity, Intent intent) {
		// TODO 加入计时器
		activity.startActivity(intent);
	}

	private void startAcitvityForResultAndResetTimer(Activity activity, Intent intent, int requestCode) {
		// TODO 加入计时器
		activity.startActivityForResult(intent, requestCode);
	}

	@Override
	public void toTenpayNativeTransaction(Activity activity, Intent intent) {
		if (Constants.BAT_FLAG) {
			// bat替换@hong
			intent.setClass(this, QwalletNativeActivity.class);
			startAcitvityAndResetTimer(activity, intent);
		} else {
			String appId = AppConfigHelper.getConfig(AppConfigDef.tenpay_bargainor_id), appKey = AppConfigHelper.getConfig(AppConfigDef.tenpay_key),
					optId = AppConfigHelper.getConfig(AppConfigDef.tenpay_op_user_id),
					optPasswd = AppConfigHelper.getConfig(AppConfigDef.tenpay_op_user_passwd);
			if ((!Constants.TRUE.equals(AppConfigHelper.getConfig(AppConfigDef.use_tenpay)))
					|| (TextUtils.isEmpty(appId) || TextUtils.isEmpty(appKey) || TextUtils.isEmpty(optId) || TextUtils.isEmpty(optPasswd))) {
				UIHelper.ToastMessage(activity, getResources().getString(R.string.config_tenpay_toast));
			} else {
				intent.setClass(this, QwalletNativeActivity.class);
				startAcitvityAndResetTimer(activity, intent);
			}

		}
	}

	@Override
	public void toTenpayMicroTransaction(Activity activity, Intent intent) {
		if (Constants.BAT_FLAG) {
			// bat替换@hong
			intent.setClass(this, QwalletMicroActivity.class);
			startAcitvityAndResetTimer(activity, intent);
		} else {
			String appId = AppConfigHelper.getConfig(AppConfigDef.tenpay_bargainor_id), appKey = AppConfigHelper.getConfig(AppConfigDef.tenpay_key),
					optId = AppConfigHelper.getConfig(AppConfigDef.tenpay_op_user_id),
					optPasswd = AppConfigHelper.getConfig(AppConfigDef.tenpay_op_user_passwd);
			if ((!Constants.TRUE.equals(AppConfigHelper.getConfig(AppConfigDef.use_tenpay)))
					|| (TextUtils.isEmpty(appId) || TextUtils.isEmpty(appKey) || TextUtils.isEmpty(optId) || TextUtils.isEmpty(optPasswd))) {
				UIHelper.ToastMessage(activity, getResources().getString(R.string.config_tenpay_toast));
			} else {
				intent.setClass(this, QwalletMicroActivity.class);
				startAcitvityAndResetTimer(activity, intent);
			}
		}
	}

	@Override
	public void toBaiduMicroTransaction(Activity activity, Intent intent) {
		if (Constants.BAT_FLAG) {
			// bat替换hong
			intent.setClass(this, BaiduMicroPayActivity.class);
			startAcitvityAndResetTimer(activity, intent);
		}else {
			 String appId = AppConfigHelper.getConfig(AppConfigDef.baidupay_id), appKey = AppConfigHelper.getConfig(AppConfigDef.baidupay_key);
			 if ((!Constants.TRUE.equals(AppConfigHelper.getConfig(AppConfigDef.use_baidupay))) || (TextUtils.isEmpty(appId) || TextUtils.isEmpty(appKey))) {
			 UIHelper.ToastMessage(activity, getResources().getString(R.string.config_baidupay_toast));
			 } else {
			 intent.setClass(this, BaiduMicroPayActivity.class);
			 startAcitvityAndResetTimer(activity, intent);
			 }
		}
	}

	@Override
	public void toBaiduNativeTransaction(Activity activity, Intent intent) {
		if (Constants.BAT_FLAG) {
			// bat替换@hong
			intent.setClass(this, BaiduNativePayActivity.class);
			startAcitvityAndResetTimer(activity, intent);
		}else {
			 String appId = AppConfigHelper.getConfig(AppConfigDef.baidupay_id), appKey = AppConfigHelper.getConfig(AppConfigDef.baidupay_key);
			 if ((!Constants.TRUE.equals(AppConfigHelper.getConfig(AppConfigDef.use_baidupay))) || (TextUtils.isEmpty(appId) || TextUtils.isEmpty(appKey))) {
			 UIHelper.ToastMessage(activity, getResources().getString(R.string.config_baidupay_toast));
			 } else {
			 intent.setClass(this, BaiduNativePayActivity.class);
			 startAcitvityAndResetTimer(activity, intent);
			 }
		}
	}

	@Override
	public void toOtherTransactionView(Activity activity, Intent intent) {
		intent.setClass(activity, OtherPayActivity.class);
		startAcitvityAndResetTimer(activity, intent);
	}

	@Override
	public void toMixTransaction(Activity activity, Intent intent) {
		intent.setClass(activity, MixPayActivity.class);
		startAcitvityAndResetTimer(activity, intent);
	}

	@Override
	public void toCashMixTransactionView(Activity activity, Intent intent, int requestCode) {
		intent.setClass(activity, CashPayActivity.class);
		startAcitvityForResultAndResetTimer(activity, intent, requestCode);
	}

	@Override
	public void toOtherMixTransactionView(Activity activity, Intent intent, int requestCode) {
		intent.setClass(activity, OtherPayActivity.class);
		startAcitvityForResultAndResetTimer(activity, intent, requestCode);
	}

	@Override
	public void toCardMixTransactionView(Activity activity, Intent intent, int requestCode) {
		intent.setClass(activity, BankCardPayActivity.class);
		startAcitvityForResultAndResetTimer(activity, intent, requestCode);
	}

	@Override
	public void toMemberMixTransactionVew(Activity activity, Intent intent, int requestCode) {
		toInputView(activity, "组合支付|会员", intent, requestCode); // 调用摄像头逻辑调整 wu@[20150827]
	}

	@Override
	public void toAlipayMixMicroTransaction(Activity activity, Intent intent, int requestCode) {
		intent.setClass(activity, AlipayMicroActivity.class);
		startAcitvityForResultAndResetTimer(activity, intent, requestCode);
	}

	@Override
	public void toAlipayMixNativeTransaction(Activity activity, Intent intent, int requestCode) {
		intent.setClass(activity, AlipayNativeActivity.class);
		startAcitvityForResultAndResetTimer(activity, intent, requestCode);
	}

	@Override
	public void toWepayMixMicroTransaction(Activity activity, Intent intent, int requestCode) {
		intent.setClass(activity, WepayMicroActivity.class);
		startAcitvityForResultAndResetTimer(activity, intent, requestCode);
	}

	@Override
	public void toWepayMixNativeTransaction(Activity activity, Intent intent, int requestCode) {
		intent.setClass(activity, WepayNativeActivity.class);
		startAcitvityForResultAndResetTimer(activity, intent, requestCode);
	}

	@Override
	public void toTenpayMixMicroTransaction(Activity activity, Intent intent, int requestCode) {
		intent.setClass(activity, QwalletMicroActivity.class);
		startAcitvityForResultAndResetTimer(activity, intent, requestCode);
	}

	@Override
	public void toTenpayMixNativeTransaction(Activity activity, Intent intent, int requestCode) {
		intent.setClass(activity, QwalletNativeActivity.class);
		startAcitvityForResultAndResetTimer(activity, intent, requestCode);
	}

	@Override
	public void toBaiduMixMicroTransaction(Activity activity, Intent intent, int requestCode) {
		intent.setClass(activity, BaiduMicroPayActivity.class);
		startAcitvityForResultAndResetTimer(activity, intent, requestCode);
	}

	@Override
	public void toBaiduMixNativeTransaction(Activity activity, Intent intent, int requestCode) {
		intent.setClass(activity, BaiduNativePayActivity.class);
		startAcitvityForResultAndResetTimer(activity, intent, requestCode);
	}

	@Override
	public void toWepayTicketCancelMix(Activity activity, Intent intent, int requestCode) {
		intent.setClass(activity, WepayTicketCancleMixActivity.class);
		startAcitvityForResultAndResetTimer(activity, intent, requestCode);
	}

	@Override
	public void toThridTicketCancelMix(Activity activity, Intent intent, int requestCode) {
		intent.setClass(activity, ThirdTicketCancleActivity.class);
		startAcitvityForResultAndResetTimer(activity, intent, requestCode);
	}

	@Override
	public void toMixMemberTicketPass(Activity activity, Intent intent, int requestCode) {
		toInputView(activity, "组合支付|会员券", intent, requestCode); // 调用摄像头逻辑调整 wu@[20150827]
//		toInputTicketView(activity, "组合支付|会员券", intent, requestCode); // 用券界面修改@hong[20150924]
	}

	@Override
	public void toMixNormalTicketPass(Activity activity, Intent intent, int requestCode) {
		// intent.setClass(activity,ScanTicketUseActivity.class);
		// startAcitvityForResultAndResetTimer(activity, intent, requestCode);
		toInputView(activity, "组合支付|券扫描", false, intent, requestCode);
//		toInputTicketView(activity, "组合支付|券扫描", false, intent, requestCode);//用券界面修改@hong[20150924]
	}

	@Override
	public void toInputView(Activity activity, String title, Intent intent, int requestCode) {
		toInputView(activity, title, true, intent, requestCode);
	}
	
	@Override
	public void toMemberInputView(Activity activity, String title,
			Intent intent, int requestCode) {
		toMemberIntent = intent;
		ComponentName componetName = new ComponentName("com.wizarpos.wizarposmemberui",
                "com.wizarpos.wizarposmemberui.view.activity.ScanFragmentActivity");
        Intent intentThird = new Intent();
        intentThird.setComponent(componetName);
        startActivityForResult(intentThird, requestCode);
	}

	@Override
	public void toInputView(Activity activity, String title, boolean isUseSwipe, Intent intent, int requestCode) {
		intent.putExtra(InputInfoActivity.TITLE, title);
		intent.putExtra(InputInfoActivity.IS_USE_SWIPE, isUseSwipe);
		intent.setClass(activity, InputInfoActivity.class);
		startActivityForResult(intent, requestCode);
	}

	@Override
	public void toInputView(Activity activity, String title, boolean isUseSwipe, boolean isUseText, boolean isUseCamera, Intent intent, int requestCode) {
		intent.putExtra(InputInfoActivity.TITLE, title);
		intent.putExtra(InputInfoActivity.IS_USE_SWIPE, isUseSwipe);
		intent.putExtra(InputInfoActivity.IS_USE_QR_CODE, isUseCamera);
		intent.putExtra(InputInfoActivity.IS_USE_TEXT, isUseText);
		intent.setClass(activity, InputInfoActivity.class);
		startActivityForResult(intent, requestCode);
	}

	@Override
	public void toInputTicketView(Activity activity, String title, boolean isUseSwipe, Intent intent, int requestCode) {
		intent.putExtra(InputInfoActivity.TITLE, title);
		intent.putExtra(InputInfoActivity.IS_USE_SWIPE, isUseSwipe);
		intent.setClass(activity, TicketScanUseActivity.class);
		startActivityForResult(intent, requestCode);
		
	}

	@Override
	public void toInputTicketView(Activity activity, String title, boolean isUseSwipe, boolean isUseText, boolean isUseCamera, Intent intent, int requestCode) {
		intent.putExtra(InputInfoActivity.TITLE, title);
		intent.putExtra(InputInfoActivity.IS_USE_SWIPE, isUseSwipe);
		intent.putExtra(InputInfoActivity.IS_USE_QR_CODE, isUseCamera);
		intent.putExtra(InputInfoActivity.IS_USE_TEXT, isUseText);
		intent.setClass(activity, TicketScanUseActivity.class);
		startActivityForResult(intent, requestCode);
		
	}

	@Override
	public void toInputTicketView(Activity activity, String title, Intent intent, int requestCode) {
		toInputTicketView(activity, title, true, intent, requestCode); 
		
	}

}
