package com.wizarpos.pay.view.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.wizarpos.pay2.lite.R;

public class TransDataAdapter extends BaseAdapter {

	private Context context;
	
	public TransDataAdapter(Context context){
		this.context = context;
	}
	
	private List<String[]> list = new ArrayList<String[]>();

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		RelativeLayout rlayout = (RelativeLayout) LayoutInflater.from(context).inflate(R.layout.component_tran_data_item, null);
		TextView tvTransType = (TextView) rlayout.findViewById(R.id.tv_trans_type);
		ImageView ivIcon = (ImageView) rlayout.findViewById(R.id.iv_trans_type_icon);
		TextView tvAmount = (TextView) rlayout.findViewById(R.id.tv_item_amount);
		TextView amount = (TextView) rlayout.findViewById(R.id.tv_total_amount);
		tvTransType.setText(list.get(position)[0]);
		String transType = list.get(position)[0];
		amount.setText(list.get(position)[1]);
		tvAmount.setText("￥" + list.get(position)[2]);
		if (transType.equals("现金消费")) {
			ivIcon.setBackgroundResource(R.drawable.type1);
		} else if (transType.equals("银行卡消费")) {
			ivIcon.setBackgroundResource(R.drawable.type2);
		} else if (transType.equals("会员卡消费")) {
			ivIcon.setBackgroundResource(R.drawable.type3);
		} else if (transType.equals("微信消费")) {
			ivIcon.setBackgroundResource(R.drawable.type5);
		} else if (transType.equals("支付宝消费")) {
			ivIcon.setBackgroundResource(R.drawable.type4);
		} else if (transType.contains("QQ钱包支付")) {
			ivIcon.setBackgroundResource(R.drawable.qq);
		} else if (transType.equals("百度支付支付")) {
			ivIcon.setBackgroundResource(R.drawable.ic_baidu);
		} else if (transType.equals("离线现金消费")) {
			ivIcon.setBackgroundResource(R.drawable.circle_delete);
		} else if (transType.contains("撤销")) {
			ivIcon.setBackgroundResource(R.drawable.manage_jycx);
		} else {
			ivIcon.setBackgroundResource(R.drawable.payother);
		}
		return rlayout;
	}

	public void setDataChanged(List<String[]> list){
		this.list = list;
		this.notifyDataSetChanged();
	}
	
}
