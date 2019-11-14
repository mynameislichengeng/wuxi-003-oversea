package com.wizarpos.pay.statistics.fragment;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.wizarpos.base.net.Response;
import com.wizarpos.pay.common.base.BasePresenter.ResultListener;
import com.wizarpos.pay.statistics.model.GroupQueryResp;
import com.wizarpos.pay.statistics.presenter.StatisticsPresenter;
import com.wizarpos.pay.ui.ProgressLayout;
import com.wizarpos.pay.view.adapter.TransDataAdapter;
import com.wizarpos.pay2.lite.R;

/**
 * 交易明细fragment
 */
public class RecordDetailFragment extends Fragment {
	private StatisticsPresenter presenter;
	private ListView listView;
	private TransDataAdapter transDataAdapter;
	private List<String[]> recordListShow;
	private TextView tvAccount, tvAmount;
	private LinearLayout noData;
	protected ProgressLayout progress;
	private int tranCode = -1, timeRange = 0;
	private String startTime = "", endTime = "";
	private int transType = 0;

	public static final String TRANS_TYPE = "TRANS_TYPE";

	public static final int TRANS_TYPE_TODAY = 0;// 今天
	public static final int TRANS_TYPE_YESTERDAY = 1;// 昨天
	public static final int TRANS_TYPE_THIS_WORK = 2;// 本周
	public static final int TRANS_TYPE_BEFORE_WORK = 3;// 上周
	public static final int TRANS_TYPE_THIS_MONTH = 4;// 本月
	public static final int TRANS_TYPE_BEFORE_MONTH = 5;// 上月
	public static final int TRANS_TYPE_TIME_RANGE = 6;// 指定范围

	public static RecordDetailFragment newInstance(int transType) {
		RecordDetailFragment fragment = new RecordDetailFragment();
		Bundle args = new Bundle();
		args.putInt(TRANS_TYPE, transType);
		fragment.setArguments(args);
		return fragment;
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		transType = getArguments().getInt(TRANS_TYPE);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.total_list, null);
		tvAccount = (TextView) view.findViewById(R.id.tv_account);
		tvAmount = (TextView) view.findViewById(R.id.tv_amount);
		noData = (LinearLayout) view.findViewById(R.id.ll_null);
//		presenter=new StatisticsPresenter(getActivity());
		progress = (ProgressLayout) view.findViewById(R.id.progress);
		listView = (ListView) view.findViewById(R.id.lv_listRecode);
		transDataAdapter = new TransDataAdapter(getActivity());
		listView.setAdapter(transDataAdapter);
		groupQuery();
		return view;
	}

	@Override
	public void onStart() {
		super.onStart();
	}
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		presenter = new StatisticsPresenter(getActivity());
	}
	private void groupQuery() {
		progress.showProgress();
		presenter.transactionGroupQuery(-1, transType, "", "", new ResultListener() {

			@Override
			public void onSuccess(Response response) {
				progress.showContent();
				GroupQueryResp result = (GroupQueryResp) response.result;
				recordListShow = result.getRecordListShow();
				double account = 0.00, amount = 0.00;
				for (int i = 0; i < recordListShow.size(); i++) {
					account = getBigDecimalDebit(recordListShow.get(i)[1], account);
					amount = getBigDecimalDebit(recordListShow.get(i)[2], amount);
				}
				int count = (int) account;
				tvAccount.setText(String.valueOf(count));
				DecimalFormat format = new DecimalFormat("#.##");
				tvAmount.setText(String.valueOf(format.format(amount)));
				transDataAdapter.setDataChanged(recordListShow);
				noData.setVisibility(View.GONE);
			}

			@Override
			public void onFaild(Response response) {
				// UIHelper.ToastMessage(getActivity(), "没有交易记录");
				progress.showContent();
				tvAccount.setText("0");
				tvAmount.setText("0.00");
				noData.setVisibility(View.VISIBLE);
			}
		});

	}

	private double getBigDecimalDebit(String string, double string2) {
		BigDecimal decimal1 = new BigDecimal(string);
		BigDecimal decimal2 = new BigDecimal(string2);
		return decimal1.add(decimal2).doubleValue();
	}

	public void getDataInfo(List<String> list) {
		
		tranCode = Integer.valueOf(list.get(0));
		timeRange = Integer.valueOf(list.get(1));
		startTime = list.get(3);
		endTime = list.get(4);
		presenter=new StatisticsPresenter(getActivity());
		presenter.transactionGroupQuery(tranCode, timeRange, startTime, endTime, new ResultListener() {

			@Override
			public void onSuccess(Response response) {
//				progress.showContent();
				GroupQueryResp result = (GroupQueryResp) response.result;
				recordListShow = result.getRecordListShow();
				double account = 0.00, amount = 0.00;
				for (int i = 0; i < recordListShow.size(); i++) {
					account = getBigDecimalDebit(recordListShow.get(i)[1], account);
					amount = getBigDecimalDebit(recordListShow.get(i)[2], amount);
				}
				int count = (int) account;
				tvAccount.setText(String.valueOf(count));
				DecimalFormat format = new DecimalFormat("#.##");
				tvAmount.setText(String.valueOf(format.format(amount)));
				transDataAdapter.setDataChanged(recordListShow);
				noData.setVisibility(View.GONE);
			}

			@Override
			public void onFaild(Response response) {
				// UIHelper.ToastMessage(getActivity(), "没有交易记录");
//				progress.showContent();
				tvAccount.setText("0");
				tvAmount.setText("0.00");
				noData.setVisibility(View.VISIBLE);
			}
		});
	}

}
