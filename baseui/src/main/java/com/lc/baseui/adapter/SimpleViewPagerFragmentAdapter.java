package com.lc.baseui.adapter;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.text.TextUtils;


import com.lc.baseui.fragment.base.BaseViewPagerFragment;
import com.lc.baseui.tools.lg;

import java.util.ArrayList;

/**
 * ViewPager的adapter
 * Created by Administrator on 2017/4/16.
 */

public class SimpleViewPagerFragmentAdapter extends FragmentPagerAdapter {
    private String TAG = SimpleViewPagerFragmentAdapter.class.getSimpleName();
    protected ArrayList<BaseViewPagerFragment> lists;

    public SimpleViewPagerFragmentAdapter(FragmentManager fm, ArrayList<BaseViewPagerFragment> lists) {
        super(fm);
        this.lists = lists;
    }

    private SimpleViewPagerFragmentAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        return lists.get(position);
    }

    @Override
    public CharSequence getPageTitle(int position) {

        BaseViewPagerFragment fragment = lists.get(position);
        if (TextUtils.isEmpty(fragment.getTitle())) {
            lg.d(lg.TAG, TAG + ">>getPageTitle()>>TextUtils.isEmpty(fragment.getTitle()");
            return super.getPageTitle(position);
        } else {
            lg.d(lg.TAG, TAG + ">>getPageTitle()>>" + fragment.getTitle());
            return fragment.getTitle();
        }

    }

    @Override
    public int getCount() {

        return lists.size();
    }
}
