package com.wizarpos.pay.statistics.adapter;

import java.util.ArrayList;
import java.util.List;

import com.wizarpos.pay2.lite.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class MixDetialAdapter extends BaseAdapter{

	private Context context;
	
	private List<String[]> mixTrans = new ArrayList<String[]>();
	
	public MixDetialAdapter(Context context) {
		this.context = context;
	}
	
	@Override
	public int getCount() {
		return mixTrans.size();
	}

	@Override
	public Object getItem(int position) {
		return mixTrans.get(position);
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	ViewHolder holder;
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if(convertView == null){
			holder = new ViewHolder();
			convertView = LayoutInflater.from(context).inflate(R.layout.item_mix_detial, null);
			holder.tvTranName = (TextView) convertView.findViewById(R.id.tvTranName);
			holder.tvTranTime = (TextView) convertView.findViewById(R.id.tvTranTime);
			holder.tvTranAmont = (TextView) convertView.findViewById(R.id.tvTranAmount);
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder) convertView.getTag();
		}
		holder.tvTranName.setText(mixTrans.get(position)[0]);
		holder.tvTranTime.setText(mixTrans.get(position)[1]);
		holder.tvTranAmont.setText("￥" + mixTrans.get(position)[2]);
		return convertView;
	}

	class ViewHolder{
		TextView tvTranName, tvTranTime, tvTranAmont;
	}
	
	public void setDataChanged( List<String[]> mixTrans){
		if(mixTrans == null){
			this.mixTrans.clear();
		}else{
			this.mixTrans = mixTrans; 
		}
		this.notifyDataSetChanged();
	}
}
