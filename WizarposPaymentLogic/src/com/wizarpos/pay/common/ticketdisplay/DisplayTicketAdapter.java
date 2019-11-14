package com.wizarpos.pay.common.ticketdisplay;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.wizarpos.wizarpospaymentlogic.R;

public class DisplayTicketAdapter extends BaseAdapter {

	private List<DisplayTicektBean> displayTickets = new ArrayList<DisplayTicektBean>();

	private Context context;

	public DisplayTicketAdapter(Context context) {
		this.context = context;
	}

	@Override
	public int getCount() {
		return displayTickets.size();
	}

	@Override
	public Object getItem(int position) {
		return displayTickets.get(position);
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	ViewHolder holder;

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = LayoutInflater.from(context).inflate(R.layout.itemview_display_tickets, null);
			holder.tvTitle = (TextView) convertView.findViewById(R.id.tvTitle);
			holder.tvSubTitle = (TextView) convertView.findViewById(R.id.tvSubTitle);
			holder.ivTicket = (ImageView) convertView.findViewById(R.id.ivTicket);
			holder.tvEndString = (TextView) convertView.findViewById(R.id.tvEndString);
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder) convertView.getTag();
		}
		
		holder.tvTitle.setText(displayTickets.get(position).getTitle());
		holder.tvSubTitle.setText(displayTickets.get(position).getSubTitle());
		ImageLoader.getInstance().displayImage("file://"+displayTickets.get(position).getBitmapPath(), holder.ivTicket);
		holder.tvEndString.setText(displayTickets.get(position).getEndString());
		
		return convertView;
	}

	class ViewHolder {
		TextView tvTitle;
		TextView tvSubTitle;
		ImageView ivTicket;
		TextView tvEndString;
	}
	
	public void setDataChanged(List<DisplayTicektBean> tickets){
		this.displayTickets = tickets;
		this.notifyDataSetChanged();
	}

}
