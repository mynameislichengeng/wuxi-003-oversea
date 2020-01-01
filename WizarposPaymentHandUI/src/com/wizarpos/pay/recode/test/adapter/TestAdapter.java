package com.wizarpos.pay.recode.test.adapter;

import android.content.Context;
import android.widget.TextView;

import com.lc.librefreshlistview.adapter.BaseRecycleAdapter;
import com.lc.librefreshlistview.holder.SimpleRecycleViewHodler;
import com.wizarpos.pay2.lite.R;

public class TestAdapter extends BaseRecycleAdapter {
    public TestAdapter(Context context) {
        this.context = context;
    }

    @Override
    protected int getLayout() {
        return R.layout.layout_test_base;
    }

    @Override
    public void onBindViewHolder(SimpleRecycleViewHodler holder, int position) {
        String item = (String) lists.get(position);
        TextView tv = holder.getView().findViewById(R.id.title);
        tv.setText(item);
    }

}
