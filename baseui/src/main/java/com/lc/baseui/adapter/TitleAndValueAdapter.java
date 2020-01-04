package com.lc.baseui.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lc.baseui.R;
import com.lc.baseui.bean.var.ItemBean;
import com.lc.baseui.listener.adapterUI.CustomerEventAdapterCallback;
import com.lc.baseui.tools.data.RefieldtUtils;
import com.lc.librefreshlistview.adapter.BaseRecycleAdapter;
import com.lc.librefreshlistview.holder.SimpleRecycleViewHodler;


/**
 * 需要的操作:
 * 1:设置setT
 * 2:设置布局setlayout,不过他有默认的
 * 3:如果有自定义事件，那么就可以实现customerEvent
 * Created by Administrator on 2017/4/25.
 */

public class TitleAndValueAdapter<T> extends BaseRecycleAdapter {
    protected String names[];

    private int layout = R.layout.adapter_left_tv_right_tv_image;
    private int arrowStatus = View.GONE;

    private CustomerEventAdapterCallback customerEvent;//自定义事件

    public TitleAndValueAdapter(Context context, String names[]) {
        this.context = context;
        this.names = names;
    }


    public void setLayout(int layout) {
        this.layout = layout;
    }


    @Override
    public int getItemCount() {
        return names.length;
    }

    @Override
    protected int getLayout() {
        return layout;
    }

    @Override
    public void onBindViewHolder(SimpleRecycleViewHodler holder, final int position) {
        //标题
        TextView tv_title = (TextView) getWidget(holder, R.id.tv_title);
        //值
        TextView tv_value = (TextView) getWidget(holder, R.id.tv_value);
        bindView(tv_title, tv_value, position);
        //
        RelativeLayout rel_value = (RelativeLayout) getWidget(holder, R.id.rel_value);
        rel_value.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickEvent(position);
            }
        });
        //箭头
        ImageView iv = (ImageView) getWidget(holder, R.id.iv_arrow);
        iv.setVisibility(arrowStatus);
        bindImageView(iv, position);
    }

    /**
     * 响应事件
     **/
    public void onClickEvent(int position) {
        String[] flag = getPart(names[position]);
        ItemBean bean = new ItemBean();
        bean.setTitle(flag[0]);
        bean.setName(flag[1]);
        if (customerEvent != null) {
            customerEvent.doCustomerEvent(position, bean, t);
        } else {
            if (onItemClick != null) {
                onItemClick.onItemClick(position, bean);
            }
        }
    }

    public void bindView(TextView tv_title, TextView tv_value, int position) {
        String[] flag = getPart(names[position]);
        //标题
        tv_title.setText(flag[0]);
        if (t != null) {
            String value = RefieldtUtils.getFieldValue(t, flag[1]);
            //值
            tv_value.setText(value);
        }
    }

    public void bindImageView(ImageView iv, int pos) {

    }

    public void setArrowStatus(int arrowStatus) {
        this.arrowStatus = arrowStatus;
    }

    protected String[] getPart(String str) {
        return str.split(":");
    }

    public void setCustomerEvent(CustomerEventAdapterCallback customerEvent) {
        this.customerEvent = customerEvent;
    }


}
