package com.wizarpos.pay.statistics.activity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;

import com.wizarpos.pay.common.Constants;
import com.wizarpos.pay.common.DialogHelper;
import com.wizarpos.pay.common.base.BaseViewActivity;
import com.wizarpos.pay.common.utils.UIHelper;
import com.wizarpos.pay.statistics.fragment.AccountFragment2;
import com.wizarpos.pay.view.ShowSpinnerData;
import com.wizarpos.pay.view.adapter.SpinnerNoIconAdapter;
import com.wizarpos.pay.view.util.Constents;
import com.motionpay.pay2.lite.R;

public class QueryActivity extends BaseViewActivity {
	public EditText startDateEt, endDateEt, swiftNumberEt;
	private SpinnerNoIconAdapter transTypeAdapter = null;
	private SpinnerNoIconAdapter transTimeRangeAdapter = null;
	private String transCode = "-1", dateCode = "0";
	private String startDate = null, endDate = null;
	private int queryCode;
	ArrayList<String> stringList = new ArrayList<String>();
	LinearLayout llSerialNum;
	private int TEXT_MAX = 13;// 输入框字符长度
	private List<ShowSpinnerData> transTypes, dateRanges;
	private ShowSpinnerData allTransType = new ShowSpinnerData(Constents.ALLTRANSTYPE, "全部");
	private ShowSpinnerData cashTransType = new ShowSpinnerData(Constents.CASHTRANSTYPE, "现金消费");
	private ShowSpinnerData bankTransType = new ShowSpinnerData(Constents.BANKTRANSTYPE, "银行卡消费");
	private ShowSpinnerData memberTransType = new ShowSpinnerData(Constents.MEMBERCARDTRANSTYPE, "会员卡消费");
	private ShowSpinnerData alipayTransType = new ShowSpinnerData(Constents.ALIPAYTRANSTYPE, "支付宝消费");
	private ShowSpinnerData wepayTransType = new ShowSpinnerData(Constents.WEPAYTRANSTYPE, "微信消费");
	private ShowSpinnerData tenpayTranstype = new ShowSpinnerData(Constents.TENPPAYTRANSTYPE, "QQ钱包消费");
	private ShowSpinnerData baidupayTransType = new ShowSpinnerData(Constents.BAIDUPAYTRANSTYPE, "百度支付消费");
	private ShowSpinnerData otherpayTransType = new ShowSpinnerData(Constents.OTHERPAYTRANSTYPE, "其它支付消费");
	private ShowSpinnerData mixTransType = new ShowSpinnerData(Constents.MIXPAYTRANSTYPE, "组合支付");
	private ShowSpinnerData cashRepeal = new ShowSpinnerData(Constents.CASHREPEAL, "现金撤销");
	private ShowSpinnerData wepayRepeal = new ShowSpinnerData(Constents.WEPAYREPEAL, "微信消费撤销");
	private ShowSpinnerData alipayRepeal = new ShowSpinnerData(Constents.ALIPAYREPEAL, "支付宝消费撤销");
	private ShowSpinnerData baiduRepeal = new ShowSpinnerData(Constents.BAIDUREPEAL, "百度支付撤销");
	private ShowSpinnerData offlineCash = new ShowSpinnerData(Constents.OFFLINECASHPAY, "离线现金消费");
	private ShowSpinnerData today = new ShowSpinnerData(Constents.TODAY, "今天");
	private ShowSpinnerData yestoday = new ShowSpinnerData(Constents.YESTODAY, "昨天");
	private ShowSpinnerData thisweek = new ShowSpinnerData(Constents.THISWEEK, "本周");
	private ShowSpinnerData lastweek = new ShowSpinnerData(Constents.LASTWEEK, "上周");
	private ShowSpinnerData thismonth = new ShowSpinnerData(Constents.THISMONTH, "本月");
	private ShowSpinnerData lastmonth = new ShowSpinnerData(Constents.LASTMONTH, "上月");
	private ShowSpinnerData chooseTimeRange = new ShowSpinnerData(Constents.CHOOSETIMERANGE, "请选择时间范围");

