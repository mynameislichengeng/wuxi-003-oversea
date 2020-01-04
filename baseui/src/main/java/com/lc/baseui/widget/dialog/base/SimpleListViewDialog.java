package com.lc.baseui.widget.dialog.base;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.lc.baseui.R;
import com.lc.librefreshlistview.adapter.BaseRecycleAdapter;

/**
 * Created by licheng on 2017/5/18.
 */

public class SimpleListViewDialog extends CustomerDialog implements BaseRecycleAdapter.OnItemClick {


    public SimpleListViewDialog(Context context) {
        super(context);
    }

    public SimpleListViewDialog(Context context, int themeResId) {
        super(context, themeResId);
    }

    protected SimpleListViewDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    @Override
    public int getLayout() {
        return R.layout.dialog_toptitle_middlelistview_bottom_one_btn;
    }

    protected RecyclerView listview;
    protected BaseRecycleAdapter adapter;
    protected TextView tv_title, tv_cacle;
    protected OnSelectItemDialog onSelectItemDialog;

    @Override
    public void init() {
        listview = (RecyclerView) findViewById(R.id.list);
        tv_title = (TextView) findViewById(R.id.tv_dialog_title);
        tv_cacle = (TextView) findViewById(R.id.tv_cancel);
        tv_cacle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }


    /**
     * 设置adapter
     * **/
    public void setAdapter(BaseRecycleAdapter adapter) {
        this.adapter = adapter;
        if (adapter != null) {
            adapter.setOnItemClick(this);
            listview.setAdapter(adapter);
        }
    }


    @Override
    public void onItemClick(int position, Object o) {
        String obj = (String) o;
        if (onSelectItemDialog != null) {
            onSelectItemDialog.onSelectItem(position, o);
        }
        dismiss();
    }

    public void setDialogTitle(String title) {
        tv_title.setText(title);
    }

    public void setDialogTitle(int res) {
        tv_title.setText(res);
    }

    public void setBottomText(String title) {
        tv_cacle.setText(title);
    }

    public void setBottomText(int title) {
        tv_cacle.setText(title);
    }

    public void setOnSelectItemDialog(OnSelectItemDialog onSelectItemDialog) {
        this.onSelectItemDialog = onSelectItemDialog;
    }

    public static interface OnSelectItemDialog {
        <T> void onSelectItem(int position, T t);
    }
}
