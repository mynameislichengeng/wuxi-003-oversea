package com.wizarpos.pay;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

//import com.wizarpos.netpay.server.NetPayProxy;
import com.wizarpos.pay.app.PaymentApplication;
import com.wizarpos.pay.cashier.fragment.ChannelHomeFragment2;
import com.wizarpos.pay.cashier.fragment.PaymentFragment2;
import com.wizarpos.pay.common.Constants;
import com.wizarpos.pay.common.base.BaseViewActivity;
import com.wizarpos.pay.common.utils.UIHelper;
import com.wizarpos.pay.db.AppConfigDef;
import com.wizarpos.pay.db.AppConfigHelper;
import com.wizarpos.pay.manage.fragment.ManagerFragment;
import com.wizarpos.pay.setting.fragment.SettingFragment;
import com.wizarpos.pay.statistics.fragment.AccountFragment2;
import com.wizarpos.pay.thirdapp.SpeakMgr;
import com.motionpay.pay2.lite.R;

public class MainFragmentActivity2 extends BaseViewActivity implements OnClickListener {
	private ArrayList<Fragment> fragments = new ArrayList<Fragment>();
	private PaymentFragment2 paymentFragment;
	private ChannelHomeFragment2 channelHomeFragment;
	private AccountFragment2 accountFragment;
	private ManagerFragment managerFragment;
	private  SettingFragment settingFragment;

	private FragmentManager mFragmentManager;
	private LinearLayout[] llLayout = new LinearLayout[4];
	private Button[] btnArray = new Button[4];
	private TextView[] bottomTitle = new TextView[4];
	int[] backgroundPics = { R.drawable.menu_h_sk, R.drawable.menu_h_zb, R.drawable.menu_h_gl, R.drawable.menu_h_sz };
	int[] backgroundPictures = { R.drawable.menu_n_sk, R.drawable.menu_n_zb, R.drawable.menu_n_gl, R.drawable.menu_n_sz };
	int currentFragmentIndex = 0;

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
//		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setMainView(R.layout.main_fragment_2);
		setupView();
		setData();
		addListener();
		updateButtonColor();

