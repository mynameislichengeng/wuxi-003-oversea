package com.wizarpos.pay.cashier.fragment;

import java.util.ArrayList;
import java.util.List;

import com.wizarpos.base.net.Response;
import com.wizarpos.log.util.LogEx;
import com.wizarpos.pay.cashier.activity.ThirdTicketCancleActivity;
import com.wizarpos.pay.cashier.activity.WepayTicketCancleActivity;
import com.wizarpos.pay.cashier.activity.WizarTicketCancleActivity;
import com.wizarpos.pay.cashier.view.GatheringActivity;
import com.wizarpos.pay.common.Constants;
import com.wizarpos.pay.common.base.BasePresenter.ResultListener;
import com.wizarpos.pay.common.utils.UIHelper;
import com.wizarpos.pay.db.AppConfigHelper;
import com.wizarpos.pay.db.AppStateDef;
import com.wizarpos.pay.db.AppStateManager;
import com.wizarpos.pay.setting.presenter.AppConfiger;
import com.wizarpos.pay.view.fragment.common.BaseViewFragment;
import com.wizarpos.pay.view.util.Constents;
import com.wizarpos.pay.view.util.DialogHelper2;
import com.wizarpos.pay.view.util.MultieChooseDialogFragment;
import com.wizarpos.pay.view.util.MultieChooseItem;
import com.motionpay.pay2.lite.R;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;

public abstract class HomeFragment extends BaseViewFragment {

	protected String isOnline = "1";

	private AppConfiger appManager;

	private MyBroadcastReceiver myBroadcastReceiver;
	private PaySuccessBroadcastReceiver paySuccessBroadcastReceiver;
	private keepSettingBroadcastReceiver keepSettingBroadcastReceiver;
	public HomeFragment() {
	}

