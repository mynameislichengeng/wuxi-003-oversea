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
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.wizarpos.base.net.Response;
import com.wizarpos.atool.tool.DateUtil;
import com.wizarpos.pay.cashier.model.TicketDef;
import com.wizarpos.pay.cashier.model.TicketInfo;
import com.wizarpos.pay.cashier.presenter.ticket.impl.TicketManagerImpl;
import com.wizarpos.pay.cashier.presenter.ticket.inf.TicketManager;
import com.wizarpos.pay.common.base.BasePresenter.ResultListener;
import com.wizarpos.pay.common.utils.Calculater;
import com.motionpay.pay2.lite.R;

public class TickeUsedAdapter extends BaseAdapter {
	private Context context;

	private TicketManager ticketManager = null;
	/** 交易金额*/
	private String initAmount = "0";
	/** 应付金额*/
	private String shouldPayAmount = "0";
	/** 扣减金额*/
	private String reduceAmount = "0";
	
	/** 添加券返回结果start*/
	//券为空
	protected final int RESPONSE_TICKET_NULL = -1;
	//添加成功
	protected final int RESPONSE_TICKET_SUCCESS = 0;
	//已经有券
	protected final int RESPONSE_TICKET_CONTAIN = 1;
	//不同类型的券不能添加
	protected final int RESPONSE_TICKET_DIFF_TYPE = -2;
	/** 添加券返回结果end*/
	
	private OnProcessListener processListener;
	/** 已经添加的券 组合支付中用到 (券改造)*/
	private ArrayList<TicketInfo> addTicketList = new ArrayList<TicketInfo>();
	
	public TickeUsedAdapter(Context context) {
		this.context = context;
		ticketManager = new TicketManagerImpl(context);
	}

	private List<TicketInfo> ticketList = new ArrayList<TicketInfo>();
	
	@Override
	public int getCount() {
		if(ticketList == null){
			return 0;
		}
		return ticketList.size();
	}

	@Override
	public Object getItem(int position) {
		return ticketList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		final TicketInfo ticketInfo = ticketList.get(position);
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = (LinearLayout) LayoutInflater.from(context).inflate(R.layout.component_use_ticket_item, null);
			holder.useTicket = convertView.findViewById(R.id.use_ticket_rl);
			holder.tvBalance = (TextView) convertView.findViewById(R.id.tvBalance);
			holder.useTime = (TextView) convertView.findViewById(R.id.tv_date);
			holder.ticketName = (TextView) convertView.findViewById(R.id.ti_tv_ticket_desc);
			holder.bUse = (Button) convertView.findViewById(R.id.b_use);
			holder.iconUse = (ImageView) convertView.findViewById(R.id.icon_use);
			holder.tvUseTicketAmount = (TextView) convertView.findViewById(R.id.tvUseTicketAmount);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		holder.iconUse.setVisibility(View.GONE);
		if ("1".equals(ticketInfo.getTicketDef().getWxFlag())) {
			holder.useTicket.setBackgroundResource(R.drawable.quan3);
		} else if ("2".equals(ticketInfo.getTicketDef().getWxFlag())) {
			holder.useTicket.setBackgroundResource(R.drawable.quan4);
		} else {
			holder.useTicket.setBackgroundResource(R.drawable.quan2);
		}
		try { //耗时方法
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			Calendar date = Calendar.getInstance();
			date.setTime(ticketInfo.getTicketDef().getCreateTime());
			date.set(Calendar.DATE, date.get(Calendar.DATE) + ticketInfo.getTicketDef().getValidPeriod());
			Date endDate = sdf.parse(sdf.format(date.getTime()));
			if (ticketInfo.getTicketDef().getValidPeriod() == -1) {
				holder.useTime.setText("永久有效");
			} else {
				holder.useTime.setText(DateUtil.format(endDate, DateUtil.P1));
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
		try {
			holder.ticketName.setText(ticketInfo.getTicketDef().getTicketName());			
			if(ticketInfo.getTicketDef().getTicketType().equals(TicketDef.TICKET_TYPE_DISCOUNT)) 
			{//如果是折扣券 不显示金额
				holder.tvBalance.setVisibility(View.INVISIBLE);
				holder.tvUseTicketAmount.setVisibility(View.INVISIBLE);
			}else
			{
				holder.tvBalance.setVisibility(View.VISIBLE);
				holder.tvUseTicketAmount.setVisibility(View.VISIBLE);
				holder.tvBalance.setText(Calculater.formotFen(ticketInfo.getTicketDef().getBalance()+""));
			}
		} catch (Exception e) {
		}
		if(ticketInfo.getSelectedCount()>0){
			holder.bUse.setBackgroundResource(R.drawable.used);
		}else{
			holder.bUse.setBackgroundResource(R.drawable.use);
		}
		holder.bUse.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(ticketInfo.getSelectedCount()>0){
					ticketInfo.setSelectedCount(0);
				}else{
					Response response = verifyLocal(ticketList.get(position));
					if(response.code != RESPONSE_TICKET_SUCCESS) {
						Toast.makeText(context, response.msg, 0).show();
						return;
					}
					if(processListener != null)
					{
						processListener.showProcess();
					}
					/** 需要调用接口验证券是否可用 @hwc 2015年12月28日09:16:18*/
					ticketManager.verifyTicket(ticketInfo.getTicketNo(), shouldPayAmount, new ResultListener() {
						
						@Override
						public void onSuccess(Response response) {
							ticketInfo.setSelectedCount(1);
							TicketInfo ticketInfo = (TicketInfo) response.getResult();
							shouldPayAmount = Calculater.subtract(shouldPayAmount, ticketInfo.getTicketDef().getBalance() + "");
							ticketList.get(position).getTicketDef().setBalance(ticketInfo.getTicketDef().getBalance());
							if(processListener != null)
								processListener.showContent();
						}
						
						@Override
						public void onFaild(Response response) {
							Toast.makeText(context, response.msg, 0).show();
							if(processListener != null)
								processListener.showContent();
						}
					});
				}
				notifyDataSetChanged();
			}
		});
		
