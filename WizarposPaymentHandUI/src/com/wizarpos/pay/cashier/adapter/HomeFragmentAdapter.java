package com.wizarpos.pay.cashier.adapter;

import java.util.ArrayList;
import java.util.List;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.wizarpos.pay.cashier.fragment.BaseNavFragment;
import com.wizarpos.pay.cashier.fragment.HomeNavAmountFragment;
import com.wizarpos.pay.cashier.fragment.HomeNavAmountFragment.OnUpdateUiListener;
import com.wizarpos.pay.cashier.fragment.HomeNavFragment;

/** 
 * @Author:Huangweicai
 * @Date:2015-7-29 上午11:00:40
 * @Reason:这里用一句话说明
 */
public class HomeFragmentAdapter extends FragmentPagerAdapter {
	private List<BaseNavFragment> mFragmentList;
	private OnUpdateUiListener listener;
	
	public HomeFragmentAdapter(FragmentManager fm,OnUpdateUiListener listener) {
		super(fm);
		this.listener = listener;
		this.mFragmentList = createFragments();
	}

	@Override
	public BaseNavFragment getItem(int position) {
		return mFragmentList.get(position);
	}

	@Override
	public int getCount() {
		return mFragmentList.size();
	}
	
	private List<BaseNavFragment> createFragments()
	{
		HomeNavAmountFragment nvFragment1 = new HomeNavAmountFragment();
		nvFragment1.setListener(listener);
		HomeNavFragment nvFragment2 = new HomeNavFragment();
		HomeNavFragment nvFragment3 = new HomeNavFragment();
		List<BaseNavFragment> list = new ArrayList<BaseNavFragment>();
		list.add(nvFragment1);
		list.add(nvFragment2);
		list.add(nvFragment3);
		return list;
	}

}
