package com.lc.baseui.activity.impl;

import android.view.View;

import com.lc.baseui.R;
import com.lc.baseui.widget.ed.AutoSearchEditNoBtn;

import java.util.ArrayList;

/**
 * 带有搜索框的模版
 * Created by licheng on 2017/5/16.
 */

public abstract class AbstractSearchEditListActivity<T> extends AbstractRefreshListActivity implements AutoSearchEditNoBtn.SearchResultListener {


    /**
     * @param lists 将lists中需要在auto中显示
     **/
    public abstract void getTransfString(ArrayList<T> lists, ArrayList<String> autoLists);

    protected AutoSearchEditNoBtn editSearch;
    protected ArrayList<String> autoLists;

    @Override
    public int getContentLayout() {
        return R.layout.layout_linear_search_listview;
    }


    private void init() {
        autoLists = new ArrayList();
    }

    @Override
    final public void init(View view) {
        init();
        editSearch = (AutoSearchEditNoBtn) view.findViewById(R.id.search_ed);
        editSearch.setCallback(this);
        super.init(view);
    }


    /**
     * 修改搜索栏的adapter
     **/
    protected void setAutoSearchAdapter() {
        getTransfString(listDatas, autoLists);
    }

    @Override
    protected void operateOnSuccess(ArrayList list) {
        super.operateOnSuccess(list);
        setAutoSearchAdapter();
    }
}
