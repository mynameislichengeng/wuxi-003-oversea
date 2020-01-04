package com.lc.baseui.widget.pp.base;

import android.app.Activity;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.PopupWindow;

/**
 * Created by licheng on 2017/5/21.
 */

public abstract class AbstractPopWindow extends PopupWindow {

    public abstract void initView(View view);

    public abstract int getLayout();

    public abstract int getWidth();

    public AbstractPopWindow(Activity context) {
        this.context = context;
        initPop(context);
    }

    protected View conentView;
    protected Activity context;

    protected void initPop(final Activity context) {
        conentView = LayoutInflater.from(context).inflate(getLayout(), null);
        // 设置SelectPicPopupWindow的View
        this.setContentView(conentView);
        // 设置SelectPicPopupWindow弹出窗体的宽
        this.setWidth(getWidth());
        // 设置SelectPicPopupWindow弹出窗体的高
        this.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        // 设置SelectPicPopupWindow弹出窗体可点击
        this.setFocusable(true);
        this.setOutsideTouchable(true);
        // 刷新状态
        this.update();
        // 实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(0000000000);
        // 点back键和其他地方使其消失,设置了这个才能触发OnDismisslistener ，设置其他控件变化等操作
        this.setBackgroundDrawable(dw);
        //变暗
        WindowManager.LayoutParams lp = context.getWindow().getAttributes();
        lp.alpha = 0.6f;
        context.getWindow().setAttributes(lp);
        this.setOnDismissListener(new OnDismissListener() {
            @Override
            public void onDismiss() {
                WindowManager.LayoutParams lp = context.getWindow().getAttributes();
                lp.alpha = 1f;
                context.getWindow().setAttributes(lp);
            }
        });
        initView(conentView);
    }

    /**
     * 显示popupWindow
     *
     * @param parent
     */
    public void showPopupWindow(View parent) {
        if (!this.isShowing()) {
            // 以下拉方式显示popupwindow
            this.showAsDropDown(parent, parent.getLayoutParams().width / 2, 18);
        } else {
            this.dismiss();
        }
    }

    /**
     * 屏幕总的宽度
     **/
    protected int getAllWidth() {
        int w = context.getWindowManager().getDefaultDisplay().getWidth();
        return w;
    }

    /**
     * 屏幕总的宽度
     **/
    protected int getAllHeight() {
        int h = context.getWindowManager().getDefaultDisplay().getHeight();
        return h;
    }
}
