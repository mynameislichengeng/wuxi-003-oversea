package com.lc.baseui.widget.button;

import android.content.Context;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.lc.baseui.R;

/**
 * 一个imagView和text，组成的button
 * <p>
 * Created by Administrator on 2017/4/26.
 */

public class SimpleButton extends RelativeLayout {
    public SimpleButton(Context context) {
        super(context);
        init(context);
    }

    public SimpleButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public SimpleButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    protected Context context;
    protected Button btn_one, btn_two, btn_three;
    protected RelativeLayout rel_one, rel_two, rel_three;
    protected View view;

    protected void init(Context context) {
        this.context = context;
        view = LayoutInflater.from(context).inflate(R.layout.widget_layout_btn_num3_top10_left20_bgblue, null);
        rel_one = (RelativeLayout) view.findViewById(R.id.rel_one);
        rel_two = (RelativeLayout) view.findViewById(R.id.rel_two);
        rel_three = (RelativeLayout) view.findViewById(R.id.rel_three);
        btn_one = (Button) view.findViewById(R.id.btn_one);
        btn_two = (Button) view.findViewById(R.id.btn_two);
        btn_three = (Button) view.findViewById(R.id.btn_three);
        LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        addView(view, params);
    }

    /**
     * 设置显示的个数
     **/
    public void setShowVisible(int visible1, int visible2, int visible3) {
        rel_one.setVisibility(visible1);
        rel_two.setVisibility(visible2);
        rel_three.setVisibility(visible3);
    }

    /**
     * 设置Text的大小
     **/
    public void setTextSize(int size) {
        btn_one.setTextSize(TypedValue.COMPLEX_UNIT_SP, size);
        btn_two.setTextSize(TypedValue.COMPLEX_UNIT_SP, size);
        btn_three.setTextSize(TypedValue.COMPLEX_UNIT_SP, size);
    }

    /**
     * 设置text的内容
     **/
    public void setTextContent(String one, String two, String three) {

        btn_one.setText(one);
        btn_two.setText(two);
        btn_three.setText(three);
    }

    /**
     * 设置text的内容
     **/
    public void setTextContent(int one, int two, int three) {
        btn_one.setText(one);
        btn_two.setText(two);
        btn_three.setText(three);
    }

    /**
     * 设置imageView的src
     **/
    public void setImageViewResouce(int resone, int restwo) {

    }

    public void setBackGround(int resone, int restwo, int resthree) {
        if (resone != 0) {
            btn_one.setBackgroundResource(resone);
        }
        if (restwo != 0) {
            btn_two.setBackgroundResource(restwo);
        }
        if (resthree != 0) {
            btn_three.setBackgroundResource(resthree);
        }
    }

    /**
     * 设置响应事件
     **/
    public void setClickEvent(OnClickListener clickOne, OnClickListener clickTwo, OnClickListener clickThree) {
        btn_one.setOnClickListener(clickOne);
        btn_two.setOnClickListener(clickTwo);
        btn_three.setOnClickListener(clickThree);
    }

    /**
     * 设置当前的tag
     **/
    public void setTag(String tag1, String tag2, String tag3) {
        btn_one.setTag(tag1);
        btn_two.setTag(tag2);
        btn_three.setTag(tag3);
    }

    /**
     * 设置按钮是否可以点击
     **/
    public void setClickable(boolean isclickableone, boolean isclickabletwo, boolean isclickablethree) {
        //
        btn_one.setClickable(isclickableone);
        btn_one.setSelected(!isclickableone);
        //
        btn_two.setClickable(isclickabletwo);
        btn_two.setSelected(!isclickabletwo);
        //
        btn_three.setClickable(isclickablethree);
        btn_three.setSelected(!isclickablethree);

    }
}
