/*package com.wizarpos.pay.view.adapter;

import java.util.ArrayList;
import java.util.List;

import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wizarpos.pay.db.UserBean;
import com.motionpay.pay2.lite.R;

public class CashierManagerAdapter extends BaseAdapter {
	public interface CashierManagerInfo {
		public void getInfoMsg(UserBean bean);
	}

	private CashierManagerInfo info;

	public CashierManagerInfo getInfo() {
		return info;
	}

	public void setInfo(CashierManagerInfo info) {
		this.info = info;
	}

	public LayoutInflater inflater = null;
	public List<UserBean> list = new ArrayList<UserBean>();

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
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		LinearLayout rlayout = (LinearLayout) inflater.inflate(R.layout.component_cashier_manager_item, null);
		// final ImageView iconChoose = (ImageView) rlayout.findViewById(R.id.choosed_icon);
		TextView tvLoginName = (TextView) rlayout.findViewById(R.id.tv_login_num);
		Button btnChangeButton = (Button) rlayout.findViewById(R.id.btn_change_password);
		TextView tvManagerName = (TextView) rlayout.findViewById(R.id.tv_manager_name);
		ImageView ivIcon = (ImageView) rlayout.findViewById(R.id.iv_icon);
		TextView tvPhoneNum = (TextView) rlayout.findViewById(R.id.tv_tel_num);
		tvManagerName.setText(list.get(position).getRealName() + "(" + list.get(position).getOperId() + ")");
		tvPhoneNum.setText(list.get(position).getPhone());
		tvLoginName.setText(String.valueOf(position));
		btnChangeButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				info.getInfoMsg(list.get(position));
			}
		});
		rlayout.setOnClickListener(new OnClickListener() { //收银员管理：增加点击操作员出现编辑页面功能 wu
			
			@Override
			public void onClick(View v) {
				info.getInfoMsg(list.get(position));
			}
		});
		// int type = list.get(position).getType();
		// if (type == 2) {
		// tvLoginName.setText("001");
		// tvManagerName.setText("超级管理员");
		// tvManagerName.setTextColor(Color.parseColor("#FF972F"));
		// tvPhoneNum.setText(list.get(position).getPhone());
		// ivIcon.setBackgroundResource(R.drawable.user_admin);
		// } else {
		// tvLoginName.setText("001");
		// tvManagerName.setText("收银员" + position);
		// tvPhoneNum.setText(list.get(position).getPhone());
		// ivIcon.setBackgroundResource(R.drawable.user_normal);
		// }
		return rlayout;
	}

}
*/