	public static void toQueryActivity(Activity activity, int transCode){
		Intent intent = new Intent(activity, QueryActivity.class);
		intent.putExtra("transCode", transCode);
		activity.startActivityForResult(intent, Constants.QUERY_QUESTCODE);
	}
	
	public static void toQueryActivity(Fragment fragment, int transCode){
		Intent intent = new Intent(fragment.getActivity(), QueryActivity.class);
		intent.putExtra("transCode", transCode);
		fragment.startActivityForResult(intent, Constants.QUERY_QUESTCODE);
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initView();
	}
	
	private void initView() {
		setMainView(R.layout.query_menu);
		setTitleText(getResources().getString(R.string.query));
		showTitleBack();
		llSerialNum = (LinearLayout) findViewById(R.id.ll_serialnum);
		queryCode = getIntent().getIntExtra("transCode", 0);
		if (queryCode == AccountFragment2.TRANS_TYPE_RECORD_DETAIL) {
			llSerialNum.setVisibility(View.VISIBLE);
		}else if(queryCode == AccountFragment2.TRANS_TYPE_TICKET_DETIAL){ //券交易记录查询
			findViewById(R.id.llTransType).setVisibility(View.GONE);
			findViewById(R.id.ll_serialnum).setVisibility(View.GONE);
		}else if (queryCode==AccountFragment2.TRANS_TYPE_TRANS_TOTAL) {//交易汇总
			llSerialNum.setVisibility(View.GONE);
		}
		startDateEt = (EditText) findViewById(R.id.start_et);
		endDateEt = (EditText) findViewById(R.id.end_et);
		swiftNumberEt = (EditText) findViewById(R.id.serial_transnumber_et);
		swiftNumberEt.setText(swiftNumberEt.getText().toString());
		swiftNumberEt.setSelection(swiftNumberEt.getText().length());
		swiftNumberEt.setHint("请输入数字部分");
		InputFilter[] filters = new InputFilter[1];
		filters[0] = new InputFilter.LengthFilter(TEXT_MAX);
		swiftNumberEt.setFilters(filters);
		swiftNumberEt.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				String editable = swiftNumberEt.getText().toString();
				if (TextUtils.isEmpty(editable)) { return; }
				if (Character.isDigit(editable.charAt(0))) {
					editable = "P" + editable;
					swiftNumberEt.setText(editable);
					swiftNumberEt.setSelection(editable.length());
				}
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {

			}

			@Override
			public void afterTextChanged(Editable s) {

			}
		});
		int[] btnIds = { R.id.btn_query };
		setOnClickListenerByIds(btnIds, this);
		showTransType();
		showDate();
		startDateEt.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				DialogHelper.showDateDialog(QueryActivity.this, startDateEt);
			}
		});
		startDateEt.setOnFocusChangeListener(new View.OnFocusChangeListener() {
			public void onFocusChange(View v, boolean hasFocus) {
				if (hasFocus) {
					DialogHelper.showDateDialog(QueryActivity.this, startDateEt);
				}
			}
		});
		endDateEt.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				DialogHelper.showDateDialog(QueryActivity.this, endDateEt);
			}
		});
		endDateEt.setOnFocusChangeListener(new View.OnFocusChangeListener() {
			public void onFocusChange(View v, boolean hasFocus) {
				if (hasFocus) {
					DialogHelper.showDateDialog(QueryActivity.this, endDateEt);
				}
			}
		});
	}

	// 交易类型
	protected void showTransType() {
		transTypeAdapter = new SpinnerNoIconAdapter(this);
		Spinner spTransType = (Spinner) findViewById(R.id.trans_type_spinner);
		transTypes = new ArrayList<ShowSpinnerData>();
		transTypes.add(allTransType);
		transTypes.add(cashTransType);
		transTypes.add(bankTransType);
		transTypes.add(memberTransType);
		transTypes.add(alipayTransType);
		transTypes.add(wepayTransType);
		transTypes.add(tenpayTranstype);
		transTypes.add(baidupayTransType);
		transTypes.add(otherpayTransType);
		transTypes.add(mixTransType);
		transTypes.add(cashRepeal);
		transTypes.add(wepayRepeal);
		transTypes.add(alipayRepeal);
		transTypes.add(baiduRepeal);
		transTypes.add(offlineCash);
		spTransType.setAdapter(transTypeAdapter);
		transTypeAdapter.setTransTypes(transTypes);
		spTransType.setAdapter(transTypeAdapter);
		spTransType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			public void onItemSelected(AdapterView<?> aview, View view, int position, long arg3) {
				transCode = String.valueOf(transTypeAdapter.getItem(position));
			}

			public void onNothingSelected(AdapterView<?> aview) {}
		});
	}

	// 时间范围
	protected void showDate() {
		Spinner spDate = (Spinner) findViewById(R.id.trans_date_spinner);
		transTimeRangeAdapter = new SpinnerNoIconAdapter(this);
		dateRanges = new ArrayList<ShowSpinnerData>();
		dateRanges.add(today);
		dateRanges.add(yestoday);
		dateRanges.add(thisweek);
		dateRanges.add(lastweek);
		dateRanges.add(thismonth);
		dateRanges.add(lastmonth);
		dateRanges.add(chooseTimeRange);
		spDate.setAdapter(transTimeRangeAdapter);
		transTimeRangeAdapter.setTransTypes(dateRanges);
		spDate.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			public void onItemSelected(AdapterView<?> aview, View view, int position, long arg3) {
				dateCode = String.valueOf(transTimeRangeAdapter.getItem(position));
				if (dateCode.equals("6")) {
					startDateEt.setEnabled(true);
					startDateEt.setFocusable(true);
					endDateEt.setEnabled(true);
					endDateEt.setFocusable(true);
					startDateEt.setText(startDate);
					endDateEt.setText(endDate);
					startDateEt.setBackgroundDrawable(getResources().getDrawable(R.drawable.search_input));
					endDateEt.setBackgroundDrawable(getResources().getDrawable(R.drawable.search_input));
				} else {
					startDateEt.setEnabled(false);
					startDateEt.setFocusable(false);
					endDateEt.setEnabled(false);
					endDateEt.setFocusable(false);
					startDateEt.setBackgroundDrawable(getResources().getDrawable(R.drawable.search_input2));
					endDateEt.setBackgroundDrawable(getResources().getDrawable(R.drawable.search_input2));
				}
			}

			public void onNothingSelected(AdapterView<?> aview) {}
		});
		startDateEt.setEnabled(false);
		startDateEt.setFocusable(false);
		endDateEt.setEnabled(false);
		endDateEt.setFocusable(false);
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.btn_query:
			if (!isNetworkConnected(this)) {
				UIHelper.ToastMessage(QueryActivity.this, "无网络连接，请检查网络");
				return;
			}
			String transType = transCode;
			String transTime = dateCode;
			String serialNum = swiftNumberEt.getText().toString();
			startDate = startDateEt.getText().toString();
			endDate = endDateEt.getText().toString();
			if (dateCode.equals("6")) {
				if (startDate.equals("") && endDate.equals("")) {
					UIHelper.ToastMessage(QueryActivity.this, "查询日期不能全为空");
					return;
				}
			}
			SimpleDateFormat dateformat = new SimpleDateFormat("yy-MM-dd");
			if (!TextUtils.isEmpty(startDate) && !TextUtils.isEmpty(endDate)) {
				Date startTime = null, endTime = null;
				try {
					startTime = dateformat.parse(startDate);
					endTime = dateformat.parse(endDate);
				} catch (ParseException e) {
					e.printStackTrace();
				}
				if (startTime.after(endTime)) {
					UIHelper.ToastMessage(QueryActivity.this, "起始日期大于结束日期");
					return;
				}
			}
			Bundle bundle = new Bundle();
			bundle.putString("transType", transType);
			bundle.putString("transTime", transTime);
			bundle.putString("serialNum", serialNum);
			bundle.putString("startDate", startDate);
			bundle.putString("endDate", endDate);
			bundle.putString("noFresh", "noFresh");
			Intent intent = new Intent();
			intent.putExtras(bundle);
			setResult(RESULT_OK, intent);
//			if (queryCode.equals("1")) {//跳到收款汇总查询结果界面
//				intent.setClass(QueryActivity.this, ShowQueryDataActivity.class);
//				startActivity(intent);
//				this.finish();
//			} else {
//				this.finish();
//			}
			this.finish();
			break;
		}
	}

}
