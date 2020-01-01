package com.lc.librefreshlistview.holder;

import android.util.SparseArray;
import android.view.View;

/**
 * 普通的viewhodler
 * **/
	public class ViewHolder {
	@SuppressWarnings("unchecked")
	/**
	 * view一定不能为空，并且它主要是放holder
	 * **/
	public static <T> T get(View view, int id) {
		SparseArray viewHolder = (SparseArray) view.getTag();
		if (viewHolder == null) {
			viewHolder = new SparseArray();
			view.setTag(viewHolder);
		}
		View childView = (View) viewHolder.get(id);
		if (childView == null) {
			childView = view.findViewById(id);
			viewHolder.put(id, childView);
		}
		return (T) childView;
	}
}