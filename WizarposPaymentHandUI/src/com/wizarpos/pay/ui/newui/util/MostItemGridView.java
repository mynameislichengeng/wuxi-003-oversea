package com.wizarpos.pay.ui.newui.util;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;

/**
 * Created by Song on 2016/3/25.
 * 显示全部内容禁用滑动的GridView
 */
public class MostItemGridView extends GridView {
    public MostItemGridView(Context context) {
        super(context);
    }

    public MostItemGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MostItemGridView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }
}
