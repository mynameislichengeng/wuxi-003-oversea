package com.wizarpos.pay.view.adapter;

import java.util.ArrayList;
import java.util.List;

import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.PopupWindow.OnDismissListener;

import com.wizarpos.log.util.LogEx;
import com.wizarpos.pay.cashier.model.TicketDef;
import com.wizarpos.pay.cashier.model.TicketPacketInfo;
import com.wizarpos.pay.common.utils.Calculater;
import com.wizarpos.pay.ui.BasePopupWindow;
import com.wizarpos.pay.ui.PopupWindowEx;
import com.wizarpos.pay2.lite.R;

public class TicketAdapter extends BaseAdapter {

	public LayoutInflater inflater = null;
	public List<TicketDef> ticketDefList = null;
	private List<TicketDef> ticketPacketList = null;
	int[] countArray = null;
	private Context context;
	
	public TicketAdapter() {
	}

	public TicketAdapter(Context context) {
		this.context = context;
	}
	
	/**
	 * 获取选中券的列表
	 * 
	 * @return
	 */
	public int[] getCountArray() {
		return countArray;
	}

	public void initCountArray() {
		countArray = new int[ticketDefList.size()];
	}

	@Override
	public int getCount() {
		return ticketDefList.size();
	}

	@Override
	public Object getItem(int position) {
		return ticketDefList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		View rlayout = inflater.inflate(R.layout.component_publish_ticket_item, null);
		final Button minusBtn = (Button) rlayout.findViewById(R.id.pti_btn_minus);
		final Button addBtn = (Button) rlayout.findViewById(R.id.pti_btn_add);
		TextView ticketNameTv = (TextView) rlayout.findViewById(R.id.pti_tv_ticket_name);
		View ticketLayout = rlayout.findViewById(R.id.ticket_choose_rl);
		TextView useTime = (TextView) rlayout.findViewById(R.id.tv_publish_date);
		final TextView ticketCountTv = (TextView) rlayout.findViewById(R.id.pti_tv_ticket_count);
		ticketCountTv.setText(countArray[position] + "");
		final TextView tvBalance = (TextView) rlayout.findViewById(R.id.tvBalance);
		final TicketDef ticketDef = ticketDefList.get(position);
		final TextView tvPeriod = (TextView) rlayout.findViewById(R.id.tvTicketPeriod);
		ticketNameTv.setText(ticketDef.getTicketName());
		tvBalance.setText(Calculater.formotFen(ticketDef.getBalance() + ""));

		if(ticketDef.getTicketType().equals(TicketDef.TICKET_TYPE_DISCOUNT) || 
				ticketDef.getTicketType().equals(TicketDef.TICKET_TYPE_GIFT)) {
			//若是礼品券或折扣券 不显示金额
			String showStr = "";	//显示的券详情
			if (TicketDef.TICKET_TYPE_DISCOUNT.equals(ticketDef.getTicketType())) {
				//折扣券
				showStr += "折扣率：" + (100 - ticketDef.getHyDiscount()) + "%";
			} else if (TicketDef.TICKET_TYPE_GIFT.equals(ticketDef.getTicketType())) {
				//礼品券
				if (ticketDef.getGift().length()>8) {
					showStr += ticketDef.getGift().replace(ticketDef.getGift().substring(9), "...");
				} else {
					showStr += ticketDef.getGift();
				}
			}
			((TextView)rlayout.findViewById(R.id.tvTicketAmount)).setText(showStr);
//			rlayout.findViewById(R.id.tvTicketAmount).setVisibility(View.INVISIBLE);
			tvBalance.setVisibility(View.GONE);
		}
		
		try {
			if ("1".equals(ticketDef.getWxFlag())) {
				ticketLayout.setBackgroundResource(R.drawable.quan3);
			} else if ("2".equals(ticketDef.getWxFlag())) {
				ticketLayout.setBackgroundResource(R.drawable.quan4);
			} else {
				ticketLayout.setBackgroundResource(R.drawable.quan2);
			}
			// if (position % 4 == 0) {
			// ticketLayout.setBackgroundResource(R.drawable.quan1);
			// } else if (position % 4 == 1) {
			// ticketLayout.setBackgroundResource(R.drawable.quan2);
			// } else if (position % 4 == 2) {
			// ticketLayout.setBackgroundResource(R.drawable.quan3);
			// } else if (position % 4 == 3) {
			// ticketLayout.setBackgroundResource(R.drawable.quan4);
			// }
//			if (ticketDef.getValidPeriod() == 0 && !TextUtils.isEmpty(ticketDef.getEndDate())) {
//				useTime.setText(ticketDef.getEndDate());
//			}else{
//				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
//				Calendar date = Calendar.getInstance();
//				date.setTime(ticketDef.getCreateTime());
//				date.set(Calendar.DATE, date.get(Calendar.DATE) + ticketDef.getValidPeriod());
//				Date endDate = sdf.parse(sdf.format(date.getTime()));
//				if (ticketDef.getValidPeriod() == -1) {
//					useTime.setText("永久有效");
//				} else {
//					useTime.setText(DateUtil.format(endDate, DateUtil.P1));
//				}
//			}
			if (!TextUtils.isEmpty(ticketDef.getEndTimestampStr())) {
				useTime.setText(ticketDef.getEndTimestampStr());
			} else {
				useTime.setText("未知");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		minusBtn.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {
				int count = countArray[position];
				if (count < 1) {
					return;
				} else {
					countArray[position] = count - 1;
				}
				if (ticketPacketList == null) ticketPacketList = new ArrayList<TicketDef>();
				if (ticketDef.isPacket()) {
					ticketPacketList.remove(ticketDef);
				}
				ticketCountTv.setText(countArray[position] + "");
			}
		});
		addBtn.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {
				int count = countArray[position];
				TicketDef ticketDef =  ticketDefList.get(position);
				if(!ticketDef.isPacket() && ticketDef.getValidPeriod() == 0 && !TextUtils.isEmpty(ticketDef.getEndDate()) && count >= 1){//友盟券最多能使用一张
					return;
				}
				if (ticketPacketList == null) ticketPacketList = new ArrayList<TicketDef>();
				if (ticketDef.isPacket()) {
					ticketPacketList.add(ticketDef);
				}
				countArray[position] = count + 1;
				ticketCountTv.setText(countArray[position] + "");
			}
		});
		if (ticketDef.isPacket())// 券包详情点击
		{
			useTime.setText("点击可查看详情");
			tvPeriod.setVisibility(View.GONE);
			ticketLayout.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					//显示卡包中券详情
					if (context != null) {
						try {
							initPopupDisCountView(ticketDef, (Activity)context);
						} catch (Exception e) {
							e.printStackTrace();
							return;
						}
					}
					LogEx.i("onClick", ticketDef.getTicketName());
				}
			});
		}
		return rlayout;
	}
	
	private View popupDisCountView;
	private BasePopupWindow popupDisCountWin;

	public void initPopupDisCountView(TicketDef ticketDef, final Activity activity) {
		// 已存单据
		popupDisCountView = activity.getLayoutInflater().inflate(
				R.layout.pop_up_ticket_pack, null);
		popupDisCountWin = new BasePopupWindow(popupDisCountView,
				LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT,
				true);
		popupDisCountWin.setOnDismissListener(new OnDismissListener() {
			@Override
			public void onDismiss() {
				backgroundAlpha(1f,activity);
			}
		});
		List<TicketPacketInfo> ticketInfo = ticketDef.getPackInfo();
		TicketPacketAdapter packetAdapter = new TicketPacketAdapter(activity,ticketInfo);
		backgroundAlpha(0.5f,activity);
		popupDisCountWin.setFocusable(true);
		TextView tvPopTicket = (TextView) popupDisCountView.findViewById(R.id.tvPopTicket);
		tvPopTicket.setText(ticketDef.getTicketName());
		ListView lvPopTicket = (ListView) popupDisCountView.findViewById(R.id.lvPopTicket);
		lvPopTicket.setAdapter(packetAdapter);
		popupDisCountView.setVisibility(View.VISIBLE);
		popupDisCountWin.showAtLocation(activity.findViewById(android.R.id.content), Gravity.CENTER, 0,0);
	}
	
	/**
	 * 设置添加屏幕的背景透明度
	 * 
	 * @param bgAlpha
	 */
	public void backgroundAlpha(float bgAlpha,Activity activity) {
		WindowManager.LayoutParams lp = activity.getWindow().getAttributes();
		lp.alpha = bgAlpha; // 0.0-1.0
		activity.getWindow().setAttributes(lp);
	}
	
	public boolean isShowingPop() {
		if (null != popupDisCountWin && popupDisCountWin.isShowing()) {
			return true;
		}
		return false;
	}
	
	public void dismissPop() {
		if (isShowingPop()) {
			popupDisCountWin.dismiss();
		}
	}
}
