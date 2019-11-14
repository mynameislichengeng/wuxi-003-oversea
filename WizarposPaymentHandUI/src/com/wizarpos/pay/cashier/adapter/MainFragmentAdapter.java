package com.wizarpos.pay.cashier.adapter;

import java.util.ArrayList;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class MainFragmentAdapter extends FragmentPagerAdapter {
	private ArrayList<Fragment> list;
//	private FragmentManager fm;

	public MainFragmentAdapter(FragmentManager fm, ArrayList<Fragment> list) {
		super(fm);
//		this.fm = fm;
		this.list = list;
	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Fragment getItem(int position) {
		return list.get(position);
	}

//	public void setFragments(ArrayList fragments) {
//		if (this.list != null) {
//			FragmentTransaction ft = fm.beginTransaction();
//			for (Fragment f : this.list) {
//				ft.remove(f);
//			}
//			ft.commit();
//			ft = null;
//			fm.executePendingTransactions();
//		}
//		this.list = fragments;
//		notifyDataSetChanged();
//	}
}
