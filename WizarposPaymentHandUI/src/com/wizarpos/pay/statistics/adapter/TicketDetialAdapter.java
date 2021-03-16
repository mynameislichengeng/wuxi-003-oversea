package com.wizarpos.pay.statistics.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.wizarpos.pay.common.utils.Calculater;
import com.wizarpos.pay.statistics.model.TicketTranLogResp;
import com.motionpay.pay2.lite.R;

/**
 * 券明细记录适配器
 * 
 * @author wu
 * 
 */
public class TicketDetialAdapter extends BaseAdapter {

	private Context context;

	public List<TicketTranLogResp> list = new ArrayList<TicketTranLogResp>();

	public TicketDetialAdapter(Context context) {
		this.context = context;
	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public TicketTranLogResp getItem(int position) {
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			convertView = (RelativeLayout) LayoutInflater.from(context).inflate(R.layout.component_tran_data_detail_item, null);
			holder = new ViewHolder();
			holder.tvTicketName = (TextView) convertView.findViewById(R.id.tvTicketName);
			holder.tvTicketAmount = (TextView) convertView.findViewById(R.id.tvTicketAmount);
			holder.tvTranTime = (TextView) convertView.findViewById(R.id.tvTranTime);
			holder.tvTicketNo = (TextView) convertView.findViewById(R.id.tvTicketNo);

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		
		try {
			holder.tvTicketName.setText(list.get(position).getTicketName());
			holder.tvTicketNo.setText(list.get(position).getTicketNo());
			holder.tvTranTime.setText(list.get(position).getTranTime());
			holder.tvTicketAmount.setText(Calculater.divide100(list.get(position).getTranAmount()));
		} catch (Exception e) {
			e.printStackTrace();
		}

		return convertView;
	}

	private class ViewHolder {
		TextView tvTicketName, tvTicketAmount, tvTranTime, tvTicketNo;
	}

	public void setDataChanged(List<TicketTranLogResp> list) {
		this.list = list;
		this.notifyDataSetChanged();
	}

	public void addDataChanged(List<TicketTranLogResp> list) {
		this.list.addAll(list);
		this.notifyDataSetChanged();
	}

	public void clear() {
		this.list.clear();
		this.notifyDataSetChanged();
	}

}
