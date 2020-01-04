package com.lc.baseui.widget.pp.impl;

import android.app.Activity;
import android.view.View;
import android.widget.Button;

import com.lc.baseui.R;
import com.lc.baseui.widget.pp.base.AbstractPopWindow;

/**
 * Created by licheng on 2017/5/21.
 */

public class SimplePopWindowBtn3 extends AbstractPopWindow {

    public SimplePopWindowBtn3(Activity context) {
        super(context);
    }

    private Button btnOne, btnTwo, btnThree;

    @Override
    public void initView(View view) {
        btnOne = (Button) view.findViewById(R.id.btn_one);
        btnTwo = (Button) view.findViewById(R.id.btn_two);
        btnThree = (Button) view.findViewById(R.id.btn_three);
        btnThree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

    @Override
    public int getLayout() {
        return R.layout.pop_simple_btn3;
    }

    @Override
    public int getWidth() {
        return getAllWidth();
    }

    /**
     * 设置三个按钮的文本
     **/
    public void setText(int resOne, int resTwo, int resThree) {
        btnOne.setText(resOne);
        btnTwo.setText(resTwo);
        btnThree.setText(resThree);
    }

    /**
     * 设置前面2个按钮的响应事件
     **/
    public void setClickEventBeforTWoBtn(View.OnClickListener resOne, View.OnClickListener resTwo) {
        btnOne.setOnClickListener(resOne);
        btnTwo.setOnClickListener(resTwo);
    }

    /**
     * 设置最后一个按钮的按钮的响应事件
     **/
    public void setClickEventThreeBtn(View.OnClickListener resThree) {
        btnThree.setOnClickListener(resThree);
    }

}