	@Override
	public void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		appManager = new AppConfiger(getActivity());
		myBroadcastReceiver = new MyBroadcastReceiver();// 网络监听广播
		paySuccessBroadcastReceiver = new PaySuccessBroadcastReceiver();// 支付成功
		keepSettingBroadcastReceiver = new keepSettingBroadcastReceiver();//设置成功
		netBroadcastRegist();
		paySuccessBroadcastRegist();
		keepSettingBroadcastReceiver();
	}

	private void keepSettingBroadcastReceiver() {
		IntentFilter filter = new IntentFilter();
		filter.addAction(Constents.KEEPSETTING);
		LocalBroadcastManager.getInstance(getActivity()).registerReceiver(keepSettingBroadcastReceiver, filter);
	}

	private void netBroadcastRegist() {
		IntentFilter filter = new IntentFilter();
		filter.addAction(Constents.PAYACTION);
		LocalBroadcastManager.getInstance(getActivity()).registerReceiver(myBroadcastReceiver, filter);
	}

	private void paySuccessBroadcastRegist() {
		IntentFilter filter = new IntentFilter();
		filter.addAction(Constents.PAYSUCCESSACTION);
		LocalBroadcastManager.getInstance(getActivity()).registerReceiver(paySuccessBroadcastReceiver, filter);
	}

	public class PaySuccessBroadcastReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			LogEx.d("PaySuccessBroadcastReceiver", "数据更新");
			String str = intent.getStringExtra("value");
			if (!TextUtils.isEmpty(str) && str.equals("success")) {
				LogEx.d("PaymentFragment", "paysuccessbroadcastReceiver");

				onPaySuccess();
			}

		}

	}

	public class keepSettingBroadcastReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			LogEx.d("SettingSuccess", "设置成功");
				onSettingSuccess();
		}

	}

	protected abstract void onPaySuccess();
	protected abstract void onSettingSuccess();
	/**
	 * 跳转到收银界面
	 */
	protected void toCashierView() {
		// if(
		// !isSupportPayItem(AppConfigHelper.getConfig(AppConfigDef.isSupportCash))
		// &&
		// !isSupportPayItem(AppConfigHelper.getConfig(AppConfigDef.isSupportBankCard))
		// &&
		// !isSupportPayItem(AppConfigHelper.getConfig(AppConfigDef.isSupportMemberCard))
		// &&
		// !isSupportPayItem(AppConfigHelper.getConfig(AppConfigDef.isSupportAlipay))
		// &&
		// !isSupportPayItem(AppConfigHelper.getConfig(AppConfigDef.isSupportWepay))
		// &&
		// !isSupportPayItem(AppConfigHelper.getConfig(AppConfigDef.isSupportTenpay))
		// &&
		// !isSupportPayItem(AppConfigHelper.getConfig(AppConfigDef.isSupportBaiduPay))
		// &&
		// !isSupportPayItem(AppConfigHelper.getConfig(AppConfigDef.isSupportMixPay))
		// &&
		// !isSupportPayItem(AppConfigHelper.getConfig(AppConfigDef.isSupportOhterPay))
		// ){
		// UIHelper.ToastMessage(getActivity(), "当前未开启任何支付方式,请到支付设置中开启");
		// }

		// 添加网络判断
		if (!Constants.FALSE.equals(AppStateManager.getState(AppStateDef.isOffline, Constants.FALSE))) {// 如果当前为在线模式,则提示是否切换到离线模式
			DialogHelper2.showChoiseDialog(getActivity(), "当前为离线模式(离线模式只能进行现金消费)?",
					new DialogHelper2.DialogChoiseListener() {
						@Override
						public void onNo() {

						}

						@Override
						public void onOK() {
							Intent intent = new Intent(getActivity(), GatheringActivity.class);
							intent.putExtra("isOnline", isOnline);
							getActivity().startActivity(intent);
						}
					});
		} else {
			Intent intent = new Intent(getActivity(), GatheringActivity.class);
			intent.putExtra("isOnline", isOnline);
			getActivity().startActivity(intent);
		}
	}

	private boolean isSupportPayItem(String payItem) {
		return Constants.TRUE.equals(AppConfigHelper.getConfig(payItem));
	}

	/**
	 * 卡券核销
	 */
	protected void showTicketCancelDialog() {
		List<MultieChooseItem> items = new ArrayList<MultieChooseItem>();
		MultieChooseItem itemThirdTicket = new MultieChooseItem();
		itemThirdTicket.setImgId(R.drawable.btn_selector_member_card_pay);
		itemThirdTicket.setTitle(getResources().getString(R.string.third_ticket_cancle));
		items.add(itemThirdTicket);
		MultieChooseItem itemWepayTicket = new MultieChooseItem();
		itemWepayTicket.setImgId(R.drawable.btn_selector_wepay_card_pay);
		itemWepayTicket.setTitle(getResources().getString(R.string.wepay_ticket_cancle));
		items.add(itemWepayTicket);
		MultieChooseItem wizarpayTicket = new MultieChooseItem();
		wizarpayTicket.setImgId(R.drawable.btn_selector_wepay_card_pay);
		wizarpayTicket.setTitle(getResources().getString(R.string.wizar_ticket_cancle));
		items.add(wizarpayTicket);
		DialogHelper2.showMultieChooseDialog(getActivity(), items,
				new MultieChooseDialogFragment.MultieChooseListener() {
					@Override
					public void onItemClick(int position) {
						if (position == 0) {
							thirdTicketCancel();
						} else if(position == 1){
							wepayTicketCancel();
						} else if(position == 2){
							wizarTicketCancel();
						}
					}
				});
	}

	/**
	 * 微信卡券核销
	 */
	protected void wepayTicketCancel() {
		Intent intent = new Intent(getActivity(), WepayTicketCancleActivity.class);
		getActivity().startActivity(intent);
	}

	/**
	 * 慧商卡券核销
	 */
	protected void wizarTicketCancel() {
		Intent intent = new Intent(getActivity(), WizarTicketCancleActivity.class);
		getActivity().startActivity(intent);
	}

	/**
	 * 第三方卡券核销
	 */
	protected void thirdTicketCancel() {
		Intent intent = new Intent(getActivity(), ThirdTicketCancleActivity.class);
		getActivity().startActivity(intent);
	}

	/**
	 * 修改网络
	 */
	protected void changeNetwork() {
		if (Constants.FALSE.equals(AppStateManager.getState(AppStateDef.isOffline, Constants.FALSE))) {// 如果当前为在线模式,则提示是否切换到离线模式
			DialogHelper2.showChoiseDialog(getActivity(), "当前为在线模式,是否切换至离线模式(离线模式只能进行现金消费)?",
					new DialogHelper2.DialogChoiseListener() {
						@Override
						public void onNo() {

						}

						@Override
						public void onOK() {
							AppStateManager.setState(AppStateDef.isOffline, Constants.TRUE);
							isOnline = "0";
							setTitleRightImage(R.drawable.nowife);
						}
					});
		} else {// 如果当前为离线模式,则提示是否切换到在线模式
			DialogHelper2.showChoiseDialog(getActivity(), "当前为离线模式,是否切换至在线模式?",
					new DialogHelper2.DialogChoiseListener() {

						@Override
						public void onNo() {

						}

						@Override
						public void onOK() {
							appManager.ping(new ResultListener() {// 测试网络

								@Override
								public void onSuccess(Response response) {
									AppStateManager.setState(AppStateDef.isOffline, Constants.FALSE);
									isOnline = "1";
									setTitleRightImage(R.drawable.wife);
								}

								@Override
								public void onFaild(Response response) {
									UIHelper.ToastMessage(getActivity(), "网络不通,无法切换到在线模式");
									setTitleRightImage(R.drawable.nowife);
								}
							});
						}
					});
		}
	}

	public class MyBroadcastReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			String str = intent.getStringExtra("value");
			if (!TextUtils.isEmpty(str) && str.equals("0")) {
				setTitleRightImage(R.drawable.nowife);
			} else if (!TextUtils.isEmpty(str) && str.equals("1")) {
				onPaySuccess();
				setTitleRightImage(R.drawable.wife);
			}

		}

	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(myBroadcastReceiver);
	}
}
