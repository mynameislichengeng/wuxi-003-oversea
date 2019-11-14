package com.wizarpos.pay.view.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wizarpos.pay.cashier.model.TicketDef;
import com.wizarpos.pay.cashier.model.TicketInfo;
import com.wizarpos.pay.common.utils.Calculater;
import com.wizarpos.pay2.lite.R;

/**
 * 微盟券
 * 
 * @author wu
 * 
 */
public class WememgTicketPublishAdapter extends BaseAdapter {

	private Context context;

	private List<TicketInfo> ticketInfos = new ArrayList<TicketInfo>();

	public WememgTicketPublishAdapter(Context context) {
		this.context = context;
	}

	@Override
	public int getCount() {
		return ticketInfos.size();
	}

	@Override
	public Object getItem(int position) {
		return ticketInfos.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	private ViewHolder holder;

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = LayoutInflater.from(context).inflate(R.layout.component_publish_ticket_item, parent, false);
			holder.addBtn = (Button) convertView.findViewById(R.id.pti_btn_add);
			holder.minusBtn = (Button) convertView.findViewById(R.id.pti_btn_minus);
			holder.tvTicketName = (TextView) convertView.findViewById(R.id.pti_tv_ticket_name);
			holder.tvEndDate = (TextView) convertView.findViewById(R.id.tv_publish_date);
			holder.tvEndDate = (TextView) convertView.findViewById(R.id.tv_publish_date);
			holder.tvTicekCount = (TextView) convertView.findViewById(R.id.pti_tv_ticket_count);
			holder.llAmount = (LinearLayout) convertView.findViewById(R.id.llCen);
			holder.vBack = convertView.findViewById(R.id.ticket_choose_rl);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		final TicketInfo ticketInfo = ticketInfos.get(position);
		TicketDef ticketDef = ticketInfo.getTicketDef();
		holder.tvTicketName.setText(ticketDef.getTicketName());
		holder.llAmount.setVisibility(View.GONE);
		holder.vBack.setBackgroundResource(R.drawable.quan4);;
		String endDate;
		if(TextUtils.isEmpty(ticketInfo.getEndDate())||"-1".equals(ticketInfo.getEndDate())){
			endDate = "永久有效";
		}else{
			endDate = ticketInfo.getEndDate();
		}
		holder.tvEndDate.setText(endDate);
		holder.tvTicekCount.setText(ticketInfo.getSelectedCount()+"");

		holder.minusBtn.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {
				ticketInfo.setSelectedCount(0);
				holder.tvTicekCount.setText("0");
			}
		});
		holder.addBtn.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {
				ticketInfo.setSelectedCount(1);
				holder.tvTicekCount.setText("1");
			}
		});
		return convertView;
	}

	private class ViewHolder {
		Button minusBtn, addBtn;
		TextView tvTicketName, tvEndDate, tvTicekCount;
		View vBack;
		LinearLayout llAmount;
	}

	public void setDataChanged(List<TicketInfo> ticketInfos) {
		this.ticketInfos = ticketInfos;
		this.notifyDataSetChanged();
	}

	public List<TicketInfo> getTicketInfos() {
		return ticketInfos;
	}
}
