package com.wizarpos.pay.statistics.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.motionpay.pay2.lite.R;

/**
 * 收款明细记录适配器
 * @author wu
 *
 */
public class RecordDetialAdapter extends BaseAdapter{

	private Context context;
	
	public List<String[]> list = new ArrayList<String[]>();
	
	public RecordDetialAdapter(Context context) {
		this.context = context;
	}
	
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
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(R.layout.component_tran_data_detail_item, null);
			holder = new ViewHolder();
			holder.tvSerialNum = (TextView) convertView.findViewById(R.id.tv_serial_num);
			holder.tvTime = (TextView) convertView.findViewById(R.id.tv_time);
			holder.ivIcon = (ImageView) convertView.findViewById(R.id.iv_trans_type_icon);
			holder.tvAmount = (TextView) convertView.findViewById(R.id.tv_item_amount);
			
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		
		holder.tvSerialNum.setText(list.get(position)[0]);
		String transType = list.get(position)[1];
		holder.tvTime.setText(list.get(position)[2] + "   (" + transType + ")");
		holder.tvAmount.setText("￥" + list.get(position)[3]);
		if(transType.contains("撤销")){
			holder.ivIcon.setBackgroundResource(R.drawable.revoke);
		}else if (transType.contains("现金")) {
			holder.ivIcon.setBackgroundResource(R.drawable.type1);
		} else if (transType.contains("银行卡")) {
			holder.ivIcon.setBackgroundResource(R.drawable.type2);
		} else if (transType.contains("会员卡")) {
			holder.ivIcon.setBackgroundResource(R.drawable.type3);
		} else if (transType.contains("微信")) {
			holder.ivIcon.setBackgroundResource(R.drawable.type5);
		} else if (transType.contains("支付宝")) {
			holder.ivIcon.setBackgroundResource(R.drawable.type4);
		} else if (transType.contains("QQ钱包")) {
			holder.ivIcon.setBackgroundResource(R.drawable.qq);
		} else if (transType.contains("百度支付")) {
			holder.ivIcon.setBackgroundResource(R.drawable.ic_baidu);
		} else if (transType.contains("组合支付")||transType.contains("混合支付")) {
			holder.ivIcon.setBackgroundResource(R.drawable.icon_merge);
		} else if (transType.contains("其它支付")) {
			holder.ivIcon.setBackgroundResource(R.drawable.payother);
		} else if (transType.contains("离线现金")) {
			holder.ivIcon.setBackgroundResource(R.drawable.circle_delete);
		} else {
			holder.ivIcon.setBackgroundResource(R.drawable.payother);
		}

		
		return convertView;
	}
	
	private class ViewHolder {
		TextView tvSerialNum, tvTime, tvAmount;
		ImageView ivIcon;
	}
	
	public void setDataChanged(List<String[]> list) {
		if(list == null){
			this.list.clear();
		}else{
			this.list = list;
		}
		this.notifyDataSetChanged();
	}
	
	public void addDataChanged(List<String[]> list){
		if(list == null){
			this.list.clear();
		}else{
			this.list.addAll(list);
		}
		this.notifyDataSetChanged();
	}

	public void clear(){
		this.list.clear();
		this.notifyDataSetChanged();
	}

}
