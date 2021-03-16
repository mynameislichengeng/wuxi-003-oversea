package com.wizarpos.pay.view.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wizarpos.pay.view.TransTypeItem;
import com.motionpay.pay2.lite.R;

/**
 * @Author:Huangweicai
 * @Date:2015-7-29 下午3:29:47
 * @Reason:这里用一句话说明
 */
public class MixPayAdapter extends RecyclerView.Adapter<MixPayAdapter.MyViewHolder> {
	private Context mContext;
	List<TransTypeItem> transTypeItems = new ArrayList<>();

	private OnItemClickLitener mOnItemClickLitener;

	public interface OnItemClickLitener {
		void onItemClick(View view, int position, TransTypeItem transTypeItem);
	}

	public MixPayAdapter(Context context) {
		mContext = context;
	}

	class MyViewHolder extends ViewHolder {

		TextView tvItem;
		ImageView ivItem;
		LinearLayout llItemPay;

		public MyViewHolder(View view) {
			super(view);
			tvItem = (TextView) view.findViewById(R.id.tvItem);
			ivItem = (ImageView) view.findViewById(R.id.ivItem);
			llItemPay = (LinearLayout) view.findViewById(R.id.llItemPay);
		}
	}

	@Override
	public int getItemCount() {
		return transTypeItems.size();
	}

	@Override
	public void onBindViewHolder(final MyViewHolder holder, int position) {
		holder.tvItem.setText((CharSequence) transTypeItems.get(position).getShowValue());
		holder.ivItem.setBackgroundResource((Integer) transTypeItems.get(position).getIcon());

		if (mOnItemClickLitener != null) {
			holder.llItemPay.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					int pos = holder.getLayoutPosition();
					mOnItemClickLitener.onItemClick(holder.itemView, pos, transTypeItems.get(pos));
				}
			});
		}

	}

	public void setItemClickListener(OnItemClickLitener listener) {
		this.mOnItemClickLitener = listener;
	}

	@Override
	public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		MyViewHolder holder = new MyViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_mix_pay, parent, false));

		return holder;
	}

	public void setDataChanged(List<TransTypeItem> transTypeItems) {
		this.transTypeItems = transTypeItems;
		notifyDataSetChanged();
	}
}
