package com.lc.baseui.fragment.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lc.baseui.tools.lg;

import java.util.ArrayList;

/**
 * 专门用来做viewPage中的fragment
 * Created by Administrator on 2017/4/16.
 */

public abstract class BaseViewPagerFragment<T> extends BaseFragment {
    private String TAG = BaseViewPagerFragment.class.getSimpleName();

    protected String flag;
    protected String title;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
    protected abstract int getLayout();
    protected abstract void initView(View view);
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        lg.d(lg.TAG,TAG+">>onCreateView()");
        View view = inflater.inflate(getLayout(), null);
        initView(view);
        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        lg.d(lg.TAG,TAG+">>onCreate( Bundle savedInstanceState)");
    }

    /**
     * 接受主界面传递activity传递过来的数据
     **/

    public void onActivitySendEvent(T t) {

    }

    /**
     * 接受主界面传递activity传递过来的数据
     **/

    public void onActivitySendEvent(ArrayList<T> t) {

    }

    public String getTAG() {
        return TAG;
    }

    public void setTAG(String TAG) {
        this.TAG = TAG;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }
}
