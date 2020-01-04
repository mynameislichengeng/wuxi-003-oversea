package com.lc.baseui.widget.listview;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lc.baseui.R;
import com.lc.librefreshlistview.adapter.BaseRecycleAdapter;

/**
 * Created by licheng on 2017/5/8.
 */

public class CustomerExpandableListView extends RelativeLayout implements View.OnClickListener {
    public CustomerExpandableListView(Context context) {
        super(context);
        initView(context);
    }

    public CustomerExpandableListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public CustomerExpandableListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    private Context context;
    private RelativeLayout rel_top, rel_main_content, rel_list, rel_btn;
    private RecyclerView recyclerView;
    private Button btn;
    private TextView textView;
    private ImageView iv;
    private BaseRecycleAdapter adapter;

    private void initView(Context context) {
        this.context = context;
        View view = LayoutInflater.from(context).inflate(R.layout.widget_layout_listview, null);
        rel_top = (RelativeLayout) view.findViewById(R.id.rel_top_title);
        rel_top.setOnClickListener(this);
        textView = (TextView) view.findViewById(R.id.tv_title);
        iv = (ImageView) view.findViewById(R.id.iv_tag);
        //
        rel_main_content = (RelativeLayout) view.findViewById(R.id.rel_main_content);
        rel_list = (RelativeLayout) view.findViewById(R.id.rel_list);
        recyclerView = (RecyclerView) view.findViewById(R.id.list);
        recyclerView.setDescendantFocusability(ViewGroup.FOCUS_AFTER_DESCENDANTS);
        rel_btn = (RelativeLayout) view.findViewById(R.id.rel_btn);
        rel_btn.setVisibility(View.GONE);
        btn = (Button) view.findViewById(R.id.btn_var);
        addView(view);
    }


    @Override
    public void onClick(View v) {
        if (rel_main_content.getVisibility() == View.GONE) {
            rel_main_content.setVisibility(View.VISIBLE);
        } else {
            rel_main_content.setVisibility(View.GONE);
        }
    }

    /**
     * 增加父view
     **/
    public void addParentView(View view) {
        rel_top.addView(view);
    }

    /**
     * 设置背景色
     **/
    public void setParentBg(int resId) {
        rel_top.setBackgroundResource(resId);
    }

    /**
     * 设置父组title
     **/
    public void setParentText(String str) {
        textView.setText(str);
    }

    /**
     * 设置adapter
     **/
    public void setAdapter(BaseRecycleAdapter adapter) {
        this.adapter = adapter;
        recyclerView.setAdapter(adapter);
    }

    /**
     * 设置button响应事件
     **/
    public void setButtonClickEvent(OnClickListener clickEvent) {
        btn.setOnClickListener(clickEvent);
    }

    /**
     * 设置是否显示底部
     **/
    public void setBottomVisible(int visible) {
        rel_btn.setVisibility(visible);
    }

    /**
     * 设置文本
     **/
    public void setBtnText(String text) {
        btn.setText(text);
    }
}
