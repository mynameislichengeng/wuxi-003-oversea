package com.wizarpos.pay.statistics.fragment;

import java.util.List;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.wizarpos.base.net.Response;
import com.wizarpos.pay.common.Constants;
import com.wizarpos.pay.common.base.BasePresenter.ResultListener;
import com.wizarpos.pay.statistics.model.GroupQueryResp;
import com.wizarpos.pay.statistics.presenter.StatisticsPresenter;
import com.wizarpos.pay.ui.ProgressLayout;
import com.wizarpos.pay.view.adapter.TransDataAdapter;
import com.wizarpos.pay.view.swipyrefresh.SwipyRefreshLayout;
import com.wizarpos.pay.view.swipyrefresh.SwipyRefreshLayout.OnRefreshListener;
import com.wizarpos.pay.view.swipyrefresh.SwipyRefreshLayoutDirection;
import com.wizarpos.pay2.lite.R;

/**
 * 收款汇总
 * @author wu
 *
 */
public class SummarizingFragment extends Fragment implements OnRefreshListener {
	private TextView totalTitle;
	private int timeRange = 0;
	private StatisticsPresenter presenter;
	private ListView listView;
	private TransDataAdapter transDataAdapter;
	private List<String[]> recordListShow;
	private ProgressLayout progress;
	private int tranCode = -1;
	private String startTime = "", endTime = "";
	private int transType = 0;
	
	private SwipyRefreshLayout mSwipyRefreshLayout;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.summarizing_fragment, container, false);
		totalTitle = (TextView) view.findViewById(R.id.pager_title);
		
		mSwipyRefreshLayout = (SwipyRefreshLayout) view.findViewById(R.id.swipyrefreshlayout);
		mSwipyRefreshLayout.setOnRefreshListener(this);

		progress = (ProgressLayout) view.findViewById(R.id.progress);
		totalTitle.setText(getResources().getString(R.string.today_total));
		presenter=new StatisticsPresenter(getActivity());
		listView = (ListView) view.findViewById(R.id.lv_listRecode);
		transDataAdapter = new TransDataAdapter(getActivity());
		listView.setAdapter(transDataAdapter);
//		startRefresh();
		progress.showProgress();
		setData();
		return view;
	}

	public void getDataInfo(List<String> list) {
		timeRange = Integer.valueOf(list.get(1));
		tranCode = Integer.valueOf(list.get(0));
		timeRange = Integer.valueOf(list.get(1));
		startTime = list.get(3);
		endTime = list.get(4);
		 if (timeRange == 0) { //简化代码
		 totalTitle.setText(getResources().getString(R.string.today_total));
		 } else if (timeRange == 1) {
		 totalTitle.setText(getResources().getString(R.string.yestoday_total));
		 } else if (timeRange == 2) {
		 totalTitle.setText(getResources().getString(R.string.this_week_total));
		 } else if (timeRange == 3) {
		 totalTitle.setText(getResources().getString(R.string.before_week_total));
		 } else if (timeRange == 4) {
		 totalTitle.setText(getResources().getString(R.string.this_month_total));
		 } else if (timeRange == 5) {
		 totalTitle.setText(getResources().getString(R.string.before_month_total));
		 } else if (timeRange == 6) {
		 totalTitle.setText(startTime+"--"+endTime);
		 }
		 query(tranCode,timeRange,startTime,endTime);
	}

	private void setData() {
//		progress.showProgress();
		presenter.transactionGroupQuery(-1, transType, "", "", new ResultListener() {
			@Override
			public void onSuccess(Response response) {
				progress.showContent();
				GroupQueryResp result = (GroupQueryResp) response.result;
				if(Constants.APP_VERSION_NAME == Constants.APP_VERSION_LAWSON)
				{
					recordListShow = result.getLAWSONList();
				}else
				{
					recordListShow = result.getRecordListShow();
				}
				transDataAdapter.setDataChanged(recordListShow);
				stopRefresh();
				if(recordListShow == null || recordListShow.isEmpty()){
					showErrorView("暂无数据");
				}
			}

			@Override
			public void onFaild(Response response) {
				progress.showContent();
				stopRefresh();
				showErrorView(response.msg);
			}
		});
	}

	private void query(int tranCode,int timeRange,String startTime,String endTime ) {
		progress.showProgress();
		presenter.transactionGroupQuery(tranCode, timeRange, startTime, endTime, new ResultListener() {
			@Override
			public void onSuccess(Response response) {
				progress.showContent();
				GroupQueryResp result = (GroupQueryResp) response.result;
				if(Constants.APP_VERSION_NAME == Constants.APP_VERSION_LAWSON)
				{
					recordListShow = result.getLAWSONList();
				}else
				{
					recordListShow = result.getRecordListShow();
				}
				transDataAdapter.setDataChanged(recordListShow);
				if(recordListShow == null || recordListShow.isEmpty()){
					showErrorView("暂无数据");
				}
			}
			@Override
			public void onFaild(Response response) {
				progress.showContent();
				showErrorView(response.msg);
			}
		});
	}
	
	private void showErrorView(String msg) {
		if (TextUtils.isEmpty(msg) == false) {
			progress.showError(msg, false);
		} else {
			progress.showError("未知异常", false);
		}
	}

	@Override
	public void onRefresh(SwipyRefreshLayoutDirection direction) {
		if (SwipyRefreshLayoutDirection.TOP == direction) {
			setData();
		}
	}
	
	/**
	 * 开始加载
	 */
	private void startRefresh() {
		Log.d("recordfragment", "开始加载");
		mSwipyRefreshLayout.setRefreshing(true);
	}
	
	/**
	 * 停止加载
	 */
	private void stopRefresh() {
		mSwipyRefreshLayout.setRefreshing(false);
		progress.showContent();
	}
}
