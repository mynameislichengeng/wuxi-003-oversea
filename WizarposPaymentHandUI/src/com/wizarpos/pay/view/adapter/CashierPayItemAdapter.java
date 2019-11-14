package com.wizarpos.pay.view.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.wizarpos.pay.view.TransTypeItem;
import com.wizarpos.pay2.lite.R;

public class CashierPayItemAdapter extends BaseAdapter {

	private Context context;

	public CashierPayItemAdapter(Context context) {
		this.context = context;
	}

	List<TransTypeItem> transTypeItems = new ArrayList<>();

	@Override
	public int getCount() {
		return transTypeItems.size();
	}

	@Override
	public Object getItem(int position) {
		return transTypeItems.size();
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	private ViewHolder holder;

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(R.layout.item_payitem, parent, false);
			holder = new ViewHolder(convertView);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.ivPayItemIcon.setImageResource(transTypeItems.get(position).getIcon());
		holder.tvPayItemName.setText(transTypeItems.get(position).getShowValue());
		return convertView;
	}

	public class ViewHolder {
		public final ImageView ivPayItemIcon;
		public final TextView tvPayItemName;
		public final View root;

		public ViewHolder(View root) {
			ivPayItemIcon = (ImageView) root.findViewById(R.id.ivPayItemIcon);
			tvPayItemName = (TextView) root.findViewById(R.id.tvPayItemName);
			this.root = root;
		}
	}

	public void setDataChanged(List<TransTypeItem> transTypeItems) {
		this.transTypeItems = transTypeItems;
		this.notifyDataSetChanged();
	}

}
