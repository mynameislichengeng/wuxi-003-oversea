package com.wizarpos.pay.cashier.fragment;

import java.util.List;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.wizarpos.pay.cashier.model.TransactionInfo2;
import com.wizarpos.pay.cashier.presenter.TransactionTemsController;
import com.wizarpos.pay.cashier.presenter.transaction.TransactionImpl2;
import com.wizarpos.pay.common.utils.Calculater;
import com.motionpay.pay2.lite.R;

/**
 * 组合支付已收
 * 
 * @author wu
 * 
 */
public class MixPayListFragment extends Fragment {
	private ListView pMixReceiveList;
	private MixReceivedAdapter mAdapter;
	/** 已收的类型对象 */
	private List<TransactionInfo2> tranInfoList;

	public MixPayListFragment() {
	}
	
	@Override
	@Nullable
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View mView = LayoutInflater.from(getActivity()).inflate(R.layout.pop_mix_pay_received, null);
		pMixReceiveList = (ListView) mView.findViewById(R.id.pMixReceiveList);
		mAdapter = new MixReceivedAdapter();
		pMixReceiveList.setAdapter(mAdapter);
		return mView;
	}

	public void setDataChanged(TransactionInfo2 mixTransactionInfo, View parent) {
		if (mixTransactionInfo != null) {
			tranInfoList = mixTransactionInfo.getPayedTransactionInfo();
		}
		mAdapter.notifyDataSetChanged();
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
				convertView = LayoutInflater.from(getActivity()).inflate(R.layout.item_pop_mix_received, null);
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

}
