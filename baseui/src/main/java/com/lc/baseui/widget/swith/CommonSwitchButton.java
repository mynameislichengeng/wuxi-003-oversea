package com.lc.baseui.widget.swith;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import com.lc.baseui.R;
import com.lc.baseui.widget.swith.base.AbstractSwitchButton;

public class CommonSwitchButton extends AbstractSwitchButton {

    public CommonSwitchButton(Context context) {
        super(context);
    }

    public CommonSwitchButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CommonSwitchButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected int getLayout() {
        return R.layout.widget_layout_common_switch_btn;
    }

    @Override
    protected void initView(View view) {
        settingSwitchBtn(view, R.id.switch_button);
    }

}
