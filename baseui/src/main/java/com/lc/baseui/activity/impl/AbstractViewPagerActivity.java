package com.lc.baseui.activity.impl;

import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.View;

import com.lc.baseui.R;
import com.lc.baseui.activity.base.TitleFragmentActivity;
import com.lc.baseui.adapter.SimpleViewPagerFragmentAdapter;
import com.lc.baseui.fragment.base.BaseFragment;
import com.lc.baseui.fragment.base.BaseViewPagerFragment;
import com.lc.baseui.listener.http.HttpClientImplListener;

import com.viewpagerindicator.TabPageIndicator;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * 需要做的:
 * 1:复写在list中添加fragment
 * 2:复写doGetData()
 * 3:operateOnSuccessCallback()
 * Created by licheng on 2017/4/28.
 */

public abstract class AbstractViewPagerActivity<T> extends TitleFragmentActivity implements HttpClientImplListener<T> {


    protected HashMap<String, BaseFragment> map;
    public abstract void firstEnter(View view);
    /**
     * 开始进行http请求
     **/
    public abstract void doGetData();

    /**
     * http,操作成功,回调
     **/
    public abstract void operateOnSuccessCallback(T t);

    //添加fragment
    public abstract void addFragment(ArrayList<BaseViewPagerFragment> lists);
    protected abstract String getTextTitle();

    //控件
    protected ViewPager viewPage;
    //列表
    protected ArrayList<BaseViewPagerFragment> lists;
    protected FragmentPagerAdapter adapter;

    @Override
    public void initData() {
        map = new HashMap();
        adapter = new SimpleViewPagerFragmentAdapter(getSupportFragmentManager(), lists);
        lists = new ArrayList<>();
        addFragment(lists);
    }

    @Override
    public int getContentLayout() {
        return R.layout.layout_viewpage_tab;
    }

    @Override
    public void init(View view) {
        if (!TextUtils.isEmpty(getTextTitle())) {
            setTitleCenter(getTextTitle());
        }
        adapter = new SimpleViewPagerFragmentAdapter(getSupportFragmentManager(), lists);
        viewPage = (ViewPager) view.findViewById(R.id.pager);
        viewPage.setOffscreenPageLimit(10);
        viewPage.setAdapter(adapter);

        TabPageIndicator indicator = (TabPageIndicator) view.findViewById(R.id.indicator);
        indicator.setViewPager(viewPage);
        firstEnter(view);
        doGetData();
    }


    @Override
    public void onSuccess(ArrayList<T> lists) {

    }

    @Override
    public void onSuccess(final T t) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                hiddenProgressDialog();
                operateOnSuccessCallback(t);
            }
        });
    }

    @Override
    public void onError(final String msg) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                hiddenProgressDialog();
                operateOnErrorCallback(msg);
            }
        });
    }


    /**
     * http,操作失败，回调
     **/
    public void operateOnErrorCallback(String msg) {
        toast(msg);

    }
}
