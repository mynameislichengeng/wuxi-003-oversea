package com.wizarpos.pay.view.adapter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.wizarpos.atool.tool.DateUtil;
import com.wizarpos.atool.tool.Tools;
import com.wizarpos.pay.cashier.model.TicketPacketInfo;
import com.motionpay.pay2.lite.R;

/**
 * @Author:Huangweicai
 * @Date:2015-8-5 下午8:20:20
 * @Reason:这里用一句话说明
 */
public class TicketPacketAdapter extends BaseAdapter {

	private List<TicketPacketInfo> ticketList = new ArrayList<TicketPacketInfo>();
	private Context mContext;

	public TicketPacketAdapter(Context context, List<TicketPacketInfo> ticketList) {
		mContext = context;
		this.ticketList = ticketList;
	}

	@Override
	public int getCount() {
		return ticketList.size();
	}

	@Override
	public Object getItem(int position) {
		return null;
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@SuppressWarnings("deprecation")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		TicketPacketInfo packetInfo = ticketList.get(position);
		ViewHolder mHolder;
		if (convertView == null) {
			convertView = LayoutInflater.from(mContext).inflate(R.layout.item_ticket_packet, null);
			mHolder = new ViewHolder();
			mHolder.tvTicketName = (TextView) convertView.findViewById(R.id.tvTicketName);
			mHolder.tvTicketNum = (TextView) convertView.findViewById(R.id.tvTicketNum);
			mHolder.tvDate = (TextView) convertView.findViewById(R.id.tv_date);
			convertView.setTag(mHolder);
		} else {
			mHolder = (ViewHolder) convertView.getTag();
		}
		if (packetInfo == null || packetInfo.getTicketName() == null) { return convertView; }
		mHolder.tvTicketName.setText(packetInfo.getTicketName() + "\n￥" + Tools.formatFen(packetInfo.getBalance()));
		mHolder.tvTicketNum.setText(packetInfo.getTicketNums() + "");
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			Calendar date = Calendar.getInstance();
			date.setTime(sdf.parse(packetInfo.getCreateTime()));
			date.set(Calendar.DATE, date.get(Calendar.DATE) + packetInfo.getValidPeriod());
			Date endDate = sdf.parse(sdf.format(date.getTime()));
			if (packetInfo.getValidPeriod() == -1) {
				mHolder.tvDate.setText("永久有效");
			} else {
				mHolder.tvDate.setText(DateUtil.format(endDate, DateUtil.P3));
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}

		return convertView;
	}

	class ViewHolder {
		TextView tvTicketName, tvTicketNum, tvDate;
	}

}
