package com.lc.baseui.widget.button;

import android.content.Context;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

/**
 * 一个imagView和text，组成的button
 *
 * Created by Administrator on 2017/4/26.
 */

public class ImageAndTextButton extends ImageAndTextButtonTwoNums {
    public ImageAndTextButton(Context context) {
        super(context);
        init(context);
    }

    public ImageAndTextButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public ImageAndTextButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    @Override
    protected void init(Context context) {
        super.init(context);
        setVisibleShowOneNums();
    }

    /**
     * 如果值显示一个
     * **/
    public void setVisibleShowOneNums(){

        rel_two.setVisibility(View.GONE);
    }

    /**
     * 设置Text的大小
     **/
    public void setTextSize(int size) {
        tvOne.setTextSize(TypedValue.COMPLEX_UNIT_SP, size);

    }

    /**
     * 设置text的内容
     **/
    public void setTextContent(String one) {
        tvOne.setText(one);

    }

    /**
     * 设置text的内容
     **/
    public void setTextContent(int one) {
        tvOne.setText(one);

    }

    /**
     * 设置imageView的src
     **/
    public void setImageViewResouce(int resone) {
        imageOne.setImageResource(resone);

    }

    /**
     * 设置响应事件
     **/
    public void setClickEvent(View.OnClickListener clickOne) {
        rel_one.setOnClickListener(clickOne);

    }
    /**
     * 隐藏iv的标记
     * **/
    public void setImageViewVisible(int visible){
        imageOne.setVisibility(visible);
    }

  /**
   * 设置标记
   * **/
  public void setBtnTag(String str){
      rel_one.setTag(str);
  }

}
