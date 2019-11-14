package com.wizarpos.pay.statistics.fragment;

import java.util.List;

import android.app.ActionBar.LayoutParams;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;
import android.widget.PopupWindow;

import com.wizarpos.pay.statistics.adapter.MixDetialAdapter;
import com.wizarpos.pay2.lite.R;

public class ItemMixDetialPopWindow {

	private PopupWindow popupWindow;
	private MixDetialAdapter adapter;

	public ItemMixDetialPopWindow(Context context) {
		View popView = LayoutInflater.from(context).inflate(R.layout.fargment_item_mix_detail, null);
		popupWindow = new PopupWindow(popView, LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT, true);
		popupWindow.setOutsideTouchable(true);
		popupWindow.setTouchable(false);
		popupWindow.setFocusable(false);
		ListView lvDetial = (ListView) popView.findViewById(R.id.lvDetail);
		adapter = new MixDetialAdapter(context);
		lvDetial.setAdapter(adapter);
	}

	public void showPopWindow(View rootView, List<String[]> mixTrans) {
		showPopWindow(rootView);
		notifyDataChanged(mixTrans);
	}

	public void showPopWindow(View rootView) {
		if (!popupWindow.isShowing()) {
			popupWindow.showAtLocation(rootView, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
		}
	}

	public void notifyDataChanged(List<String[]> mixTrans) {
		if (adapter == null) { return; }
		adapter.setDataChanged(mixTrans);
	}

}