		return convertView;
	}

	private class ViewHolder {
		TextView ticketName, useTime,tvBalance,tvUseTicketAmount;
		Button bUse;
		ImageView iconUse;
		View useTicket;
	}

	public void setDataChanged(List<TicketInfo> ticketList) {
		this.ticketList = ticketList;
		this.notifyDataSetChanged();
	}
	
	public void setInitAmount(String initAmount) {
		this.initAmount = initAmount;
		this.shouldPayAmount = initAmount;
	}
	
	public void setShouldPayAmount(String shouldPayAmount) {
		this.shouldPayAmount = shouldPayAmount;
	}

	public void addDataChanged(List<TicketInfo> ticketList) {
		this.ticketList.addAll(ticketList);
		this.notifyDataSetChanged();
	}
	
	public List<TicketInfo> getSelectedTicketInfos(){
		List<TicketInfo> selectedTicketInfos = new ArrayList<TicketInfo>();
		for(TicketInfo ticketInfo: ticketList){
			if(ticketInfo.getSelectedCount()>0){
				selectedTicketInfos.add(ticketInfo);
			}
		}
		return selectedTicketInfos;
	}
	
	/**
	 * 
	 * @Author: Huangweicai
	 * @date 2015-12-28 上午9:26:50 
	 * @param ticketInfo
	 * @return 
	 * @Description:验证券是否可用(本地)
	 */
	private Response verifyLocal(TicketInfo ticketInfo) {
		List<TicketInfo> allTicket = new ArrayList<TicketInfo>();
		allTicket.addAll(getSelectedTicketInfos());
		allTicket.addAll(addTicketList);
		Response response = new Response();
		if(ticketInfo == null)
		{
			response.code = RESPONSE_TICKET_NULL;
			response.msg = "添加券";
			return response;
		}
		if(allTicket.contains(ticketInfo))
		{
			response.code = RESPONSE_TICKET_CONTAIN;
			response.msg = "已使用该券,不能重复添加";
			return response;
		}
		String ticketType = ticketInfo.getTicketDef().getTicketType();
		if(ticketType.equals(TicketDef.TICKET_TYPE_DISCOUNT))
		{//如果是折扣券
			if(allTicket.size()>0)
			{
				response.code = RESPONSE_TICKET_DIFF_TYPE;
				response.msg = "折扣券只能单独使用";
				return response;
			}
		}else
		{//如果是代金券
			for(TicketInfo ticketBean:allTicket)
			{
				if(ticketBean.getTicketDef().getTicketType().equals(TicketDef.TICKET_TYPE_DISCOUNT))
				{
					response.code = RESPONSE_TICKET_DIFF_TYPE;
					response.msg = "类型不同的券不能同时使用";
					return response;
				}
			}
		}
		response.code = RESPONSE_TICKET_SUCCESS;
		response.msg = "验证成功";
		return response;
	}
	
	/**
	 * 
	 * @Author: Huangweicai
	 * @date 2015-12-28 上午9:17:08 
	 * @param processListener 
	 * @Description:与activity通信 (进度条更新)
	 */
	public void setProcessListener(OnProcessListener processListener)
	{
		this.processListener = processListener;
	}
	
	/**
	 * 
	 * @Author: Huangweicai
	 * @date 2015-12-25 下午3:56:27
	 * @Description:adapter与activity通信
	 */
	public interface OnProcessListener
	{
		/** 显示进度条*/
		public void showProcess();
		/** 显示内容*/
		public void showContent();
	}

	public void setAddTicketList(ArrayList<TicketInfo> addTicketList) {
		this.addTicketList = addTicketList;
	}

	
}
