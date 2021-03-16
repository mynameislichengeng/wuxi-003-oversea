package com.wizarpos.pay.cashier.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RadioButton;

import com.wizarpos.base.net.Response;
import com.wizarpos.log.util.LogEx;
import com.wizarpos.pay.cashier.adapter.MainFragmentAdapter;
import com.wizarpos.pay.cashier.adapter.MainMenuAdapter;
import com.wizarpos.pay.common.Constants;
import com.wizarpos.pay.common.MerchantLogoHelper;
import com.wizarpos.pay.common.base.BasePresenter.ResultListener;
import com.wizarpos.pay.common.utils.Calculater;
import com.wizarpos.pay.db.AppConfigDef;
import com.wizarpos.pay.db.AppConfigHelper;
import com.wizarpos.pay.db.AppStateDef;
import com.wizarpos.pay.db.AppStateManager;
import com.wizarpos.pay.statistics.activity.RecordActivity;
import com.wizarpos.pay.statistics.model.GroupQueryPay;
import com.wizarpos.pay.statistics.model.GroupQueryResp;
import com.wizarpos.pay.statistics.presenter.StatisticsPresenter;
import com.wizarpos.pay.view.util.MapValueGetKey;
import com.motionpay.pay2.lite.R;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class PaymentFragment2 extends HomeFragment implements OnItemClickListener{
	private Button btnIpay, ticketCancle;
	private StatisticsPresenter presenter;
	private List<String[]> recordListShow = new ArrayList<String[]>();
	/** 交易汇总查询返回对象集合*/
	private List<GroupQueryPay> queryPayList;
//	private ListView listView;
	private MainMenuAdapter mainMenuAdapter = null;
	private MainFragmentAdapter mainFragmentAdapter;
	private ViewPager viewPager;
	private TotalFragment2 todayTotalFragment;
	private TotalFragment2 weekTotalFragment;
	private TotalFragment2 monthTotalFragment;
	private RadioButton[] radioButtons = new RadioButton[3];
	private String todayAmount="0.00";//今天汇总
	private String weekAmount="0.00";//本周汇总
	private String monthAmount="0.00";//本月汇总
	private int currentFragmentIndex = 0;

	public PaymentFragment2() {
	}
	
	@Override
	public void initView() {
		mainView = LayoutInflater.from(getActivity()).inflate(R.layout.main_full, null);
		setMainView(mainView);
		setTitleText("收款");
		showTitleLeftImg(MerchantLogoHelper.getMerchantLogoUrl());
		setTitleRightImage(R.drawable.wife);
		setupView(mainView);
		initViewPager();
		addListener();
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		presenter = new StatisticsPresenter(getActivity());
	}

	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		progresser.showProgress();
		onPaySuccess();
	}
	
	private void initViewPager() {
		todayTotalFragment = TotalFragment2.newInstance("今日累计（元）");
		weekTotalFragment = TotalFragment2.newInstance("本周累计（元）");
		monthTotalFragment = TotalFragment2.newInstance("本月累计（元）");	
		ArrayList<Fragment> list = new ArrayList<Fragment>();
		list.add(todayTotalFragment);
		list.add(weekTotalFragment);
		list.add(monthTotalFragment);
		mainFragmentAdapter = new MainFragmentAdapter(getChildFragmentManager(),list);
		viewPager.setAdapter(mainFragmentAdapter);
	}

	@Override
	protected void onTitleRightClicked() {
		super.onTitleRightClicked();
		changeNetwork();
	}
	
	private void setupView(View view) {
		viewPager = (ViewPager) view.findViewById(R.id.total_amount);
		radioButtons[0] = (RadioButton) view.findViewById(R.id.first_radio_button);
		radioButtons[1] = (RadioButton) view.findViewById(R.id.second_radio_button);
		radioButtons[2] = (RadioButton) view.findViewById(R.id.third_radio_button);
		boolean net = Constants.FALSE.equals(AppStateManager.getState(AppStateDef.isOffline, Constants.FALSE));
		if (!net) {
			isOnline = "0";
			setTitleRightImage(R.drawable.nowife);
		} else {
			isOnline = "1";
			setTitleRightImage(R.drawable.wife);
		}

		btnIpay = (Button) view.findViewById(R.id.btn_ipay);
		ticketCancle = (Button) view.findViewById(R.id.btn_ticket_use);
		showTicketCancleBtn();
		ListView listView = (ListView) view.findViewById(R.id.list_data);
		listView.setOnItemClickListener(this);
		mainMenuAdapter = new MainMenuAdapter(LayoutInflater.from(getActivity()));
		listView.setAdapter(mainMenuAdapter);
	}

	private void setData() { //FIXME bug  Observer android.support.v4.view.ViewPager$PagerObserver@41a08928 was not registered wu
		todayTotalFragment.updateAmount(todayAmount);
		weekTotalFragment.updateAmount(weekAmount);
		monthTotalFragment.updateAmount(monthAmount);
	}

	private void addListener() {
		viewPager.addOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int index) {
				currentFragmentIndex = index;
				updateButtonColor();
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {

			}

			@Override
			public void onPageScrollStateChanged(int arg0) {

			}
		});
		
		btnIpay.setOnClickListener(new OnClickListener() {
			public void onClick(View arg0) {
				toCashierView();
			}
		});
		
		ticketCancle.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				showTicketCancelDialog();
			}
		});

	}

	protected void onPaySuccess() {
		getTransDetialByToday();
		getTransTotalByToday();
	}
	private void getTransDetialByToday() {
		presenter.transactionGroupQuery(new ResultListener() {
			double amount = 0.00d;

			public void onSuccess(Response response) {
				progresser.showContent();
				GroupQueryResp result = (GroupQueryResp) response.result;
				recordListShow = result.getRecordListShow();
				queryPayList = result.getQueryPayList();
				for (int i = 0; i < recordListShow.size(); i++) {
					amount = getBigDecimalDebit(recordListShow.get(i)[2], amount);
				}
				// 需要重新封装一个list来显示
				List<String[]> mainMenuList = new ArrayList<String[]>();
				double cashAmount = 0.00, revokeAmount = 0.00;
				for (int j = 0; j < recordListShow.size(); j++) {
					if (recordListShow.get(j)[0].contains("现金消费")) {
						cashAmount = getBigDecimalDebit(recordListShow.get(j)[2], cashAmount);
						recordListShow.remove(recordListShow.get(j));
						j--;
					} else if (recordListShow.get(j)[0].contains("撤销")) {
						revokeAmount = getBigDecimalDebit(recordListShow.get(j)[2], revokeAmount);
						recordListShow.remove(recordListShow.get(j));
						j--;
					} else {
						mainMenuList = recordListShow;
					}
				}
				DecimalFormat format = new DecimalFormat("#.##");
				if (0.00 != cashAmount) {
					String[] cashData = { "现金消费", "", String.valueOf(format.format(cashAmount)) };
					mainMenuList.add(cashData);
				}
				if (0.00 != revokeAmount) {
					String[] revokeData = { "撤销", "", String.valueOf(format.format(revokeAmount)) };
					mainMenuList.add(revokeData);
				}
//				mainMenuAdapter.list = mainMenuList;
//				mainMenuAdapter.notifyDataSetChanged();
				mainMenuAdapter.setDataChanged(mainMenuList);
			}

			@Override
			public void onFaild(Response response) {
				// UIHelper.ToastMessage(getActivity(), getResources().getString(R.string.no_cashier_count));
				progresser.showContent();
				amount = 0.00;
			}
		});
	}

	protected void updateButtonColor() {
		for (int i = 0; i < radioButtons.length; i++) {
			if (i == this.currentFragmentIndex) {
				radioButtons[i].setBackgroundResource(R.drawable.scroll_active);
			} else {
				radioButtons[i].setBackgroundResource(R.drawable.scroll_normal);
			}
		}

	}

	private double getBigDecimalDebit(String string, double debit2) {
		BigDecimal decimal1 = new BigDecimal(string);
		BigDecimal decimal2 = new BigDecimal(String.valueOf(debit2));
		return decimal1.add(decimal2).doubleValue();
	}

	
	/**
	 * 获取汇总信息
	 */
	private void getTransTotalByToday() {
		presenter.getTransactionTotalData(new ResultListener() {

			@Override
			public void onSuccess(Response response) {
				LogEx.d("TotalFragment", String.valueOf(response.result));
				List<Long> list = (List<Long>) response.result;
				todayAmount = Calculater.formotFen(String.valueOf(list.get(0)));
				weekAmount = Calculater.formotFen(String.valueOf(list.get(1)));
				monthAmount = Calculater.formotFen(String.valueOf(list.get(2)));
				setData();
			}

			@Override
			public void onFaild(Response response) {
				LogEx.d("TotalFragment", String.valueOf(response.result));
				setData();
			}
		});
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		String[] queryPay = (String[]) mainMenuAdapter.getItem(position);
		if(queryPay == null){
			return;
		}
		String titleName = queryPay[0];
		//根据value获得key
		MapValueGetKey map = new MapValueGetKey(Constants.TRAN_TYPE);
		int tranCode = 0;
		if(map.getKey(titleName) == null || !(map.getKey(titleName) instanceof String))
		{
			if(titleName.equals("组合支付  (券)"))
			{//组合支付(券)跳转和组合支付一样 2015年12月08日14:24:31 @hwc
				tranCode = Integer.valueOf(Constants.SC_800_MIXPAY);
			}else
			{	
				LogEx.w(PaymentFragment2.class.getName(), "not value.");
				return;
			}
		}else
		{
			tranCode = Integer.valueOf(map.getKey(titleName).toString());
		}
		if(tranCode <= 0)
		{
			LogEx.w(PaymentFragment2.class.getName(), "tranCode <= 0.");
			return;
		}
		Intent intent = new Intent();
		intent.setClass(getActivity(), RecordActivity.class);
		intent.putExtra(RecordActivity.TRAN_CODE_TAG, tranCode);
		startActivity(intent);
	}

	@Override
	protected void onSettingSuccess() {
		showTicketCancleBtn();
	}

	private void showTicketCancleBtn() {
		boolean showTicketCancle = Constants.TRUE.equals(AppConfigHelper.getConfig(AppConfigDef.isSupportTicketCancel));
		if (showTicketCancle) {
			ticketCancle.setVisibility(View.VISIBLE);
		}else {
			ticketCancle.setVisibility(View.GONE);
		}
	}

}
