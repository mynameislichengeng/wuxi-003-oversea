package com.wizarpos.pay.recode.test;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.lc.librefreshlistview.linear.SimpleLinearRecycleView;
import com.lc.librefreshlistview.listener.RefreshEventListener;
import com.wizarpos.pay.recode.test.adapter.TestAdapter;
import com.wizarpos.pay2.lite.R;

import java.util.ArrayList;
import java.util.List;

public class TestActivity extends AppCompatActivity {

    private SimpleLinearRecycleView simpleLinearRecycleView;
    private TestAdapter testAdapter;
    private ArrayList<String> mList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.base_simplelinear_recycleview);
        initData();
        simpleLinearRecycleView = findViewById(R.id.rel_list);
        simpleLinearRecycleView.setRecycleViewAdapter(testAdapter);
        simpleLinearRecycleView.setRefreshEventListener(new RefreshEventListener() {
            @Override
            public void onTopDownRefresh(boolean isManual) {

            }

            @Override
            public void onBottomLoadMore(boolean isSilence) {

            }

            @Override
            public void onCompleteRefresh() {

            }
        });
    }

    private void initData() {
        mList = new ArrayList<>();
        testAdapter = new TestAdapter(this);
        testAdapter.setLists(mList);
        for (int i = 0; i < 10; i++) {
            mList.add("你好:" + i);
        }

    }
}
