package com.wizarpos.pay.view.adapter;

import java.util.ArrayList;
import java.util.List;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.wizarpos.atool.tool.Tools;
import com.wizarpos.pay.db.CashPayRepair;
import com.motionpay.pay2.lite.R;

public class OfflineTransDataAdapter extends BaseAdapter {

	public LayoutInflater inflater = null;
	public List<CashPayRepair> list = new ArrayList<CashPayRepair>();

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

		RelativeLayout rlayout = (RelativeLayout) inflater.inflate(R.layout.component_tran_offline_data_item, null);
		TextView tvSerialNum = (TextView) rlayout.findViewById(R.id.tv_serial_num_offline);
		TextView tvTime = (TextView) rlayout.findViewById(R.id.tv_time);
		TextView tvAmount = (TextView) rlayout.findViewById(R.id.tv_item_amount);
		String msg = list.get(position).getMsg();
		com.alibaba.fastjson.JSONObject jsonObject = JSON.parseObject(msg);
		tvSerialNum.setText(jsonObject.get("offlineTranLogId").toString());
		tvTime.setText(jsonObject.get("tranTime").toString());
		tvAmount.setText("￥" + Tools.formatFen(Long.parseLong(jsonObject.get("amount").toString())));
		return rlayout;
	}

}
