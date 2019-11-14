package com.wizarpos.pay.cashier.fragment;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.wizarpos.base.net.Response;
import com.wizarpos.pay.cashier.bean.BaseListDataBean;
import com.wizarpos.pay.cashier.bean.HomeNavBean;
import com.wizarpos.pay.common.base.BasePresenter.ResultListener;
import com.wizarpos.pay.common.view.RunningTextView;
import com.wizarpos.pay.statistics.model.GroupQueryResp;
import com.wizarpos.pay.statistics.presenter.StatisticsPresenter;
import com.wizarpos.pay2.lite.R;

/** 
 * @Author:Huangweicai
 * @Date:2015-7-29 上午11:24:40
 * @Reason:金额统计fragment
 */
public class HomeNavAmountFragment extends BaseNavFragment{
	private RunningTextView tvValue;
	private TextView tvHomeTile;
	private StatisticsPresenter presenter;
	/** 数据对象*/
	private HomeNavBean mNavBean = new HomeNavBean();
	/** 昨日数据*/
	private BaseListDataBean yesterDayData = new BaseListDataBean();
	/** 今日数据*/
	private BaseListDataBean todayDayData = new BaseListDataBean();
	/** 本月（累计）数据*/
	private BaseListDataBean totalDayData = new BaseListDataBean();
	private List<BaseListDataBean> dataList = new ArrayList<BaseListDataBean>();
	private final int TODAY_TAG = 0;
	private final int YESTERDAY_TAG = 1;
	private final int TOTAL_TAG = 4;
	
	private OnUpdateUiListener listener;
	
	public HomeNavAmountFragment() {
	}
	
	@Override
	public void initView() {
		View mainView = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_home_nav, null);
		setMainView(mainView);
		tvValue = (RunningTextView) mainView.findViewById(R.id.tvHomeValue);
		tvHomeTile = (TextView) mainView.findViewById(R.id.tvHomeTile);
		tvHomeTile.setText("本月收款金额");
//        dashedCircularProgress = (DashedCircularProgress) mainView.findViewById(
//                R.id.holoCircularProgressBar);
        presenter = new StatisticsPresenter(getActivity());
        todayDayData.setTitle("今日发券");
        todayDayData.setDrableSouce(R.drawable.type1);
        yesterDayData.setTitle("昨日发券");
        yesterDayData.setDrableSouce(R.drawable.type1);
        totalDayData.setTitle("累计发券");
        totalDayData.setDrableSouce(R.drawable.type1);
        
        dataList.add(todayDayData);
        dataList.add(yesterDayData);
        dataList.add(totalDayData);
        doQuery(TOTAL_TAG);
        doQuery(TODAY_TAG);
        doQuery(YESTERDAY_TAG);
	}
    
    public void updateUI()
    {
		tvValue.setFormat("00.00");
		setNumText(totalDayData.getIntValue());
//		dashedCircularProgress.reset();
//		dashedCircularProgress.setValue(999);
		
    }
    
    /**
     * @Reason:查询数据
     */
    private void doQuery(final int timeRange) {
		progresser.showProgress();
		presenter.transactionGroupQuery(-1, timeRange, null, null, new ResultListener() {
			@Override
			public void onSuccess(Response response) {
				progresser.showContent();
				GroupQueryResp result = (GroupQueryResp) response.result;
				dealResponse(result,timeRange);
			}

			@Override
			public void onFaild(Response response) {
				progresser.showError(false);
//				monthTotalFragment.getAmount(String.valueOf(amountMonth));
			}
		});
	}
    
    private void setNumText(String num)
    {
    	
    }
    private void setNumText(double num)
    {
    	tvValue.setFormat("00.00");
		tvValue.playNumber(num);
    }
    
    private void dealResponse(GroupQueryResp result,int timeRange)
    {
    	List<String[]> recordListMonth = result.getRecordListShow();
		double amountMonth = 0.00d;
		for (int i = 0; i < recordListMonth.size(); i++) {
			amountMonth = getBigDecimalDebit(recordListMonth.get(i)[2], amountMonth);
		}
		switch (timeRange) {
		case TODAY_TAG:
			todayDayData.setIntValue(amountMonth);
			todayDayData.setValue(String.valueOf(amountMonth));
			if(listener != null)
			{
				listener.onUpdate(dataList);
			}
			break;
		case YESTERDAY_TAG:
			yesterDayData.setIntValue(amountMonth);
			yesterDayData.setValue(String.valueOf(amountMonth));
			if(listener != null)
			{
				listener.onUpdate(dataList);
			}
			break;
		case TOTAL_TAG:
			totalDayData.setIntValue(amountMonth);
			DecimalFormat format = new DecimalFormat("#.##");
			totalDayData.setValue(String.valueOf(format.format(amountMonth)));
			if(listener != null)
			{
				listener.onUpdate(dataList);
			}
			setNumText(amountMonth);
			break;
		}
    }
    
    // TAG: 封装!Huangweicai
    private double getBigDecimalDebit(String string, double debit2) {
		BigDecimal decimal1 = new BigDecimal(string);
		BigDecimal decimal2 = new BigDecimal(String.valueOf(debit2));
		return decimal1.add(decimal2).doubleValue();
	}
    
    public interface OnUpdateUiListener
	{
		void onUpdate(List<BaseListDataBean> dataList);
	}
    
    public void setListener(OnUpdateUiListener listener)
    {
    	this.listener = listener;
    }
    
}
