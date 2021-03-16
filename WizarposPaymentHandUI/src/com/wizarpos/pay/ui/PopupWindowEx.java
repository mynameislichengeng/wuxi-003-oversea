package com.wizarpos.pay.ui;

import java.util.ArrayList;
import java.util.List;

import android.app.ActionBar.LayoutParams;
import android.content.Context;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnKeyListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.wizarpos.pay.cashier.model.TransactionInfo2;
import com.wizarpos.pay.cashier.presenter.TransactionTemsController;
import com.wizarpos.pay.cashier.presenter.transaction.TransactionImpl2;
import com.wizarpos.pay.common.utils.Calculater;
import com.motionpay.pay2.lite.R;

/**
 * 
 * @Author:Huangweicai
 * @Date:2015-7-28 下午2:24:26
 * @Reason:popUp
 */
public class PopupWindowEx implements OnTouchListener {
	private final String LOG_TAG = "PopupWindowEx";
	private PopupWindow tPopupWindow;
	private ListView pMixReceiveList;
	private Context mContext;
	private MixReceivedAdapter mAdapter;
	/** 已收的类型对象 */
	private List<TransactionInfo2> tranInfoList;

	public PopupWindowEx(Context context) {
		this.mContext = context;
		this.tranInfoList = new ArrayList<TransactionInfo2>();
		initPopUp();
	}

	private View mView;

	/**
	 * 
	 * @Author:Huangweicai
	 * @Date:2015-7-28 下午2:23:02
	 * @Reason:初始化Pop
	 */
	private void initPopUp() {
		if (tPopupWindow != null) {
			tPopupWindow.dismiss();
		}
		mView = LayoutInflater.from(mContext).inflate(R.layout.pop_mix_pay_received, null);
		pMixReceiveList = (ListView) mView.findViewById(R.id.pMixReceiveList);
		tPopupWindow = new PopupWindow(mView, LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT, true);
		tPopupWindow.setTouchable(true);
		tPopupWindow.setFocusable(true);
		tPopupWindow.setOutsideTouchable(true);
		// 设置SelectPicPopupWindow弹出窗体动画效果
		tPopupWindow.setAnimationStyle(R.style.PopupAnimation);
		mAdapter = new MixReceivedAdapter();
		pMixReceiveList.setAdapter(mAdapter);

		mView.setOnTouchListener(this);

	}

	/**
	 * 
	 * @Author:Huangweicai
	 * @Date:2015-7-28 下午3:22:34
	 * @Reason:更新UI(弹出POP)
	 * @param mixTransactionInfo
	 * @param parent
	 */
	public void setDataChanged(TransactionInfo2 mixTransactionInfo, View parent) {
		if (mixTransactionInfo != null) {
			tranInfoList = mixTransactionInfo.getPayedTransactionInfo();
		}
		mAdapter.notifyDataSetChanged();
		showPopupWindow(parent);
	}

	/**
	 * 显示popupWindow
	 * 
	 * @param parent
	 */
	public void showPopupWindow(View parent) {
		if (!tPopupWindow.isShowing()) {
			// 以下拉方式显示popupwindow
			tPopupWindow.showAtLocation(parent, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
		} else {
			tPopupWindow.dismiss();
		}
	}

	private class MixReceivedAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			if (tranInfoList == null) { return 0; }
			return tranInfoList.size();
		}

		@Override
		public Object getItem(int position) {
			return null;
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder = null;
			if (convertView == null) {
				convertView = LayoutInflater.from(mContext).inflate(R.layout.item_pop_mix_received, null);
				holder = new ViewHolder();
				holder.tvReceivedCardType = (TextView) convertView.findViewById(R.id.tvReceivedCardType);
				holder.tvReceivedAmount = (TextView) convertView.findViewById(R.id.tvReceivedAmount);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			// TAG: 应收对象数据未确定Huangweicai
			if (tranInfoList != null) {
				TransactionInfo2 tranInfo = tranInfoList.get(position);
				if (tranInfo.getTransactionType() == TransactionTemsController.TRANSACTION_TYPE_CHANGE_DEL) {
					holder.tvReceivedAmount.setText(Calculater.formotFen(tranInfo.getReduceAmount() + ""));
				} else {
					holder.tvReceivedAmount.setText(Calculater.formotFen(tranInfo.getRealAmount() + ""));
				}
				holder.tvReceivedCardType.setText(TransactionImpl2.getTransactionDes(tranInfo.getTransactionType()));
			}

			return convertView;
		}

		class ViewHolder {
			TextView tvReceivedCardType;
			TextView tvReceivedAmount;
		}

	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		int height = mView.findViewById(R.id.main).getTop();
		int y = (int) event.getY();
		if (event.getAction() == MotionEvent.ACTION_UP) {
			if (y < height) {
				tPopupWindow.dismiss();
			}
		}
		return true;
	}

	public void hide() {
		if (tPopupWindow == null) { return; }
		tPopupWindow.dismiss();
	}

	public boolean isShowing() {
		if (tPopupWindow == null) { return false; }
		return tPopupWindow.isShowing();
	}

}
