package com.lc.baseui.widget.swith.base;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;

import com.lc.baseui.widget.swith.callback.OnSwitchChangeListener;
import com.suke.widget.SwitchButton;

public abstract class AbstractSwitchButton extends RelativeLayout {


    protected abstract int getLayout();

    protected abstract void initView(View view);

    protected Context context;
    protected SwitchButton switchButton;// 参考https://github.com/zcweng/SwitchButton

    protected OnSwitchChangeListener onSwitchChangeListener;

    public AbstractSwitchButton(Context context) {
        super(context);
        init(context);
    }

    public AbstractSwitchButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public AbstractSwitchButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        this.context = context;
        View view = LayoutInflater.from(context).inflate(getLayout(), null);
        initView(view);
        addView(view);
    }

    protected void settingSwitchBtn(View view, int id) {
        switchButton = (SwitchButton) view.findViewById(id);
        switchButton.setChecked(false);
        switchButton.isChecked();
        switchButton.setEnabled(true);//disable button
        switchButton.toggle();     //switch state
        switchButton.toggle(true);//switch without animation

        switchButton.setOnCheckedChangeListener(new SwitchButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SwitchButton view, boolean isChecked) {

                if (AbstractSwitchButton.this.onSwitchChangeListener != null) {

                    if (isChecked) {
                        //当被打开
                        AbstractSwitchButton.this.onSwitchChangeListener.onSwitchOpenClick();
                    } else {
                        //当被关闭
                        AbstractSwitchButton.this.onSwitchChangeListener.onSwitchCloseClick();
                    }
                }
            }
        });
    }

    public void setOnSwitchChangeListener(OnSwitchChangeListener onSwitchChangeListener) {
        this.onSwitchChangeListener = onSwitchChangeListener;
    }

    /**
     * 设置是否选中
     *
     * @param checked
     */
    public void setViewSwitchCheckedStatus(boolean checked) {
        switchButton.setChecked(checked);
    }


}
