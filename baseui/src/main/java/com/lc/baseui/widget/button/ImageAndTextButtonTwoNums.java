package com.lc.baseui.widget.button;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lc.baseui.R;

/**
 * 一个imagView和text，组成的button
 * <p>
 * Created by Administrator on 2017/4/26.
 */

public class ImageAndTextButtonTwoNums extends RelativeLayout {
    public ImageAndTextButtonTwoNums(Context context) {
        super(context);
        init(context);
    }

    public ImageAndTextButtonTwoNums(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public ImageAndTextButtonTwoNums(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    protected Context context;
    protected RelativeLayout rel_one, rel_two;
    protected ImageView imageOne, imageTwo;
    protected TextView tvOne, tvTwo;
    protected View view;

    protected void init(Context context) {
        this.context = context;
        view = LayoutInflater.from(context).inflate(R.layout.widget_coustomer_btn_from_tv_imageview_top10_left20_bgblue, null);
        rel_one = (RelativeLayout) view.findViewById(R.id.rel_one);
        rel_two = (RelativeLayout) view.findViewById(R.id.rel_two);
        imageOne = (ImageView) view.findViewById(R.id.iv_one_tag);
        imageTwo = (ImageView) view.findViewById(R.id.iv_two_tag);
        tvOne = (TextView) view.findViewById(R.id.tv_one_content);
        tvTwo = (TextView) view.findViewById(R.id.tv_two_content);
        LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        addView(view, params);
    }


    /**
     * 设置Text的大小
     **/
    public void setTextSize(int size) {
        tvOne.setTextSize(TypedValue.COMPLEX_UNIT_SP, size);
        tvTwo.setTextSize(TypedValue.COMPLEX_UNIT_SP, size);
    }

    /**
     * 设置text的内容
     **/
    public void setTextContent(String one, String two) {
        if (one == null || two == null) {
            return;
        }
        tvOne.setText(one);
        tvTwo.setText(two);
    }

    /**
     * 设置text的内容
     **/
    public void setTextContent(int one, int two) {
        tvOne.setText(one);
        tvTwo.setText(two);
    }

    /**
     * 设置imageView的src
     **/
    public void setImageViewResouce(int resone, int restwo) {
        imageOne.setImageResource(resone);
        imageTwo.setImageResource(restwo);
    }

    /**
     * 设置响应事件
     **/
    public void setClickEvent(View.OnClickListener clickOne, View.OnClickListener clickTwo) {
        rel_one.setOnClickListener(clickOne);
        rel_two.setOnClickListener(clickTwo);
    }

    /**
     * 标记
     **/
    public void setTag(String tagOne, String tagTwo) {
        rel_one.setTag(tagOne);
        rel_two.setTag(tagTwo);
    }

    /**
     * 设置整体的背景色
     * **/
    public void setBg(int resone, int restwo){
        if(resone!=0){
            rel_one.setBackgroundResource(resone);
        }
        if(restwo!=0){
            rel_two.setBackgroundResource(restwo);
        }


    }
}
