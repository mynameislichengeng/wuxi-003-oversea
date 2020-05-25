package com.lc.baseui.widget.tv;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lc.baseui.R;

public class DubboTopBottomTextView extends RelativeLayout {

    private Context context;
    private TextView tvTop, tvBottom;

    private View partView;

    public DubboTopBottomTextView(Context context) {
        super(context);
        initView(context);

    }

    public DubboTopBottomTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public DubboTopBottomTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    private void initView(Context context) {
        this.context = context;
        View view = LayoutInflater.from(context).inflate(R.layout.widget_custom_dubbo_textview, null);
        //上
        tvTop = (TextView) view.findViewById(R.id.tv_top);
        //下
        tvBottom = (TextView) view.findViewById(R.id.tv_bottom);
        //分割
        partView = view.findViewById(R.id.partView);
        addView(view);
    }

    /**
     * 设置文本
     *
     * @param topRes
     * @param bottomRes
     */
    public void setText(int topRes, int bottomRes) {
        if (topRes != 0) {

            tvTop.setText(topRes);
        }
        if (bottomRes != 0) {

            tvBottom.setText(bottomRes);
        }
    }

    public void setTextSize(float topSize, float bottomSize) {
        if (topSize != 0) {
            tvTop.setTextSize(topSize);
        }

        if (bottomSize != 0) {
            tvBottom.setTextSize(bottomSize);
        }
    }

    public void setTopPadding(int left, int top, int right, int bottom) {
        tvTop.setPadding(left, top, right, bottom);
    }

    public void setBottomPadding(int left, int top, int right, int bottom) {
        tvBottom.setPadding(left, top, right, bottom);
    }


    public void setPartViewMargin(int height) {
        ViewGroup.LayoutParams params = partView.getLayoutParams();
        params.height = height;
        partView.setLayoutParams(params);
    }


}
