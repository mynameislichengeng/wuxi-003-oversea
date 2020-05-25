package com.lc.baseui.widget.dialog;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.lc.baseui.R;
import com.lc.baseui.widget.dialog.base.CustomerDialog;
import com.lc.librefreshlistview.adapter.BaseRecycleAdapter;

/**
 * Created by licheng on 2017/5/18.
 */

public class SimpleListViewDialog extends CustomerDialog implements BaseRecycleAdapter.OnItemClick {


    public SimpleListViewDialog(Context context) {
        super(context, R.style.common_dialog);
    }

    public SimpleListViewDialog(Context context, int themeResId) {
        super(context, themeResId);
    }

    protected SimpleListViewDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    @Override
    public int getLayout() {
        return R.layout.dialog_toptitle_middlelistview_bottom_two_btn;
    }

    protected TextView tv_title;
    protected RecyclerView listview;
    protected Button btnCancle, btnOk;

    protected BaseRecycleAdapter adapter;
    protected OnSelectItemDialog onSelectItemDialog;
    protected OnCancleAndSuceClickListener onCancleAndSuceClickListener;

    @Override
    public void init() {
        listview = (RecyclerView) findViewById(R.id.list);
        tv_title = (TextView) findViewById(R.id.tv_dialog_title);
        btnCancle = (Button) findViewById(R.id.dialog_left_btn);
        btnCancle.setOnClickListener(this);
        btnOk = (Button) findViewById(R.id.dialog_right_btn);
        btnOk.setOnClickListener(this);
    }


    /**
     * 设置adapter
     **/
    public void setAdapter(BaseRecycleAdapter adapter) {
        this.adapter = adapter;
        if (adapter != null) {
            adapter.setOnItemClick(this);
            listview.setAdapter(adapter);
        }
    }


    @Override
    public void onItemClick(int position, Object o) {
        if (onSelectItemDialog != null) {
            onSelectItemDialog.onSelectItem(position, o);
        }
        dismiss();
    }

    /**
     *
     **/
    @Override
    public void onClick(View view) {
        dismiss();
        if (view.getId() == R.id.dialog_left_btn) {
            if (onCancleAndSuceClickListener != null) {
                onCancleAndSuceClickListener.onCancle(view);
            }
        } else if (view.getId() == R.id.dialog_right_btn) {
            if (onCancleAndSuceClickListener != null) {
                onCancleAndSuceClickListener.onSure(view);
            }
        }
    }


    public void setDialogTitle(String title) {
        tv_title.setText(title);
    }

    public void setDialogTitle(int res) {
        tv_title.setText(res);
    }

    public void setDialogTitleVisible(int visible) {
        tv_title.setVisibility(visible);
    }


    public void setOnSelectItemDialog(OnSelectItemDialog onSelectItemDialog) {
        this.onSelectItemDialog = onSelectItemDialog;
    }

    public void setOnCancleAndSuceClickListener(OnCancleAndSuceClickListener onCancleAndSuceClickListener) {
        this.onCancleAndSuceClickListener = onCancleAndSuceClickListener;
    }

    public void setBtnLeftText(int res) {
        btnCancle.setText(res);
    }


    public static interface OnCancleAndSuceClickListener {
        void onSure(View view);

        void onCancle(View view);
    }


    public static interface OnSelectItemDialog {
        <T> void onSelectItem(int position, T t);
    }
}
