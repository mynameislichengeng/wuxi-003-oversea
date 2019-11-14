package com.wizarpos.pay.cashier.fragment;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
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
import com.wizarpos.pay.cashier.fragment.TotalFragment2.OnTotalFragmentClickedListener;
import com.wizarpos.pay.cashier.model.TicketTotalRespBean;
import com.wizarpos.pay.common.Constants;
import com.wizarpos.pay.common.MerchantLogoHelper;
import com.wizarpos.pay.common.base.BasePresenter.ResultListener;
import com.wizarpos.pay.common.utils.Calculater;
import com.wizarpos.pay.common.utils.UIHelper;
import com.wizarpos.pay.db.AppConfigDef;
import com.wizarpos.pay.db.AppConfigHelper;
import com.wizarpos.pay.db.AppStateDef;
import com.wizarpos.pay.db.AppStateManager;
import com.wizarpos.pay.statistics.activity.QueryActivity;
import com.wizarpos.pay.statistics.activity.RecordActivity;
import com.wizarpos.pay.statistics.activity.TicketDetialActivity;
import com.wizarpos.pay.statistics.fragment.AccountFragment2;
import com.wizarpos.pay.statistics.model.GroupQueryPay;
import com.wizarpos.pay.statistics.model.GroupQueryResp;
import com.wizarpos.pay.statistics.presenter.StatisticsPresenter;
import com.wizarpos.pay2.lite.R;

public class ChannelHomeFragment2 extends HomeFragment implements OnItemClickListener ,OnTotalFragmentClickedListener{
	private Button btnIpay, ticketCancle;
	private StatisticsPresenter presenter;
	private List<String[]> recordListShow = new ArrayList<String[]>();
	/** 交易汇总查询返回对象集合 */
	private List<GroupQueryPay> queryPayList;
	// private ListView listView;
	private MainMenuAdapter mainMenuAdapter = null;
	private MainFragmentAdapter mainFragmentAdapter;
	private ViewPager viewPager;

	private TotalFragment2 ticketTranAmontFragment;
	private TotalFragment2 newMemberAmountFragment;
	private TotalFragment2 chanelMonthTotalFragment;

	private RadioButton[] radioButtons = new RadioButton[3];
	private String monthAmount = "0.00";// 本月汇总

	private String ticketTranAmount = "0.00";// 券交易金额
	private String newMemberAmount = "0";// 新增会员数
	// private String publishedTotalTicketAmount="0";//累计券发放数

	private int currentFragmentIndex = 0;

	public ChannelHomeFragment2() {
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
		ArrayList<Fragment> list = new ArrayList<Fragment>();
		ticketTranAmontFragment = TotalFragment2.newInstance("券交易金额");
		newMemberAmountFragment = TotalFragment2.newInstance("新增会员数");
		chanelMonthTotalFragment = TotalFragment2.newInstance("本月收款金额");
		
		ticketTranAmontFragment.setOnTotalFragmentClickedListener(this);
		newMemberAmountFragment.setOnTotalFragmentClickedListener(this);
		chanelMonthTotalFragment.setOnTotalFragmentClickedListener(this);
		
		list.add(ticketTranAmontFragment);
		list.add(newMemberAmountFragment);
		list.add(chanelMonthTotalFragment);

		mainFragmentAdapter = new MainFragmentAdapter(getChildFragmentManager(), list);
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

	private void setData() {
		ticketTranAmontFragment.updateAmount(ticketTranAmount);
		newMemberAmountFragment.updateAmount(newMemberAmount);
		chanelMonthTotalFragment.updateAmount(monthAmount);
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
		todayAmount();
		getTicketTotalData();
	}

	private void todayAmount() {
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
				// mainMenuAdapter.list = mainMenuList;
				// mainMenuAdapter.notifyDataSetChanged();
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
	 * 获取头部数据
	 */
	private void getTicketTotalData() {
		presenter.getTicketTotalData(new ResultListener() {

			@Override
			public void onSuccess(Response response) {
				TicketTotalRespBean ticketTotalRespBean = (TicketTotalRespBean) response.result;
				ticketTranAmount = ticketTotalRespBean.getTicketTranAmount();
				newMemberAmount = ticketTotalRespBean.getMerchantMemNum();
				monthAmount = Calculater.formotFen(String.valueOf(ticketTotalRespBean.getMonthTranAmount()));
				setData();
			}

			@Override
			public void onFaild(Response response) {
				UIHelper.ToastMessage(getActivity(), response.msg);
			}
		});
	}
	
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		if (queryPayList == null || queryPayList.size() == 0) {
			LogEx.w(ChannelHomeFragment2.class.getName(), "queryPayList is null.");
			return;
		}
		GroupQueryPay queryPay = queryPayList.get(position);
		if (queryPay.getTranCode() <= 0) {
			LogEx.w(ChannelHomeFragment2.class.getName(), "queryPay.getTranCode() <= 0.");
			return;
		}
		Intent intent = new Intent();
		intent.setClass(getActivity(), RecordActivity.class);
		intent.putExtra(RecordActivity.TRAN_CODE_TAG, queryPay.getTranCode());
		startActivity(intent);
	}

	@Override
	public void onTotalFragmentClicked(String currentTitle) {
		if (currentFragmentIndex == 0) {// 券交易金额
			QueryActivity.toQueryActivity(this, AccountFragment2.TRANS_TYPE_TICKET_DETIAL);
		} else if (currentFragmentIndex == 1) { // 新增会员数
			
		} else { // 本月收款金额
			
		}
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if(resultCode == Activity.RESULT_OK && requestCode == Constants.QUERY_QUESTCODE){//券明细 wu
//			Bundle result = data.getExtras();
			data.setClass(getActivity(), TicketDetialActivity.class);
			startActivity(data);
		}
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
