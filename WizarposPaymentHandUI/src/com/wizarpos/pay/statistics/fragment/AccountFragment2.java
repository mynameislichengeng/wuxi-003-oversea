package com.wizarpos.pay.statistics.fragment;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;

import com.wizarpos.pay.common.Constants;
import com.wizarpos.pay.common.MerchantLogoHelper;
import com.wizarpos.pay.statistics.activity.QueryActivity;
import com.wizarpos.pay.statistics.presenter.StatisticsPresenter;
import com.wizarpos.pay.view.ArrayItem;
import com.wizarpos.pay.view.fragment.BaseFragment;
import com.wizarpos.pay2.lite.R;

/**
 * 账本
 * @author wu
 *
 */
public class AccountFragment2 extends BaseFragment {
	public static final int TRANS_TYPE_RECORD_DETAIL = 0;
	public static final int TRANS_TYPE_TRANS_TOTAL = 1;
	public static final int TRANS_TYPE_TICKET_DETIAL = 2;
	
	private RecordFragment2 recordFragment;
	private SummarizingFragment summarizingFragment;
	// private RecordDetailFragment recordDetailFragment;
	private FragmentManager mFragmentManager;
	ArrayList<Fragment> fragments = new ArrayList<Fragment>();
	int currentFragmentIndex = 0;
	private int transCode = 0;
	private ArrayAdapter<ArrayItem> titleAdapter = null;
	ImageButton ibtn_search;
	ArrayList<String[]> recordListShow;
	private int transTypeCode = -1, timeRange = 0;
	private String startTime = "", endTime = "", serialNum = "", noFresh = "";
	ArrayList<String[]> detialRecords;
	private View mainView;

	private ArrayList<String> summarizingList = null;
	
//	query(int tranCode, int timeRange, String startTime, String endTime, String orderNo) {

	
	@Override
	public void initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		mainView = LayoutInflater.from(getActivity()).inflate(R.layout.account_main_menu, container, false);
		setMainView(mainView);
		setTitleRightImage(R.drawable.search);
		showToolbar();
//		showTitleLeftImg(MerchantLogoHelper.getMerchantLogoUrl());
		progresser.showContent();
		setupView(mainView);
		addListener();
		spToolbarTitle.setVisibility(View.VISIBLE);
		currentFragmentIndex = 0;
	}
	

	private void addFragment(int positon) {
		mFragmentManager.beginTransaction().add(R.id.flStatic, fragments.get(positon)).commit();
		showCurrentFragment(positon);
	}

	private void showCurrentFragment(int showPosition) {
		for (int i = 0; i < fragments.size(); i++) {
			if (i == showPosition) {
				// fragments.get(i).onResume();
				mFragmentManager.beginTransaction().show(fragments.get(i)).commit();
			} else {
				mFragmentManager.beginTransaction().hide(fragments.get(i)).commit();
			}
		}
	}

	private void setupView(View view) {
		ibtn_search = (ImageButton) view.findViewById(R.id.ibtn_search_mainmenu);
		recordFragment = new RecordFragment2();
		summarizingFragment = new SummarizingFragment();
		mFragmentManager = getFragmentManager();
		fragments.add(recordFragment);
		fragments.add(summarizingFragment);
		titleAdapter = new ArrayAdapter<ArrayItem>(getActivity(), R.layout.simple_title_spinner_item);
		titleAdapter.setDropDownViewResource(R.layout.simple_title_spinner_dropdown_item);
		showTitleType();

	}

	// 交易类型
	protected void showTitleType() {
		int[] numbers = { 0, 1 };
		String[] transTitles = { "收款记录", "收款汇总" };
		for (int i = 0; i < numbers.length; i++) {
			titleAdapter.add(new ArrayItem(numbers[i], transTitles[i]));
		}
		spToolbarTitle.setAdapter(titleAdapter);
		spToolbarTitle.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> arg0, View view, int position, long arg3) {
				transCode = titleAdapter.getItem(position).getRealValue();
				if (transCode == TRANS_TYPE_RECORD_DETAIL) {
					if (fragments.get(0).isAdded()) {
						// fragments.get(currentFragmentIndex).onPause();
						showCurrentFragment(0);
					} else {
						addFragment(0);
					}
					currentFragmentIndex = 0;
				} else {
					if (fragments.get(1).isAdded()) {
						// fragments.get(currentFragmentIndex).onPause();
						showCurrentFragment(1);
					} else {
						addFragment(1);
					}
					currentFragmentIndex = 1;
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {

			}

		});

	}

	private void addListener() {
		ibtn_search.setOnClickListener(new OnClickListener() {
			public void onClick(View arg0) {
//				Intent intent = new Intent(getActivity(), QueryActivity.class);
//				intent.putExtra("transCode", transCode);
//				startActivityForResult(intent, Constants.QUERY_QUESTCODE);
				QueryActivity.toQueryActivity(getActivity(), transCode);
			}
		});
	}
	
	
	/**
	 * 刷新界面,在MainFragmentActivity2中点击的时候调用 Hwc
	 */
	public void refreshData()
	{
		if (transCode == TRANS_TYPE_RECORD_DETAIL) {
			recordFragment.query(transTypeCode, timeRange, startTime, endTime, serialNum);
		} else if (transCode == TRANS_TYPE_TRANS_TOTAL) {
			if(summarizingList == null)
			{
				summarizingList = new ArrayList<>();
				String tranCode = "-1";
				String timeRange = "0";
				summarizingList.add(tranCode);
				summarizingList.add(timeRange);
				summarizingList.add(serialNum);
				summarizingList.add(startTime);
				summarizingList.add(endTime);
				summarizingList.add(noFresh);
			}
			summarizingFragment.getDataInfo(summarizingList);
		}
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == Activity.RESULT_OK) {
			if (requestCode == Constants.QUERY_QUESTCODE) {
				recordListShow = new ArrayList<String[]>();
				Bundle result = data.getExtras();
				transTypeCode = Integer.parseInt((String) result.get("transType"));
				timeRange = Integer.parseInt((String) result.get("transTime"));
				serialNum = (String) result.get("serialNum");
				startTime = (String) result.get("startDate");
				endTime = (String) result.get("endDate");
				noFresh = (String) result.get("noFresh");
				ArrayList<String> list = new ArrayList<String>();
				list.add(String.valueOf(transTypeCode));
				list.add(String.valueOf(timeRange));
				list.add(serialNum);
				list.add(startTime);
				list.add(endTime);
				list.add(noFresh);
				if (transCode == TRANS_TYPE_RECORD_DETAIL) {
//					recordFragment.getDataInfo(list);
					recordFragment.query(transTypeCode, timeRange, startTime, endTime, serialNum);
				} else if (transCode == TRANS_TYPE_TRANS_TOTAL) {
					summarizingList = list;
					summarizingFragment.getDataInfo(list);
				}
			}
		}
	}

	@Override
	protected void onTitleRightClicked() {
		Intent intent = new Intent(getActivity(), QueryActivity.class);
		intent.putExtra("transCode", transCode);
		startActivityForResult(intent, Constants.QUERY_QUESTCODE);
	}
}
