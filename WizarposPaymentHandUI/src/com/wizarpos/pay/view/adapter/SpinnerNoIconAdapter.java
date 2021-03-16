package com.wizarpos.pay.view.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wizarpos.pay.view.ShowSpinnerData;
import com.motionpay.pay2.lite.R;

public class SpinnerNoIconAdapter extends BaseAdapter {

	public List<ShowSpinnerData> transTypes;
	private Context mContext;

	public SpinnerNoIconAdapter(Context mContext) {
		this.mContext = mContext;
	}

	@Override
	public int getCount() {
		if (transTypes == null) { return 0; }
		return transTypes.size();
	}

	@Override
	public Object getItem(int position) {
		return transTypes.get(position).getRealValue();
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		LinearLayout rlayout = (LinearLayout) LayoutInflater.from(mContext).inflate(R.layout.item_without_icon, null);
		for (int i = 0; i < transTypes.size(); i++) {
			TextView textView = (TextView) rlayout.findViewById(R.id.textView);
			textView.setText(transTypes.get(position).getShowValue());
		}
		return rlayout;
	}

	public void setTransTypes(List<ShowSpinnerData> transTypes) {
		this.transTypes = transTypes;
		this.notifyDataSetChanged();
	}
}
