package com.lc.baseui.widget.cb;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.RelativeLayout;

import com.lc.baseui.R;

import java.util.HashMap;

/**
 * Created by licheng on 2017/5/7.
 */

public class MultiCheckBox extends RelativeLayout {
    public MultiCheckBox(Context context) {
        super(context);
        init(context);
    }

    public MultiCheckBox(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public MultiCheckBox(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private Context context;
    private CheckBox cb1_1, cb1_2, cb2_1, cb2_2, cb3_1, cb3_2;
    private HashMap<Integer, CheckBox> map = new HashMap<Integer, CheckBox>();

    private void init(Context context) {
        this.context = context;
        View view = LayoutInflater.from(context).inflate(R.layout.widget_layout_cb_nums6, null);
        cb1_1 = (CheckBox) view.findViewById(R.id.cb_one_one);
        cb1_2 = (CheckBox) view.findViewById(R.id.cb_one_two);
        cb2_1 = (CheckBox) view.findViewById(R.id.cb_two_one);
        cb2_2 = (CheckBox) view.findViewById(R.id.cb_two_two);
        cb3_1 = (CheckBox) view.findViewById(R.id.cb_three_one);
        cb3_2 = (CheckBox) view.findViewById(R.id.cb_three_two);
        addView(view);
        map.put(1, cb1_1);
        map.put(2, cb1_2);
        map.put(3, cb2_1);
        map.put(4, cb2_2);
        map.put(5, cb3_1);
        map.put(6, cb3_2);
    }

    /**
     * 设置文本
     **/
    public void setText(String[] str) {
        cb1_1.setText(str[0]);
        cb1_2.setText(str[1]);
        cb2_1.setText(str[2]);
        cb2_2.setText(str[3]);
        cb3_1.setText(str[4]);
        cb3_2.setText(str[5]);
    }
    /**
     * 设置文本
     **/
    public void setText(int[] str) {
        cb1_1.setText(str[0]);
        cb1_2.setText(str[1]);
        cb2_1.setText(str[2]);
        cb2_2.setText(str[3]);
        cb3_1.setText(str[4]);
        cb3_2.setText(str[5]);
    }
    /**
     * 设置visible
     **/
    public void setCbAllVisible(int visible) {
        cb1_1.setVisibility(visible);
        cb1_2.setVisibility(visible);
        cb2_1.setVisibility(visible);
        cb2_2.setVisibility(visible);
        cb3_1.setVisibility(visible);
        cb3_2.setVisibility(visible);
    }


    /**
     * 隐藏某个
     **/
    public void setCbVisible(int position, int visible) {
        CheckBox cb = map.get(position);
        cb.setVisibility(visible);
    }
    /**
     * 是否选中
     * **/
    public void setCheck(boolean b[]){
        cb1_1.setChecked(b[0]);
        cb1_2.setChecked(b[1]);
        cb2_1.setChecked(b[2]);
        cb2_2.setChecked(b[3]);
        cb3_1.setChecked(b[4]);
        cb3_2.setChecked(b[5]);
    }

    public void setCheck(int position,boolean b){
        CheckBox cb = map.get(position);
        cb.setChecked(b);
    }
}