		currentFragmentIndex = 0;
		addFragment(currentFragmentIndex);// 显示第一个fragment
		
	}

	private void setupView() {
		llLayout[0] = (LinearLayout) findViewById(R.id.ll_pay);
		llLayout[1] = (LinearLayout) findViewById(R.id.ll_count);
		llLayout[2] = (LinearLayout) findViewById(R.id.ll_manager);
		llLayout[3] = (LinearLayout) findViewById(R.id.ll_setting);
		btnArray[0] = (Button) findViewById(R.id.btn_main_cashier);
		btnArray[1] = (Button) findViewById(R.id.btn_main_account);
		btnArray[2] = (Button) findViewById(R.id.btn_main_manager);
		btnArray[3] = (Button) findViewById(R.id.btn_main_setting);
		bottomTitle[0] = (TextView) findViewById(R.id.tv_mainmenu_shou_kuan);
		bottomTitle[1] = (TextView) findViewById(R.id.tv_mainmenu_zhang_ben);
		bottomTitle[2] = (TextView) findViewById(R.id.tv_mainmenu_guan_li);
		bottomTitle[3] = (TextView) findViewById(R.id.tv_mainmenu_she_zhi);
	}

	private void setData() {
		if(Constants.APP_VERSION_NAME == Constants.APP_VERSION__CHANNEL){ //增加渠道版和普通版区分
			channelHomeFragment = new ChannelHomeFragment2();
			fragments.add(channelHomeFragment);
		}else{
			paymentFragment = new PaymentFragment2();
			fragments.add(paymentFragment);
		}
		mFragmentManager = getSupportFragmentManager();
		// accountFragment = new AccountFragment();
		accountFragment = new AccountFragment2();// 做滑动
		managerFragment = new ManagerFragment();
		settingFragment = new SettingFragment();
		
		fragments.add(accountFragment);
		fragments.add(managerFragment);
		fragments.add(settingFragment);
	}

	private void addListener() {
		for (LinearLayout layout : llLayout) {
			layout.setOnClickListener(this);
		}
		for (Button btn : btnArray) {
			btn.setOnClickListener(this);
		}

	}

	private void updateButtonColor() {
		for (int i = 0; i < btnArray.length; i++) {
			if (i == this.currentFragmentIndex) {
				btnArray[i].setBackgroundResource(backgroundPics[i]);
			} else {
				btnArray[i].setBackgroundResource(backgroundPictures[i]);
			}
			if (i == this.currentFragmentIndex) {
				bottomTitle[i].setTextColor(this.getResources().getColor(R.color.blue_color));
				bottomTitle[i].setAlpha(0.9f);
			} else {
				bottomTitle[i].setTextColor(this.getResources().getColor(R.color.black));
				bottomTitle[i].setAlpha(0.5f);
			}
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_main_cashier:
			if (currentFragmentIndex != 0) {
				if (fragments.get(0).isAdded()) {
					fragments.get(currentFragmentIndex).onPause();
					showCurrentFragment(0);
				} else {
					addFragment(0);
				}

				currentFragmentIndex = 0;

			}
			break;
		case R.id.btn_main_account:
			if (currentFragmentIndex != 1) {
				if (fragments.get(1).isAdded()) {
					fragments.get(currentFragmentIndex).onPause();
					showCurrentFragment(1);
					//如果不是第一次加载，就刷新界面 Hwc
					AccountFragment2 accountFramgnet = (AccountFragment2) fragments.get(1);
					accountFramgnet.refreshData();
				} else {
					addFragment(1);
				}

				currentFragmentIndex = 1;
			}
			break;
		case R.id.btn_main_manager:
			if (currentFragmentIndex != 2) {
				if (fragments.get(2).isAdded()) {
					fragments.get(currentFragmentIndex).onPause();
					showCurrentFragment(2);
				} else {
					addFragment(2);
				}

				currentFragmentIndex = 2;

			}
			break;
		case R.id.btn_main_setting:
			if (currentFragmentIndex != 3) {
				if (fragments.get(3).isAdded()) {
					fragments.get(currentFragmentIndex).onPause();
					showCurrentFragment(3);
				} else {
					addFragment(3);
				}

				currentFragmentIndex = 3;
			}
			break;
		case R.id.ll_pay:
			if (currentFragmentIndex != 0) {
				if (fragments.get(0).isAdded()) {
					fragments.get(currentFragmentIndex).onPause();
					showCurrentFragment(0);
				} else {
					addFragment(0);
				}

				currentFragmentIndex = 0;

			}
			break;
		case R.id.ll_count:
			if (currentFragmentIndex != 1) {
				if (fragments.get(1).isAdded()) {
					fragments.get(currentFragmentIndex).onPause();
					showCurrentFragment(1);
				} else {
					addFragment(1);
				}

				currentFragmentIndex = 1;
			}
			break;
		case R.id.ll_manager:
			if (currentFragmentIndex != 2) {
				if (fragments.get(2).isAdded()) {
					fragments.get(currentFragmentIndex).onPause();
					showCurrentFragment(2);
				} else {
					addFragment(2);
				}

				currentFragmentIndex = 2;

			}
			break;
		case R.id.ll_setting:
			if (currentFragmentIndex != 3) {
				if (fragments.get(3).isAdded()) {
					fragments.get(currentFragmentIndex).onPause();
					showCurrentFragment(3);
				} else {
					addFragment(3);
				}

				currentFragmentIndex = 3;

			}
			break;
		}
		updateButtonColor();

	}

	private void showCurrentFragment(int showPosition) {
		for (int i = 0; i < fragments.size(); i++) {
			if (i == showPosition) {
				mFragmentManager.beginTransaction().show(fragments.get(i)).commit();
			} else {
				mFragmentManager.beginTransaction().hide(fragments.get(i)).commit();
			}
		}
	}

	private void addFragment(int positon) {
		mFragmentManager.beginTransaction().add(R.id.flReplace, fragments.get(positon)).commit();
		showCurrentFragment(positon);
	}

	/*
	 * 退出
	 */
	private int im = 0;

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			switch (im++) {
			case 0:
				UIHelper.ToastMessage(this, getResources().getString(R.string.again_exit));
				Timer timer = new Timer();
				timer.schedule(new TimerTask() {
					public void run() {
						im = 0;
					}
				}, 3000);
				break;
			case 1:
				PaymentApplication.getInstance().resetAppState();
				//释放网络收单相关资源 hwc
				if(AppConfigHelper.getConfig(AppConfigDef.isUseNetPay, false)) {
//					NetPayProxy.getInstance().stopServer(this);
					SpeakMgr.getInstants().onDestroy();
				}
				this.finish();
				break;
			default:
				break;
			}
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

}
