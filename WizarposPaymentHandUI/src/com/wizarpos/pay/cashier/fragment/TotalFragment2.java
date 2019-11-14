package com.wizarpos.pay.cashier.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import com.wizarpos.pay2.lite.R;

public class TotalFragment2 extends Fragment {
	private TextView todayTotal;
	private TextView totalTimeRange;
	private String amount = "--";// 汇总
	public static final String TITILE = "title";
	public static final String TRANS_AMOUNT = "TRANS_AMOUNT";
	private String title;

	public interface OnTotalFragmentClickedListener {

		void onTotalFragmentClicked(String currentTitle);

	}

	private OnTotalFragmentClickedListener onTotalFragmentClickedListener;

	public void setOnTotalFragmentClickedListener(OnTotalFragmentClickedListener listener) {
		this.onTotalFragmentClickedListener = listener;
	}

	public TotalFragment2() {
	}
	
	public static TotalFragment2 newInstance(String title) {
		TotalFragment2 fragment = new TotalFragment2();
		Bundle args = new Bundle();
		args.putString(TITILE, title);
		fragment.setArguments(args);
		return fragment;
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		title = getArguments().getString(TITILE);
	}

	public void updateAmount(String amount) {
		this.amount = amount;
		try {
			if (todayTotal != null) {
				todayTotal.setText(String.valueOf(amount));
			}
		} catch (Exception e) {}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.today_amount, container, false);
		totalTimeRange = (TextView) view.findViewById(R.id.title_time_range);
		todayTotal = (TextView) view.findViewById(R.id.tv_today_tatal_amount);
		totalTimeRange.setText(title);
		todayTotal.setText(String.valueOf(amount));
		todayTotal.setOnClickListener(new OnClickListener() { // 增加点击回调

					@Override
					public void onClick(View v) {
						if (onTotalFragmentClickedListener == null) { return; }
						onTotalFragmentClickedListener.onTotalFragmentClicked(title);
					}
				});
		return view;
	}

}
