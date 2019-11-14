package com.wizarpos.pay.statistics.activity;

import android.os.Bundle;

import com.wizarpos.log.util.LogEx;
import com.wizarpos.pay.common.Constants;
import com.wizarpos.pay.common.base.BaseViewActivity;
import com.wizarpos.pay.statistics.fragment.RecordFragment2;
import com.wizarpos.pay2.lite.R;

/**
 * 
 * @ClassName: RecordActivity 
 * @author Huangweicai
 * @date 2015-9-11 下午4:22:27 
 * @Description: 统计数据详情  首页点击统计数据Item进入该activity详情 
 */
public class RecordActivity extends BaseViewActivity{
	
	private static final String LOG_TAG = RecordActivity.class.getName();

	private RecordFragment2 recordFragment;
	
	/** 传递数据TAG*/
	public static String TRAN_CODE_TAG = "tranCode";
	public static String TIME_RANGE_TAG = "timeRange";
	public static String START_TIME_TAG = "startTime";
	public static String END_TIME_TAG = "endTime";
	public static String ORDER_NO_TAG = "orderNo";
	
	/** 今天*/
	public static int TIME_RANGE_TODAY = 0;
	
	/** 传递过来的参数*/
	private int tranCode = -1;
	private int timeRange;
	private String startTime;
	private String endTime;
	private String orderNo;
	
	private Bundle bundle ;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setMainView(R.layout.activity_record);
		initData();
		setTitleText(Constants.TRAN_TYPE.get(String.valueOf(tranCode)));
		showTitleBack();
		initView();
	}
	 
	
	@Override
	protected void onResume() {
		super.onResume();
//		updateUI();
		LogEx.i(LOG_TAG, "onResume");
	}

	private void initData() {
		if(getIntent().hasExtra(TRAN_CODE_TAG))//服务器Code码
		{
			tranCode = getIntent().getIntExtra(TRAN_CODE_TAG, -1);
		}else
		{
			LogEx.w(LOG_TAG, "tranCode is null.");
		}
		if(getIntent().hasExtra(TIME_RANGE_TAG))
		{
			timeRange = getIntent().getIntExtra(TIME_RANGE_TAG, TIME_RANGE_TODAY);
		}
		if(getIntent().hasExtra(START_TIME_TAG))//开始时间
		{
			startTime = getIntent().getStringExtra(START_TIME_TAG);
		}
		if(getIntent().hasExtra(END_TIME_TAG))//结束时间
		{
			endTime = getIntent().getStringExtra(END_TIME_TAG);
		}
		if(getIntent().hasExtra(ORDER_NO_TAG))//单号
		{
			orderNo = getIntent().getStringExtra(ORDER_NO_TAG);
		}
		LogEx.i(LOG_TAG, "tranCode : " + tranCode + " timeRange: " + timeRange
				+ " startTime : " + startTime + " endTime :" + endTime + " orderNo :" + orderNo + ".");
	
		bundle = new Bundle();
		bundle.putInt(TRAN_CODE_TAG, tranCode);
		bundle.putInt(TIME_RANGE_TAG, timeRange);
		bundle.putString(START_TIME_TAG, startTime);
		bundle.putString(END_TIME_TAG, endTime);
		bundle.putString(ORDER_NO_TAG, orderNo);
		
	}

	private void initView() {
		recordFragment = new RecordFragment2();
		recordFragment.setArguments(bundle);
		getSupportFragmentManager().beginTransaction().add(R.id.rlMain, recordFragment).commit();
	}
	
	
	/**
	 * 
	 * @Author: Huangweicai
	 * @date 2015-9-11 下午4:36:34 
	 * @Description: 拿到数据后让Fragment请求并更新UI
	 */
	private void updateUI()
	{
		LogEx.i(LOG_TAG, "updateUI");
		if(tranCode == -1)
		{
			LogEx.w(LOG_TAG, "tranCode == -1.");
			return;
		}
		recordFragment.query(tranCode, timeRange, startTime, endTime, orderNo);
	}
	
}
