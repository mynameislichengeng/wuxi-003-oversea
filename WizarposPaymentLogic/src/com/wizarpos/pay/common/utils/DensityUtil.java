package com.wizarpos.pay.common.utils;

import android.content.Context;
import android.view.View;

/**
 * Created by Song on 2016/4/13.
 */
public class DensityUtil {
    private DensityUtil() {
    }

    public static float getDensity(Context context) {
        return context.getResources().getDisplayMetrics().density;
    }

    public static int dip2px(Context context, float dpValue) {
        float scale = context.getResources().getDisplayMetrics().density;
        return (int)(dpValue * scale + 0.5F);
    }

    public static int px2dip(Context context, float pxValue) {
        float scale = context.getResources().getDisplayMetrics().density;
        return (int)(pxValue / scale + 0.5F);
    }

    /**
     * 将px值转换为sp值，保证文字大小不变
     *
     * @param context
     * @param pxValue
     *            （DisplayMetrics类中属性scaledDensity）
     * @return
     */
    public static int px2sp(Context context, float pxValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (pxValue / fontScale + 0.5f);
    }

    /**
     * 将sp值转换为px值，保证文字大小不变
     *
     * @param context
     * @param spValue
     *            （DisplayMetrics类中属性scaledDensity）
     * @return
     */
    public static int sp2px(Context context, float spValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }

    public static int getScreenWidth(Context context) {
        return context.getResources().getDisplayMetrics().widthPixels;
    }

    public static int getScreenHeight(Context context) {
        return context.getResources().getDisplayMetrics().heightPixels;
    }

    public static int getWidth(View view) {
        int w = View.MeasureSpec.makeMeasureSpec(0, 0);
        int h = View.MeasureSpec.makeMeasureSpec(0, 0);
        view.measure(w, h);
        return view.getMeasuredWidth();
    }

    public static int getHeight(View view) {
        int w = View.MeasureSpec.makeMeasureSpec(0, 0);
        int h = View.MeasureSpec.makeMeasureSpec(0, 0);
        view.measure(w, h);
        return view.getMeasuredHeight();
    }
}
