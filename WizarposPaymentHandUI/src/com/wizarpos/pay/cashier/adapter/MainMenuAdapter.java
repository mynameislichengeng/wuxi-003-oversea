package com.wizarpos.pay.cashier.adapter;

import java.util.ArrayList;
import java.util.List;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.motionpay.pay2.lite.R;

public class MainMenuAdapter extends BaseAdapter {

	
	public MainMenuAdapter(LayoutInflater inflater) {
		this.inflater = inflater;
	}
	
	public LayoutInflater inflater = null;
	public List<String[]> list = new ArrayList<String[]>();

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
		RelativeLayout rlayout = (RelativeLayout) inflater.inflate(R.layout.component_mainmenu_data_item, null);
		ImageView tvIcon = (ImageView) rlayout.findViewById(R.id.iv_type_icon);
		TextView tvTranType = (TextView) rlayout.findViewById(R.id.tv_trans_type);
		TextView tvAmount = (TextView) rlayout.findViewById(R.id.tv_cashier_amount);
		String transType = list.get(position)[0];
		if (transType.equals("现金消费")) {
			tvIcon.setBackgroundResource(R.drawable.type1);
		} else if (transType.equals("银行卡消费")) {
			tvIcon.setBackgroundResource(R.drawable.type2);
		} else if (transType.equals("会员卡消费")) {
			tvIcon.setBackgroundResource(R.drawable.type3);
		} else if (transType.equals("微信消费")) {
			tvIcon.setBackgroundResource(R.drawable.type5);
		} else if (transType.equals("支付宝消费")) {
			tvIcon.setBackgroundResource(R.drawable.type4);
		} else if (transType.equals("QQ钱包支付")) {
			tvIcon.setBackgroundResource(R.drawable.qq);
		} else if (transType.equals("撤销")) {
			tvIcon.setBackgroundResource(R.drawable.revoke);
		} else if (transType.equals("百度支付")) {
			tvIcon.setBackgroundResource(R.drawable.ic_baidu);
		} else {
			tvIcon.setBackgroundResource(R.drawable.payother);
		}
		tvTranType.setText(list.get(position)[0]);
		tvAmount.setText(list.get(position)[2] + "元");
		return rlayout;
	}
	
	public void setDataChanged(List<String[]> list){
		this.list = list;
		this.notifyDataSetChanged();
	}

}
